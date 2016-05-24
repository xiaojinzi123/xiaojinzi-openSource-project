package com.example.cxj.zhihu.db.skin;

import android.content.Context;

import com.example.cxj.zhihu.modular.skin.entity.JsonSkin;

import java.util.List;

import xiaojinzi.dbOrm.android.core.DBEntity;
import xiaojinzi.dbOrm.android.core.Db;
import xiaojinzi.dbOrm.android.core.SqlFactory;


/**
 * Created by cxj on 2016/3/24.
 */
public class SkinDb {

    /**
     * 构造函数私有化
     */
    private SkinDb() {
    }

    /**
     * 定义一个私有的静态的自己
     */
    private static SkinDb skinDb = null;

    /**
     * 数据库框架
     */
    private static Db db = null;

    /**
     * 获取数据库操作对象的实例
     *
     * @param context 上下文对象
     * @return
     */
    public synchronized static SkinDb getInstance(Context context) {
        if (skinDb == null) {
            skinDb = new SkinDb();
            db = new Db(new SkinDbOpenHelper(context));
        }
        return skinDb;
    }

    /**
     * 插入一个实体对象
     *
     * @param jsonSkin
     * @return
     */
    public boolean insert(JsonSkin jsonSkin) {
        return db.insert(jsonSkin);
    }

    public boolean delete(JsonSkin jsonSkin) {
        return db.delete(jsonSkin);
    }

    public boolean update(JsonSkin jsonSkin) {
        return db.update(jsonSkin);
    }

    public List<JsonSkin> queryAll() {
        return (List<JsonSkin>) db.queryAll(JsonSkin.class);
    }

    public JsonSkin queryById(Integer id) {
        return (JsonSkin) db.querySingle(JsonSkin.class, id);
    }

    /**
     * 根据皮肤id获取实体对象
     *
     * @param skinId
     * @return
     */
    public JsonSkin queryBySkinId(Integer skinId) {
        String sql = SqlFactory.getInstance().getSqlEntity(JsonSkin.class).getSql(DBEntity.QUERYALL_FLAG) +
                " where jsonSkinId = ?";
        List<?> list = db.query(JsonSkin.class, sql, new String[]{skinId + ""});
        if (list == null || list.size() == 0) {
            return null;
        } else {
            return (JsonSkin) list.get(0);
        }
    }

}
