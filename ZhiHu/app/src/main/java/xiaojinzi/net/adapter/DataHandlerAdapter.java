package xiaojinzi.net.adapter;

import java.io.File;
import java.io.InputStream;

import xiaojinzi.net.AsyncHttp;


/**
 * 适配器 be replaced by {@link BaseDataHandlerAdapter}
 * 
 * @author xiaojinzi
 *
 */
@Deprecated
public class DataHandlerAdapter implements AsyncHttp.DataHandler {

	@Override
	public void handler(String data) {
	}

	@Override
	public void handler(InputStream is) {
	}

	@Override
	public File getFile() {
		return null;
	}

	@Override
	public void handler(File f) {
	}

	@Override
	public void error(Exception e) {
	}

	@Override
	public void handler(byte[] bt) {
	}

}
