package com.huaishi.wwww.base.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.huaishi.wwww.base.dao.BaseDao;
import com.huaishi.wwww.common.StringUtil;

/**
 * 这个是数据库操作的基本对象,可以让继承这个类的数据库对象十分方便
 * 
 * @dateTime 2015年12月20日 下午6:39:48
 * @Company xjzCompany
 * @author xiaojinzi
 */
public abstract class BaseDaoImpl<T> implements BaseDao<T, Integer> {

	@Autowired
	protected SessionFactory sessionFactory;

	@Override
	public void save(T entity) {
		getSession().save(entity);
	}

	@Override
	public void delete(T entity) {
		getSession().delete(entity);
	}

	@Override
	public void deleteByPk(Integer pk) {
		T t = query(pk);
		getSession().delete(t);
	}

	@Override
	public void deleteAll() {
		getSession().delete("FROM " + StringUtil.getLastContent(getEntityClass().getName(), "."), null);
	}

	@Override
	public void update(T entity) {
		getSession().update(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T query(Integer pk) {
		return (T) getSession().get(getEntityClass(), pk);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> queryAll() {
		String hql = "FROM " + //
				StringUtil.getLastContent(getEntityClass().getName(), ".");
		Query query = getSession().createQuery(hql);
		return (List<T>) query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> queryForPagerList(String hql, Object... args) {
		Query query = getSession().createQuery(hql);
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				Object arg = args[i];
				query.setParameter(i, arg);
			}
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> queryForPagerList(int startIndex, int pagerSize) {
		String hql = "FROM " + //
				StringUtil.getLastContent(getEntityClass().getName(), ".");
		Query query = getSession().createQuery(hql);
		return (List<T>) query.setFirstResult(startIndex).setMaxResults(pagerSize).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> queryForPagerList(int startIndex, int pagerSize, String where, Object... args) {
		String hql = "FROM " + //
				StringUtil.getLastContent(getEntityClass().getName(), ".") + " e";
		if (where != null) {
			hql += " where " + where;
		}
		Query query = getSession().createQuery(hql);
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		return (List<T>) query.setFirstResult(startIndex).setMaxResults(pagerSize).list();
	}

	// @SuppressWarnings("unchecked")
	// @Override
	// public List<T> queryForPagerList(int startIndex, int pagerSize, String
	// hql, Object... args) {
	// Query query = getSession().createQuery(hql);
	// query.setFirstResult(startIndex).setMaxResults(pagerSize);
	// if (args != null) {
	// for (int i = 0; i < args.length; i++) {
	// query.setParameter(i, args[i]);
	// }
	// }
	// return query.list();
	// }

	@Override
	public long getCount() {
		// String sql = "select count(*) from " +
		// StringUtil.getLastContent(getEntityClass().getName(), ".") + " e ";
		// Query query = getSession().createQuery(sql);
		// List list = query.list();
		// if (list == null || list.size() == 0) {
		// return 0;
		// } else {
		// Number number = (Number) list.get(0);
		// return number.longValue();
		// }
		return getCount(null);
	}

	@Override
	public long getCount(String where, Object... args) {
		String sql = "select count(*) from " + StringUtil.getLastContent(getEntityClass().getName(), ".") + " e ";
		if (where != null) {
			sql += " where " + where;
		}
		Query query = getSession().createQuery(sql);

		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		@SuppressWarnings("unchecked")
		List<T> list = query.list();
		if (list == null || list.size() == 0) {
			return 0;
		} else {
			Number number = (Number) list.get(0);
			return number.longValue();
		}
	}

	/**
	 * 返回当前线程绑定的Session,和spring配合使用有效,<br>
	 * spring在开启事务的时候自动帮我绑定
	 * 
	 * @return
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 获取遥操作的实体对象的Class类型
	 * 
	 * @return
	 */
	public abstract Class<T> getEntityClass();

}
