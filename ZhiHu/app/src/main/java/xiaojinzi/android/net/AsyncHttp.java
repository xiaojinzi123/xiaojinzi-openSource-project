package xiaojinzi.android.net;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import xiaojinzi.android.net.db.JsonCache;
import xiaojinzi.android.net.db.JsonCacheDao;
import xiaojinzi.android.net.filter.NetFilter;
import xiaojinzi.android.util.common.StringUtil;
import xiaojinzi.android.util.log.L;
import xiaojinzi.java.net.Http;
import xiaojinzi.java.util.FileUtil;
import xiaojinzi.java.util.InputStreamUtil;

/**
 * 异步请求的一个小框架, 可以多次调用get进行请求数据,<br>
 * 但是回调接口是不同的即可<br>
 * 1.如果要使用json的缓存,请调用AsyncHttp.initCacheJson(Context context)方法<br>
 * 来初始化一个默认的拦截器,这个拦截器实现了当访问的网址出现在数据库当中的时候,将使用缓存,<br>
 * 访问失败的时候,根据访问的网址去数据库检索,如果有, 则返回数据库中的信息,轻松实现没有网络<br>
 * 的情况的缓存数据
 *
 * @param <Parameter>
 * @author cxj QQ:347837667
 * @date 2015年12月9日
 */
public class AsyncHttp<Parameter> implements Runnable {

    /**
     * 类的标识
     */
    protected static final String TAG = "AsyncHttp";


    /**
     * 控制是否输出log
     */
    private static final boolean isLog = false;

    /**
     * 默认使用的编码方式
     */
    public static String CHARENCODING = "UTF-8";

    /**
     * 网络请求的过滤器
     */
    private static Vector<NetFilter> netFilters = new Vector<NetFilter>();

    /**
     * 如果使用缓存机制,这个数据库的操作对象就会被用到
     */
    private static JsonCacheDao cacheDao;

    /**
     * 设置缓存的拦截器
     */
    public static void initCacheJson(Context context) {

        cacheDao = new JsonCacheDao(context);

        // 添加一个json数据的拦截器
        netFilters.add(new NetFilter() {
            @Override
            public boolean resultInfoReturn(ResultInfo<?> resultInfo) { // 数据返回,但是还没有处理的时候

                // 获取请求的网址
                String url = resultInfo.url;

                // 如果请求的数据是字符串
                if (resultInfo.responseDataStyle == BaseDataHandler.STRINGDATA && resultInfo.loadDataType == ResultInfo.NET) {

                    // 根据这个url进行数据库的查询
                    List<JsonCache> list = cacheDao.selectByUrl(url);

                    // 如果有记录,就拿到json,塞到结果的对象中
                    if (list == null || list.size() == 0) { //网络访问成功之后,如果数据库中没有,那么就插入,如果有,那么更新一下
                        cacheDao.insert(new JsonCache(null, url, (String) resultInfo.result));
                        if (isLog)
                            L.s(TAG, "缓存成功:" + url);
                    } else {
                        JsonCache jsonCache = list.get(0);
                        jsonCache.setJson((String) resultInfo.result);
                        cacheDao.update(jsonCache);
                    }

                } else if (resultInfo.responseDataStyle == BaseDataHandler.ERRORDATA && resultInfo.netTaskResponseDataStyle == BaseDataHandler.STRINGDATA) { // 如果是请求错误的情况,并且原来是希望请求字符串类型的数据的

                    // 根据这个url进行数据库的查询
                    List<JsonCache> list = cacheDao.selectByUrl(url);

                    // 如果有记录,就拿到json,塞到结果的对象中
                    if (list != null && list.size() != 0) {
                        resultInfo.result = list.get(0).getJson();
                        resultInfo.responseDataStyle = BaseDataHandler.STRINGDATA;
                        if (isLog)
                            L.s(TAG, "访问网络失败,但是库中有缓存,使用了缓存数据:" + url);
                    }
                }

                return false;
            }

            @Override
            public boolean netTaskPrepare(NetTask<?> netTask) { // 请求准备的时候
                return false;
            }

            @Override
            public boolean netTaskBegin(NetTask<?> netTask, ResultInfo<?> resultInfo) { // 结果对象创建,准备取请求数据,装载到结果对象里面之前

                // 获取要请求的网址
                String url = netTask.url;

                // 如果请求的数据是字符串,并且是使用缓存的
                if (netTask.responseDataStyle == BaseDataHandler.STRINGDATA && netTask.isUseJsonCache) {

                    // 根据这个url进行数据库的查询
                    List<JsonCache> list = cacheDao.selectByUrl(url);

                    // 如果有记录,就拿到json,塞到结果的对象中
                    if (list != null && list.size() > 0) {
                        JsonCache jsonCache = list.get(0);
                        resultInfo.result = jsonCache.getJson();
                        resultInfo.loadDataType = ResultInfo.LOCAL;

                        if (isLog)
                            L.s(TAG, "拦截一次请求,数据库中存在:" + url);

                        // 返回true,表示拦截这次的请求
                        return true;
                    }
                }

                return false;
            }
        });
    }

    /**
     * 执行的任务对象集合
     */
    private Vector<NetTask<Parameter>> netTasks = new Vector<NetTask<Parameter>>();

    /**
     * 结果信息对象集合
     */
    private Vector<ResultInfo<Parameter>> resultInfos = new Vector<ResultInfo<Parameter>>();

    @SuppressLint("HandlerLeak")
    private Handler h = new Handler() {
        public void handleMessage(android.os.Message msg) {

            // 取出一个结果
            ResultInfo<Parameter> resultInfo = resultInfos.remove(0);

            for (int i = 0; i < netFilters.size(); i++) {
                NetFilter filter = netFilters.get(i);
                boolean b = filter.resultInfoReturn(resultInfo);
                if (b) {
                    if (isLog) {
                        L.s(TAG, "返回的结果被拦截");
                    }
                    resultInfo = null;
                    return;
                }
            }

            if (resultInfo.dataHandler == null && resultInfo.parameterDataHandler == null) {
                return;
            }

            // 拿到返回数据的类型
            int responseDataStyle = resultInfo.responseDataStyle;

            try {
                if (responseDataStyle == BaseDataHandler.INPUTSTREAMDATA) {
                    InputStream is = (InputStream) resultInfo.result;
                    if (resultInfo.dataHandler == null) {
                        resultInfo.parameterDataHandler.handler(is, resultInfo.parameters);
                    } else {
                        resultInfo.dataHandler.handler(is);
                    }
                } else if (responseDataStyle == BaseDataHandler.BYTEARRAYDATA) {
                    byte[] bt = (byte[]) resultInfo.result;
                    if (resultInfo.dataHandler == null) {
                        resultInfo.parameterDataHandler.handler(bt, resultInfo.parameters);
                    } else {
                        resultInfo.dataHandler.handler(bt);
                    }
                } else if (responseDataStyle == BaseDataHandler.STRINGDATA) {
                    String content = (String) resultInfo.result;
                    if (resultInfo.dataHandler == null) {
                        resultInfo.parameterDataHandler.handler(content, resultInfo.parameters);
                    } else {
                        resultInfo.dataHandler.handler(content);
                    }
                } else if (responseDataStyle == BaseDataHandler.FILEDATA) {
                    File f = (File) resultInfo.result;
                    if (resultInfo.dataHandler == null) {
                        resultInfo.parameterDataHandler.handler(f, resultInfo.parameters);
                    } else {
                        resultInfo.dataHandler.handler(f);
                    }
                } else if (responseDataStyle == BaseDataHandler.ERRORDATA) {
                    Exception e = (Exception) resultInfo.result;
                    if (resultInfo.dataHandler == null) {
                        resultInfo.parameterDataHandler.error(e, resultInfo.parameters);
                    } else {
                        resultInfo.dataHandler.error(e);
                    }
                }
            } catch (Exception e) {

                if (isLog) {
                    L.s(TAG, "挂了");
                    e.printStackTrace();
                }
                if (resultInfo.dataHandler == null) {
                    resultInfo.parameterDataHandler.error(e, resultInfo.parameters);
                } else {
                    resultInfo.dataHandler.error(e);
                }
            }

        }

        ;
    };

    /**
     * 添加过滤器
     *
     * @param netFilter
     */
    public static void addNetFilter(NetFilter netFilter) {
        netFilters.add(netFilter);
    }

    /**
     * get请求封装,返回字符串,默认使用json缓存
     *
     * @param spec        请求的网址
     * @param dataHandler {@link BaseDataHandler}
     *                    数据请求后回调的接口,可以为null,这样子框架认为只是为了提交数据,不为了对返回的数据做处理
     */
    public void get(String spec, BaseDataHandler dataHandler) {
        get(spec, BaseDataHandler.STRINGDATA, dataHandler);
    }

    /**
     * get请求封装,返回字符串,不使用json缓存
     *
     * @param spec        请求的网址
     * @param dataHandler {@link BaseDataHandler}
     *                    数据请求后回调的接口,可以为null,这样子框架认为只是为了提交数据,不为了对返回的数据做处理
     */
    public void getWithoutJsonCache(String spec, BaseDataHandler dataHandler) {
        getWithoutJsonCache(spec, BaseDataHandler.STRINGDATA, dataHandler);
    }

    /**
     * get请求封装
     *
     * @param spec              请求的网址
     * @param responseDataStyle 响应的数据的形式 {@link BaseDataHandler#INPUTSTREAMDATA } or
     *                          {@link BaseDataHandler#BYTEARRAYDATA}or
     *                          {@link BaseDataHandler#STRINGDATA}or
     *                          {@link BaseDataHandler#FILEDATA} or
     *                          {@link BaseDataHandler#ERRORDATA}
     * @param dataHandler       {@link BaseDataHandler}
     *                          数据请求后回调的接口,可以为null,这样子框架认为只是为了提交数据,不为了对返回的数据做处理
     */
    public void get(String spec, int responseDataStyle, BaseDataHandler dataHandler) {
        get(spec, responseDataStyle, dataHandler, null, true);
    }

    /**
     * get请求封装,不使用json缓存
     *
     * @param spec              请求的网址
     * @param responseDataStyle 响应的数据的形式 {@link BaseDataHandler#INPUTSTREAMDATA } or
     *                          {@link BaseDataHandler#BYTEARRAYDATA}or
     *                          {@link BaseDataHandler#STRINGDATA}or
     *                          {@link BaseDataHandler#FILEDATA} or
     *                          {@link BaseDataHandler#ERRORDATA}
     * @param dataHandler       {@link BaseDataHandler}
     *                          数据请求后回调的接口,可以为null,这样子框架认为只是为了提交数据,不为了对返回的数据做处理
     */
    public void getWithoutJsonCache(String spec, int responseDataStyle, BaseDataHandler dataHandler) {
        get(spec, responseDataStyle, dataHandler, null, false);
    }

    /**
     * get请求封装
     *
     * @param spec              请求的网址
     * @param responseDataStyle 响应的数据的形式 {@link BaseDataHandler#INPUTSTREAMDATA } or
     *                          {@link BaseDataHandler#BYTEARRAYDATA}or
     *                          {@link BaseDataHandler#STRINGDATA}or
     *                          {@link BaseDataHandler#FILEDATA} or
     *                          {@link BaseDataHandler#ERRORDATA}
     * @param dataHandler       {@link BaseDataHandler}
     *                          数据请求后回调的接口,可以为null,这样子框架认为只是为了提交数据,不为了对返回的数据做处理
     * @param pd                进度条对话框
     */
    public void get(String spec, int responseDataStyle, BaseDataHandler dataHandler, ProgressDialog pd,
                    boolean isUseJsonCache) {

        // 添加一个网络任务到集合中
        netTasks.add(new NetTask<Parameter>(spec, responseDataStyle, dataHandler, pd, isUseJsonCache));

        new Thread(this).start();

    }

    /**
     * get请求封装,可以传递数据,返回字符串,默认使用json数据的缓存
     *
     * @param spec                 请求的网址
     * @param parameterDataHandler {@link ParameterHandler}
     *                             数据请求后回调的接口,可以为null,这样子框架认为只是为了提交数据,不为了对返回的数据做处理
     * @param parameters           传递的参数
     */
    public void get(String spec, ParameterHandler<Parameter> parameterDataHandler, Parameter... parameters) {
        get(spec, ParameterHandler.STRINGDATA, parameterDataHandler, parameters);
    }

    /**
     * get请求封装,可以传递数据,返回字符串,不使用json数据的缓存
     *
     * @param spec                 请求的网址
     * @param parameterDataHandler {@link ParameterHandler}
     *                             数据请求后回调的接口,可以为null,这样子框架认为只是为了提交数据,不为了对返回的数据做处理
     * @param parameters           传递的参数
     */
    public void getWithoutJsonCache(String spec, ParameterHandler<Parameter> parameterDataHandler,
                                    Parameter... parameters) {
        getWithoutJsonCache(spec, ParameterHandler.STRINGDATA, parameterDataHandler, parameters);
    }

    /**
     * get请求封装,可以传递数据,<br>
     * 如果是请求的是json数据,默认使用json缓存
     *
     * @param spec                 请求的网址
     * @param responseDataStyle    响应的数据的形式 {@link ParameterHandler#INPUTSTREAMDATA } or
     *                             {@link ParameterHandler#BYTEARRAYDATA}or
     *                             {@link ParameterHandler#STRINGDATA}or
     *                             {@link ParameterHandler#FILEDATA} or
     *                             {@link ParameterHandler#ERRORDATA}
     * @param parameterDataHandler {@link ParameterHandler}
     *                             数据请求后回调的接口,可以为null,这样子框架认为只是为了提交数据,不为了对返回的数据做处理
     * @param parameters           传递的参数
     */
    public void get(String spec, int responseDataStyle, ParameterHandler<Parameter> parameterDataHandler,
                    Parameter... parameters) {
        get(spec, responseDataStyle, parameterDataHandler, null, true, parameters);
    }

    /**
     * get请求封装,可以传递数据,<br>
     * 如果是请求的是json数据,不使用json缓存
     *
     * @param spec                 请求的网址
     * @param responseDataStyle    响应的数据的形式 {@link ParameterHandler#INPUTSTREAMDATA } or
     *                             {@link ParameterHandler#BYTEARRAYDATA}or
     *                             {@link ParameterHandler#STRINGDATA}or
     *                             {@link ParameterHandler#FILEDATA} or
     *                             {@link ParameterHandler#ERRORDATA}
     * @param parameterDataHandler {@link ParameterHandler}
     *                             数据请求后回调的接口,可以为null,这样子框架认为只是为了提交数据,不为了对返回的数据做处理
     * @param parameters           传递的参数
     */
    public void getWithoutJsonCache(String spec, int responseDataStyle,
                                    ParameterHandler<Parameter> parameterDataHandler, Parameter... parameters) {
        get(spec, responseDataStyle, parameterDataHandler, null, false, parameters);
    }

    /**
     * get请求封装,可以传递数据
     *
     * @param spec                 请求的网址
     * @param responseDataStyle    响应的数据的形式 {@link ParameterHandler#INPUTSTREAMDATA } or
     *                             {@link ParameterHandler#BYTEARRAYDATA}or
     *                             {@link ParameterHandler#STRINGDATA}or
     *                             {@link ParameterHandler#FILEDATA} or
     *                             {@link ParameterHandler#ERRORDATA}
     * @param parameterDataHandler {@link ParameterHandler}
     *                             数据请求后回调的接口,可以为null,这样子框架认为只是为了提交数据,不为了对返回的数据做处理
     * @param pd                   进度条对话框
     * @param parameters           传递的参数
     */
    public void get(String spec, int responseDataStyle, ParameterHandler<Parameter> parameterDataHandler,
                    final ProgressDialog pd, boolean isUseJsonCache, Parameter... parameters) {

        // 添加一个网络任务到集合中
        netTasks.add(
                new NetTask<Parameter>(spec, responseDataStyle, parameterDataHandler, pd, isUseJsonCache, parameters));

        new Thread(this).start();

    }

    /**
     * 可以传递参数的回调接口
     *
     * @param <Parameter>
     * @author cxj QQ:347837667
     * @date 2015年12月8日
     */
    public interface ParameterHandler<Parameter> {

        /**
         * 没有任何事情发生
         */
        public static final int NOTHING = -2;

        /**
         * 错误的形式
         */
        public static final int ERRORDATA = -1;

        /**
         * 流的形式
         */
        public static final int INPUTSTREAMDATA = 0;

        /**
         * 字节数组的形式
         */
        public static final int BYTEARRAYDATA = 1;

        /**
         * 字符串形式
         */
        public static final int STRINGDATA = 2;

        /**
         * 文件的形式
         */
        public static final int FILEDATA = 3;

        /**
         * 返回的是字符串的时候调用
         *
         * @param data
         */
        public void handler(String data, Parameter... p) throws Exception;

        /**
         * 返回的是输入流的时候调用
         *
         * @param is
         */
        public void handler(InputStream is, Parameter... p) throws Exception;

        /**
         * 返回的是字节数组的时候调用
         *
         * @param bt
         */
        public void handler(byte[] bt, Parameter... p) throws Exception;

        /**
         * 当请求的是文件的时候,这个方法必须实现,并且返回一个文件对象,框架会把数据存放到返回的这个文件对象中
         *
         * @return
         */
        public File getFile() throws Exception;

        ;

        /**
         * 当文件存放完毕,调用这个方法
         *
         * @param f
         */
        public void handler(File f, Parameter... p) throws Exception;

        ;

        /**
         * 访问错误的时候调用
         *
         * @param e
         */
        public void error(Exception e, Parameter... p);
    }

    /**
     * 数据处理的接口,这个是上面的接口进行了升级,可以抛出异常<br>
     * 为了以前的代码还能运行,此接口是过时的接口的父类
     *
     * @author cxj
     */
    public interface BaseDataHandler {
        /**
         * 没有任何事情发生
         */
        public static final int NOTHING = -2;

        /**
         * 错误的形式
         */
        public static final int ERRORDATA = -1;

        /**
         * 流的形式
         */
        public static final int INPUTSTREAMDATA = 0;

        /**
         * 字节数组的形式
         */
        public static final int BYTEARRAYDATA = 1;

        /**
         * 字符串形式
         */
        public static final int STRINGDATA = 2;

        /**
         * 文件的形式
         */
        public static final int FILEDATA = 3;

        /**
         * 返回的是字符串的时候调用
         *
         * @param data
         */
        public void handler(String data) throws Exception;

        /**
         * 返回的是输入流的时候调用
         *
         * @param is
         */
        public void handler(InputStream is) throws Exception;

        /**
         * 返回的是字节数组的时候调用
         *
         * @param bt
         */
        public void handler(byte[] bt) throws Exception;

        /**
         * 当请求的是文件的时候,这个方法必须实现,并且返回一个文件对象,框架会把数据存放到返回的这个文件对象中
         *
         * @return
         */
        public File getFile() throws Exception;

        ;

        /**
         * 当文件存放完毕,调用这个方法
         *
         * @param f
         */
        public void handler(File f) throws Exception;

        ;

        /**
         * 访问错误的时候调用
         *
         * @param e
         */
        public void error(Exception e);
    }

    /**
     * 数据处理的接口
     * <p/>
     * Be replaced by BaseDataHandler ,see {@link BaseDataHandler}
     *
     * @author xiaojinzi
     */
    @Deprecated
    public interface DataHandler extends BaseDataHandler {

        @Override
        void handler(String data);

        @Override
        void handler(InputStream is);

        @Override
        void handler(byte[] bt);

        @Override
        File getFile();

        @Override
        void handler(File f);

    }

    @Override
    public void run() {

        // 拿到一个任务
        NetTask<Parameter> netTask = netTasks.remove(0);

        // 网络任务准备请求的时候的拦截
        for (int i = 0; i < netFilters.size(); i++) {
            NetFilter filter = netFilters.get(i);
            boolean b = filter.netTaskPrepare(netTask);
            if (b) {
                if (isLog) {
                    L.s(TAG, "请求准备的时候被拦截");
                }
                netTask = null;
                return;
            }
        }

        // 创建一个结果对象
        ResultInfo<Parameter> info = new ResultInfo<Parameter>();

        // 回调接口
        info.dataHandler = netTask.dataHandler;
        info.parameterDataHandler = netTask.parameterDataHandler;
        info.parameters = netTask.parameters;
        info.url = netTask.url;
        info.isUseJsonCache = netTask.isUseJsonCache;

        try {
            // 存储数据的类型
            info.responseDataStyle = netTask.responseDataStyle;
            info.netTaskResponseDataStyle = netTask.responseDataStyle;

//            if (netTask.isUseJsonCache) { // 判断这个请求是否使用缓存
            // 网络任务开始请求的时候的拦截
            for (int i = 0; i < netFilters.size(); i++) {
                NetFilter filter = netFilters.get(i);
                boolean b = filter.netTaskBegin(netTask, info);
                if (b) {
                    if (isLog) {
                        L.s(TAG, "请求被拦截,直接使用拦截器中返回的结果");
                    }
                    // 添加返回对象
                    resultInfos.add(info);
                    // 发送任意消息
                    h.sendEmptyMessage(0);
                    return;
                }
            }
//            } else {
//                if (isLog) {
//                    L.s(TAG, "请求 '" + netTask.url + "' 不使用用缓存");
//                }
//            }

            // 获取一个网络请求返回的输入流
            InputStream is = Http.getInputStream(netTask.url);

            if (netTask.pd != null) {
                netTask.pd.setMax(Http.getContentLength(Thread.currentThread().getId()));
            }

            switch (netTask.responseDataStyle) {

                case BaseDataHandler.INPUTSTREAMDATA: // 流的类型
                    // 存储结果
                    info.result = is;
                    break;

                case BaseDataHandler.BYTEARRAYDATA: // 字节数组的类型
                    byte[] arr = InputStreamUtil.getByteArr(is);
                    info.result = arr;
                    break;

                case BaseDataHandler.STRINGDATA: // 字符串的类型
                    // 获取输入流转化后的字符串
                    String content = StringUtil.isToStr(is, CHARENCODING, netTask.pd);
                    info.result = content;
                    break;

                case BaseDataHandler.FILEDATA: // 文件形式的类型
                    info.result = FileUtil.isToFile(is, netTask.dataHandler.getFile(), netTask.pd);
                    break;

            }

        } catch (Exception e) {
            info.result = e;
            // 存储数据的类型
            info.responseDataStyle = BaseDataHandler.ERRORDATA;
        }

        // 添加返回对象
        resultInfos.add(info);

        // 发送任意消息
        h.sendEmptyMessage(0);

    }

}
