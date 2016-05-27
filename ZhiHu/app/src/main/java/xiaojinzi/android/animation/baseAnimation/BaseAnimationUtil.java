package xiaojinzi.android.animation.baseAnimation;

/**
 * 基本的动画工具类
 * 
 * @author cxj QQ:347837667
 * @date 2015年12月22日
 *
 */
public abstract class BaseAnimationUtil {
	/**
	 * 设置动画结束后是否还原到原来的位置
	 */
	public static boolean fillAfter = false;

	/**
	 * 动画的默认时长
	 */
	public static int defaultDuration = 2000;

	/**
	 * 动画默认的参照中心点的百分比
	 */
	public static float defaultCenterX = 0.5f;

	/**
	 * 动画默认的参照中心点的百分比
	 */
	public static float defaultCenterY = 0.5f;
}
