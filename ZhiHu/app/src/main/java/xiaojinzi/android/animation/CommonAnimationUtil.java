package xiaojinzi.android.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * 常见的动画的工具<br>
 * 1.比如控件的抖动
 * 
 * @author xiaojinzi
 *
 */
public class CommonAnimationUtil {

	/**
	 * 水平的抖动模式
	 */
	public static final int HORIZONTALSHAKE = 0;

	/**
	 * 数值的抖动模式
	 */
	public static final int VERTICALSHAKE = 1;

	/**
	 * 默认的动画时间
	 */
	public static final int DEFAULTDURATION = 100;

	/**
	 * 默认的重复次数
	 */
	public static final int DEFAULTREPEATCOUNT = 4;

	/**
	 * 默认的水平偏移百分比
	 */
	public static final float DEFAULTHORIZONTALPERCENT = 0.1f;

	/**
	 * 默认的数值偏移百分比
	 */
	public static final float DEFAULTVERTICALPERCENT = 0.1f;

	/**
	 * 类似于窗口抖动的效果,模式表示如何抖动
	 * 
	 * @param v
	 *            要进行抖动的控件
	 * @param mode
	 *            抖动的模式 {@link #HORIZONTALSHAKE} or {@link #VERTICALSHAKE}
	 */
	public static void shake(View v, int mode) {
		shake(v, mode, DEFAULTHORIZONTALPERCENT, DEFAULTVERTICALPERCENT, DEFAULTDURATION, DEFAULTREPEATCOUNT);
	}

	/**
	 * 类似于窗口抖动的效果,模式表示如何抖动
	 * 
	 * @param v
	 *            要进行抖动的控件
	 * @param mode
	 *            抖动的模式 {@link #HORIZONTALSHAKE} or {@link #VERTICALSHAKE}
	 * @param horizontalPercent
	 *            水平抖动的百分比,也就是水平偏移多少,这里采用百分比,自身*百分比=偏移量
	 * @param verticalPercent
	 *            竖直抖动的百分比,也就是竖直偏移多少,这里采用百分比,自身*百分比=偏移量
	 */
	public static void shake(View v, int mode, float horizontalPercent, float verticalPercent) {
		shake(v, mode, horizontalPercent, verticalPercent, DEFAULTDURATION, DEFAULTREPEATCOUNT);
	}

	/**
	 * 类似于窗口抖动的效果,模式表示如何抖动
	 * 
	 * @param v
	 *            要进行抖动的控件
	 * @param mode
	 *            抖动的模式 {@link #HORIZONTALSHAKE} or {@link #VERTICALSHAKE}
	 * @param horizontalPercent
	 *            水平抖动的百分比,也就是水平偏移多少,这里采用百分比,自身*百分比=偏移量
	 * @param verticalPercent
	 *            竖直抖动的百分比,也就是竖直偏移多少,这里采用百分比,自身*百分比=偏移量
	 * @param repeatCount
	 *            抖动的次数
	 */
	public static void shake(View v, int mode, float horizontalPercent, float verticalPercent, int repeatCount) {
		shake(v, mode, horizontalPercent, verticalPercent, DEFAULTDURATION, repeatCount);
	}

	/**
	 * 类似于窗口抖动的效果,模式表示如何抖动
	 * 
	 * @param v
	 *            要进行抖动的控件
	 * @param mode
	 *            抖动的模式 {@link #HORIZONTALSHAKE} or {@link #VERTICALSHAKE}
	 * @param duration
	 *            抖动的时间
	 * @param horizontalPercent
	 *            水平抖动的百分比,也就是水平偏移多少,这里采用百分比,自身*百分比=偏移量
	 * @param verticalPercent
	 *            竖直抖动的百分比,也就是竖直偏移多少,这里采用百分比,自身*百分比=偏移量
	 */
	public static void shake(View v, int mode, int duration, float horizontalPercent, float verticalPercent) {
		shake(v, mode, horizontalPercent, verticalPercent, duration, DEFAULTREPEATCOUNT);
	}

	/**
	 * 类似于窗口抖动的效果,模式表示如何抖动
	 * 
	 * @param v
	 *            要进行抖动的控件
	 * @param mode
	 *            抖动的模式 {@link #HORIZONTALSHAKE} or {@link #VERTICALSHAKE}
	 * @param horizontalPercent
	 *            水平抖动的百分比,也就是水平偏移多少,这里采用百分比,自身*百分比=偏移量
	 * @param verticalPercent
	 *            竖直抖动的百分比,也就是竖直偏移多少,这里采用百分比,自身*百分比=偏移量
	 * @param duration
	 *            抖动的时间
	 * @param repeatCount
	 *            抖动的次数
	 */
	public static void shake(View v, int mode, float horizontalPercent, float verticalPercent, int duration,
			int repeatCount) {

		TranslateAnimation t = null;

		switch (mode) {

		case HORIZONTALSHAKE: // 如果是水平的抖动模式

			t = TranslateAnimationUtil.translateSelf(-horizontalPercent, horizontalPercent, 0, 0, duration);
			t.setRepeatMode(Animation.REVERSE);
			t.setRepeatCount(4);
			v.startAnimation(t);

			break;
		case VERTICALSHAKE: // 如果是竖直的抖动模式

			t = TranslateAnimationUtil.translateSelf(0, 0, -verticalPercent, verticalPercent, duration);
			t.setRepeatMode(Animation.REVERSE);
			t.setRepeatCount(4);
			v.startAnimation(t);

			break;
		}
	}

}
