package com.example.cxj.zhihu.modular.collection.db;

import android.content.Context;

import com.example.cxj.zhihu.modular.collection.entity.DbStory;

import java.util.List;

import xiaojinzi.dbOrm.android.core.DBEntity;
import xiaojinzi.dbOrm.android.core.Db;
import xiaojinzi.dbOrm.android.core.SqlFactory;


/**
 * Created by cxj on 2016/4/1.
 */
public class MyCollectionDb {

    /**
     * 构造函数私有化
     */
    private MyCollectionDb() {
    }

    /**
     * 定义一个私有的静态的自己
     */
    private static MyCollectionDb myCollectionDb = null;

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
    public synchronized static MyCollectionDb getInstance(Context context) {
        if (myCollectionDb == null) {
            myCollectionDb = new MyCollectionDb();
            db = new Db(new MyCollectionDbOpenHelper(context));
        }
        return myCollectionDb;
    }

    /**
     * 插入一个实体对象
     *
     * @param dbStory
     * @return
     */
    public boolean insert(DbStory dbStory) {
        return db.insert(dbStory);
    }

    /**
     * 删除一个实体对象
     *
     * @param dbStory
     * @return
     */
    public boolean delete(DbStory dbStory) {
        return db.delete(dbStory);
    }

    /**
     * 更新一个对象
     *
     * @param dbStory
     * @return
     */
    public boolean update(DbStory dbStory) {
        return db.update(dbStory);
    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<DbStory> queryAll() {
        return (List<DbStory>) db.queryAll(DbStory.class);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public DbStory queryById(Integer id) {
        return (DbStory) db.querySingle(DbStory.class, id);
    }

    /**
     * 根据故事id查询实体对象
     *
     * @param storyId
     * @return
     */
    public DbStory queryByStoryId(Integer storyId) {
        if (storyId == null) {
            return null;
        }
        String sql = SqlFactory.getInstance().getSqlEntity(DbStory.class).getSql(DBEntity.QUERYALL_FLAG) +
                " where dbStoryId = ?";
        List<DbStory> dbStorys = (List<DbStory>) db.query(DbStory.class, sql, new String[]{storyId + ""});
        return dbStorys.size() == 0 ? null : dbStorys.get(0);
    }

}
