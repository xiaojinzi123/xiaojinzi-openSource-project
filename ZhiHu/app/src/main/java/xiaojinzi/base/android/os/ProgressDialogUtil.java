package xiaojinzi.base.android.os;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 进度条对话框的工具类
 * 
 * @author xiaojinzi
 *
 */
public class ProgressDialogUtil {

	public static int MAX = 100;

	public static boolean defaultCancelable = false;

	public static String defaultMessage = "please wait";

	/**
	 * 采用水平进度条的样式显示的进度条
	 * 
	 * @param context
	 * @return
	 */
	public static ProgressDialog show(Context context) {
		return show(context, ProgressDialog.STYLE_HORIZONTAL, defaultCancelable);
	}

	/**
	 * 显示进度条对话框
	 * 
	 * @param context
	 * @param style
	 * @param cancelAble
	 * @return
	 */
	public static ProgressDialog show(Context context, int style, boolean cancelAble) {
		ProgressDialog pd = new ProgressDialog(context);
		// 水平进度条的样式
		pd.setProgressStyle(style);
		pd.setMessage(defaultMessage);
		pd.setCancelable(cancelAble);
		pd.setMax(MAX);
		pd.show();
		return pd;
	}

}
