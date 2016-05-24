package xiaojinzi.net.db;

import java.util.List;

import android.content.Context;

import xiaojinzi.dbOrm.android.core.DBEntity;
import xiaojinzi.dbOrm.android.core.Db;
import xiaojinzi.dbOrm.android.core.SqlFactory;


/**
 * json缓存的表的数据库操作
 * 
 * @author cxj QQ:347837667
 * @date 2015年12月10日
 *
 */
public class JsonCacheDao {

	// 只支持基本数据类型
	private Db db = null;

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public JsonCacheDao(Context context) {
		db = new Db(new NetCacheDb(context));
	}

	/**
	 * 添加
	 * 
	 * @param jsonCache
	 */
	public synchronized void insert(JsonCache jsonCache) {
		jsonCache.setCacheDate(System.currentTimeMillis() + "");
		db.insert(jsonCache);
	}

	/**
	 * 更新
	 * 
	 * @param jsonCache
	 */
	public synchronized void update(JsonCache jsonCache) {
		db.update(jsonCache);
	}

	/**
	 * 根据json请求的网址删除记录
	 * 
	 * @param url
	 */
	public synchronized void deleteByUrl(String url) {
		String sql = SqlFactory.getInstance().getSqlEntity(JsonCache.class).getSql(DBEntity.DELETEALL_FLAG);
		sql = sql + " where jsonUrl = ?";
		db.execute(sql, new Object[] { url });
	}

	/**
	 * 返回json请求的网址的jsonCache对象
	 * 
	 * @param url
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized List<JsonCache> selectByUrl(String url) {
		String sql = SqlFactory.getInstance().getSqlEntity(JsonCache.class).getSql(DBEntity.QUERYALL_FLAG);
		sql = sql + " where jsonUrl = ?";
		return (List<JsonCache>) db.query(JsonCache.class, sql, new String[] { url });
	}

	/**
	 * 返回一个url对应的记录是否存在
	 * 
	 * @param url
	 * @return
	 */
	public synchronized boolean isUrlExist(String url) {
		List<JsonCache> list = selectByUrl(url);
		return list == null || list.size() == 0 ? false : true;
	}

}
