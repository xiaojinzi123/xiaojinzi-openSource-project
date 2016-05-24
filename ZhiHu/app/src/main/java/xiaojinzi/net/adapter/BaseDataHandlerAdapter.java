package xiaojinzi.net.adapter;

import java.io.File;
import java.io.InputStream;

import xiaojinzi.net.AsyncHttp;

/**
 * 数据处理回调的接口,回调的方法都是可以在主线程中直接更新UI的
 * 
 * @author cxj
 *
 */
public class BaseDataHandlerAdapter implements AsyncHttp.BaseDataHandler {

	@Override
	public void handler(String data) throws Exception {
	}

	@Override
	public void handler(InputStream is) throws Exception {
	}

	@Override
	public void handler(byte[] bt) throws Exception {
	}

	@Override
	public File getFile() throws Exception {
		return null;
	}

	@Override
	public void handler(File f) throws Exception {
	}

	@Override
	public void error(Exception e) {
	}

}
