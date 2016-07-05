package xiaojinzi.base.java.util;

import android.graphics.Point;

/**
 * 有关数学的工具类
 * 
 * @author cxj
 *
 */
public class MathUtil {

	/**
	 * 判断一个点是不是在一个圆里面,包括边界
	 * 
	 * @param circleCenter
	 *            圆心
	 * @param radius
	 *            半径
	 * @param anotherPoint
	 *            另一个点
	 * @return
	 */
	public static boolean isCircleContainsPoint(Point circleCenter, double radius, Point anotherPoint) {

		if (radius < 0) {
			throw new IllegalAccessError("半径不能小于0");
		}
		return (Math.pow(circleCenter.x - anotherPoint.x, 2) + Math.pow(circleCenter.y - anotherPoint.y, 2))
				- radius * radius > 0 ? false : true;
	}

}
