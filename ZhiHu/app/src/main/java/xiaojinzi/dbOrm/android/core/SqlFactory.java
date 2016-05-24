package xiaojinzi.dbOrm.android.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个是一个sql语句的工厂类,<br>
 * 用来针对一个实体产生一个对应的SqlEntity对象,<br>
 * 这个对象中包含了基本的增删改查的Sql语句
 * 
 * @author xiaojinzi
 *
 */
public class SqlFactory {

	private static SqlFactory factory = null;

	/**
	 * 构造函数私有化
	 */
	private SqlFactory() {
	}

	/**
	 * 返回一个自己,实例对象
	 * 
	 * @return
	 */
	public static SqlFactory getInstance() {
		if (factory == null) {
			factory = new SqlFactory();
		}
		return factory;
	}

	private Map<Class<? extends Object>, DBEntity> map = new HashMap<Class<? extends Object>, DBEntity>();

	/**
	 * 获取一个实体对象的DBEntity对象 {@link DBEntity}
	 * 
	 * @param entity
	 * @return
	 */
	public DBEntity getSqlEntity(Class<? extends Object> clazz) {

		// 先从集合中获取
		DBEntity dbEntity = map.get(clazz);

		if (dbEntity == null) { // 如果是空的,就交给DBEntity自己解析出一个自己
			dbEntity = DBEntity.parse(clazz);
			map.put(clazz, dbEntity);
		}

		return dbEntity;

	}

}
