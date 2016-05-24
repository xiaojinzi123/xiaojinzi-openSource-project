package xiaojinzi.view.nineImage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import xiaojinzi.view.common.RectEntity;


/**
 * Created by cxj on 2016/3/26.
 * 显示图片的九宫格控件
 */
public class NineImageView extends ViewGroup {

    public NineImageView(Context context) {
        this(context, null);
    }

    public NineImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 上下文对象
     */
    private Context context = null;

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        this.context = context;
    }

    /**
     * 用于保存每一个孩子的在父容器的位置
     */
    private List<RectEntity> rectEntityList = new ArrayList<RectEntity>();

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
        computeViewsLocation();
        // 循环集合中的各个菜单的位置信息,并让孩子到这个位置上
        for (int i = 0; i < getChildCount(); i++) {
            // 循环中的位置
            RectEntity e = rectEntityList.get(i);
            // 循环中的孩子
            View v = getChildAt(i);
            // 让孩子到指定的位置
            v.layout(e.leftX, e.leftY, e.rightX, e.rightY);
        }
    }


    //========================私有的方法 start===================

    /**
     * 每一行显示三个图片
     */
    private int column = 3;

    /**
     * 图片之间的间隔距离
     */
    private int intervalDistance = 4;

    /**
     * 自身的宽和高
     */
    private int mWidth;
    private int mHeight;

    /**
     * 用于计算孩子们的位置信息
     */
    private void computeViewsLocation() {
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }
        if (childCount == rectEntityList.size()) {
            return;
        }
        rectEntityList.clear();
        mWidth = getWidth();
        mHeight = getHeight();
        //得到要显示的行数
        int rows = 0;
        if (childCount % column == 0) {
            rows = childCount / column;
        } else {
            rows = (childCount - childCount % column) / column + 1;
        }
        rows = 3;
        //得到每行的高度
        int eachRowHeight = (int) ((mHeight - (rows + 1) * intervalDistance) / new Float(rows));
        //得到每个图片显示的宽度
        int eachViewWidth = (int) ((mWidth  - (column + 1) * intervalDistance ) / new Float(column));

        if (childCount % column != 0) {
            rows--;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < column; j++) {
                RectEntity r = new RectEntity();
                r.leftX = j * eachViewWidth + (j + 1) * intervalDistance;
                r.leftY = i * eachRowHeight + (i + 1) * intervalDistance;
                r.rightX = (j + 1) * eachViewWidth + (j + 1) * intervalDistance;
                r.rightY = (i + 1) * eachRowHeight + (i + 1) * intervalDistance;
                rectEntityList.add(r);
            }
        }

        for (int i = 0; i < childCount % column; i++) {
            RectEntity r = new RectEntity();
            r.leftX = i * eachViewWidth + (i + 1) * intervalDistance;
            r.leftY = (rows) * eachRowHeight + (rows + 1) * intervalDistance;
            r.rightX = (i + 1) * eachViewWidth + (i + 1) * intervalDistance;
            r.rightY = (rows + 1) * eachRowHeight + (rows + 1) * intervalDistance;
            rectEntityList.add(r);
        }

    }

    //========================私有的方法 end=====================


    //========================暴露的方法 start=====================

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

    //========================暴露的方法 end=====================

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

}
