package com.huaishi.wwww.base.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.huaishi.wwww.base.dao.BaseDao;
import com.huaishi.wwww.base.service.BaseService;
import com.huaishi.wwww.common.L;
import com.huaishi.wwww.common.ReflectionUtil;
import com.huaishi.wwww.common.Result;

public abstract class BaseServiceImpl<T> implements BaseService<T, Integer> {

	/**
	 * 插入前的检测,默认不检测
	 * 
	 * @param entity
	 * @return
	 */
	protected boolean checkForInsert(T entity, Result<T> result) {
		return true;
	}

	protected void afterInsert(T entity, Result<T> result) {
		result.entity = entity;
		result.resultText = "insert success";
	}

	@Transactional
	@Override
	public boolean save(T entity, Result<T> result) {

		boolean b = checkForInsert(entity, result);

		if (!b) {
			return false;
		}
		try {
			getBaseDao().save(entity);
			afterInsert(entity, result);
			return true;
		} catch (Exception e) {
			L.err(e);
			return false;
		}
	}

	/**
	 * 删除之前的检查
	 * 
	 * @param entity
	 * @param result
	 * @return
	 */
	protected boolean checkForDelete(T entity, Result<T> result) {
		return true;
	}

	protected void afterDelete(T entity, Result<T> result) {
		result.entity = entity;
		result.resultText = "delete success";
	}

	@Transactional
	@Override
	public boolean delete(T entity, Result<T> result) {
		boolean b = checkForDelete(entity, result);
		if (!b) {
			return false;
		}
		try {
			getBaseDao().delete(entity);
			afterDelete(entity, result);
			return true;
		} catch (Exception e) {
			L.err(e);
			return false;
		}
	}

	@Transactional
	@Override
	public boolean deleteByPk(Integer pk) {
		try {
			getBaseDao().deleteByPk(pk);
			return true;
		} catch (Exception e) {
			L.err(e);
			return false;
		}
	}

	@Transactional
	@Override
	public boolean deleteAll() {
		try {
			getBaseDao().deleteAll();
			return true;
		} catch (Exception e) {
			L.err(e);
			return false;
		}
	}

	/**
	 * 更新之前的检查
	 * 
	 * @param entity
	 * @return
	 */
	protected boolean checkBeforeUpdateById(T entity) {
		return true;
	}

	/**
	 * 更新之后
	 * 
	 * @param entity
	 * @param result
	 */
	protected void afterUpdateById(T entity, Result<T> result) {
		result.entity = entity;
		result.resultText = "update success";
	}

	/**
	 * 数据的更新,前台的实体的数据设置到后台的实体中<br>
	 * 如果子类需要更新功能,需要重写这个方法
	 * 
	 * @param entity
	 *            前台的实体
	 * @param readEntity
	 *            后台的实体
	 * @return 只要返回不是null,表示更新失败
	 */
	protected String doUpdate(T entity, T readEntity) {
		return null; // 只要返回不是null,表示更新失败
	}

	@Transactional
	@Override
	public boolean update(T entity, Result<T> result) {
		try {
			// 通过反射获取id的数值
			Integer id = ReflectionUtil.getFieldValue(entity, "id");
			if (id != null) {
				// 查询出来要更新的对象,更新这个步骤就是要更新的对象中的属性赋值给查询出来的对象,这个没法提取,只能子类实现
				T e = query(id);
				if (e != null) {
					String str = doUpdate(entity, e);
					if (str == null) {
						// 执行数据库的更新
						getBaseDao().update(e);
						// 调用更新之后的过程
						afterUpdateById(e, result);
					} else {
						result.resultText = str;
					}
				} else {
					result.resultText = "the entity is not exist";
				}
			} else {
				result.resultText = "the entity do not have 'id' property value";
			}

			return true;
		} catch (Exception e) {
			result.resultText = e.getMessage() + ";" + e.getCause().toString();
			L.err(e);
			return false;
		}
	}

	@Transactional
	@Override
	public T query(Integer pk) {
		return getBaseDao().query(pk);
	}

	@Transactional
	@Override
	public List<T> queryAll() {
		return getBaseDao().queryAll();
	}

	@Transactional
	@Override
	public List<T> queryForPagerList(String hql, Object... args) {
		return getBaseDao().queryForPagerList(hql, args);
	}

	@Transactional
	@Override
	public List<T> queryForPagerList(int startIndex, int pagerSize) {
		return getBaseDao().queryForPagerList(startIndex, pagerSize);
	}

	@Transactional
	@Override
	public List<T> queryForPagerList(int startIndex, int pagerSize, String where, Object... args) {
		return getBaseDao().queryForPagerList(startIndex, pagerSize, where, args);
	}

	// @Transactional
	// @Override
	// public List<T> queryForPagerList(int startIndex, int pagerSize, String
	// hql, Object... args) {
	// return getBaseDao().queryForPagerList(startIndex, pagerSize, hql, args);
	// }

	@Transactional
	@Override
	public long getCount() {
		return getBaseDao().getCount();
	}

	@Transactional
	@Override
	public long getCount(String where, Object... args) {
		return getBaseDao().getCount(where, args);
	}

	/**
	 * 初始化数据库操作对象
	 * 
	 * @return
	 */
	public abstract BaseDao<T, Integer> getBaseDao();

}
