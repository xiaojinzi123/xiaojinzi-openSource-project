package xiaojinzi.net;


import android.os.Handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.Vector;

import xiaojinzi.base.android.common.StringUtil;
import xiaojinzi.base.android.log.L;

import xiaojinzi.base.android.store.FileUtil;
import xiaojinzi.base.android.thread.ThreadPool;
import xiaojinzi.base.java.io.InputStreamUtil;
import xiaojinzi.base.java.net.Http;
import xiaojinzi.base.java.net.HttpRequest;
import xiaojinzi.base.java.net.ResultInfo;
import xiaojinzi.base.java.net.handler.ResponseHandler;
import xiaojinzi.net.filter.NetFilter;
import xiaojinzi.net.filter.PdHttpRequest;


/**
 * 异步请求的一个小框架, 可以多次调用get进行请求数据,<br>
 * 但是回调接口是不同的即可<br>
 * 1.如果要使用json的缓存,请调用AsyncHttp.initCacheJson(Context context)方法<br>
 * 来初始化一个默认的拦截器,这个拦截器实现了当访问的网址出现在数据库当中的时候,将使用缓存,<br>
 * 访问失败的时候,根据访问的网址去数据库检索,如果有, 则返回数据库中的信息,轻松实现没有网络<br>
 * 的情况的缓存数据
 *
 * @author cxj QQ:347837667
 * @date 2015年12月9日
 */
public class AsyncHttp implements Runnable {

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
    public static final String CHARENCODING = "UTF-8";

    /**
     * 网络请求的过滤器
     */
    private static Vector<NetFilter> netFilters = new Vector<NetFilter>();

    public AsyncHttp() {
    }

    /**
     * 执行的任务对象集合
     */
    private Vector<PdHttpRequest> netTasks = new Vector<PdHttpRequest>();

    /**
     * 结果信息对象集合
     */
    private Vector<ResultInfo> resultInfos = new Vector<ResultInfo>();

    private Handler h = new Handler() {
        public void handleMessage(android.os.Message msg) {

            // 取出一个结果
            ResultInfo resultInfo = resultInfos.remove(0);

            HttpRequest httpRequest = resultInfo.httpRequest;

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

            //拿到请求结果的处理器
            ResponseHandler parameterDataHandler = resultInfo.httpRequest.getResponseHandler();

            if (parameterDataHandler == null && parameterDataHandler == null) {
                return;
            }

            // 拿到返回数据的类型
            int responseDataStyle = resultInfo.httpRequest.getResponseDataStyle();

            try { //尝试数据处理
                if (responseDataStyle == ResponseHandler.INPUTSTREAMDATA) {
                    InputStream is = (InputStream) resultInfo.result;
                    parameterDataHandler.handler(is, httpRequest.getParameters());
                } else if (responseDataStyle == ResponseHandler.BYTEARRAYDATA) {
                    byte[] bt = (byte[]) resultInfo.result;
                    parameterDataHandler.handler(bt, httpRequest.getParameters());
                } else if (responseDataStyle == ResponseHandler.STRINGDATA) {
                    String content = (String) resultInfo.result;
                    parameterDataHandler.handler(content, httpRequest.getParameters());
                } else if (responseDataStyle == ResponseHandler.FILEDATA) {
                    File f = (File) resultInfo.result;
                    parameterDataHandler.handler(f, httpRequest.getParameters());
                } else if (responseDataStyle == ResponseHandler.ERRORDATA) {
                    Exception e = (Exception) resultInfo.result;
                    parameterDataHandler.error(e, httpRequest.getParameters());
                }
            } catch (Exception e) {
                if (isLog) {
                    L.s(TAG, "处理数据挂了");
                    e.printStackTrace();
                }
                parameterDataHandler.error(e, httpRequest.getParameters());
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
     * 发送一个请求出去
     *
     * @param netTask
     */
    public void send(PdHttpRequest netTask) {
        netTasks.add(netTask);
        ThreadPool.getInstance().invoke(this);
//        new Thread(this).start();
    }


    @Override
    public void run() {

        // 拿到一个任务
        PdHttpRequest netTask = netTasks.remove(0);

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
        ResultInfo info = new ResultInfo();

        // 保存请求对象到结果对象中
        info.httpRequest = netTask;

        try {
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

            //生命一个输入流
            InputStream is;

            //如果是一个本地的文件,也就是路径前面是"file:"打头的
            if (netTask.getRequesutUrl().startsWith(HttpRequest.LOCALFILEURLPREFIX)) {
                File f = new File(netTask.getRequesutUrl().substring(HttpRequest.LOCALFILEURLPREFIX.length()));
                if (f.isFile()) {
                    is = new FileInputStream(f);
                } else {
                    // 获取一个网络请求返回的输入流
                    is = Http.sendRequest(netTask);
                }
            } else {
                // 获取一个网络请求返回的输入流
                is = Http.sendRequest(netTask);
            }

            //获取响应的长度
            Integer contentLength = Http.getContentLength(Thread.currentThread().getId());

            if (netTask.getPd() != null) {
                netTask.getPd().setMax(contentLength);
            }

            switch (netTask.getResponseDataStyle()) {

                case ResponseHandler.INPUTSTREAMDATA: // 流的类型
                    // 存储结果
                    info.result = is;
                    break;

                case ResponseHandler.BYTEARRAYDATA: // 字节数组的类型
                    byte[] arr = InputStreamUtil.getByteArr(is);
                    info.result = arr;
                    break;

                case ResponseHandler.STRINGDATA: // 字符串的类型
                    // 获取输入流转化后的字符串
                    String content = StringUtil.isToStr(is, CHARENCODING, netTask.getPd());
                    info.result = content;
                    break;

                case ResponseHandler.FILEDATA: // 文件形式的类型
                    info.result = FileUtil.isToFile(is, netTask.getResponseHandler().getFile(), netTask.getPd());
                    break;

            }

        } catch (Exception e) {
            info.result = e;
            // 存储数据的类型
            info.httpRequest.setResponseDataStyle(ResponseHandler.ERRORDATA);
        }

        // 添加返回对象
        resultInfos.add(info);

        // 发送任意消息
        h.sendEmptyMessage(0);

    }

}
