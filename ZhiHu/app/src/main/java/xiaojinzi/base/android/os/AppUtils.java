package xiaojinzi.base.android.os;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

/**
 * 跟App相关的辅助类
 *
 * @author xiaojinzi
 */
public class AppUtils {

    private AppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 获取应用程序名称,获取失败返回null
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息],获取失败返回null
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断一个应用是不是第三方的程序
     *
     * @param info 应用信息对象
     * @return
     */
    public static boolean isSystemApp(ApplicationInfo info) {
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return false;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return false;
        }

        return true;
    }

    /**
     * 卸载一个应用
     *
     * @param context  上下文
     * @param packName 包名
     */
    public static void unInstallApk(Context context, String packName) {
        Uri packageURI = Uri.parse("package:" + packName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);
    }

    /**
     * 安装一个应用
     *
     * @param file    安装文件的位置
     * @param context 上下文
     */
    public static void installApk(File file, Context context) {

        // 定义一个意图
        Intent intent = new Intent();

        // 设置意图的动作
        intent.setAction(Intent.ACTION_VIEW);

        // 设置意图的数据和类型为安装apk
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

        // 利用上下文对象来启动这个activity
        context.startActivity(intent);

    }

}
