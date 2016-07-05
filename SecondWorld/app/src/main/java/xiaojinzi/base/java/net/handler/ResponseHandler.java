package xiaojinzi.base.java.net.handler;

import java.io.File;
import java.io.InputStream;

/**
 * 可以传递参数的回调接口
 *
 * @param <Parameter>
 * @author cxj QQ:347837667
 * @date 2015年12月8日
 */
public interface ResponseHandler<Parameter> {

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
