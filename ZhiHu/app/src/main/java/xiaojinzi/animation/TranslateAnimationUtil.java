package xiaojinzi.animation;

import android.view.View;
import android.view.animation.TranslateAnimation;
import xiaojinzi.animation.baseAnimation.BaseAnimationUtil;

/**
 * 平移动画的工具类
 * 
 * @author xiaojinzi
 *
 */
public class TranslateAnimationUtil extends BaseAnimationUtil {

	/**
	 * 相对于自己平移,利用绝对坐标进行平移
	 * 
	 * @param v
	 *            动画作用的View控件
	 * @param fromX
	 *            相对于view控件自己本身的位置的起始点x
	 * @param toX
	 *            相对于view控件自己本身的位置的结束点x
	 * @param fromY
	 *            相对于view控件自己本身的位置的起始点y
	 * @param toY
	 *            相对于view控件自己本身的位置的结束点y
	 */
	public static void translateSelfAbsolute(View v, float fromX, float toX, float fromY, float toY) {
		translateSelfAbsolute(v, fromX, toX, fromY, toY, defaultDuration);
	}

	/**
	 * 返回一个动画
	 * 
	 * @param fromX
	 *            相对于view控件自己本身的位置的起始点x
	 * @param toX
	 *            相对于view控件自己本身的位置的结束点x
	 * @param fromY
	 *            相对于view控件自己本身的位置的起始点y
	 * @param toY
	 *            相对于view控件自己本身的位置的结束点y
	 * @return
	 */
	public static TranslateAnimation translateSelfAbsolute(float fromX, float toX, float fromY, float toY) {
		return translateSelfAbsolute(fromX, toX, fromY, toY, defaultDuration);
	}

	/**
	 * 相对于自己平移,利用绝对坐标进行平移
	 * 
	 * @param v
	 *            动画作用的View控件
	 * @param fromX
	 *            相对于view控件自己本身的位置的起始点x
	 * @param toX
	 *            相对于view控件自己本身的位置的结束点x
	 * @param fromY
	 *            相对于view控件自己本身的位置的起始点y
	 * @param toY
	 *            相对于view控件自己本身的位置的结束点y
	 * @param duration
	 *            动画的时长,毫秒为单位
	 */
	public static void translateSelfAbsolute(View v, float fromX, float toX, float fromY, float toY, int duration) {
		v.startAnimation(translateSelfAbsolute(fromX, toX, fromY, toY, duration));
	}

	/**
	 * 返回一个动画
	 * 
	 * @param fromX
	 *            相对于view控件自己本身的位置的起始点x
	 * @param toX
	 *            相对于view控件自己本身的位置的结束点x
	 * @param fromY
	 *            相对于view控件自己本身的位置的起始点y
	 * @param toY
	 *            相对于view控件自己本身的位置的结束点y
	 * @param duration
	 *            动画的时长,毫秒为单位
	 * @return
	 */
	public static TranslateAnimation translateSelfAbsolute(float fromX, float toX, float fromY, float toY,
			int duration) {
		TranslateAnimation animation = new TranslateAnimation(fromX, toX, fromY, toY);
		animation.setDuration(duration);
		animation.setFillAfter(fillAfter);
		return animation;
	}

	/**
	 * 相对于自己平移,利用百分比进行平移
	 * 
	 * @param v
	 *            动画作用的View控件
	 * @param fromXPercent
	 *            动画的起始点横坐标的百分比
	 * @param toXPercent
	 *            动画的结束点横坐标的百分比
	 * @param fromYPercent
	 *            动画的起始点纵坐标的百分比
	 * @param toYPercent
	 *            动画的结束点纵坐标的百分比
	 */
	public static void translateSelf(View v, float fromXPercent, float toXPercent, float fromYPercent,
			float toYPercent) {
		translateSelf(v, fromXPercent, toXPercent, fromYPercent, toYPercent, defaultDuration);
	}

	/**
	 * 返回一个动画
	 * 
	 * @param fromXPercent
	 *            动画的起始点横坐标的百分比
	 * @param toXPercent
	 *            动画的结束点横坐标的百分比
	 * @param fromYPercent
	 *            动画的起始点纵坐标的百分比
	 * @param toYPercent
	 *            动画的结束点纵坐标的百分比
	 * @return
	 */
	public static TranslateAnimation translateSelf(float fromXPercent, float toXPercent, float fromYPercent,
			float toYPercent) {
		return translateSelf(fromXPercent, toXPercent, fromYPercent, toYPercent, defaultDuration);
	}

	/**
	 * 相对于自己平移,利用百分比进行平移
	 * 
	 * @param v
	 *            动画作用的View控件
	 * @param fromXPercent
	 *            动画的起始点横坐标的百分比
	 * @param toXPercent
	 *            动画的结束点横坐标的百分比
	 * @param fromYPercent
	 *            动画的起始点纵坐标的百分比
	 * @param toYPercent
	 *            动画的结束点纵坐标的百分比
	 * @param duration
	 *            动画的时长,毫秒为单位
	 */
	public static void translateSelf(View v, float fromXPercent, float toXPercent, float fromYPercent, float toYPercent,
			int duration) {
		v.startAnimation(translateSelf(fromXPercent, toXPercent, fromYPercent, toYPercent, duration));
	}

	/**
	 * 
	 * @param fromXPercent
	 *            动画的起始点横坐标的百分比
	 * @param toXPercent
	 *            动画的结束点横坐标的百分比
	 * @param fromYPercent
	 *            动画的起始点纵坐标的百分比
	 * @param toYPercent
	 *            动画的结束点纵坐标的百分比
	 * @param duration
	 *            动画的时长,毫秒为单位
	 * @return 返回一个动画
	 */
	public static TranslateAnimation translateSelf(float fromXPercent, float toXPercent, float fromYPercent,
			float toYPercent, int duration) {
		int mode = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation animation = new TranslateAnimation(mode, fromXPercent, mode, toXPercent, mode, fromYPercent,
				mode, toYPercent);
		animation.setDuration(duration);
		animation.setFillAfter(fillAfter);
		return animation;
	}

	/**
	 * 相对于父容器平移,利用百分比进行平移
	 * 
	 * @param v
	 *            动画作用的View控件
	 * @param fromXPercent
	 *            动画的起始点横坐标的百分比
	 * @param toXPercent
	 *            动画的结束点横坐标的百分比
	 * @param fromYPercent
	 *            动画的起始点纵坐标的百分比
	 * @param toYPercent
	 *            动画的结束点纵坐标的百分比
	 */
	public static void translateParent(View v, float fromXPercent, float toXPercent, float fromYPercent,
			float toYPercent) {
		translateParent(v, fromXPercent, toXPercent, fromYPercent, toYPercent, defaultDuration);
	}

	/**
	 * 相对于父容器平移,利用百分比进行平移
	 * 
	 * @param v
	 *            动画作用的View控件
	 * @param fromXPercent
	 *            动画的起始点横坐标的百分比
	 * @param toXPercent
	 *            动画的结束点横坐标的百分比
	 * @param fromYPercent
	 *            动画的起始点纵坐标的百分比
	 * @param toYPercent
	 *            动画的结束点纵坐标的百分比
	 * @param duration
	 *            动画的时长,毫秒为单位
	 */
	public static void translateParent(View v, float fromXPercent, float toXPercent, float fromYPercent,
			float toYPercent, int duration) {
		int mode = TranslateAnimation.RELATIVE_TO_PARENT;
		TranslateAnimation animation = new TranslateAnimation(mode, fromXPercent, mode, toXPercent, mode, fromYPercent,
				mode, toYPercent);
		animation.setDuration(duration);
		animation.setFillAfter(fillAfter);
		v.startAnimation(animation);
	}

}
