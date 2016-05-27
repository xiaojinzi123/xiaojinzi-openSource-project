package xiaojinzi.android.net;

import xiaojinzi.android.net.AsyncHttp.BaseDataHandler;
import xiaojinzi.android.net.AsyncHttp.ParameterHandler;

/**
 * 任务执行完毕后结果的处理
 *
 * @author cxj QQ:347837667
 * @date 2015年12月8日
 */
public class ResultInfo<Parameter> {

    /**
     * 加载数据是本地加载的
     */
	public static final int LOCAL = 1;

    /**
     * 加载数据是网络加载的
     */
	public static final int NET = 2;

    public Parameter[] parameters;

    /* 请求的网址 */
    public String url;

    /* 返回的结果 */
    public Object result = null;

    /* 返回的数据是何种数据类型 */
    public int responseDataStyle;

    /**
     * 请求对象希望请求的下来的数据格式,在发生错误的时候,
     * 上面的responseDataStyle变量就会被修改,
     * 这个变量可以保存最初希望得到的数据类型
     */
    public int netTaskResponseDataStyle;

    /* 请求完成后回调的接口 */
    public BaseDataHandler dataHandler = null;

    public ParameterHandler<Parameter> parameterDataHandler;

    /**
     * 是否使用缓存的json数据
     */
    public boolean isUseJsonCache = true;

    /**
     * 加载数据的方式
     */
    public int loadDataType = NET;

}
