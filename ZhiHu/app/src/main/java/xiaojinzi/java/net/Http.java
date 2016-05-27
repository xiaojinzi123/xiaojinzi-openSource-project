package xiaojinzi.java.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import xiaojinzi.java.net.exception.ResponseCodeException;
import xiaojinzi.java.util.StringUtil;

/**
 * 封装了网络请求
 * 
 * @author xiaojinzi
 *
 */
public class Http {

	/**
	 * 默认超时的时间,5秒
	 */
	public static int TIMEOUTMILLIS = 5000;

	/**
	 * 由于多线程可能会调用这个get方法,所以进行了优化,<br>
	 * 就只有获取长度这里是数据共享的,所以这里封装了一个<br>
	 * 获取长度的方法,并且获取之后这个长度就从集合中消失了<br>
	 * see {@link Http#getContentLength(Long)}
	 */
	@Deprecated
	public static int length = 0;

	/**
	 * android提供的类似于Map的集合对象
	 */
	private static Map<Long, Integer> contentLengthMap = new HashMap<Long, Integer>();

	/**
	 * 在执行方法{@link Http#get(String, String)} 或者
	 * {@link Http#getInputStream(String)}<br>
	 * 中,如果请求正常,那么返回内容的长度将会通过这个方法存放到集合中
	 * 
	 * @param threadId
	 *            线程的id
	 * @param contentLength
	 *            线程成功执行的请求返回数据的长度
	 */
	private synchronized static void putContentLength(Long threadId, Integer contentLength) {
		contentLengthMap.put(threadId, contentLength);
	}

	/**
	 * 外界可以通过这个方法获取返回结果的长度,获取一次之后,这个数据将从集合中移除
	 * 
	 * @param threadId
	 *            线程的id
	 * @return 线程成功执行的请求返回数据的长度
	 */
	public synchronized static Integer getContentLength(Long threadId) {
		return contentLengthMap.remove(threadId);
	}

	/**
	 * gen请求获取资源
	 * 
	 * @param spec
	 *            请求的网址
	 * @param charEncoding
	 *            流转化成字符串的编码方式
	 * @return 返回null就是相应码不是200
	 * @throws IOException
	 */
	public static String get(String spec, String charEncoding) throws IOException {
		InputStream is = getInputStream(spec);
		// 返回流对应的字符串
		return is == null ? null : StringUtil.isToStr(is, charEncoding);
	}

	/**
	 * @param spec
	 *            请求的网址
	 * @param charEncoding
	 *            流转化成字符串的编码方式
	 * @return 返回null就是相应码不是200
	 * @throws IOException
	 */
	public static InputStream getInputStream(String spec) throws IOException {
		// 创建Url对象
		URL url = new URL(spec);

		// 获取连接对象
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// 设置超时的时间
		conn.setConnectTimeout(TIMEOUTMILLIS);

		// 以下是设置一些头信息
		conn.addRequestProperty("Accept", "Accept text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.addRequestProperty("Accept-Charset", "GB2312,GBK,utf-8;q=0.7,*;q=0.7");
		conn.addRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
		// conn.addRequestProperty("Accept-Encoding", "gzip, deflate");
		conn.addRequestProperty("Accept-Language", "zh-cn,zh;q=0.5");
		conn.addRequestProperty("Connection", "keep-alive");

		// 获取相应码
		int responseCode = conn.getResponseCode();

		// 如果请求成功
		if (responseCode == HttpURLConnection.HTTP_OK) {

			// 获取输入流
			InputStream is = conn.getInputStream();

			length = conn.getContentLength();

			long threadId = Thread.currentThread().getId();
			putContentLength(threadId, conn.getContentLength());

			return is;

		} else {
			throw new ResponseCodeException("the responseCode is not HTTP_OK:200");
		}
	}

}
