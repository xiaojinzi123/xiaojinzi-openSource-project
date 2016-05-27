package xiaojinzi.android.util.adapter;

import java.io.File;

import xiaojinzi.android.util.store.FileUtil.FileHander;

/**
 * FileHander 接口的适配器 {@link FileHander }
 * 
 * @author cxj QQ:347837667
 * @date 2015年12月8日
 *
 */
public class FileHanderAdapter implements FileHander {

	@Override
	public void fileHander(File f) {
	}

	@Override
	public void error(File f, Exception e) {
	}

}
