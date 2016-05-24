package xiaojinzi.dbOrm.android.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import xiaojinzi.base.android.log.L;
import xiaojinzi.base.java.reflection.ReflectionUtil;
import xiaojinzi.dbOrm.android.core.exception.CreateEntityException;


/**
 * 这个类是框架的最核心的一个类，用来操作所有的增删改查的操作,并且缓存了用到的sql语句,不会每次都新创建
 *
 * @author xiaojinzi
 */
public class Db {

	/**
	 * 类的标识
	 */
	public static final String TAG = "xiaojinzi.android.dbOrm.core.Db";

	/**
	 * 控制是否打印Log
	 */
	private static boolean isLog = false;

	private SqlFactory factory = null;

	private SQLiteOpenHelper sqLiteOpenHelper = null;

	public Db(SQLiteOpenHelper sqLiteOpenHelper) {
		super();
		this.sqLiteOpenHelper = sqLiteOpenHelper;
		factory = SqlFactory.getInstance();
	}

	/**
	 * 获取一个表的记录数目
	 * 
	 * @param clazz
	 * @return
	 */
	public long getCount(Class<? extends Object> clazz) {
		SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
		DBEntity sqlEntity = SqlFactory.getInstance().getSqlEntity(clazz);
		Cursor c = db.rawQuery(sqlEntity.getSql(DBEntity.QUERYCOUNNT_FLAG), null);
		if (c.moveToNext()) {
			long count = c.getLong(0);
			db.close();
			return count;
		}
		db.close();
		return 0;
	}

	/**
	 * 根据自定义的sql语句,查询实体对象
	 *
	 * @param clazz
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public List<? extends Object> query(Class<? extends Object> clazz, String sql, String[] selectionArgs) {
		return cursorToList(clazz, executeQuery(clazz, null, selectionArgs, sql));
	}

	/**
	 * 查询单个,如果您的这个主键并不是真的<br>
	 * 主键,查询出来多个的情况会按照取第一个处理
	 *
	 * @param clazz
	 * @param pkVlaue
	 *            根据这个主键的数据查询的
	 * @return
	 */
	public Object querySingle(Class<? extends Object> clazz, Object pkVlaue) {
		List<? extends Object> list = cursorToList(clazz,
				executeQuery(clazz, DBEntity.QUERYSINGLE_FLAG, new String[] { "" + pkVlaue }, null));
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 查询所有
	 *
	 * @param clazz
	 *            和表映射的实体对象
	 * @return
	 */
	public List<? extends Object> queryAll(Class<? extends Object> clazz) {
		return queryAll(clazz, null);
	}

	/**
	 * 查询所有
	 * 
	 * @param clazz
	 *            和表映射的实体对象
	 * @param d
	 *            进度条对话框
	 * @return
	 */
	public List<? extends Object> queryAll(Class<? extends Object> clazz, ProgressDialog d) {
		return cursorToList(clazz, executeQuery(clazz, DBEntity.QUERYALL_FLAG, null, null), d);
	}

	/**
	 * 插入一个实体对象
	 *
	 * @param o
	 */
	public boolean insert(Object o) {
		return execute(o, DBEntity.INSERT_FLAG);
	}

	/**
	 * 删除一个实体对象
	 *
	 * @param o
	 * @return
	 */
	public boolean delete(Object o) {
		return execute(o, DBEntity.DELETESINGLE_FLAG);
	}

	/**
	 * 删除所有
	 * 
	 * @param clazz
	 * @return
	 */
	public boolean deleteAll(Class<? extends Object> clazz) {
		String sql = SqlFactory.getInstance().getSqlEntity(clazz).getSql(DBEntity.DELETEALL_FLAG);
		// 获取数据库的写出对象
		SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
		db.execSQL(sql);
		db.close();
		return true;
	}

	/**
	 * 更新一个实体对象
	 *
	 * @param o
	 */
	public boolean update(Object o) {
		return execute(o, DBEntity.UPDATESINGLE_FLAG);
	}

	/**
	 * 执行数据库更新操作<br>
	 * 更新包括：insert delete update
	 *
	 * @param o
	 * @param flag
	 * @return
	 */
	private boolean execute(Object o, String flag) {
		DBEntity dbEntity = factory.getSqlEntity(o.getClass());
		return execute(dbEntity.getSql(flag), dbEntity.getValues(o, flag));
	}

	/**
	 * 执行sql语句
	 * 
	 * @param sql
	 * @param bindArgs
	 * @return
	 */
	public boolean execute(String sql, Object[] bindArgs) {
		// 获取数据库的写出对象
		SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
		db.execSQL(sql, bindArgs);
		db.close();
		return true;
	}

	/**
	 * 返回一个结果集
	 *
	 * @param clazz
	 * @param flag
	 * @param selectionArgs
	 * @return
	 */
	private Cursor executeQuery(Class<?> clazz, String flag, String[] selectionArgs, String sql) {
		// 获取数据库的读入对象
		SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
		DBEntity dbEntity = factory.getSqlEntity(clazz);
		if (sql == null) { // 如果不是自定义的sql语句
			return db.rawQuery(dbEntity.getSql(flag), selectionArgs);
			// return null;
		} else {// 如果想采用自定义的sql语句
			return db.rawQuery(sql, selectionArgs);
		}
	}

	/**
	 * 把一个结果集对象转化为一个集合,利用<br>
	 * 反射技术实现数据库中的字段和实体中的数据的衔接
	 *
	 * @param clazz
	 *            集合中存放的对象的Class对象
	 * @param c
	 *            要转化的结果集对象
	 * @return 返回一个集合,里面装了转换后的所有实体对象
	 */
	public static List<? extends Object> cursorToList(Class<? extends Object> clazz, Cursor c) {
		return cursorToList(clazz, c, null);
	}

	/**
	 * 把一个结果集对象转化为一个集合,利用<br>
	 * 反射技术实现数据库中的字段和实体中的数据的衔接
	 *
	 * @param clazz
	 *            集合中存放的对象的Class对象
	 * @param c
	 *            要转化的结果集对象
	 * @param d
	 *            进度条对话框
	 * @return 返回一个集合,里面装了转换后的所有实体对象
	 */
	public static List<? extends Object> cursorToList(Class<? extends Object> clazz, Cursor c, ProgressDialog d) {

		if (c == null) {
			return null;
		}

		// 获取Db实体对象
		DBEntity dbEntity = SqlFactory.getInstance().getSqlEntity(clazz);

		// 创建返回值对象
		List<Object> list = new ArrayList<Object>();

		// 获取这个类中的所有字段,包括了私有的属性
		// Field[] fs = clazz.getDeclaredFields();

		// try {
		// while (c.moveToNext()) {
		// // 创建对象
		// Object t = clazz.newInstance();
		//
		// // 循环所有的字段
		// for (int i = 0; i < fs.length; i++) {
		// Field field = fs[i];
		//
		// if (field.isAnnotationPresent(Column.class)) { // 如果这个字段上有列的注解
		// // 获取这个列的注解对象
		// Column column = field.getAnnotation(Column.class);
		// // 获取列注解上标注这个字段是数据库中的哪一个字段
		// String columnName = column.name();
		//
		// // 获取这个字段的类型
		// Class<?> fieldType = field.getType();
		//
		// int index = c.getColumnIndex(columnName);
		// Object value = null;
		//
		// Class<? extends Object> parameterType = null;
		//
		// if (fieldType == int.class) { // 如果是整形
		// parameterType = int.class;
		// value = c.getInt(index);
		// } else if (fieldType == Integer.class) {// 如果是整形
		// parameterType = Integer.class;
		// value = c.getInt(index);
		// } else if (fieldType == long.class) {// 如果是长整型
		// parameterType = long.class;
		// value = c.getLong(index);
		// } else if (fieldType == Long.class) {// 如果是长整型
		// parameterType = Long.class;
		// value = c.getLong(index);
		// } else if (fieldType == String.class) { // 如果是字符串的
		// parameterType = String.class;
		// value = c.getString(index);
		// } else if (fieldType == boolean.class || fieldType == Boolean.class)
		// { // 如果是布尔型的
		// int tmp = c.getInt(index);
		// if (tmp == 0) {
		// value = false;
		// } else {
		// value = true;
		// }
		// if (fieldType == boolean.class) {
		// parameterType = boolean.class;
		// } else {
		// parameterType = Boolean.class;
		// }
		// }
		//
		// // 获取这个字段对应的set方法名称
		// String methodNameOfField =
		// ReflectionUtil.getSetMethodNameOfField(field);
		//
		// // 根据这个方法名称获取方法
		// Method method = clazz.getMethod(methodNameOfField, parameterType);
		//
		// // 反射执行这个方法
		// method.invoke(t, value);
		//
		// }
		//
		// }
		//
		// // 添加这个实体对象到集合中
		// list.add(t);
		//
		// }
		//
		// // 关闭结果集
		// c.close();
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// throw new RuntimeException(TAG + "\n 反射查询失败失败 ");
		// }

		if (d != null) {
			d.setMax(c.getCount());
		}

		while (c.moveToNext()) { // 如何能移动到下一条

			if (d != null) {
				d.setProgress(d.getProgress() + 1);
			}

			// 创建对象
			Object t;

			try {
				t = clazz.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				throw new CreateEntityException("不能从Class类'" + clazz.getName() + //
						"'创建对象,实体对象中可能没有默认的构造函数");
			}

			// 拿到实体对象遥操作的字段的集合,循环它
			Map<String, Field> map = dbEntity.getMap_columFields();

			Set<String> keySet = map.keySet();

			Iterator<String> it = keySet.iterator();

			while (it.hasNext()) {

				// 获取数据库的字段名字
				String columnName = it.next();

				// 获取这个数据库字段对应的实体对象中的字段
				Field field = map.get(columnName);

				// 获取这个字段的类型
				// Class<?> fieldType = field.getType();

				// 获取这个字段对应的下标
				int index = c.getColumnIndex(columnName);

				if (index == -1) { // 如果这个字段对应的下标为-1,表示结果集中没有这个字段,直接进行下面的循环
					continue;
				}

				// Object value = null;

				// Class<? extends Object> parameterType = null;
				String value = c.getString(index);

				// if (fieldType == int.class) { // 如果是整形
				// // parameterType = int.class;
				// value = c.getInt(index);
				// } else if (fieldType == Integer.class) {// 如果是整形
				// // parameterType = Integer.class;
				// value = c.getInt(index);
				// } else if (fieldType == long.class) {// 如果是长整型
				// // parameterType = long.class;
				// value = c.getLong(index);
				// } else if (fieldType == Long.class) {// 如果是长整型
				// // parameterType = Long.class;
				// value = c.getLong(index);
				// } else if (fieldType == String.class) { // 如果是字符串的
				// // parameterType = String.class;
				// value = c.getString(index);
				// } else if (fieldType == boolean.class || fieldType ==
				// Boolean.class) { // 如果是布尔型的
				// int tmp = c.getInt(index);
				// if (tmp == 0) {
				// value = false;
				// } else {
				// value = true;
				// }
				// if (fieldType == boolean.class) {
				// // parameterType = boolean.class;
				// } else {
				// // parameterType = Boolean.class;
				// }
				// }

				// 获取这个字段对应的set方法名称
				// String methodNameOfField =
				// ReflectionUtil.getSetMethodNameOfField(field);
				//
				// try {
				// // 根据这个方法名称获取方法
				// Method method = clazz.getMethod(methodNameOfField,
				// parameterType);
				// // 反射执行这个方法
				// method.invoke(t, value);
				// } catch (NoSuchMethodException e) {
				// L.s(TAG, "不能找到对应的Set方法");
				// } catch (Exception e) {
				// L.s(TAG, "不能通过方法注入属性");
				// }

				try {
					/* 对字段进行注入,私有的也强行注入,包括: 1.八种基本数据类型 2.字符串 */
					boolean b = ReflectionUtil.setValue(field, t, value);

					if (isLog) {
						if (b) {
							L.s(TAG, "set value of field '" + field.getName() + "' success");
						} else {
							L.s(TAG, "Can not set value '" + value + "' of field '" + field.getName() + "'");
						}
					}
				} catch (Exception e) {
					if (isLog) {
						e.printStackTrace();
						L.s(TAG, "Can not set value '" + value + "' of field '" + field.getName() + "'");
					}
				}

			}

			// 添加这个实体对象到集合中
			list.add(t);

		}

		// 关闭结果集
		c.close();

		// 返回这个list集合
		return list;
	}

}
