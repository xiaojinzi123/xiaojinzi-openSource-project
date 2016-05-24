package xiaojinzi.net.adapter;

import java.io.File;
import java.io.InputStream;

import xiaojinzi.net.AsyncHttp;


/**
 * 适配器
 * 
 * @author cxj QQ:347837667
 * @date 2015年12月8日
 *
 * @param <Parameter>
 */
public class ParameterHandlerAdapter<Parameter> implements AsyncHttp.ParameterHandler<Parameter> {

	@Override
	public void handler(String data, Parameter... p) throws Exception {
	}

	@Override
	public void handler(InputStream is, Parameter... p) throws Exception {
	}

	@Override
	public void handler(byte[] bt, Parameter... p) throws Exception {
	}

	@Override
	public File getFile() throws Exception {
		return null;
	}

	@Override
	public void handler(File f, Parameter... p) throws Exception {
	}

	@Override
	public void error(Exception e, Parameter... p) {
	}

}
