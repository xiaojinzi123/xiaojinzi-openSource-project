package xiaojinzi.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import xiaojinzi.animation.baseAnimation.BaseAnimationUtil;

/**
 * 缩放的动画的工具类
 * 
 * @author xiaojinzi
 *
 */
public class ScaleAnimationUtil extends BaseAnimationUtil {

	/**
	 * 
	 * @param v
	 *            动画作用的控件View
	 * @param fromXPercent
	 *            相对于自己的动画的起始点横坐标的百分比
	 * @param toXPercent
	 *            相对于自己的动画的结束点横坐标的百分比
	 * @param fromYPercent
	 *            相对于自己的动画的起始点纵坐标的百分比
	 * @param toYPercent
	 *            相对于自己的动画的结束点纵坐标的百分比
	 * @param duration
	 *            动画的时长
	 */
	public static void scaleSelf(View v, float fromXPercent, float toXPercent, float fromYPercent, float toYPercent,
			int duration) {
		scaleSelf(v, fromXPercent, toXPercent, fromYPercent, toYPercent, defaultCenterX, defaultCenterY, duration);
	}

	/**
	 * 返回一个动画
	 * 
	 * @param fromXPercent
	 *            相对于自己的动画的起始点横坐标的百分比
	 * @param toXPercent
	 *            相对于自己的动画的结束点横坐标的百分比
	 * @param fromYPercent
	 *            相对于自己的动画的起始点纵坐标的百分比
	 * @param toYPercent
	 *            相对于自己的动画的结束点纵坐标的百分比
	 * @param duration
	 *            动画的时长
	 * @return
	 */
	public static ScaleAnimation scaleSelf(float fromXPercent, float toXPercent, float fromYPercent, float toYPercent,
			int duration) {
		return scaleSelf(fromXPercent, toXPercent, fromYPercent, toYPercent, defaultCenterX, defaultCenterY, duration);
	}

	/**
	 * 使用默认时间和默认的缩放中心点来缩放
	 * 
	 * @param v
	 *            动画作用的控件View
	 * @param fromXPercent
	 *            相对于自己的动画的起始点横坐标的百分比
	 * @param toXPercent
	 *            相对于自己的动画的结束点横坐标的百分比
	 * @param fromYPercent
	 *            相对于自己的动画的起始点纵坐标的百分比
	 * @param toYPercent
	 *            相对于自己的动画的结束点纵坐标的百分比
	 */
	public static void scaleSelf(View v, float fromXPercent, float toXPercent, float fromYPercent, float toYPercent) {
		scaleSelf(v, fromXPercent, toXPercent, fromYPercent, toYPercent, defaultCenterX, defaultCenterY);
	}

	/**
	 * 返回一个动画
	 * 
	 * @param fromXPercent
	 *            相对于自己的动画的起始点横坐标的百分比
	 * @param toXPercent
	 *            相对于自己的动画的结束点横坐标的百分比
	 * @param fromYPercent
	 *            相对于自己的动画的起始点纵坐标的百分比
	 * @param toYPercent
	 *            相对于自己的动画的结束点纵坐标的百分比
	 * @return
	 */
	public static ScaleAnimation scaleSelf(float fromXPercent, float toXPercent, float fromYPercent, float toYPercent) {
		return scaleSelf(fromXPercent, toXPercent, fromYPercent, toYPercent, defaultCenterX, defaultCenterY);
	}

	/**
	 * 使用默认动画时间来缩放
	 * 
	 * @param v
	 *            动画作用的控件View
	 * @param fromXPercent
	 *            相对于自己的动画的起始点横坐标的百分比
	 * @param toXPercent
	 *            相对于自己的动画的结束点横坐标的百分比
	 * @param fromYPercent
	 *            相对于自己的动画的起始点纵坐标的百分比
	 * @param toYPercent
	 *            相对于自己的动画的结束点纵坐标的百分比
	 * @param centerX
	 *            缩放动画的缩放的中心点横坐标的百分比
	 * @param centerY
	 *            缩放动画的缩放的中心点纵坐标坐标的百分比
	 */
	public static void scaleSelf(View v, float fromXPercent, float toXPercent, float fromYPercent, float toYPercent,
			float centerX, float centerY) {
		scaleSelf(v, fromXPercent, toXPercent, fromYPercent, toYPercent, centerX, centerY, defaultDuration);
	}

	/**
	 * 返回一个动画
	 * 
	 * @param fromXPercent
	 *            相对于自己的动画的起始点横坐标的百分比
	 * @param toXPercent
	 *            相对于自己的动画的结束点横坐标的百分比
	 * @param fromYPercent
	 *            相对于自己的动画的起始点纵坐标的百分比
	 * @param toYPercent
	 *            相对于自己的动画的结束点纵坐标的百分比
	 * @param centerX
	 *            缩放动画的缩放的中心点横坐标的百分比
	 * @param centerY
	 *            缩放动画的缩放的中心点纵坐标坐标的百分比
	 * @return
	 */
	public static ScaleAnimation scaleSelf(float fromXPercent, float toXPercent, float fromYPercent, float toYPercent,
			float centerX, float centerY) {
		return scaleSelf(fromXPercent, toXPercent, fromYPercent, toYPercent, centerX, centerY, defaultDuration);
	}

	/**
	 * 相对于自己进行缩放
	 * 
	 * @param v
	 *            动画作用的控件View
	 * @param fromXPercent
	 *            相对于自己的动画的起始点横坐标的百分比
	 * @param toXPercent
	 *            相对于自己的动画的结束点横坐标的百分比
	 * @param fromYPercent
	 *            相对于自己的动画的起始点纵坐标的百分比
	 * @param toYPercent
	 *            相对于自己的动画的结束点纵坐标的百分比
	 * @param centerX
	 *            缩放动画的缩放的中心点横坐标的百分比
	 * @param centerY
	 *            缩放动画的缩放的中心点纵坐标坐标的百分比
	 * @param duration
	 *            动画的时长
	 */
	public static void scaleSelf(View v, float fromXPercent, float toXPercent, float fromYPercent, float toYPercent,
			float centerX, float centerY, int duration) {
		v.startAnimation(scaleSelf(fromXPercent, toXPercent, fromYPercent, toYPercent, centerX, centerY, duration));
	}

	/**
	 * 返回一个动画
	 * 
	 * @param fromXPercent
	 *            相对于自己的动画的起始点横坐标的百分比
	 * @param toXPercent
	 *            相对于自己的动画的结束点横坐标的百分比
	 * @param fromYPercent
	 *            相对于自己的动画的起始点纵坐标的百分比
	 * @param toYPercent
	 *            相对于自己的动画的结束点纵坐标的百分比
	 * @param centerX
	 *            缩放动画的缩放的中心点横坐标的百分比
	 * @param centerY
	 *            缩放动画的缩放的中心点纵坐标坐标的百分比
	 * @param duration
	 *            动画的时长
	 * @return
	 */
	public static ScaleAnimation scaleSelf(float fromXPercent, float toXPercent, float fromYPercent, float toYPercent,
			float centerX, float centerY, int duration) {
		ScaleAnimation animation = new ScaleAnimation(fromXPercent, toXPercent, fromYPercent, toYPercent,
				Animation.RELATIVE_TO_SELF, centerX, Animation.RELATIVE_TO_SELF, centerY);
		animation.setDuration(duration);
		animation.setFillAfter(fillAfter);
		return animation;
	}

	/**
	 * 使用默认的动画时长和默认的父容器的缩放的中心点来缩放
	 * 
	 * @param v
	 *            动画作用的控件View
	 * @param fromXPercent
	 *            相对于父容器的动画的起始点横坐标的百分比
	 * @param toXPercent
	 *            相对于父容器的动画的结束点横坐标的百分比
	 * @param fromYPercent
	 *            相对于父容器的动画的起始点纵坐标的百分比
	 * @param toYPercent
	 *            相对于父容器的动画的结束点纵坐标的百分比
	 */
	public static void scaleParent(View v, float fromXPercent, float toXPercent, float fromYPercent, float toYPercent) {
		scaleParent(v, fromXPercent, toXPercent, fromYPercent, toYPercent, defaultCenterX, defaultCenterY);
	}

	/**
	 * 使用默认动画时长来缩放
	 * 
	 * @param v
	 *            动画作用的控件View
	 * @param fromXPercent
	 *            相对于父容器的动画的起始点横坐标的百分比
	 * @param toXPercent
	 *            相对于父容器的动画的结束点横坐标的百分比
	 * @param fromYPercent
	 *            相对于父容器的动画的起始点纵坐标的百分比
	 * @param toYPercent
	 *            相对于父容器的动画的结束点纵坐标的百分比
	 * @param centerX
	 *            缩放动画的缩放的中心点横坐标的百分比
	 * @param centerY
	 *            缩放动画的缩放的中心点纵坐标坐标的百分比
	 */
	public static void scaleParent(View v, float fromXPercent, float toXPercent, float fromYPercent, float toYPercent,
			float centerX, float centerY) {
		scaleParent(v, fromXPercent, toXPercent, fromYPercent, toYPercent, centerX, centerY, defaultDuration);
	}

	/**
	 * 相对于父容器进行缩放
	 * 
	 * @param v
	 *            动画作用的控件View
	 * @param fromXPercent
	 *            相对于父容器的动画的起始点横坐标的百分比
	 * @param toXPercent
	 *            相对于父容器的动画的结束点横坐标的百分比
	 * @param fromYPercent
	 *            相对于父容器的动画的起始点纵坐标的百分比
	 * @param toYPercent
	 *            相对于父容器的动画的结束点纵坐标的百分比
	 * @param centerX
	 *            缩放动画的缩放的中心点横坐标的百分比
	 * @param centerY
	 *            缩放动画的缩放的中心点纵坐标坐标的百分比
	 * @param duration
	 *            动画的时长
	 */
	public static void scaleParent(View v, float fromXPercent, float toXPercent, float fromYPercent, float toYPercent,
			float centerX, float centerY, int duration) {
		ScaleAnimation animation = new ScaleAnimation(fromXPercent, toXPercent, fromYPercent, toYPercent,
				Animation.RELATIVE_TO_PARENT, centerX, Animation.RELATIVE_TO_PARENT, centerY);
		animation.setDuration(duration);
		animation.setFillAfter(fillAfter);
		v.startAnimation(animation);
	}

}
