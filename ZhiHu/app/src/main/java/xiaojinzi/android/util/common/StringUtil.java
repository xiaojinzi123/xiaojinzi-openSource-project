package xiaojinzi.android.util.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.ProgressDialog;

/**
 * 在Android环境下的字符串的工具类
 * 
 * @author cxj
 *
 */
public class StringUtil {
	
	/**
	 * 把一个流转化成字符串
	 * 
	 * @param is
	 *            要转化的流对象
	 * @param charEncoding
	 *            编码方式
	 * @param pd
	 *            进度条对话框
	 * @return
	 * @throws IOException
	 */
	public static String isToStr(InputStream is, String charEncoding, ProgressDialog pd) throws IOException {

		// 定义缓冲区
		byte[] bts = new byte[1024];

		// 定义读取的长度
		int len = -1;

		// 定义字节数组的输出的工具类
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		// 循环读取
		while ((len = is.read(bts)) != -1) {
			// 写出到out中
			out.write(bts, 0, len);
			if (pd != null) {
				pd.setProgress(pd.getProgress() + len);
			}
		}

		// 得到字符串
		String content = new String(out.toByteArray(), charEncoding);

		// 关闭资源
		is.close();
		out.close();

		// 返回数据
		return content;
	}
}
