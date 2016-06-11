package com.example.cxj.zhihu.db.json;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * json数据缓存的数据库
 * 
 * @author cxj QQ:347837667
 * @date 2015年12月12日
 *
 */
public class NetCacheDb extends SQLiteOpenHelper {

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public NetCacheDb(Context context) {
		super(context, "netCache.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table jsonCache(_id integer primary key autoincrement,jsonUrl varchar(200),json text,cacheDate varchar(40))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
