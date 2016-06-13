package xiaojinzi.animation;

import android.view.View;
import android.view.animation.RotateAnimation;

import xiaojinzi.animation.baseAnimation.BaseAnimationUtil;

/**
 * 旋转动画的工具类
 * 
 * @author xiaojinzi
 *
 */
public class RotateAnimationUtil extends BaseAnimationUtil {

	/**
	 * 相对于自己旋转,旋转中心点为自己的中心,时长也是默认的
	 * 
	 * @param v
	 * @param fromDegrees
	 * @param toDegrees
	 */
	public static void rotateSelf(View v, float fromDegrees, float toDegrees) {
		rotateSelf(v, fromDegrees, toDegrees, defaultCenterX, defaultCenterY, defaultDuration);
	}

	/**
	 * 返回一个动画:<br>
	 * 相对于自己旋转,旋转中心点为自己的中心,时长也是默认的
	 * 
	 * @param fromDegrees
	 * @param toDegrees
	 * @return
	 */
	public static RotateAnimation rotateSelf(float fromDegrees, float toDegrees) {
		return rotateSelf(fromDegrees, toDegrees, defaultCenterX, defaultCenterY, defaultDuration);
	}

	/**
	 * 相对于自己旋转,旋转中心点为自己的中心,时长自定义
	 * 
	 * @param v
	 * @param fromDegrees
	 * @param toDegrees
	 * @param duration
	 */
	public static void rotateSelf(View v, float fromDegrees, float toDegrees, int duration) {
		rotateSelf(v, fromDegrees, toDegrees, defaultCenterX, defaultCenterY, duration);
	}

	/**
	 * 返回一个动画:<br>
	 * 相对于自己旋转,旋转中心点为自己的中心,时长自定义
	 * 
	 * @param fromDegrees
	 * @param toDegrees
	 * @param duration
	 * @return
	 */
	public static RotateAnimation rotateSelf(float fromDegrees, float toDegrees, int duration) {
		return rotateSelf(fromDegrees, toDegrees, defaultCenterX, defaultCenterY, duration);
	}

	/**
	 * 相对于自己旋转,旋转中心点为用户自定义,时长是默认的
	 * 
	 * @param v
	 * @param fromDegrees
	 * @param toDegrees
	 * @param centerX
	 * @param centerY
	 */
	public static void rotateSelf(View v, float fromDegrees, float toDegrees, float centerX, float centerY) {
		rotateSelf(v, fromDegrees, toDegrees, centerX, centerY, defaultDuration);
	}

	/**
	 * 返回一个动画:<br>
	 * 相对于自己旋转,旋转中心点为用户自定义,时长是默认的
	 * 
	 * @param fromDegrees
	 * @param toDegrees
	 * @param centerX
	 * @param centerY
	 * @return
	 */
	public static RotateAnimation rotateSelf(float fromDegrees, float toDegrees, float centerX, float centerY) {
		return rotateSelf(fromDegrees, toDegrees, centerX, centerY, defaultDuration);
	}

	/**
	 * 相对于自己旋转
	 * 
	 * @param v
	 *            要旋转的控件
	 * @param fromDegrees
	 *            起始角度
	 * @param toDegrees
	 *            结束角度
	 * @param centerX
	 *            旋转中心x,建议用百分比，比如0.5f
	 * @param centerY
	 *            旋转中心y,建议用百分比，比如0.5f
	 * @param duration
	 *            旋转的时长,毫秒的单位
	 */
	public static void rotateSelf(View v, float fromDegrees, float toDegrees, float centerX, float centerY,
			int duration) {

		RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, RotateAnimation.RELATIVE_TO_SELF,
				centerX, RotateAnimation.RELATIVE_TO_SELF, centerY);

		// 动画的时长
		animation.setDuration(duration);

		animation.setFillAfter(fillAfter);

		v.startAnimation(animation);

	}

	/**
	 * 返回一个旋转动画
	 * 
	 * @param fromDegrees
	 *            起始角度
	 * @param toDegrees
	 *            结束角度
	 * @param centerX
	 *            旋转中心x,建议用百分比，比如0.5f
	 * @param centerY
	 *            旋转中心y,建议用百分比，比如0.5f
	 * @param duration
	 *            旋转的时长,毫秒的单位
	 * @return
	 */
	public static RotateAnimation rotateSelf(float fromDegrees, float toDegrees, float centerX, float centerY,
			int duration) {

		RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, RotateAnimation.RELATIVE_TO_SELF,
				centerX, RotateAnimation.RELATIVE_TO_SELF, centerY);

		// 动画的时长
		animation.setDuration(duration);

		animation.setFillAfter(fillAfter);

		return animation;

	}

	/**
	 * 默认使用父容器的中心点进行旋转,默认时间为2000毫秒
	 * 
	 * @param v
	 * @param fromDegrees
	 * @param toDegrees
	 */
	public static void rotateParent(View v, float fromDegrees, float toDegrees) {
		rotateParent(v, fromDegrees, toDegrees, defaultCenterX, defaultCenterY);
	}

	/**
	 * 使用默认时间为2000毫秒
	 * 
	 * @param v
	 * @param fromDegrees
	 * @param toDegrees
	 * @param centerX
	 * @param centerY
	 */
	public static void rotateParent(View v, float fromDegrees, float toDegrees, float centerX, float centerY) {
		rotateParent(v, fromDegrees, toDegrees, centerX, centerY, defaultDuration);
	}

	/**
	 * 相对于父容器旋转
	 * 
	 * @param v
	 *            要旋转的控件
	 * 
	 * @param fromDegrees
	 *            起始角度
	 * @param toDegrees
	 *            结束角度
	 * @param centerX
	 *            旋转中心x,建议用百分比，比如0.5f
	 * @param centerY
	 *            旋转中心y,建议用百分比，比如0.5f
	 * @param duration
	 *            旋转的时长,毫秒的单位
	 */
	public static void rotateParent(View v, float fromDegrees, float toDegrees, float centerX, float centerY,
			int duration) {

		RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, RotateAnimation.RELATIVE_TO_PARENT,
				centerX, RotateAnimation.RELATIVE_TO_PARENT, centerY);

		// 动画的时长
		animation.setDuration(duration);

		animation.setFillAfter(fillAfter);

		// 启动动画
		v.startAnimation(animation);

	}

}
