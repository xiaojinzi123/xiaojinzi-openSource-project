package xiaojinzi.view.popupMenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import java.util.ArrayList;
import java.util.List;

import xiaojinzi.base.android.log.L;
import xiaojinzi.viewAnimation.RotateAnimationUtil;
import xiaojinzi.viewAnimation.TranslateAnimationUtil;
import xiaojinzi.view.common.RectEntity;
import xiaojinzi.viewAnimation.adapter.AnimationListenerAdapter;


/**
 * 这是一个小菜单,点击可以弹出小菜单<br>
 * 1.这个是一个可以拥有孩子的控件,自动会<br>
 * 把第一个孩子作为主菜单,主菜单一直都是<br>
 * 显示状态,其他孩子作为小菜单弹出或者收缩,所以孩子的个数不能少于一个! <br>
 * <p/>
 * 2.使用的时候这个控件要占据屏幕全部的大小,<br>
 * 因为这个还有一个遮盖的作用,建议外层是一个相对布局<br>
 * <p/>
 * 3.菜单默认显示在屏幕的右下角,距离底部和右边的距离可以通过控件的內边距来调整
 *
 * @author cxj QQ:347837667
 * @date 2015年12月22日
 */
public class SmartMenu extends ViewGroup implements View.OnClickListener {

    public SmartMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData(context);
    }

    public SmartMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartMenu(Context context) {
        this(context, null);
    }

    /**
     * 初始化数据
     */
    private void initData(Context context) {
        this.setBackgroundColor(closeColor);
    }

    /**
     * 控件之间的距离
     */
    private int betweenMargin = 20;

    /**
     * 控件距离父容器右边的距离
     */
    private int rightMargin = 0;

    /**
     * 控件距离父容器下方的距离
     */
    private int bottomMargin = 0;

    /**
     * 主菜单
     */
    private View menu = null;

    /**
     * 菜单开启的时候的背景颜色
     */
    private int openColor = Color.parseColor("#99ffffff");

    /**
     * 菜单关闭的时候的背景颜色
     */
    private int closeColor = Color.parseColor("#00ffffff");

    /**
     * 动画的时长
     */
    private int animationDuration = 400;

    /**
     * 是否展开
     */
    private boolean isMenuOpen = false;

    private int myWidth;
    private int myHeight;

    /**
     * 所有孩子的位置的集合
     */
    private List<RectEntity> rects = new ArrayList<RectEntity>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 宽和高的计算模式
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //拿到父容器推荐的宽和高
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeigth = MeasureSpec.getSize(heightMeasureSpec);
        if (modeWidth == LayoutParams.WRAP_CONTENT) {
            //sizeWidth = 计算的值
        }
        if (modeHeight == LayoutParams.WRAP_CONTENT) {
            //sizeHeigth = 计算的值
        }
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(sizeWidth, sizeHeigth);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        // 计算所需的参数
        compute();

        // 循环集合中的各个菜单的位置信息,并让孩子到这个位置上
        for (int i = 0; i < rects.size(); i++) {
            // 循环中的位置
            RectEntity e = rects.get(i);
            // 循环中的孩子
            View v = getChildAt(i);
            // 让孩子到指定的位置
            v.layout(e.leftX, e.leftY, e.rightX, e.rightY);
            // 如果不是第一个孩子,就默认让它隐藏
            if (i > 0) {
                v.setVisibility(View.INVISIBLE);
            }
        }

    }

    /**
     * 计算需要的数据
     */
    private void compute() {

        // 清空集合
        rects.clear();

        //如果一个孩子都没有,那就是用户使用错误,直接返回
        if (getChildCount() == 0) {
            return;
        }

        this.rightMargin = getPaddingRight();
        this.bottomMargin = getPaddingBottom();

        // 拿出第一个孩子作为主菜单
        menu = getChildAt(0);

        // 主菜单的点击事件监听
        menu.setOnClickListener(this);

        //获取当前空间的宽和高
        myWidth = getWidth();
        myHeight = getHeight();

        //创建一个矩形对象
        RectEntity entity = new RectEntity();

        //计算矩形的左上角的点的坐标和右下角的点的坐标
        entity.rightX = myWidth - rightMargin;
        entity.rightY = myHeight - bottomMargin;
        entity.leftX = entity.rightX - menu.getMeasuredWidth();
        entity.leftY = entity.rightY - menu.getMeasuredHeight();

        //添加第一个矩形对象到集合中
        rects.add(entity);

        //获取所有孩子的个数
        int childCount = getChildCount();

        //第一个孩子已经安排为主菜单,所以这里从第二个开始循环
        for (int i = 1; i < childCount; i++) {
            //拿到一个孩子创建一个相应的矩形对象
            View view = getChildAt(i);
            //设置点击事件
            view.setOnClickListener(this);
            entity = new RectEntity();

            //计算这个孩子在父容器中的位置,也就是矩形确定的区域
            entity.leftY = rects.get(0).leftY - i * (view.getMeasuredHeight() + betweenMargin);
            entity.rightY = entity.leftY + view.getMeasuredHeight();

            entity.rightX = rects.get(0).rightX;
            entity.leftX = entity.rightX - view.getMeasuredWidth();

            //同样添加到集合中
            rects.add(entity);
        }

    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        changeState(true);
        isMenuOpen = true;
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        changeState(false);
        isMenuOpen = false;
    }

    /**
     * 返回菜单是否打开状态
     *
     * @return
     */
    public boolean isMenuOpen() {
        return isMenuOpen;
    }

    @Override
    public void onClick(View v) {
        if (v == menu) {
            if (isMenuOpen) {
                closeMenu();
            } else {
                openMenu();
            }
        } else {

            //点击了小菜单肯定是展开状态所以需要关闭菜单
            closeMenu();

            //获取孩子的个数
            int childCount = getChildCount();

            //循环找出用户点击的小菜单,然后回调接口
            for (int i = 1; i < childCount; i++) {
                View view = getChildAt(i);
                if (v == view) {
                    if (onClickListener != null)
                        onClickListener.click(v, i);
                    myListener.posotion = i;
                    myListener.v = v;
                    return;
                }
            }

        }
    }

    /**
     * 弹出动画和收回的动画
     */
    private RotateAnimation toAnimation = RotateAnimationUtil.rotateSelf(0, 360, animationDuration);
    private RotateAnimation backAnimation = RotateAnimationUtil.rotateSelf(360, 0, animationDuration);

    /**
     * 监听动画的结束
     */
    private MyListener myListener = new MyListener();

    /**
     * 实现了动画的监听接口的类,AnimationListenerAdapter是一个适配器,
     * 也就是实现了一个接口中的所有方法,方法中都不写具体代码,
     * 供给子类继承的时候可选择性的重写某个方法
     */
    private class MyListener extends AnimationListenerAdapter {

        /**
         * 是否是展开状态
         */
        public boolean isOpen;

        /**
         * 当前点击的小菜单的下标
         * 如果没有,或者不是点的小菜单引起的收回,这个就是一个null值
         */
        public Integer posotion;

        /**
         * 当前点击的小菜单
         * 如果没有,或者不是点的小菜单引起的收回,这个就是一个null值
         */
        public View v;

        @Override
        public void onAnimationEnd(Animation animation) {

            int childCount = getChildCount();

            for (int i = childCount - 1; i > 0; i--) {
                View view = getChildAt(i);
                if (isOpen) { // 如果要展开
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.clearAnimation();
                    view.setVisibility(View.INVISIBLE);
                }
            }

            if (stateAnimationListener != null) {
                stateAnimationListener.animationEnd(isOpen);
            }

            if (onMenuCloseListener != null && !isOpen) {
                onMenuCloseListener.handle(v, posotion);
            }

        }
    }

    /**
     * 弹出或者收起菜单
     */
    public void changeState(final boolean isOpen) {

        toAnimation.setAnimationListener(myListener);
        backAnimation.setAnimationListener(myListener);

        myListener.isOpen = isOpen;
        myListener.v = null;
        myListener.posotion = null;

        //开启主菜单的动画
        if (!isOpen) {
            menu.startAnimation(backAnimation);
        } else {
            menu.startAnimation(toAnimation);
        }

        int childCount = getChildCount();

        //开启子菜单们的动画
        for (int i = 1; i < childCount; i++) {
            final View view = getChildAt(i);
            if (isOpen) { // 如果展开
                view.setEnabled(true);
                TranslateAnimationUtil.translateSelfAbsolute(view, 0, 0, menu.getTop() - view.getTop(), 0, animationDuration);
            } else {
                view.setEnabled(false);
                TranslateAnimationUtil.translateSelfAbsolute(view, 0, 0, 0, menu.getTop() - view.getTop(), animationDuration);
            }

        }

        //更换背景颜色
        if (isOpen) {
            SmartMenu.this.setBackgroundColor(openColor);
        } else {
            SmartMenu.this.setBackgroundColor(closeColor);
        }
    }

    private boolean isMyEvent = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {

        int action = e.getAction();

        switch (action & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN: //如果是按下,判断是不是自己的事件
                isMyEvent = false;
                if (isMenuOpen) {
                    isMyEvent =  true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isMyEvent) {
                    closeMenu();
                }
                break;
        }
        return isMyEvent;
    }

    //===================================下面是动画结束的回调接口   start==============================

    /**
     * 动画结束的监听接口
     */
    private StateAnimationListener stateAnimationListener = null;

    /**
     * 设置动画结束监听
     *
     * @param l
     */
    public void setStateAnimationListener(StateAnimationListener l) {
        this.stateAnimationListener = l;
    }

    /**
     * 针对这个自定义控件的动画结束监听的接口对象
     *
     * @author xiaojinzi
     */
    public interface StateAnimationListener {

        /**
         * 动画结束回调的接口
         *
         * @param isOpenState 菜单是否是打开状态
         */
        public void animationEnd(boolean isOpenState);

    }

    //===================================上面是动画结束的回调接口   end==============================

    //===================================下面是点击弹出来的小菜单的回调接口   start==============================

    /**
     * 点击小菜单的回调接口
     */
    private OnClickListener onClickListener = null;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * 小菜单的点击事件
     */
    public interface OnClickListener {

        /**
         * 回调的方法
         *
         * @param v        被点击的小菜单
         * @param position 点击的是第几个菜单
         */
        public void click(View v, int position);

    }

    //===================================上面是点击弹出来的小菜单的回调接口   end==============================

    //===================================下面是菜单收缩的回调接口   start==============================

    /**
     * 菜单收缩的时候的回调接口
     */
    private OnMenuCloseListener onMenuCloseListener = null;

    /**
     * 设置菜单收回的监听
     *
     * @param onMenuCloseListener
     */
    public void setOnMenuCloseListener(OnMenuCloseListener onMenuCloseListener) {
        this.onMenuCloseListener = onMenuCloseListener;
    }

    /**
     * 小菜单的点击事件
     */
    public interface OnMenuCloseListener {

        /**
         * 回调的方法
         *
         * @param v        被点击的小菜单,如果不是点击小菜单引起的收缩,值为null
         * @param position 点击的是第几个菜单,如果不是点击小菜单引起的收缩,值为null
         */
        public void handle(View v, Integer position);

    }

    //===================================上面是菜单收缩的回调接口   end==============================


}
