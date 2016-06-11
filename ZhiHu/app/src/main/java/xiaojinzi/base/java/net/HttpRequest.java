package xiaojinzi.base.java.net;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xiaojinzi.base.java.net.handler.ResponseHandler;

/**
 * 网络任务的对象
 *
 * @author cxj
 */
public class HttpRequest<Parameter> {

    /**
     * 请求是GET请求
     */
    public static final int GET = 0;

    /**
     * 请求是POST请求
     */
    public static final int POST = 1;

    /**
     * 本地文件的前缀标识
     */
    public static final String LOCALFILEURLPREFIX = "file:";


    /**
     * 加载数据是本地加载的
     */
    public static final int LOCAL = 11;

    /**
     * 加载数据是网络加载的
     */
    public static final int NET = 12;

    /**
     * 加载数据的方式
     */
    private int loadDataType = NET;

    /**
     * 请求的方式,默认是get请求
     */
    private int requestMethod = GET;


    /**
     * 请求的网址
     */
    private String requesutUrl;

    /**
     * 记录一下请求的数据类型,做缓存功能的时候有用
     */
    private int preResponseDataStyle = ResponseHandler.STRINGDATA;

    /**
     * 请求的数据类型,默认是字符串,处理返回的结果的时候就是根据这个来判断需要调用哪一个方法来处理数据,
     */
    private int responseDataStyle = ResponseHandler.STRINGDATA;

    /**
     * 请求完成后回调处理数据的接口
     */
    private ResponseHandler<Parameter> responseHandler = null;

    /**
     * 参数的数组,每个请求可以携带数据,传递给处理的方法
     */
    private Parameter[] parameters;

    /**
     * post的时候要提交的文件
     */
    private List<FileInfo> filesParameter = new ArrayList<FileInfo>();

    /**
     * 普通字段
     */
    private Map<String, String> textParameter = new HashMap<String, String>();

    /**
     * 请求的头
     */
    private Map<String, String> requestHeaders = new HashMap<String, String>();

    /**
     * 空参数构造函数
     */
    public HttpRequest() {
    }

    /**
     * 构造函数
     *
     * @param requesutUrl
     */
    public HttpRequest(String requesutUrl) {
        this.requesutUrl = requesutUrl;
    }

    /**
     * 构造函数
     *
     * @param requesutUrl
     * @param responseDataStyle
     */
    public HttpRequest(String requesutUrl, int responseDataStyle) {
        this.requesutUrl = requesutUrl;
        this.responseDataStyle = responseDataStyle;
    }

    public HttpRequest<Parameter> setRequesutUrl(String requesutUrl) {
        this.requesutUrl = requesutUrl;
        return this;
    }

    public String getRequesutUrl() {
        return requesutUrl;
    }

    public HttpRequest<Parameter> setResponseDataStyle(int responseDataStyle) {
        this.preResponseDataStyle = this.responseDataStyle;
        this.responseDataStyle = responseDataStyle;
        return this;
    }

    public HttpRequest<Parameter> setResponseHandler(ResponseHandler<Parameter> responseHandler) {
        this.responseHandler = responseHandler;
        return this;
    }


    public HttpRequest<Parameter> setParameters(Parameter... parameters) {
        this.parameters = parameters;
        return this;
    }

    public HttpRequest<Parameter> setRequestMethod(int requestMethod) {
        this.requestMethod = requestMethod;
        return this;
    }

    public void setLoadDataType(int loadDataType) {
        this.loadDataType = loadDataType;
    }

    public int getRequestMethod() {
        return requestMethod;
    }


    public int getPreResponseDataStyle() {
        return preResponseDataStyle;
    }

    public int getResponseDataStyle() {
        return responseDataStyle;
    }

    public ResponseHandler<Parameter> getResponseHandler() {
        return responseHandler;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public int getLoadDataType() {
        return loadDataType;
    }

    /**
     * 添加普通的参数
     *
     * @param name
     * @param value
     */
    public HttpRequest<Parameter> addTextParameter(String name, String value) {
        textParameter.put(name, value);
        return this;
    }

    /**
     * 添加请求头
     *
     * @param name
     * @param value
     * @return
     */
    public HttpRequest<Parameter> addRequestHeader(String name, String value) {
        requestHeaders.put(name, value);
        return this;
    }

    /**
     * 获取所有的普通字段
     *
     * @return
     */
    public Map<String, String> getTextParameter() {
        return textParameter;
    }

    /**
     * 获取头信息
     *
     * @return
     */
    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    /**
     * 添加要post的文件,只有请求方式是POST的时候起作用
     *
     * @param key
     * @param file
     */
    public HttpRequest<Parameter> addPostFile(String key, File file, String contentType) {
        //做了简装性质的判断
        if (key != null && !"".equals(key) && file != null && file.exists() && file.isFile()) {
            filesParameter.add(new FileInfo(key, file, contentType));
        }
        return this;
    }

    /**
     * 获取要提交的文件
     *
     * @return
     */
    public List<FileInfo> getFilesParameter() {
        return filesParameter;
    }

}
