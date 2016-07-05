package xiaojinzi.base.android.os;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 有关系统的工具类
 * 
 * @author xiaojinzi
 *
 */
public class SystemUtil {

	/**
	 * 获取app的名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppName(Context context) {
		return context.getApplicationInfo().loadLabel(context.getPackageManager()) + "";
	}

	/**
	 * 这个方法获取最近运行任何中最上面的一个应用的包名,<br>
	 * 进行了api版本的判断,然后利用不同的方法获取包名,具有兼容性
	 * 
	 * @param context
	 *            上下文对象
	 * @return 返回包名,如果出现异常或者获取失败返回""
	 */
	public static String getTopAppInfoPackageName(Context context) {
		if (SystemInfo.apiVersion < 22) { // 如果版本低于22
			// 获取到activity的管理的类
			ActivityManager m = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			// 获取最近的一个运行的任务的信息
			List<RunningTaskInfo> tasks = m.getRunningTasks(1);

			if (tasks != null && tasks.size() > 0) { // 如果集合不是空的

				// 返回任务栈中最上面的一个
				RunningTaskInfo info = m.getRunningTasks(1).get(0);

				return info.baseActivity.getPackageName();
			} else {
				return "";
			}
		} else {

			final int PROCESS_STATE_TOP = 2;
			try {
				// 获取正在运行的进程应用的信息实体中的一个字段,通过反射获取出来
				Field processStateField = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
				// 获取所有的正在运行的进程应用信息实体对象
				List<ActivityManager.RunningAppProcessInfo> processes = ((ActivityManager) context
						.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
				// 循环所有的进程,检测某一个进程的状态是最上面,也是就最近运行的一个应用的状态的时候,就返回这个应用的包名
				for (ActivityManager.RunningAppProcessInfo process : processes) {
					if (process.importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
							&& process.importanceReasonCode == 0) {
						int state = processStateField.getInt(process);
						if (state == PROCESS_STATE_TOP) { // 如果这个实体对象的状态为最近的运行应用
							String[] packname = process.pkgList;
							// 返回应用的包名
							return packname[0];
						}
					}
				}
			} catch (Exception e) {
			}
			return "";
		}
	}

	/**
	 * 跳转到某一个包名的应用详情界面,<br>
	 * 就是系统自带的应用详界面,里面有删除缓存,卸载应用等操作
	 */
	@SuppressLint("InlinedApi")
	public static void showInstalledAppDetails(Context context, String packageName) throws Exception {
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setData(Uri.parse("package:" + packageName));
		context.startActivity(intent);
	}

	/**
	 * 分享功能
	 *
	 * @param context
	 *            上下文
	 * @param activityTitle
	 *            Activity的名字
	 * @param msgTitle
	 *            消息标题
	 * @param msgText
	 *            消息内容
	 * @param imgPath
	 *            图片路径，不分享图片则传null
	 */
	public static void shareMsg(Context context,String activityTitle, String msgTitle, String msgText,
						 String imgPath) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (imgPath == null || imgPath.equals("")) {
			intent.setType("text/plain"); // 纯文本
		} else {
			File f = new File(imgPath);
			if (f != null && f.exists() && f.isFile()) {
				intent.setType("image/jpg");
				Uri u = Uri.fromFile(f);
				intent.putExtra(Intent.EXTRA_STREAM, u);
			}
		}
		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent, activityTitle));
	}

}
