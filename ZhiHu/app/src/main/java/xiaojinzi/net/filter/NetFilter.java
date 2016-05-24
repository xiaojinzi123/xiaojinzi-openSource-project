package xiaojinzi.net.filter;


import xiaojinzi.net.NetTask;
import xiaojinzi.net.ResultInfo;

/**
 * 网络过滤器
 *
 * @author cxj QQ:347837667
 * @date 2015年12月10日
 */
public interface NetFilter {

    /**
     * 网络准备开始的时候的过滤,
     * 装载结果的对象还没有被创建
     *
     * @param netTask
     * @return
     */
    boolean netTaskPrepare(NetTask<?> netTask);

    /**
     * 网络开始的时候的过滤,如果返回对象不是null,<br>
     * 则放弃请求,直接把返回的对象当成结果<br>
     * 注意这里请把
     *
     * @param netTask
     */
    boolean netTaskBegin(NetTask<?> netTask, ResultInfo<?> resultInfo);

    /**
     * 结果信息返回的时候的过滤
     *
     * @param resultInfo
     */
    boolean resultInfoReturn(ResultInfo<?> resultInfo);

}
