package xiaojinzi.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import xiaojinzi.android.animation.RotateAnimationUtil;

/**
 * 动画的按钮
 * 
 * @author xiaojinzi
 *
 */
public class AnimationButton extends View {

	public AnimationButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initData();
	}

	public AnimationButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AnimationButton(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// 获取宽和高的计算模式
//		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
//		int modeHeigth = MeasureSpec.getMode(heightMeasureSpec);

		int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);

		// if (modeWidth == MeasureSpec.UNSPECIFIED) { // 如果是包裹内容
		measuredWidth = 50;
		// }
		// if (modeHeigth == MeasureSpec.UNSPECIFIED) { // 如果是包裹内容
		measuredHeight = 50;
		// }
		setMeasuredDimension(measuredWidth, measuredHeight);

	}

	// 画笔对象
	private Paint p = null;

	private Paint p_bg = null;

	private Paint p_text = null;

	// 绘制的画笔的线条宽度
	private float lineWidth = 5;

	// 线条的颜色
	private int lineColor = Color.parseColor("#99CC00");

	private int bgColor = Color.parseColor("#0099CC");

	private int textColor = Color.WHITE;

	// 绘制的图像在这个框框中
	private RectF oval1 = null;

	// 进度的最大值和最小值
	public static final float MAX = 1000;
	public static final float MIN = 0;
	// 最大值和100相差几倍
	private int tt = 10;

	// 当前的进度
	private float currentProgress = 30;

	// 圆弧的起点
	private float startAngle = 270;

	// 进度为0的时候的绘制角度
	private float sweepAngle = 0;

	// 绘制文本的时候的正中的点
	private Point p_center = null;

	/**
	 * 初始化数据
	 */
	private void initData() {
		p = new Paint();
		p_text = new Paint();
		p_bg = new Paint();
		// p.setStyle(Paint.Style.STROKE);// 设置空心
		p.setStrokeWidth(lineWidth);
		p.setColor(lineColor);

		p_text.setColor(textColor);
		p_text.setTextSize(40);
		p_text.setStrokeWidth(10);

		p_bg.setColor(bgColor);
	}

	@Override
	public void draw(Canvas c) {
		super.draw(c);

		// 计算相关的数据
		compute();

		if (oval1 == null) {
			// 绘制的区域为自己这个控件的大小
			oval1 = new RectF(0, 0, getWidth(), getHeight());
			p_center = new Point();
			Rect r = getRect("10%");
			p_center.x = getWidth() / 2 - r.width() / 2;
			p_center.y = getHeight() / 2 + r.height() / 2;
		}
		c.drawArc(oval1, 0, 360, false, p_bg);// 背景

		c.drawArc(oval1, startAngle, sweepAngle, false, p);// 小弧形

		// 绘制百分比文本
		c.drawText((int) currentProgress / tt + "%", p_center.x, p_center.y, p_text);
	}

	/**
	 * 计算绘制的时候所需要的数据
	 */
	private void compute() {
		// 拿到进度的百分比
		float percent = currentProgress / MAX;
		// 拿到需要绘制的圆弧的角度
		sweepAngle = 360 * percent;
		// 计算出圆弧开始的角度
		startAngle = 90 - sweepAngle / 2;
	}

	/**
	 * 设置进度
	 * 
	 * @param progress
	 */
	public void setProgress(int progress) {
		if (progress < MIN) {
			this.currentProgress = MIN;
		} else if (progress > MAX) {
			this.currentProgress = MAX;
		} else {
			this.currentProgress = progress;
		}
		if (l != null) {
			l.progressChange((int) (currentProgress));
		}
		// 重新绘制控件
		this.invalidate();
		if (this.currentProgress == MAX) {
			RotateAnimationUtil.rotateSelf(this, 0, 360, 500);
		}
	}

	/**
	 * 返回包围一个文本的宽和高
	 * 
	 * @param content
	 * @return
	 */
	private Rect getRect(String content) {
		Rect rect = new Rect();
		p_text.getTextBounds(content, 0, content.length(), rect);
		return rect;
	}

	private OnProgressChange l = null;

	/**
	 * 设置进度监听
	 * 
	 * @param l
	 */
	public void setOnProgressChange(OnProgressChange l) {
		this.l = l;
	}

	/**
	 * 进度改变的监听接口
	 * 
	 * @author xiaojinzi
	 * 
	 */
	public interface OnProgressChange {
		/**
		 * 当进度发送改变的时候调用,达到最大值的时候停止调用<br>
		 * {@link AnimationButton#MAX} or {@link AnimationButton#MIN}
		 * 
		 * @param progress
		 */
		void progressChange(int progress);
	}

}
