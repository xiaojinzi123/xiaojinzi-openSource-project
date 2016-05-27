package xiaojinzi.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义等级进度的控件
 * 
 * @author xiaojinzi
 *
 */
public class LevelProgress extends View {

	public LevelProgress(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public LevelProgress(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LevelProgress(Context context) {
		this(context, null);
	}

	private float radius = 0;

}
