package xiaojinzi.view.scaleSlideMenu;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

import xiaojinzi.view.common.RectEntity;


/**
 * Created by cxj on 2016/5/15.
 *
 * @author 小金子
 *         这个是一个自定义的层叠式的侧滑菜单
 *         请遵循一下使用原则:
 *         1.如果是两个孩子的情况
 *         第一个孩子是菜单,默认是左边
 *         第二个是主界面
 *         3.不是两个孩子都会报错
 *         <p>
 *         1.用到的类:RectEntity
 *         保存位置信息
 */
public class ScaleSlideMenu extends ViewGroup {

    //=================================常量 start =====================================

    /**
     * 最大的距离生效的百分比
     */
    public static final float MAX_SLIDEPERCENT = 0.4f;

    /**
     * 最小的距离生效的百分比
     */
    public static final float MIN_SLIDEPERCENT = 0.1f;

    /**
     * 最小的菜单宽度占用百分比
     */
    public static final float MIN_MENUPERCENT = 0.5f;

    /**
     * 最大的菜单宽度的占用百分比
     */
    public static final float MAX_MENUPERCENT = 0.8f;


    /**
     * 侧滑的时候,当触摸的x距离屏幕小于屏幕宽度的10%的时候会生效
     */
    public static final float DEFALUT_SLIDEPERCENT = MIN_SLIDEPERCENT;

    /**
     * 滑动的时候是一个普通的模式
     */
    public static final int NORMAL_SLIDE_MODE = 0;

    /**
     * 滑动的时候是一个缩放的模式
     */
    public static final int SCALE_SLIDE_MODE = 1;


    /**
     * 菜单在左边
     */
    public static final int MENU_GRAVITY_LEFT = 0;


    /**
     * 菜单在右边
     */
    public static final int MENU_GRAVITY_RIGHT = 1;

    //=================================常量 end =====================================

    public ScaleSlideMenu(Context context) {
        this(context, null);
    }

    public ScaleSlideMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleSlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
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
        vt = VelocityTracker.obtain();
    }

    //======================================成员变量 start===========================================

    /**
     * 上下文对象
     */
    private Context context;

    /**
     * 触摸移动的时候需要平滑的移动的数据计算工具类
     */
    private Scroller scroller;

    /**
     * 用于计算触摸的时候的速度
     */
    private VelocityTracker vt = null;

    /**
     * 平滑移动动画的时长
     */
    private int defalutDuring = 800;

    /**
     * 菜单是不是打开状态
     */
    private boolean isMenuOpen;

    /**
     * 菜单状态状态改变之前的状态
     */
    private boolean preIsMenuOpen;

    /**
     * 侧滑的模式,默认是缩放的模式
     */
    private int slideMode = SCALE_SLIDE_MODE;

    /**
     * 菜单的位置在左边还是在右边
     */
    private int menuGravity = MENU_GRAVITY_LEFT;

    /**
     * 侧滑菜单生效的距离百分比
     */
    private float slidePercent = DEFALUT_SLIDEPERCENT;

    /**
     * 菜单宽度占屏幕的比例,默认是最小的
     */
    private float menuPercent = MAX_MENUPERCENT;

    /**
     * 自身的宽
     */
    private int mWidth;

    /**
     * 自身的高
     */
    private int mHeight;

    //======================================成员变量 end===========================================

    //======================================重写方法 start===========================================

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //检查孩子的个数
        checkChildCount();

        //宽度的计算模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //推荐的宽度的值
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        //高度的计算模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //推荐的高度的值
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //菜单
        View menuView = getChildAt(0);
        LayoutParams lp = menuView.getLayoutParams();
        lp.width = LayoutParams.WRAP_CONTENT;
        //生成菜单的宽度的计算模式和大小,菜单的宽度大小受一个百分比限制
        int menuWidthSpec = MeasureSpec.makeMeasureSpec((int) (widthSize * menuPercent), MeasureSpec.AT_MOST);
        //生成菜单的高度的计算模式和大小
        int menuHeightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
        //让菜单的试图部分去测量自己
        menuView.measure(menuWidthSpec, menuHeightSpec);


        //主界面
        View mainView = getChildAt(1);
        //生成主界面的宽度的计算模式和大小
        int mainWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST);
        //生成主界面的高度的计算模式和大小
        int mainHeightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
        //让主界面的试图部分去测量自己
        mainView.measure(mainWidthSpec, mainHeightSpec);

        //侧滑控件的大小呢就直接采用父容器推荐的，所以
        //ViewGroup.LayoutParams.WRAP_CONTENT 和 ViewGroup.LayoutParams.MATCH_PARENT 的情况下大小是一致的,这里做一个说明
        //作用是保存自身的大小,没有这句话控件不显示,也就是看不到啦
        setMeasuredDimension(widthSize, heightSize);

        //保存自身的宽度和高度,其他地方有用
        mWidth = getWidth();
        mHeight = getHeight();
    }

    @Override
    public void computeScroll() { //当View完成滚动的时候调用
        //如果轨迹中还有没有滚完的点
        if (scroller.computeScrollOffset()) {
            removeRepeatData();//消除重复数据
            scrollTo(scroller.getCurrX(), 0);
            //如果完成了滑动
            if (getScrollX() == scroller.getFinalX() && scroller.getCurrX() == scroller.getFinalX() && isScrolling) {
                //标识停止滚动了
                isScrolling = false;
                if (onMenuStateListener != null && preIsMenuOpen != isMenuOpen) {
                    preIsMenuOpen = isMenuOpen;
                    onMenuStateListener.onMenuState(isMenuOpen);
                }
            }
            scaleMainView();
        }
    }

    /**
     * 是否拦截孩子的事件
     */
    private boolean isInterceptTouchEvent = false;

    /**
     * 是否已经判断过事件属于谁了
     */
    private boolean isAdjustEvent = false;

    /**
     * 手指接触屏幕的时候的坐标
     */
    private Point downPoint = new Point();

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        //获取事件的类型
        int action = ev.getAction();

        //筛选事件
        switch (action & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN: //按下的情况下

                isInterceptTouchEvent = false;
                isAdjustEvent = false;
                isMove = false;
                vt.clear();

                //不管拦截不拦截,先保存按下时候的坐标
                downPoint.x = (int) ev.getX();
                downPoint.y = (int) ev.getY();

                //拦截菜单打开的情况下主界面的事件
                if (isMenuOpen) {

                    //菜单的位置参数对象
                    RectEntity menuRect = rectEntities.get(0);

                    //如果判断出来按下的时候坐标是菜单的区域,则直接拦截,这个事件不传递给孩子
                    if (menuGravity == MENU_GRAVITY_LEFT) {
                        if (downPoint.x > Math.abs(menuRect.leftX)) {
                            isAdjustEvent = true;
                            isInterceptTouchEvent = true;
                        }
                    } else {
                        if (downPoint.x < (mWidth - (menuRect.rightX - menuRect.leftX))) {
                            isAdjustEvent = true;
                            isInterceptTouchEvent = true;
                        }
                    }
                }else{

                }
                break;
            case MotionEvent.ACTION_MOVE: // 移动

                if (isAdjustEvent) { //如果已经判断过事件了
                    return isInterceptTouchEvent;
                }

                //记录下当前移动后的点
                finalX = (int) ev.getX();
                finalY = (int) ev.getY();

                //菜单没有打开
                if (!isMenuOpen) {
                    //计算坐标之间的差值
                    int dx = Math.abs(finalX - downPoint.x);
                    int dy = Math.abs(finalY - downPoint.y);

                    if (dx > dy) { //说明水平方向的滑动多余竖直方向的,需要拦截
                        if (menuGravity == MENU_GRAVITY_LEFT) { //如果菜单在左边
                            if (downPoint.x < mWidth * slidePercent) {
                                isAdjustEvent = true;
                                isInterceptTouchEvent = true;
                                currentX = downPoint.x;
                                currentY = downPoint.y;
                            }
                        } else { //菜单在右边
                            if (downPoint.x > (mWidth - mWidth * slidePercent)) {
                                isAdjustEvent = true;
                                isInterceptTouchEvent = true;
                                currentX = downPoint.x;
                                currentY = downPoint.y;
                            }
                        }
                    }
                }
                break;
        }

        return isInterceptTouchEvent;
    }

    /**
     * 安排孩子的位置
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        //计算所有孩子的位置信息
        computePosition();

        //获取孩子的个数
        int childCount = getChildCount();
        int size = rectEntities.size();

        for (int i = 0; i < size && i < childCount; i++) {

            View v = getChildAt(i);
            RectEntity rectEntity = rectEntities.get(i);

            v.layout(rectEntity.leftX, rectEntity.leftY, rectEntity.rightX, rectEntity.rightY);
        }

    }

    private int currentX;
    private int currentY;
    private int finalX;
    private int finalY;

    /**
     * 是否移动了
     */
    private boolean isMove;

    /**
     * 是否正在滚动
     */
    private boolean isScrolling;

    /**
     * 事件在经过孩子的事件分发之后才到这里的
     *
     * @param e
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {

        //获取事件的类型(动作)
        int action = e.getAction();

        //如果不需要侧滑出菜单,并且菜单是关闭状态
        if (isScrolling) {
            return false;
        }

        vt.addMovement(e);

        switch (action & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN: //按下

                //保存按下的时候的坐标
                currentX = (int) e.getX();
                currentY = (int) e.getY();

                break;

            case MotionEvent.ACTION_MOVE: //移动

                //保存移动之后的新的坐标点
                finalX = (int) e.getX();
                finalY = (int) e.getY();

                //如果新旧坐标不一致
                if (finalX != currentX || finalY != currentY) {

                    //计算水平距离和垂直距离
                    int dx = finalX - currentX;
                    int dy = finalY - currentY;

                    //让整个试图跟着手指移动起来
                    scrollBy(-dx, 0);

                    scaleMainView();

                    //移动之后旧的坐标进行更新
                    currentX = (int) e.getX();
                    currentY = (int) e.getY();
                    isMove = true;
                }

                break;
            case MotionEvent.ACTION_UP://抬起

                if (isMove) {
                    //计算速度
                    vt.computeCurrentVelocity(1000, Integer.MAX_VALUE);
                    //水平方向的速度
                    float xVelocity = vt.getXVelocity();

                    //如果速度的绝对值大于200,我们就认为试图有一个抛出的感觉

                    //如果速度达到了
                    if (xVelocity > 200) {
                        if (menuGravity == MENU_GRAVITY_LEFT) {
                            openMenu();
                        } else {
                            closeMenu();
                        }
                    } else if (xVelocity < -200) {
                        if (menuGravity == MENU_GRAVITY_LEFT) {
                            closeMenu();
                        } else {
                            openMenu();
                        }
                    } else {
                        judgeShouldSmoothToLeftOrRight();
                    }
                } else {
                    closeMenu();
                }
                break;
        }

        return true;
    }

    //======================================重写方法 end===========================================

    //======================================私有方法 start===========================================


    /**
     * 存储孩子的位置信息,由方法{@link ScaleSlideMenu#computePosition()}计算而来
     */
    private List<RectEntity> rectEntities = new ArrayList<RectEntity>();

    /**
     * 计算所有孩子的位置信息
     *
     * @return
     */
    private void computePosition() {
        rectEntities.clear();
        //获取孩子的个数
        int childCount = getChildCount();

        if (childCount == 2) { //如果是两个孩子的情况
            //第一个孩子是一个菜单
            View menuView = getChildAt(0);

            //闯进第一个孩子的位置参数
            RectEntity menuRect = new RectEntity();
            if (menuGravity == MENU_GRAVITY_LEFT) { //如果菜单是在左边的
                menuRect.rightX = 0;
                menuRect.leftX = menuRect.rightX - menuView.getMeasuredWidth();
            } else {
                menuRect.leftX = mWidth;
                menuRect.rightX = menuRect.leftX + menuView.getMeasuredWidth();
            }

            menuRect.leftY = 0;
            menuRect.rightY = menuRect.leftY + menuView.getMeasuredHeight();

            //第二个孩子是一个主界面
            View mainView = getChildAt(1);

            //闯进第一个孩子的位置参数
            RectEntity mainRect = new RectEntity();
            mainRect.leftX = 0;
            mainRect.rightX = mainRect.leftX + mainView.getMeasuredWidth();
            mainRect.leftY = 0;
            mainRect.rightY = mainRect.leftY + mainView.getMeasuredHeight();

            //添加位置信息到集合
            rectEntities.add(menuRect);
            rectEntities.add(mainRect);

        } else { //如果是三个孩子的情况

        }

    }


    /**
     * 检查孩子个数
     */
    private void checkChildCount() {
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("the childCount must be 2");
        }
    }

    /**
     * 用于消除滑动的时候出现的重复数据
     * 因为平滑的滑动的时候产生的数据很多都是重复的
     * 都是所以这里如果遇到事重复的就拿下一个,直到不重复为止
     * 1.当前的值{@link View#getScrollX()}不等于{@link Scroller#getCurrX()}
     * 2.
     */
    private void removeRepeatData() {
        //获取当前的滚动的值
        int scrollX = getScrollX();
        //如果当前的值不是最后一个值,并且当前的值等于scroller中的当前值,那么获取下一个值
        while (scrollX != scroller.getFinalX() && scrollX == scroller.getCurrX()) {
            scroller.computeScrollOffset();
        }
    }

    /**
     * 平滑的移动到指定位置
     */
    private void smoothTo(int finalX) {
        isScrolling = true;
        scroller.startScroll(getScrollX(), 0, finalX - getScrollX(), 0, defalutDuring);
        removeRepeatData();//消除重复数据
        scrollTo(scroller.getCurrX(), 0);
    }

    /**
     * 缩放主界面和菜单
     */
    private void scaleMainView() {
        if (slideMode == SCALE_SLIDE_MODE) { //如果模式是缩放的模式
            float percent = ((Number) Math.abs(getScrollX())).floatValue() / ((Number) Math.abs(rectEntities.get(0).leftX)).floatValue();
            //缩放主界面
            getChildAt(1).setScaleX(1f - 0.2f * (percent));
            getChildAt(1).setScaleY(1f - 0.2f * (percent));
            //缩放菜单
            getChildAt(0).setScaleX(0.6f + 0.4f * (percent));
            getChildAt(0).setScaleY(0.6f + 0.4f * (percent));
        }
    }

    /**
     * 判断当前的位置应该是滑出菜单还是关闭菜单
     */
    private void judgeShouldSmoothToLeftOrRight() {
        //菜单的位置参数对象
        RectEntity menuRect = rectEntities.get(0);
        if (menuGravity == MENU_GRAVITY_LEFT) { //如果菜单在左边
            //菜单左边的横左边
            int menuLeftX = menuRect.leftX;
            //获取到当前的位置
            int scrollX = getScrollX();
            if (menuLeftX / 2 > scrollX) {
                openMenu();
            } else {
                closeMenu();
            }
        } else {
            //菜单的宽度
            int menuWidth = menuRect.rightX - menuRect.leftX;
            //获取到当前的位置
            int scrollX = getScrollX();
            if (menuWidth / 2 > scrollX) {
                closeMenu();
            } else {
                openMenu();
            }
        }

    }

    //======================================私有方法 end===========================================


    //============================================用户方法区域 start============================================

    /**
     * 设置侧滑的百分比,0-1
     *
     * @param slidePercent 0-1
     */
    public void setSlidePercent(float slidePercent) {
        if (slidePercent > MAX_SLIDEPERCENT) {
            slidePercent = MAX_SLIDEPERCENT;
        }
        if (slidePercent > MIN_SLIDEPERCENT) {
            slidePercent = MIN_SLIDEPERCENT;
        }
        this.slidePercent = slidePercent;
    }

    /**
     * 获取侧滑的百分比
     *
     * @return
     */
    public float getSlidePercent() {
        return slidePercent;
    }

    /**
     * 设置侧滑的模式
     *
     * @param slideMode 侧滑的模式
     *                  {@link ScaleSlideMenu#NORMAL_SLIDE_MODE}
     *                  {@link ScaleSlideMenu#SCALE_SLIDE_MODE}
     */
    public void setSlideMode(int slideMode) {
        this.slideMode = slideMode;
    }

    /**
     * 获取侧滑的模式
     *
     * @return
     */
    public int getSlideMode() {
        return slideMode;
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        if (menuGravity == MENU_GRAVITY_LEFT) {
            //拿到菜单的位置参数
            RectEntity menuRect = rectEntities.get(0);
            smoothTo(menuRect.leftX);
        } else {
            //拿到菜单的位置参数
            RectEntity menuRect = rectEntities.get(0);
            smoothTo(menuRect.rightX - menuRect.leftX);
        }

        preIsMenuOpen = isMenuOpen;
        isMenuOpen = true;
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        smoothTo(0);
        preIsMenuOpen = isMenuOpen;
        isMenuOpen = false;
    }

    public boolean isMenuOpen() {
        return isMenuOpen;
    }

    //============================================用户方法区域 end============================================
    //============================================用户接口区域 start==========================================

    /**
     * 菜单状态的监听接口
     */
    public interface OnMenuStateListener {
        /**
         * 回调的方法
         *
         * @param state
         */
        public void onMenuState(boolean state);
    }

    /**
     * 菜单状态的监听接口
     */
    private OnMenuStateListener onMenuStateListener = null;

    /**
     * 设置菜单的状态监听
     *
     * @param onMenuStateListener
     */
    public void setOnMenuStateListener(OnMenuStateListener onMenuStateListener) {
        this.onMenuStateListener = onMenuStateListener;
    }

    //============================================用户接口区域 end============================================

}
