package xiaojinzi.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class MyProgress extends View {

	public MyProgress(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initData();
	}

	public MyProgress(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyProgress(Context context) {
		this(context, null);
	}

	// 自己是宽度是充满父容器,高度是定死
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		p_out = new Paint();
		p_out.setColor(bg);
		p_in = new Paint();
		p_in.setColor(fore);
	}

	private Paint p_out = null;
	private Paint p_in = null;

	private static int bg = Color.parseColor("#F6C629");
	private static int fore = Color.parseColor("#00909D");

	// 进度条的高度,这里定死了
	private static int myHeight = 24 * 2;

	private RectF r_out = null;
	private RectF r_in = null;

	private static int max = 100;
	private static int min = 0;

	private int progress = 20;

	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);

		if (r_out == null) {
			r_out = new RectF(0, 0, getWidth(), getHeight());
			r_in = new RectF();
		}

		c.drawRoundRect(r_out, myHeight / 2, myHeight / 2, p_out);

		r_in.set(0, 0, getWidth() * progress / 100, getHeight());

		c.drawRoundRect(r_in, myHeight / 2, myHeight / 2, p_in);

	}

}
