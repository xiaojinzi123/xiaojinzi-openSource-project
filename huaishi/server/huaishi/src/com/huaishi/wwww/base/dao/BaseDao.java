package com.huaishi.wwww.base.dao;

import java.util.List;

/**
 * 对数据库操作的接口
 * 
 * @dateTime 2015年11月21日 下午1:09:01
 * @Company xjzCompany
 * @author xiaojinzi
 */
public interface BaseDao<T, PK> {

	/**
	 * 保存一个实体对象
	 * 
	 * @param entity
	 */
	public void save(T entity);

	/**
	 * 删除一个实体对象
	 * 
	 * @param entity
	 */
	public void delete(T entity);

	/**
	 * 删除一个实体对象
	 * 
	 * @param entity
	 */
	public void deleteByPk(PK pk);

	/**
	 * 删除全部
	 */
	public void deleteAll();

	/**
	 * 更新一个实体对象
	 * 
	 * @param entity
	 */
	public void update(T entity);

	/**
	 * 根据主键查询一个实体对象
	 * 
	 * @param pk
	 * @return
	 */
	public T query(PK pk);

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public List<T> queryAll();

	/**
	 * 根据hql语句查询集合
	 * 
	 * @param hql
	 *            hibernate的hql语句
	 * @param args
	 *            占位符的数据
	 * @return
	 */
	public List<T> queryForPagerList(String hql, Object... args);

	/**
	 * 分页查询
	 * 
	 * @param startIndex
	 * @param pagerSize
	 * @return
	 */
	public List<T> queryForPagerList(int startIndex, int pagerSize);

	/**
	 * 根据条件分页查询
	 * 
	 * @param startIndex
	 *            起始下标
	 * @param pagerSize
	 *            取出的数据条数
	 * @param where
	 *            条件
	 * @return
	 */
	public List<T> queryForPagerList(int startIndex, int pagerSize, String where, Object... args);

	/**
	 * 对有条件的限制的进行分页查询
	 * 
	 * @param startIndex
	 * @param pagerSize
	 * @param hql
	 * @param args
	 *            条件
	 * @return
	 */
	// public List<T> queryForPagerList(int startIndex, int pagerSize, String
	// hql, Object... args);

	/**
	 * 获取总记录数目
	 * 
	 * @return
	 */
	public long getCount();

	/**
	 * 获取总数,有条件
	 * 
	 * @param where
	 *            条件
	 * @param args
	 *            占位符的数据
	 * @return
	 */
	public long getCount(String where, Object... args);

}
