package xiaojinzi.android.net;

import android.app.ProgressDialog;
import xiaojinzi.android.net.AsyncHttp.BaseDataHandler;
import xiaojinzi.android.net.AsyncHttp.ParameterHandler;

/**
 * 网路请求的任务
 * 
 * @author cxj QQ:347837667
 * @date 2015年12月8日
 *
 */
public class NetTask<Parameter> {

	/* 请求的网址 */
	public String url;

	/* 请求的数据类型 */
	public int responseDataStyle;

	/* 请求完成后回调的接口 */
	public BaseDataHandler dataHandler = null;

	public ParameterHandler<Parameter> parameterDataHandler = null;

	/* 进度条控件对象 */
	public ProgressDialog pd;

	/* 参数的数组 */
	public Parameter[] parameters;

	/**
	 * 是否使用缓存的json数据
	 */
	public boolean isUseJsonCache = true;

	public NetTask(String url, int responseDataStyle, BaseDataHandler dataHandler, ProgressDialog pd, boolean isUseJsonCache) {
		super();
		this.url = url;
		this.responseDataStyle = responseDataStyle;
		this.dataHandler = dataHandler;
		this.pd = pd;
		this.isUseJsonCache = isUseJsonCache;
	}

//	public NetTask(String url, int responseDataStyle, BaseDataHandler dataHandler, ProgressDialog pd,
//			Parameter... parameters) {
//		super();
//		this.url = url;
//		this.responseDataStyle = responseDataStyle;
//		this.dataHandler = dataHandler;
//		this.pd = pd;
//		this.parameters = parameters;
//	}

	public NetTask(String url, int responseDataStyle, ParameterHandler<Parameter> parameterDataHandler,
			ProgressDialog pd, boolean isUseJsonCache, Parameter... parameters) {
		super();
		this.url = url;
		this.responseDataStyle = responseDataStyle;
		this.parameterDataHandler = parameterDataHandler;
		this.pd = pd;
		this.isUseJsonCache = isUseJsonCache;
		this.parameters = parameters;
	}

}
