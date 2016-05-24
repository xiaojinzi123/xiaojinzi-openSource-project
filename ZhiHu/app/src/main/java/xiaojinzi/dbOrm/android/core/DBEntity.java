package xiaojinzi.dbOrm.android.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xiaojinzi.base.android.log.L;
import xiaojinzi.base.java.reflection.ReflectionUtil;
import xiaojinzi.base.java.util.ArrayUtil;
import xiaojinzi.base.java.util.StringUtil;
import xiaojinzi.dbOrm.android.annotation.Column;
import xiaojinzi.dbOrm.android.annotation.Table;
import xiaojinzi.dbOrm.android.core.exception.AutoPKException;
import xiaojinzi.dbOrm.android.core.exception.TableNameNotFoundException;


/**
 * 这个类保存了一个实体对象和数据库中 <br>
 * 的字段进行交互的时候,所遇到的所有的sql<br>
 * 语句,得到这个类需要调用其静态的方法parse,<br>
 * 对传入的Class类进行注解的解析,生成各种sql<br>
 * 语句 ,并且缓存了sql语句,下次一样的sql语句并不会再次创建<br>
 * 也可以生成sql操作的时候用到的数据,解放了<br>
 * 数据库 操作的实现
 *
 * @author xiaojinzi
 */
public class DBEntity {

	/**
	 * 类的标识
	 */
	private static String TAG = "xiaojinzi.android.dbOrm.core.DBEntity";

	/**
	 * 控制是否打印Log
	 */
	private boolean isLog = false;

	// ==================================================静态变量=================================
	/**
	 * 插入的标识
	 */
	public static final String INSERT_FLAG = "insert";

	/**
	 * 删除所有的标识
	 */
	public static final String DELETEALL_FLAG = "deleteAll";

	/**
	 * 删除一个的标识
	 */
	public static final String DELETESINGLE_FLAG = "deleteSingle";

	/**
	 * 更新所有的标识
	 */
	public static final String UPDATEALL_FLAG = "updateAll";

	/**
	 * 更新一个的标识
	 */
	public static final String UPDATESINGLE_FLAG = "updateSingle";

	/**
	 * 查询所有的标识
	 */
	public static final String QUERYALL_FLAG = "queryAll";

	/**
	 * 查询单个
	 */
	public static final String QUERYSINGLE_FLAG = "querySingle";

	/**
	 * 查询总个数
	 */
	public static final String QUERYCOUNNT_FLAG = "queryCount";

	// ============================end=======静态变量=================================

	/**
	 * 表的名字
	 */
	private String tableName;

	/**
	 * 存放各个标识的对应的sql语句
	 */
	private Map<String, String> map_sqls = new HashMap<String, String>();

	/**
	 * 存放各个标识对应的要用到的数据对应的字段
	 */
	private Map<String, Field[]> map_fields = new HashMap<String, Field[]>();

	/**
	 * 存放数据库列和实体对象中字段绑定的关系
	 */
	private Map<String, Field> map_columFields = new HashMap<String, Field>();

	/**
	 * 构造函数私有化
	 */
	private DBEntity() {
	}

	/**
	 * 通过这个解析的方法获取一个实例对象,<br>
	 * 主要是解析出一个类中需要操作的字段数组,<br>
	 * 需要用到的基本sql语句
	 *
	 * @param clazz
	 *            需要解析的Class
	 * @return
	 */
	public static DBEntity parse(Class<? extends Object> clazz) {

		// 记录插入的时候或者更新的时候有几个字段需要插入或者更新
		int count = 0;

		// 记录主键的字段
		Field autoPkField = null;

		/**
		 * 主键对应的数据库的字段名字
		 */
		String autoPkStr = null;
		// String autoPkColumName = null;

		// 创建一个Db实体对象
		DBEntity sqlEntity = new DBEntity();

		StringBuffer sb_insert = new StringBuffer();
		StringBuffer sb_deleteSingle = new StringBuffer();
		StringBuffer sb_deleteAll = new StringBuffer();
		StringBuffer sb_updateSingle = new StringBuffer();
		StringBuffer sb_updateAll = new StringBuffer();
		StringBuffer sb_querySingle = new StringBuffer();
		StringBuffer sb_queryAll = new StringBuffer();
		StringBuffer sb_queryCount = new StringBuffer();

		// 用?进行占位符的时候,记录每一个Sql语句里面每一个?对应的字段,所以这里采用有序的集合,而不能采用无序的集合
		// 没有?的Sql就不需要这个集合
		List<Field> l_insert = new ArrayList<Field>();
		List<Field> l_deleteSingle = new ArrayList<Field>();
		List<Field> l_updateSingle = new ArrayList<Field>();
		List<Field> l_updateAll = new ArrayList<Field>();
		List<Field> l_querySingle = new ArrayList<Field>();

		// 从Class类中获取表的名称
		String name = findTableNameFromClass(clazz);
		sqlEntity.setTableName(name);

		// 根据表的名字预处理一部分Sql语句
		sb_insert.append("insert into " + name + " (");
		sb_deleteSingle.append("delete from " + name + " where ");
		// 删除所有已经完成
		sb_deleteAll.append("delete from " + name);
		sb_updateSingle.append("update " + name + " set ");
		sb_updateAll.append("update " + name + " set ");
		sb_queryAll.append("select ");
		sb_queryCount.append("select count(*) from " + name);

		// 获取所有的字段
		Field[] fields = clazz.getDeclaredFields();

		// 循环实体对象中所有的字段
		for (int i = 0; i < fields.length; i++) { // 循环所有的字段

			// 循环中的每一个字段
			Field field = fields[i];

			if (field.isAnnotationPresent(Column.class)) { // 如果这个字段上有列Colum这个注解

				// 获取这个字段上的注解对象
				Column column = field.getAnnotation(Column.class);

				// 获取注解上的信息
				String columName = column.name(); // 获取这个字段对应的数据库字段名称

				// 如果值为"",就使用字段的名称的首字母小写作为字段名称
				if ("".equals(columName)) {
					columName = StringUtil.firstCharToLowerCase(field.getName());
				}

				boolean autoPk = column.autoPk(); // 获取是否是自动递增的主键

				// 让这个字段名称和实体中的字段进行绑定
				sqlEntity.putColumField(columName, field);

				if (autoPk) { // 如果是主键

					// 删除单个的Sql语句完成
					sb_deleteSingle.append(columName + " = ?");
					l_deleteSingle.add(field);

					// 如果主键字段不止一个,抛出异常
					if (autoPkField != null) {
						throw new AutoPKException("there are two autoPk field in '" + clazz.getName() + "'");
					}

					autoPkField = field;
					autoPkStr = columName;

				} else {
					// 插入和更新的Sql语句的拼接
					sb_insert.append(columName + ",");
					l_insert.add(field);

					sb_updateSingle.append(columName + " = ?,");
					l_updateSingle.add(field);

					sb_updateAll.append(columName + " = ?,");
					l_updateAll.add(field);

					count++;
				}

				// 查询语句的拼接
				sb_queryAll.append(columName + ",");

			}

		}

		// 如果一个类中没有主键,就抛出没有主键异常
		if (autoPkField == null) {
			throw new AutoPKException("there are no autoPkField in '" + clazz.getName() + "'");
		}

		// 稍作处理,删除字符串中最后一个字符
		sb_insert.deleteCharAt(sb_insert.length() - 1);
		sb_updateSingle.deleteCharAt(sb_updateSingle.length() - 1);
		sb_updateAll.deleteCharAt(sb_updateAll.length() - 1);
		sb_queryAll.deleteCharAt(sb_queryAll.length() - 1);

		// 字段循环完毕现在的Sql语句是这样子的：举例说明
		// insertSql= "insert into entity (name,pass,age"
		// updateSingleSql= "update entity set name = ?,pass = ?,age = ?"
		// deleteSingleSql= "delete from entity where id = ?"
		// queryAllSql= "select id,name,pass,age"

		// 更新的Sql语句完毕
		sb_updateSingle.append(" where " + autoPkStr + " = ?");
		l_updateSingle.add(autoPkField);

		// 查询所有的Sql语句完毕
		sb_queryAll.append(" from " + name);

		// 查询单个的Sql语句完毕
		sb_querySingle.append(sb_queryAll.toString() + " where " + autoPkStr + " = ?");
		l_querySingle.add(autoPkField);

		sb_insert.append(") values (");

		for (int i = 0; i < count; i++) {
			sb_insert.append("?,");
		}

		// 此时的Sql语句
		// insertSql= "insert into entity (name,pass,age) values (?,?,?,"
		// updateSingleSql= "update entity set name = ?,pass = ?,age = ? where
		// id = ?"
		// deleteSingleSql= "delete from entity where id = ?"
		// queryAllSql= "select id,name,pass,age from entity"

		// 插入的语句也结束了
		// insertSql= "insert into entity (name,pass,age) values (?,?,?)"
		sb_insert.deleteCharAt(sb_insert.length() - 1);
		sb_insert.append(")");

		sqlEntity.addSql(INSERT_FLAG, sb_insert.toString());
		sqlEntity.addSql(DELETESINGLE_FLAG, sb_deleteSingle.toString());
		sqlEntity.addSql(DELETEALL_FLAG, sb_deleteAll.toString());
		sqlEntity.addSql(UPDATESINGLE_FLAG, sb_updateSingle.toString());
		sqlEntity.addSql(UPDATEALL_FLAG, sb_updateAll.toString());
		sqlEntity.addSql(QUERYSINGLE_FLAG, sb_querySingle.toString());
		sqlEntity.addSql(QUERYALL_FLAG, sb_queryAll.toString());
		sqlEntity.addSql(QUERYCOUNNT_FLAG, sb_queryCount.toString());

		sqlEntity.addField(INSERT_FLAG, ArrayUtil.listToArray(l_insert));
		sqlEntity.addField(DELETESINGLE_FLAG, ArrayUtil.listToArray(l_deleteSingle));
		sqlEntity.addField(UPDATEALL_FLAG, ArrayUtil.listToArray(l_updateAll));
		sqlEntity.addField(UPDATESINGLE_FLAG, ArrayUtil.listToArray(l_updateSingle));
		sqlEntity.addField(QUERYSINGLE_FLAG, ArrayUtil.listToArray(l_querySingle));

		return sqlEntity;
	}

	/**
	 * 从Class类中获取表的名称,如果有Table<br>
	 * 注解,就用注解上的,<br>
	 * 如果有注解但是没有值,采用使用类的名称首字母小写,<br>
	 * 如果没有就抛出异常{@link TableNameNotFoundException}
	 *
	 * @param clazz
	 */
	private static String findTableNameFromClass(Class<? extends Object> clazz) {
		if (clazz.isAnnotationPresent(Table.class)) { // 如果这个类上有Table的注解
			Table t = clazz.getAnnotation(Table.class);
			return "".equals(t.value())
					? StringUtil.firstCharToLowerCase(StringUtil.getLastContent(clazz.getName(), ".")) : t.value(); // 从注解中获取表的名字
		} else { // 如果没有Table注解,表名默认使用类的名字
			throw new TableNameNotFoundException("Can not found table name in '" + clazz.getName() + "'");
		}
	}

	/**
	 * 获取Sql语句
	 *
	 * @param flag
	 * @return
	 */
	public String getSql(String flag) {
		return map_sqls.get(flag);
	}

	/**
	 * 添加某一个标识的对应的sql语句
	 *
	 * @param flag
	 * @param sql
	 */
	private void addSql(String flag, String sql) {
		map_sqls.put(flag, sql);
	}

	/**
	 * 通过标识获取这个标识应该用到的数据
	 *
	 * @param o
	 *            数据的来源,一般都是实体对象
	 * @param flag
	 *            标识符{@link DBEntity}
	 * @return
	 */
	public Object[] getValues(Object o, String flag) {

		Object[] obs = null;

		// 从集合中获取这个标识对应的字段数组
		Field[] fields = map_fields.get(flag);

		// 如果数组不是null,说明这个标识对应的操作是需要数据的
		if (fields != null) {

			// 值的数组的长度就是字段数组的长度
			obs = new Object[fields.length];

			// 循环取出各个字段在这个对象中的数据
			for (int i = 0; i < obs.length; i++) {

				Field field = fields[i];

				try {
					// 如果这个字段是基本数据类型
					if (ReflectionUtil.isBaseClassType(field.getType())) {
						field.setAccessible(true);
						obs[i] = field.get(o);
					} else {
						if (isLog) {
							L.s(TAG, "get value of field '" + field.getName() + "' failed");
						}
					}
				} catch (IllegalAccessException e) {
					if (isLog) {
						e.printStackTrace();
						L.s(TAG, "get value of field '" + field.getName() + "' failed");
					}
				}
			}
		}

		return obs;
	}

	/**
	 * 添加某一个标识对应的数据字段
	 * @param flag
	 * @param obs
	 */
	private void addField(String flag, Object[] obs) {
		Field[] fields = new Field[obs.length];

		for (int i = 0; i < fields.length; i++) {
			fields[i] = (Field) obs[i];
		}

		map_fields.put(flag, fields);
	}

	/**
	 * 添加一个数据库字段名字对应的字段
	 * 
	 * @param key
	 *            数据库中的字段名称
	 * @param f
	 *            实体中的字段
	 */
	public void putColumField(String key, Field f) {
		map_columFields.put(key, f);
	}

	/**
	 * 获取一个字段名字对应的实体对象中的一个字段
	 * 
	 * @param key
	 *            数据库中的字段名称
	 * @return 与数据库中的字段名称对应的实体中的字段
	 */
	public Field getField(String key) {
		return map_columFields.get(key);
	}

	public Map<String, Field> getMap_columFields() {
		return map_columFields;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

}
