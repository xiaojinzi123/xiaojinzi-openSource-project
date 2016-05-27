package xiaojinzi.android.view.advView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

import xiaojinzi.android.adapter.gesture.GestureListenerAdapter;
import xiaojinzi.android.util.log.L;

/**
 * 广告控件,用法暂时定为下面几种:
 * 1.通过添加图片的地址来展示图片
 * 2.指示器选择使用RadioButton来实现
 * Created by cxj on 2016/2/28.
 */
public class AdvView extends ViewGroup implements View.OnClickListener, Runnable {

    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            setCurrentImage(what);
        }
    };

    public AdvView(Context context) {
        this(context, null);
    }

    public AdvView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdvView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        this.setOnClickListener(this);
        flag = true;//让线程生效
        t = new Thread(this);
        t.start();
    }

    /**
     * 上下文对象
     */
    private Context context;

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
            AdvView.this.velocityX = (int) velocityX;
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    /**
     * 控件自身的宽度
     */
    private int mWidth;

    /**
     * 控件自身的高度
     */
    private int mHeight;

    /**
     * 平滑移动动画的时长
     */
    private int defalutDuring = 1200;

    /**
     * 不断自动更换图片的线程
     */
    private Thread t;

    /**
     * 安排孩子的位置,每一个孩子都是父容器的大小,手指滑动可以显示出下一个孩子
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mWidth = getWidth();
        mHeight = getHeight();
        //获取到孩子的个数
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            v.layout(mWidth * i, 0, mWidth * (i + 1), mHeight);
        }
    }

    /**
     * 指示器的画笔
     */
    private Paint p = new Paint();

    /**
     * 指示器的水平间距
     */
    private int indicatorHorizontalSpacing = 8;

    /**
     * 指示器的半径
     */
    private int indicatorRadius = 6;

    /**
     * 集合中存放的是指示器的几个小圆点的坐标
     */
    private List<Point> indicatorCirclePoints = new ArrayList<Point>();

    /**
     * 是否显示指示器
     */
    private boolean indicatorVisiable = true;

    /**
     * 绘制孩子
     *
     * @param c
     */
    @Override
    protected void dispatchDraw(Canvas c) {
        super.dispatchDraw(c);
        if (indicatorVisiable) {
            //计算指示器有关的数据
            computerIndicatorData();
            for (int i = 0; i < imageCount; i++) {
                if (currIndex == -1 || i == currIndex) {
                    currIndex = i;
                    p.setColor(selectIndicatorColor);
                } else {
                    p.setColor(unSelectIndicatorColor);
                }
                int x = indicatorCirclePoints.get(i).x;
                int y = indicatorCirclePoints.get(i).y;
                c.drawCircle(getScrollX() + x, y, indicatorRadius, p);
            }
        }
    }

    /**
     * 测量孩子的长和宽
     *
     * @param widthMeasureSpec  父容器推荐的宽
     * @param heightMeasureSpec 父容器推荐的高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            removeRepeatData();
            scrollTo(scroller.getCurrX(), 0);
            count = 0;
        }
        requestLayout();
    }

    private int currentX;
    private int currentY;
    private int finalX;
    private int finalY;

    /**
     * 手指是否移动
     */
    private boolean isMove = false;

    private boolean isTouchScreen;

    /**
     * 是否是我的事件
     */
    private boolean isMyEvent = false;

    private boolean isJudge = false;

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        //获取是什么状态,按下还是移动还是抬起
        int action = e.getAction();
        gestureDetector.onTouchEvent(e);

        if (action == MotionEvent.ACTION_DOWN) { //按下
            count = 0;
            isTouchScreen = true;
            isJudge = false;
            velocityX = 0;
            currentX = (int) e.getX();
            currentY = (int) e.getY();
        } else if (action == MotionEvent.ACTION_MOVE) { //移动

            count = 0;
            finalX = (int) e.getX();
            finalY = (int) e.getY();
            int dx = finalX - currentX;
            int dy = finalY - currentY;
            if (!isJudge) {
                if (dx == 0 && dy == 0) {
                    return super.onTouchEvent(e);
                } else {
                    if (Math.abs(dy) > Math.abs(dx)) {
                        isMyEvent = false;
                        requestDisallowInterceptTouchEvent(false);
                    } else {
                        isMyEvent = true;
                        requestDisallowInterceptTouchEvent(true);
                    }
                }
            }


            //已经判断过了,这次的事件该是谁的
            isJudge = true;

            if (isMyEvent) {
                scrollBy(-dx, 0);
                currentX = (int) e.getX();
                currentY = (int) e.getY();
                if (onMoveStateListener != null) {
                    onMoveStateListener.onMoveState(MOVING);
                }
                isMove = true;
                return true;
            } else {
                return super.onTouchEvent(e);
            }
        } else if (action == MotionEvent.ACTION_UP) { //抬起
            count = 0;
            isTouchScreen = false;
            if (!isMove) {
                return super.onTouchEvent(e);
            }
            isMove = false;
            if (onMoveStateListener != null) {
                onMoveStateListener.onMoveState(STOPED);
            }
            //说明飞溅了
            if (velocityX != 0) {
                if (velocityX < 0) {
                    currIndex++;
                    setCurrentImage(currIndex);
                } else {
                    currIndex--;
                    setCurrentImage(currIndex);
                }
                return true;
            }
            scroller.abortAnimation();
            //拿到现在的画布偏移量
            int scrollX = getScrollX();
            //拿到现在显示的图片的下标
            int index = scrollX / mWidth;
            //判断是左边还是右边多一点,从而应该判断应该往左还是往右
            if (scrollX % mWidth > mWidth / 2) {
                index++;
            }
            setCurrentImage(index);
            return true;
        }
        return super.onTouchEvent(e);
    }

    //========================控件对外提供的方法 start===================

    /**
     * 填充父容器的布局对象
     */
    private LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    /**
     * 图片的个数
     */
    private int imageCount = 0;

    /**
     * 添加图片地址,让轮番图多一张图滚动
     *
     * @param imageUrl
     * @param defalutImageId 图片加载完成前显示的图片的资源id
     */
    public void addImageUrl(String imageUrl, int defalutImageId) {
        ImageView iv = new ImageView(context);
        iv.setLayoutParams(lp);
        iv.setImageResource(defalutImageId);
        this.addView(iv);
        requestLayout();
        imageCount++;
        if (onLoadImageListener != null) {
            onLoadImageListener.loadImage(iv, imageUrl);
        }
    }

    //========================控件对外提供的方法 end===================

    //========================一些私有公用方法 start=======================


    /**
     * 飞溅的滑动
     *
     * @param velocityX
     * @param finalX
     */
    private void fling(int velocityX, int finalX) {
        scroller.abortAnimation();
        scroller.fling(getScrollX(), 0, -velocityX, 0, getScrollX(), finalX, 0, 0);
        scrollTo(scroller.getCurrX(), 0);
    }

    /**
     * 平滑的移动到指定位置
     */
    private void smoothTo(int finalX) {
        scroller.abortAnimation();
        scroller.startScroll(getScrollX(), 0, finalX - getScrollX(), 0, defalutDuring);
        scrollTo(scroller.getCurrX(), 0);
    }

    /**
     * 平滑的移动dx的距离
     *
     * @param dx
     */
    private void smoothBy(int dx) {
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

    /**
     * 计算指示器有关的数据
     * 1.n个小圆点的圆心坐标
     */
    private void computerIndicatorData() {
        if (indicatorCirclePoints.size() == 0) {
            int mTop = mHeight * 4 / 5;
            int tmpLeft = (mWidth - imageCount * 2 * indicatorRadius - (imageCount - 1) * indicatorHorizontalSpacing) / 2;
            for (int i = 0; i < imageCount; i++) {
                indicatorCirclePoints.add(new Point(tmpLeft + indicatorRadius, mTop));
                tmpLeft = tmpLeft + indicatorRadius * 2 + indicatorHorizontalSpacing;
            }
        }
    }

    /**
     * 判断一个点是不是在一个圆里面,包括边界
     *
     * @param circleCenter 圆心
     * @param radius       半径
     * @param anotherPoint 另一个点
     * @return
     */
    public static boolean isCircleContainsPoint(Point circleCenter, double radius, Point anotherPoint) {

        if (radius < 0) {
            throw new IllegalAccessError("半径不能小于0");
        }
        return (Math.pow(circleCenter.x - anotherPoint.x, 2) + Math.pow(circleCenter.y - anotherPoint.y, 2))
                - radius * radius > 0 ? false : true;
    }

    /**
     * 根据坐标获取选择的指示器的下标,
     *
     * @param p 触摸的坐标
     * @return 如果点击的不是指示器就返回-1
     */
    private int getSelectIndicatorIndex(Point p) {
        for (int i = 0; i < indicatorCirclePoints.size(); i++) {
            //获取每一个小圆点的圆心坐标
            Point point = indicatorCirclePoints.get(i);
            if (isCircleContainsPoint(point, indicatorRadius, p)) {
                return i;
            }
        }
        return -1;
    }


    //========================一些私有公用方法 end=======================

    //========================一些共有公用方法 start=======================

    /**
     * 现在选中的下标
     */
    private int currIndex = -1;

    /**
     * 获取当前选中的图片下标
     *
     * @return
     */
    public int getCurrIndex() {
        return currIndex;
    }

    /**
     * 设置被选中的下标
     *
     * @param index
     */
    public void setCurrentImage(int index) {
        if (index < 0) {
            index = 0;
        }
        if (index > imageCount - 1) {
            index = imageCount - 1;
        }
        currIndex = index;
        smoothTo(currIndex * mWidth);
        if (onSelectionListener != null) {
            onSelectionListener.onSelect(currIndex);
        }
    }

    /**
     * 设置指示器的小圆点的半径
     *
     * @param indicatorRadius
     */
    public void setIndicatorRadius(int indicatorRadius) {
        this.indicatorRadius = indicatorRadius;
        invalidate();
    }

    /**
     * 设置小圆点之间的间距
     *
     * @param indicatorHorizontalSpacing
     */
    public void setIndicatorHorizontalSpacing(int indicatorHorizontalSpacing) {
        this.indicatorHorizontalSpacing = indicatorHorizontalSpacing;
        invalidate();
    }

    public void setIndicatorVisiable(boolean indicatorVisiable) {
        this.indicatorVisiable = indicatorVisiable;
    }

    /**
     * 指示器被选中的小圆点的颜色
     */
    private int selectIndicatorColor = Color.RED;

    /**
     * 指示器没有被选中的小圆点的颜色
     */
    private int unSelectIndicatorColor = Color.WHITE;

    /**
     * 设置指示器选中的那个小圆点的颜色,默认是红色的
     *
     * @param color
     */
    public void setSelectIndicatorColor(int color) {
        selectIndicatorColor = color;
        invalidate();
    }

    /**
     * 设置指示器没有选中的小圆点的颜色
     *
     * @param color
     */
    public void setUnSelectIndicatorColor(int color) {
        unSelectIndicatorColor = color;
        invalidate();
    }

    /**
     * 是否自动更换图片
     */
    private boolean isAutoChange;

    /**
     * 开始自动切换
     */
    public void startAutoChange() {
        isAutoChange = true;
        if (!t.isAlive()) {
            flag = true;
            t = new Thread(this);
            t.start();
        }
    }

    /**
     * 结束自动切换
     */
    public void stopAutoChange() {
        isAutoChange = false;
    }

    /**
     * 退出的时候调用
     */
    public void exit() {
        isAutoChange = false;
        flag = false;
    }

    //========================一些共有公用方法 end=======================


    //========================暴露的接口对象 start=====================


    /**
     * 加载图片的监听
     */
    public interface OnLoadImageListener {
        /**
         * 加载图片
         *
         * @param imageView
         */
        public void loadImage(ImageView imageView, String imageUrl);
    }


    /**
     * 图片加载接口对象
     */
    private OnLoadImageListener onLoadImageListener = null;

    /**
     * 设置图片加载的接口
     *
     * @param onLoadImageListener
     */
    public void setOnLoadImageListener(OnLoadImageListener onLoadImageListener) {
        this.onLoadImageListener = onLoadImageListener;
    }

    /**
     * 移动状态的监听接口
     */
    public interface OnMoveStateListener {
        /**
         * 回掉的接口
         *
         * @param state
         */
        public void onMoveState(int state);
    }

    /**
     * 正在移动
     */
    public static final int MOVING = 1;

    /**
     * 暂停移动了
     */
    public static final int STOPED = 0;

    /**
     * 移动监听接口
     */
    private OnMoveStateListener onMoveStateListener = null;

    /**
     * 设置移动监听接口
     *
     * @param onMoveStateListener
     */
    public void setOnMoveStateListener(OnMoveStateListener onMoveStateListener) {
        this.onMoveStateListener = onMoveStateListener;
    }

    /**
     * 图片选中哪一张的时候回掉
     */
    public interface OnSelectionListener {
        /**
         * 回掉的接口
         *
         * @param index
         */
        public void onSelect(int index);
    }

    private OnSelectionListener onSelectionListener = null;

    public void setOnSelectionListener(OnSelectionListener onSelectionListener) {
        this.onSelectionListener = onSelectionListener;
    }

    /**
     * 点击图片的时候回调的接口
     */
    public interface OnImageClickListener {
        /**
         * 回掉的接口
         *
         * @param index
         */
        public void imageClick(int index);
    }

    private OnImageClickListener onImageClickListener = null;

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    //========================暴露的接口对象 end=======================

    @Override
    public void onClick(View v) {
        //获取用户触摸的指示器的下标,-1表示触摸的不是指示器
        int selectIndicatorIndex = getSelectIndicatorIndex(new Point(currentX, currentY));
        if (selectIndicatorIndex != -1) { //如果触摸的是指示器
            setCurrentImage(selectIndicatorIndex);
            setCurrentImage(selectIndicatorIndex);
        } else {
            if (onImageClickListener != null) {
                onImageClickListener.imageClick(currIndex);
            }
        }
    }


    /**
     * 控制线程的死活
     */
    private boolean flag;

    /**
     * 线程再用户没有触摸的时候等待几秒钟之后才可以动
     */
    private int count = 0;

    /**
     * 用来不断切换选中的图片
     */
    @Override
    public void run() {
        while (flag) {
            try {
                count++;
                if (isAutoChange && !isTouchScreen && imageCount > 0 && count > 5) {
                    if (currIndex == imageCount - 1) {
                        h.sendEmptyMessage(0);
//                        setCurrentImage(0);
                    } else {
//                        setCurrentImage(currIndex + 1);
                        h.sendEmptyMessage(currIndex + 1);
                    }
                    count = 0;
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
