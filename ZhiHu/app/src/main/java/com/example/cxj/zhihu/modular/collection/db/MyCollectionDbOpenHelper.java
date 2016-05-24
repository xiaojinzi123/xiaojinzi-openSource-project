package com.example.cxj.zhihu.modular.collection.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cxj on 2016/4/1.
 */
public class MyCollectionDbOpenHelper extends SQLiteOpenHelper {

    /**
     * 版本号
     */
    private static int version = 1;

    /**
     * 数据库的名字
     */
    private static String name = "dbStory.db";

    /**
     * 构造函数
     *
     * @param context
     */
    public MyCollectionDbOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table dbStory(_id integer primary key autoincrement,dbStoryId integer,title varChar(20),ga_prefix varChar(20),image text,type integer,date text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
