package xiaojinzi.android.adapter.gesture;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * 手势监听类的一个空实现的类
 * 
 * @author xiaojinzi
 * 
 */
public class GestureListenerAdapter implements
		GestureDetector.OnGestureListener {

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return true;
	}

}
