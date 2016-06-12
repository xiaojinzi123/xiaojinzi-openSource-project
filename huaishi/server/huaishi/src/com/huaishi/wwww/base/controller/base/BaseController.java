package com.huaishi.wwww.base.controller.base;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huaishi.wwww.base.service.BaseService;
import com.huaishi.wwww.common.Constant;
import com.huaishi.wwww.common.L;
import com.huaishi.wwww.common.Msg;
import com.huaishi.wwww.common.Paging;
import com.huaishi.wwww.common.ReflectionUtil;
import com.huaishi.wwww.common.Result;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 控制器基本的增删改查的基类
 * 
 * @dateTime 2015年12月29日 下午4:10:57
 * @Company xjzCompany
 * @author xiaojinzi
 */
public abstract class BaseController<Entity> {

	/**
	 * 类的标识
	 */
	private static final String TAG = "BaseController";

	protected static Msg msg = new Msg();

	/**
	 * 获取业务操作对象
	 * 
	 * @return
	 */
	protected abstract BaseService<Entity, Integer> getBaseService();

	/**
	 * 获取显示的页面地址,拼凑到springMvc的路径中
	 * 
	 * @return
	 */
	protected String getListUrl() {
		return null;
	}

	/**
	 * 转发到显示的界面
	 * 
	 * @param request
	 * @param response
	 */
	protected void goToListPager(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void beforInsert(Entity m) {
	}

	protected void aterfInsert(Entity m) {
	}

	/**
	 * 增加实体对象
	 * 
	 * @param m
	 * @param isReturnJson
	 *            是否返回json数据
	 * @param isWriteObject
	 *            是否返回插入成功的实体对象
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping("insert")
	public void insert(Entity m, Boolean isReturnJson, Boolean isWriteObject, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		boolean b = false;

		beforInsert(m);

		// 创建结果对象
		Result<Entity> result = new Result<Entity>();

		// 让业务对象去保存
		b = getBaseService().save(m, result);

		L.s(TAG, "添加实体成功了么:" + b);

		if (isReturnJson == null || isReturnJson == false) {
			goToListPager(request, response);
			return;
		}

		if (b) {
			setMsgInfo(true, result.resultText, isWriteObject != null && isWriteObject == true ? result.entity : null,
					getJsonConfig(), response);
		} else {
			setMsgInfo(false, result.resultText, getJsonConfig(), response);
		}

	}

	protected void befordeleteById(Entity m) {
	}

	protected void afterdeleteById(Entity m) {
	}

	@RequestMapping("deleteById")
	public void deleteById(HttpServletRequest request, Boolean isReturnJson, HttpServletResponse response, Entity m)
			throws ServletException, IOException {

		boolean b = false;

		befordeleteById(m);

		Result<Entity> result = new Result<Entity>();

		// 通过反射获取id的数值
		Integer id = ReflectionUtil.getFieldValue(m, "id");
		if (id != null) {
			Entity e = getBaseService().query(id);
			b = getBaseService().delete(e, result);
		}

		afterdeleteById(m);

		L.s(TAG, "删除成功了么:" + b);

		if (isReturnJson == null || isReturnJson == false) {
			goToListPager(request, response);
			return;
		}

		if (b) {
			setMsgInfo(true, result.resultText, getJsonConfig(), response);
		} else {
			setMsgInfo(false, result.resultText, getJsonConfig(), response);
		}

	}

	protected void beforupdateById(Entity m) {
	}

	protected void afterupdateById(Entity m) {
	}

	@RequestMapping("updateById")
	public void updateById(HttpServletRequest request, Boolean isReturnJson, HttpServletResponse response, Entity m)
			throws ServletException, IOException {

		boolean b = false;

		beforupdateById(m);

		Result<Entity> result = new Result<Entity>();

		b = getBaseService().update(m, result);

		afterupdateById(m);

		L.s(TAG, "更新成功了么:" + b);

		if (isReturnJson == null || isReturnJson == false) {
			goToListPager(request, response);
			return;
		}
		if (b) {
			setMsgInfo(true, result.resultText, getJsonConfig(), response);
		} else {
			setMsgInfo(false, result.resultText, getJsonConfig(), response);
		}

	}

	/**
	 * 获取查询时候的条件
	 * 
	 * @return
	 */
	public String getQueryForListCondition() {
		return null;
	}

	/**
	 * 获取条件的数据
	 * 
	 * @return
	 */
	public Object[] getQueryForListArgs() {
		return null;
	}

	protected void beforlist() {
	}

	protected void afterlist() {
	}

	@RequestMapping("list")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response, //
			Integer nowPage, Integer pageSize) {

		ModelAndView mv = new ModelAndView();

		beforlist();

		Paging<Entity> p = new Paging<Entity>(pageSize == null ? Constant.back.pageSize : pageSize,
				getBaseService().getCount(getQueryForListCondition(), getQueryForListArgs()));

		if (nowPage != null) { // 如果有当前页参数,设置到分页对象中
			p.setNowPage(nowPage);
		}

		String where = getQueryForListCondition();

		// 分页查询
		List<Entity> entitys = getBaseService().queryForPagerList((int) p.getStartIndex(), p.getPageSize(), where,
				getQueryForListArgs());

		afterlist();

		p.setRows(entitys);
		mv.addObject("uri", getEntityBaseUri());
		mv.addObject("p", p);
		mv.setViewName(getListUrl());
		return mv;
	}
	
	/**
	 * 获取这个控制器的访问路径
	 * 
	 * @return
	 */
	private String getEntityBaseUri() {
		Class<?> clazz = this.getClass();
		boolean b = clazz.isAnnotationPresent(RequestMapping.class);
		if (b) {
			RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
			String[] values = requestMapping.value();
			return values[0];
		}
		return null;
	}

	/**
	 * 设置json返回的数据和提示信息,没有返回的实体对象
	 * 
	 * @param isOk
	 *            是否是成功的
	 * @param msgText
	 *            提示信息
	 * @param response
	 *            响应对象
	 * @throws IOException
	 *             可能抛出的异常
	 */
	public static void setMsgInfo(boolean isOk, String msgText, JsonConfig cfg, HttpServletResponse response)
			throws IOException {
		setMsgInfo(isOk, msgText, null, cfg, response);
	}

	/**
	 * 设置json返回的数据和提示信息
	 * 
	 * @param isOk
	 *            是否是成功的
	 * @param msgText
	 *            提示信息
	 * @param data
	 *            返回的数据,会转化成json
	 * @param cfg
	 *            json的配置
	 * @param response
	 *            响应对象
	 * @throws IOException
	 *             可能抛出的异常
	 */
	public static void setMsgInfo(boolean isOk, String msgText, Object data, JsonConfig cfg,
			HttpServletResponse response) throws IOException {
		msg.setData(data);
		msg.setMsg(isOk == true ? Msg.OK : Msg.ERROR);
		msg.setMsgText(msgText);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		if (cfg == null) {
			response.getWriter().write(JSONObject.fromObject(msg).toString());
		} else {
			response.getWriter().write(JSONObject.fromObject(msg, cfg).toString());
		}
	}

	/**
	 * 获取json的配置
	 * 
	 * @return
	 */
	protected JsonConfig getJsonConfig() {
		return null;
	}

}
