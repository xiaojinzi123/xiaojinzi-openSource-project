package xiaojinzi.android.util.os;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

/**
 * 通知栏的工具类
 *
 * @author Administrator
 */
public class NotifiUtil {

    /**
     * @param context 上下文对象
     * @param content 通知的内容
     */
    public static void notifi(Context context, String content, boolean isAllowClear) {
        notifi(context, content, null, isAllowClear);
    }

    /**
     * 发送一个通知
     *
     * @param context    上下文对象
     * @param content    通知的内容
     * @param openIntent 点击触发的意图
     */
    public static void notifi(Context context, String content, Intent openIntent, boolean isAllowClear) {
        notifi(context, android.R.drawable.stat_notify_chat, "新消息", "标题",
                content, System.currentTimeMillis(), openIntent, null, isAllowClear);
    }

    /**
     * 使用系统的布局发送一个通知
     *
     * @param context     上下文对象
     * @param icon        通知的图标
     * @param tickerText  通知的提示
     * @param title       通知的标题
     * @param content     通知的内容
     * @param when        通知的时间
     * @param openIntent  点击触发的意图
     * @param contentView 自定义视图的实现
     */
    public static void notifi(Context context, int icon, String tickerText,
                              String title, String content, long when, Intent openIntent,
                              RemoteViews contentView, boolean isAllowClear) {

        // 获取通知栏的服务对象
        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setSmallIcon(icon).
                setTicker(tickerText).
                setWhen(when).
                setDefaults(Notification.DEFAULT_SOUND).setContentTitle(title).setContentText(content);

        if (contentView == null) {
            if (openIntent != null) {
                PendingIntent contentIntent = PendingIntent.getActivity(
                        context, 0, openIntent, 0);// 当点击消息时就会向系统发送openintent意图

                // 设置点击触发的intent意图
                b.setContentIntent(contentIntent);
            }
        } else {

            b.setContent(contentView);

            if (openIntent != null) {
                PendingIntent contentIntent = PendingIntent.getActivity(
                        context, 0, openIntent, 0);
                // 设置点击触发的intent意图
                b.setContentIntent(contentIntent);
            }

        }

        Notification notification = b.build();

        if (!isAllowClear) {
            notification.flags = Notification.FLAG_NO_CLEAR;
        }

        // 发送通知
        nm.notify(0, notification);

    }

    /**
     * 通知一个自定义视图
     *
     * @param context
     * @param icon
     * @param tickerText
     * @param when
     * @param contentView
     * @param openIntent
     * @param isAllowClear
     */
    public static void notifi(Context context, int icon, String tickerText,
                              long when, RemoteViews contentView, Intent openIntent, boolean isAllowClear) {
        notifi(context, icon, tickerText, null, null, when, openIntent,
                contentView, isAllowClear);
    }

    /**
     * 通知一个自定义视图
     *
     * @param context
     * @param contentView
     * @param openIntent
     * @param isAllowClear
     */
    public static void notifi(Context context, RemoteViews contentView,
                              Intent openIntent, boolean isAllowClear) {
        notifi(context, android.R.drawable.stat_notify_chat, "新消息", null, null,
                System.currentTimeMillis(), openIntent, contentView, isAllowClear);
    }

    /**
     * 通知一个自定义视图
     *
     * @param context
     * @param contentView
     */
    public static void notifi(Context context, RemoteViews contentView, boolean isAllowClear) {
        notifi(context, contentView, null, isAllowClear);
    }

    /**
     * 清除所有通知
     */
    public static void clearAllNotifiy(Context context) {
        // 获取通知栏的服务对象
        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
    }

}
