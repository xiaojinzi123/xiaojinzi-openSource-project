package xiaojinzi.android.view.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import xiaojinzi.android.adapter.gesture.GestureListenerAdapter;
import xiaojinzi.android.util.log.L;

/**
 * Created by cxj on 2016/4/1.
 * 一个水平滚动的基类
 */
public abstract class MyHorizontalScrollView extends ViewGroup {

    public MyHorizontalScrollView(Context context) {
        this(context, null);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        this.context = context;
        scroller = new Scroller(context);
        gestureDetector = new GestureDetector(context, listener);
    }

    /**
     * 上下文对象
     */
    protected Context context;

    /**
     * 触摸移动的时候需要平滑的移动的数据计算工具类
     */
    private Scroller scroller;

    /**
     * 手势装饰者
     */
    private GestureDetector gestureDetector;

    /**
     * 飞溅出去的水平初速度
     */
    private int velocityX;

    /**
     * 手势监听者
     */
    private GestureDetector.OnGestureListener listener = new GestureListenerAdapter() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            MyHorizontalScrollView.this.velocityX = (int) velocityX;
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    //重写的方法      start ====================================================


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int maxHeight = 0;
        if (mode == MeasureSpec.UNSPECIFIED) { //高度是包裹内容的
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                int measuredHeight = view.getMeasuredHeight();
                if (measuredHeight > maxHeight) {
                    maxHeight = measuredHeight;
                }
            }
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), maxHeight);
    }

    /**
     * 控件自身的宽度
     */
    protected int mWidth;

    /**
     * 控件自身的高度
     */
    protected int mHeight;


    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            removeRepeatData();
            scrollTo(scroller.getCurrX(), 0);
        }
        requestLayout();
    }

    private int currentX;
    private int currentY;
    private int finalX;
    private int finalY;

    private boolean isMove = false;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //获取是什么状态,按下还是移动还是抬起
        int action = e.getAction();
        //让触摸的一个类计算出有没有抛出的动作时候的初速度
        gestureDetector.onTouchEvent(e);
        if (action == MotionEvent.ACTION_DOWN) { //按下
            velocityX = 0;
            currentX = (int) e.getX();
            currentY = (int) e.getY();
        } else if (action == MotionEvent.ACTION_MOVE) { //移动
            finalX = (int) e.getX();
            finalY = (int) e.getY();
            int dx = finalX - currentX;
            int dy = finalY - currentY;
            if (Math.abs(dx) > Math.abs(dy)) {
                requestDisallowInterceptTouchEvent(true);
            }
            if (getScrollX() - dx <= getLeftBound()) {
                dx = getScrollX();
            }
            if (getScrollX() - dx >= getRightBound()) {
                dx = getScrollX() - getRightBound();
            }
            scrollBy(-dx, 0);
            currentX = (int) e.getX();
            currentY = (int) e.getY();
//        } else if (action == MotionEvent.ACTION_UP) { //抬起
        } else {
            //说明飞溅了
            if (velocityX != 0) {
                L.s("feijian le " + velocityX);
                if (velocityX < 0) {
                    smoothTo(getRightBound());
                } else {
                    smoothTo(getLeftBound());
                }
                return true;
            }else {
                if (getScrollX() < getCenterBound()) {
                    smoothTo(getLeftBound());
                } else {
                    smoothTo(getRightBound());
                }
            }
        }
        super.onTouchEvent(e);
        return true;
    }


    //重写的方法      end ====================================================

    //私有方法      start ====================================================

    /**
     * 平滑移动动画的时长
     */
    private int defalutDuring = 1200;

    /**
     * 飞溅的滑动
     *
     * @param velocityX
     * @param minX
     * @param maxX
     */
    protected void fling(int velocityX, int minX, int maxX) {
        scroller.abortAnimation();
        scroller.fling(getScrollX(), 0, -velocityX, 0, minX, maxX, 0, 0);
        scrollTo(scroller.getCurrX(), 0);
    }

    /**
     * 平滑的移动到指定位置
     */
    protected void smoothTo(int finalX) {
        scroller.abortAnimation();
        scroller.startScroll(getScrollX(), 0, finalX - getScrollX(), 0, defalutDuring);
        scrollTo(scroller.getCurrX(), 0);
    }

    /**
     * 平滑的移动dx的距离
     *
     * @param dx
     */
    protected void smoothBy(int dx) {
        scroller.startScroll(getScrollX(), 0, dx, 0, defalutDuring);
    }

    /**
     * 用于消除滑动的时候出现的重复数据
     * 因为平滑的滑动的时候产生的数据很多都是重复的
     * 都是所以这里如果遇到事重复的就拿下一个,直到不重复为止
     */
    private void removeRepeatData() {
        int scrollX = getScrollX();
        while (scrollX != scroller.getFinalX() && scrollX == scroller.getCurrX()) {
            scroller.computeScrollOffset();
        }
    }

    //私有方法      end ====================================================
    //需要子类实现的方法      start ====================================================


    /**
     * 获取左边的边界x值
     *
     * @return
     */
    public int getLeftBound() {
        return 0;
    }

    /**
     * 获取右边的边界x值
     *
     * @return
     */
    public int getRightBound() {
        return mWidth;
    }


    public int getCenterBound() {
        return mWidth / 2;
    }


    //需要子类实现的方法      end ====================================================


}
