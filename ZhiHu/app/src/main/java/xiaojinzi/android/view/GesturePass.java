package xiaojinzi.android.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import xiaojinzi.java.util.MathUtil;

/**
 * 手势密码的输入框<br>
 * 1.小圆的半径是自身的控件长和宽较短的这个长度的1/24<br>
 * 2.线条宽度和半径一致
 * 
 * @author cxj
 *
 */
public class GesturePass extends View {

	public GesturePass(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// 初始化数据
		initData();
	}

	public GesturePass(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GesturePass(Context context) {
		this(context, null);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		p_circle_nor = new Paint();
		p_stroke_circle_nor = new Paint();
		p_stroke_circle_select = new Paint();
		p_line = new Paint();
		p_circle_select = new Paint();

		p_circle_nor.setColor(constant.solidCircleNor); // 小圆的前景色
		p_stroke_circle_nor.setColor(constant.strokeCircleNor);
		p_line.setColor(constant.lineColorNor); // 线条的颜色
		p_circle_select.setColor(constant.solidCircleSelect); // 小圆选中之后的颜色
		p_stroke_circle_select.setColor(constant.strokeCircleSelect); // 小圆选中之后的颜色

		// 设置画笔为实心的
		p_stroke_circle_nor.setStyle(Style.FILL);
		p_stroke_circle_select.setStyle(Style.FILL);
		p_stroke_circle_nor.setStrokeWidth(constant.strokeCircleLineWidth);
		p_stroke_circle_select.setStrokeWidth(constant.strokeCircleLineWidth);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 手势的九个点,实心圆的九个点的集合
	 */
	private List<Point> solidCircleList = new ArrayList<Point>();

	/**
	 * 这里存放所有的手势滑动之后碰到的圆的圆心
	 */
	private List<Point> list = new ArrayList<Point>();

	/**
	 * 小圆的半径
	 */
	private int radius;

	/**
	 * 小圆的画笔,正常没有选择的状态
	 */
	private Paint p_circle_nor = null;

	/**
	 * 空心圆没有选择的时候的画笔
	 */
	private Paint p_stroke_circle_nor = null;

	/**
	 * 空心圆选择之后的画笔
	 */
	private Paint p_stroke_circle_select = null;

	/**
	 * 当手势已经滑动到了之后的画笔
	 */
	private Paint p_circle_select = null;

	/**
	 * 直线的画笔
	 */
	private Paint p_line = null;

	/**
	 * 临时的点对象
	 */
	private Point currentPoint = null;

	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		// 计算所需的数据
		computePreData();

		// 循环画出线条
		for (int i = 0; i < list.size() - 1; i++) {
			Point p1 = list.get(i);
			Point p2 = list.get(i + 1);
			c.drawLine(p1.x, p1.y, p2.x, p2.y, p_line);
		}

		// 如果临时的那个点不为null,说明用户正在滑动,那么最后一个点和用户当前的临时点也要连线
		if (currentPoint != null && list.size() > 0) {
			c.drawLine(list.get(list.size() - 1).x, list.get(list.size() - 1).y, currentPoint.x, currentPoint.y,
					p_line);
		}

		// 循环九个圆,然后画出这九个圆
		for (int i = 0; i < solidCircleList.size(); i++) {
			Point p = solidCircleList.get(i);
			if (list.contains(p)) {
				// 绘制实心圆
				c.drawCircle(p.x, p.y, radius, p_circle_select);
				// 绘制空心圆
				c.drawCircle(p.x, p.y, radius * constant.strokeRadiusPercent, p_stroke_circle_select);
			} else {
				// 绘制实心圆
				c.drawCircle(p.x, p.y, radius, p_circle_nor);
				// 绘制空心圆
				c.drawCircle(p.x, p.y, radius * constant.strokeRadiusPercent, p_stroke_circle_nor);
			}
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {

		int action = e.getAction();
		int x = (int) e.getX();
		int y = (int) e.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN: // 如果是按下

			p_circle_select.setColor(constant.solidCircleSelect);
			p_stroke_circle_select.setColor(constant.strokeCircleSelect);

			p_line.setColor(constant.lineColorNor);

			list.clear();
			currentPoint = new Point();
			currentPoint.x = x;
			currentPoint.y = y;
			check();
			break;
		case MotionEvent.ACTION_MOVE: // 如果是移动
			currentPoint.x = x;
			currentPoint.y = y;
			check();

			break;
		case MotionEvent.ACTION_UP: // 如果是抬起
			currentPoint = null;
			boolean b = false;
			if (l != null) {

				String result = computerResult();

				// 如果绘制的密码不是为null并且不是为空字符串""的时候
				if (!TextUtils.isEmpty(result)) {
					// 结果交给用户处理,返回true代表密码正确,false代表密码错误
					b = l.onResul(result);
					if (b) {
						l.success(result);
					} else {
						l.error(result);
					}
				}

			}
			if (b) { // 如果用户说正确
				// 修改实心圆和空心圆的颜色值
				p_circle_select.setColor(constant.solidCircleTrue);
				p_stroke_circle_select.setColor(constant.strokeCircleTrue);

				// 修改线条的颜色为正确的时候显示的颜色
				p_line.setColor(constant.lineColorTrue);

			} else { // 如果用户说错误
				// 修改实心圆和空心圆的颜色值
				p_circle_select.setColor(constant.solidCircleFalse);
				p_stroke_circle_select.setColor(constant.strokeCircleFalse);

				// 修改线条的颜色为错误的时候显示的颜色
				p_line.setColor(constant.lineColorFalse);

			}
			break;
		}

		// 重画
		invalidate();

		return true;
	}

	/**
	 * 检测临时的这个点是不是已经在九个空心圆的中的其中一个里面了
	 */
	private void check() {
		for (int i = 0; i < solidCircleList.size(); i++) {
			Point p = solidCircleList.get(i);
			// 如果发现九个圆有一个是包含这个点的,并且集合中没有添加过这个点！那就添加这个点到集合
			if (MathUtil.isCircleContainsPoint(p, radius * constant.strokeRadiusPercent, currentPoint)
					&& !list.contains(p)) {
				list.add(p);
				break;
			}
		}
	}

	/**
	 * 计算结果并返回
	 * 
	 * @return
	 */
	private String computerResult() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Point p = list.get(i);
			int index = solidCircleList.indexOf(p);
			sb.append(index + 1);
		}
		return sb.toString();
	}

	/**
	 * 计算绘制的时候所需要的数据<br>
	 * 1.计算九个圆的圆心位置<br>
	 * 2.计算半径
	 */
	private void computePreData() {
		solidCircleList.clear();
		int width = getWidth();
		int height = getHeight();
		// 计算小圆的半径
		if (width > height) {
			radius = height / constant.radiusPercent;
		} else {
			radius = width / constant.radiusPercent;
		}
		// 确定线条的宽度
		p_line.setStrokeWidth(radius);

		// 确定九个小圆的位置,并添加到集合中
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Point p = new Point();
				p.x = j * width / 3 + width / 6;
				p.y = i * height / 3 + height / 6;
				solidCircleList.add(p);
			}
		}
	}

	/**
	 * 重置,还原初始的状态
	 */
	public void reset() {
		this.list.clear();
		this.invalidate();
	}

	// =====================以下是接口和接口的适配器和常量的内部类=========
	private OnResultListener l = null;

	/**
	 * 设置监听结果
	 * 
	 * @param l
	 */
	public void setOnResultListener(OnResultListener l) {
		this.l = l;
	}

	/**
	 * 监听结果的接口
	 * 
	 * @author cxj
	 *
	 */
	public interface OnResultListener {

		/**
		 * 对监听结果的用户暴露滑动后的生成的结果,以数字拼接的字符串为结果,如果返回false,代表
		 * 
		 * @param result
		 * @return
		 */
		public boolean onResul(String result);

		/**
		 * 成功的时候调用的方法
		 * 
		 * @param result
		 */
		public void success(String result);

		/**
		 * 密码错误的时候调用的方法
		 * 
		 * @param result
		 */
		public void error(String result);

	}

	public abstract static class OnResultListenerAdapter implements OnResultListener {

		@Override
		public void success(String result) {
		}

		@Override
		public void error(String result) {
		}

	}

	/**
	 * 常量存放的地方
	 * 
	 * @author cxj
	 *
	 */
	private static class constant {
		/**
		 * 自身控件长和宽较短的一个与半径的比值
		 */
		public static int radiusPercent = 24;

		/**
		 * 这是是空心圆的半径,这个半径基于实心圆的半径,这个值代表,是实心圆的半径的几倍
		 */
		public static int strokeRadiusPercent = 3;

		/**
		 * 空心圆的线条宽度
		 */
		public static int strokeCircleLineWidth = 4;

		/**
		 * 正常的时候线条的颜色,不是选中的小圆之间连线的颜色,比如最后一个圆和临时点之间的连线
		 */
		public static int lineColorNor = Color.GREEN;

		/**
		 * 小圆被选中的之间的连线的颜色
		 */
		public static int lineColorSelect = Color.GREEN;

		/**
		 * 当密码正确的时候的线条颜色
		 */
		public static int lineColorTrue = Color.GREEN;
		/**
		 * 当密码错误的时候的线条颜色
		 */
		public static int lineColorFalse = Color.RED;

		/**
		 * 没有选中的时候的实心小圆的颜色
		 */
		public static int solidCircleNor = Color.parseColor("#4686BA");

		/**
		 * 选中的时候实心小圆的颜色
		 */
		public static int solidCircleSelect = Color.BLUE;

		/**
		 * 当密码正确的时候的实心小圆的颜色
		 */
		public static int solidCircleTrue = Color.GREEN;

		/**
		 * 当密码错误的时候的实心小圆的颜色
		 */
		public static int solidCircleFalse = Color.RED;

		/**
		 * 没有选中的时候的空心小圆的颜色
		 */
		public static int strokeCircleNor = Color.parseColor("#AA4686BA");

		/**
		 * 选中的时候空心小圆的颜色
		 */
		public static int strokeCircleSelect = Color.parseColor("#880000FF");

		/**
		 * 当密码正确的时候的空心小圆的颜色
		 */
		public static int strokeCircleTrue = Color.parseColor("#7700CC00");

		/**
		 * 当密码错误的时候的空心小圆的颜色
		 */
		public static int strokeCircleFalse = Color.parseColor("#88FF0000");

	}

}
