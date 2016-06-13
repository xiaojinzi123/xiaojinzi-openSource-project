package xiaojinzi.view.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cxj on 2016/4/25.
 * <p/>
 * 作为所有容器类的基类
 * 因为此类中很好的实现了测量的代码,处理了内边距,实现类无需关心内边距
 * 当然了,一个抽象的类不可能知道实现类孩子真的需要多么大的大小
 * 所以这里提供了四个有关的方法,也是你继承的时候需要关心的:
 * 1.获取推荐给孩子的宽度极其计算模式
 * {@link BaseMeasureViewGroup#getChildWidthMeasureSpec(int, int, View, int, int)}
 * <p/>
 * 2.获取推荐给孩子的高度极其计算模式
 * {@link BaseMeasureViewGroup#getChildHeightMeasureSpec(int, int, View, int, int)}
 * <p/>
 * 3.在自定义控件是包裹的时候被调用,用来获取自身真正需要多大的宽度
 * {@link BaseMeasureViewGroup#getWrapWidth(int)}
 * <p/>
 * 4.在自定义控件是包裹的时候被调用,用来获取自身真正需要多大的高度
 * {@link BaseMeasureViewGroup#getWrapHeight(int)}
 */
public abstract class BaseMeasureViewGroup extends ViewGroup {

    public BaseMeasureViewGroup(Context context) {
        this(context, null);
    }

    public BaseMeasureViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseMeasureViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initthis(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 针对自己进行的一个初始化
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void initthis(Context context, AttributeSet attrs, int defStyleAttr) {
    }

    /**
     * 初始化,留给继承这个类写一些初始化的代码
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
    }

    /**
     * 获取某一个孩子的宽度极其计算模式
     *
     * @param clildCounts
     * @param i
     * @param v
     * @param parentWidthMode
     * @param parentWidthSize @return
     */
    protected int getChildWidthMeasureSpec(int clildCounts, int i, View v, int parentWidthMode, int parentWidthSize) {
        return MeasureSpec.makeMeasureSpec(parentWidthSize, parentWidthMode);
    }

    /**
     * 获取某一个孩子的高度极其计算模式
     *
     * @param clildCounts
     * @param i
     * @param v
     * @param parentHeightMode
     * @param parentHeightSize @return
     */
    protected int getChildHeightMeasureSpec(int clildCounts, int i, View v, int parentHeightMode, int parentHeightSize) {
        return MeasureSpec.makeMeasureSpec(parentHeightSize, parentHeightMode);
    }

    /**
     * 获取自身包裹的时候的宽度
     *
     * @param widthSize 包裹的时候能给的最大宽度
     * @return
     */
    protected int getWrapWidth(int widthSize) {
        return widthSize;
    }


    /**
     * 获取自身包裹的时候的高度
     *
     * @param heightSize 包裹的时候能给的最大高度
     * @return
     */
    protected int getWrapHeight(int heightSize) {
        return heightSize;
    }

    /**
     * 测量的时候,做两件事情
     * 1.测量所有的孩子
     * 2.测量完孩子,根据孩子和父容器推荐的宽和高设置自己的宽和高
     *
     * @param widthMeasureSpec  父容器推荐的宽度和计算模式
     * @param heightMeasureSpec 父容器推荐的高和计算模式
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //获取宽度的推荐模式和推荐宽度
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //获取高度的推荐模式和推荐高度
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //获取孩子的个数
        int clildCounts = getChildCount();
        //分别测量每一个孩子
        for (int i = 0; i < clildCounts; i++) {

            View v = getChildAt(i);
            if ((v.getVisibility()) == GONE) {
                continue;
            }
            //计算孩子的宽度和模式
            int chileWidthSpec = getChildWidthMeasureSpec(clildCounts, i, v, widthMode, widthSize - getPaddingLeft() - getPaddingRight());
            //计算孩子的高度和模式
            int chileHeightSpec = getChildHeightMeasureSpec(clildCounts, i, v, heightMode, heightSize - getPaddingTop() - getPaddingBottom());

            //如果孩子可见,那就叫孩子去测量
            v.measure(chileWidthSpec, chileHeightSpec);
        }

        if (widthMode == MeasureSpec.AT_MOST) { //如果自身宽度是包裹的
            //包裹的时候,内容+两边边距
            widthSize = getWrapWidth(widthSize) + getPaddingLeft() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.AT_MOST) { //如果自身高度是包裹的
            //包裹的时候,内容+两边的边距
            heightSize = getWrapHeight(heightSize) + getPaddingTop() + getPaddingBottom();
        }

        //设置自己的大小
        setMeasuredDimension(widthSize, heightSize);

    }


}
