package xiaojinzi.android.graphics;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * android中图形化界面的绘制的时候的字符串工具类
 * 
 * @author xiaojinzi
 *
 */
public class GraphicsUtil {

	/**
	 * 返回包围一个字符串的矩形
	 * 
	 * @param content
	 * @param p
	 * @return
	 */
	public static Rect getRect(String content, Paint p) {

		Rect rect = new Rect();

		// 返回包围整个字符串的最小的一个Rect区域
		p.getTextBounds(content, 0, content.length(), rect);

		return rect;

	}

}
