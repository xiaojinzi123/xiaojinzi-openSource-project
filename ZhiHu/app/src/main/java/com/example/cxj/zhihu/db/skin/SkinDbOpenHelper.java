package com.example.cxj.zhihu.db.skin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cxj on 2016/3/24.
 */
public class SkinDbOpenHelper extends SQLiteOpenHelper {

    /**
     * 数据库文件的版本号
     */
    private static int version = 1;

    /**
     * 数据库文件名字
     */
    private static String name = "skin.db";

    /**
     * 构造方法
     * @param context
     */
    public SkinDbOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table jsonSkin(_id integer primary key autoincrement,jsonSkinId integer,skinName varChar(20),jsonSkinData text,imageUrl text,description text,useEnvironment integer)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
