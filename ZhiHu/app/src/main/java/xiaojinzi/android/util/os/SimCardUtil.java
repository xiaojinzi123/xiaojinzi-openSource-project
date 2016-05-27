package xiaojinzi.android.util.os;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * sim卡的工具类
 * 
 * @author xiaojinzi
 *
 */
public class SimCardUtil {
	/**
	 * 获取sim卡的唯一序列号
	 * 
	 * @return
	 */
	public static String getSimSer(Context context) {

		// 获取管理者
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		// 返回sim卡信息
		return manager.getSimSerialNumber();

	}
}
