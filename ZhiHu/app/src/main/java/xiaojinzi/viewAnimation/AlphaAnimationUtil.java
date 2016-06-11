package xiaojinzi.viewAnimation;

import android.view.View;
import android.view.animation.AlphaAnimation;

import xiaojinzi.viewAnimation.baseAnimation.BaseAnimationUtil;


/**
 * 透明度动画的工具类
 * 
 * @author xiaojinzi
 *
 */
public class AlphaAnimationUtil extends BaseAnimationUtil {

	/**
	 * 从完全透明到完全不透明,使用默认的时长
	 * 
	 * @param v
	 *            动画作用的控件
	 */
	public static void alphaToShow(View v) {
		alpha(v, 0, 1);
	}

	/**
	 * 返回一个从完全透明到完全不透明,使用默认的时长的动画
	 * 
	 * @return
	 */
	public static AlphaAnimation alphaToShow() {
		return alpha(0, 1);
	}

	/**
	 * 从完全不透明到完全透明,使用默认的时长
	 * 
	 * @param v
	 */
	public static void alphaToHide(View v) {
		alpha(v, 1, 0);
	}

	/**
	 * 返回一个从完全不透明到完全透明,使用默认的时长的动画
	 * 
	 * @return
	 */
	public static AlphaAnimation alphaToHide() {
		return alpha(1, 0);
	}

	/**
	 * 使用自定义的时长开启动画,从完全透明到完全不透明
	 * 
	 * @param v
	 *            动画作用的控件
	 * @param duration
	 *            动画作用的时长
	 */
	public static void alphaToShow(View v, int duration) {
		alpha(v, 0, 1, duration);
	}

	/**
	 * 返回使用自定义的时长从完全透明到完全不透明的动画
	 * 
	 * @param duration
	 *            动画作用的时长
	 * @return
	 */
	public static AlphaAnimation alphaToShow(int duration) {
		return alpha(0, 1, duration);
	}

	/**
	 * 使用自定义的时长开启动画,从完全不透明到完全透明
	 * 
	 * @param v
	 *            动画作用的控件
	 * @param duration
	 *            动画作用的时长
	 */
	public static void alphaToHide(View v, int duration) {
		alpha(v, 1, 0, duration);
	}

	/**
	 * 返回使用自定义的时长从完全不透明到完全透明的动画
	 * 
	 * @param duration
	 *            动画作用的时长
	 * @return
	 */
	public static AlphaAnimation alphaToHide(int duration) {
		return alpha(1, 0, duration);
	}

	/**
	 * 使用默认的时长开启动画
	 * 
	 * @param v
	 *            动画作用的控件
	 * @param fromAlpha
	 *            开始的透明度
	 * @param toAlpha
	 *            结束的透明度
	 */
	public static void alpha(View v, float fromAlpha, float toAlpha) {
		alpha(v, fromAlpha, toAlpha, defaultDuration);
	}

	/**
	 * 返回一个透明度动画
	 * 
	 * @param fromAlpha
	 *            开始的透明度
	 * @param toAlpha
	 *            结束的透明度
	 * @return
	 */
	public static AlphaAnimation alpha(float fromAlpha, float toAlpha) {
		return alpha(fromAlpha, toAlpha, defaultDuration);
	}

	/**
	 * 透明度动画
	 * 
	 * @param v
	 *            动画作用的控件
	 * @param fromAlpha
	 *            开始的透明度
	 * @param toAlpha
	 *            结束的透明度
	 * @param duration
	 *            动画作用的时长
	 */
	public static void alpha(View v, float fromAlpha, float toAlpha, int duration) {
		AlphaAnimation animation = new AlphaAnimation(fromAlpha, toAlpha);
		animation.setDuration(duration);
		animation.setFillAfter(fillAfter);
		v.startAnimation(animation);
	}

	/**
	 * 返回一个透明度动画
	 * 
	 * @param fromAlpha
	 *            开始的透明度
	 * @param toAlpha
	 *            结束的透明度
	 * @param duration
	 *            动画作用的时长
	 * @return
	 */
	public static AlphaAnimation alpha(float fromAlpha, float toAlpha, int duration) {
		AlphaAnimation animation = new AlphaAnimation(fromAlpha, toAlpha);
		animation.setDuration(duration);
		animation.setFillAfter(fillAfter);
		return animation;
	}

}
