package xiaojinzi.base.java.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 有关输入流的工具类
 * 
 * @author xiaojinzi
 *
 */
public class InputStreamUtil {

	/**
	 * 根据输入流获取字节
	 * 
	 * @param is
	 *            输入流
	 * @return
	 * @throws Exception
	 *             可能抛出的异常
	 */
	public static byte[] getByteArr(InputStream is) throws Exception {

		final ByteArrayOutputStream out = new ByteArrayOutputStream();

		// 迭代输入流,拿出数据
		inputStreamIterator(is, new HanderByteArray() {
			@Override
			public void hander(byte[] bt, int len) {
				out.write(bt, 0, len);
			}
		});

		return out.toByteArray();
	}

	/**
	 * 对一个输入流进行迭代,中间读出的字节数据交给用户自己处理
	 * 
	 * @param is
	 *            输入流
	 * @param it
	 *            迭代接口
	 * @throws Exception
	 *             读取的时候抛出的异常
	 */
	public static void inputStreamIterator(InputStream is, HanderByteArray it) throws Exception {
		byte[] bt = new byte[1024];
		int len = -1;
		while ((len = is.read(bt)) != -1) {
			it.hander(bt, len);
		}
		is.close();
	}

	/**
	 * 处理迭代出来的字节数组
	 * 
	 * @author cxj
	 *
	 */
	public interface HanderByteArray {

		/**
		 * 迭代的时候回调的方法
		 * 
		 * @param bt
		 *            暴露出去的数据
		 * @param len
		 *            读取的长度
		 * @throws Exception 
		 */
		public void hander(byte[] bt, int len) throws Exception;

	}

}
