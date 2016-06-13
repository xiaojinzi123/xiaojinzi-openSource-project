package xiaojinzi.view.tabHost;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by cxj on 2016/4/25.
 * 一个自定义的TabHost
 */
public class MTabHost extends RadioGroup {

    /**
     * 文字在图片的上面
     */
    public static final int TOP_TEXTPOSITION = 0;

    /**
     * 文字在图片的下面
     */
    public static final int BOTTOM_TEXTPOSITION = 1;

    /**
     * 文字在图片的左边
     */
    public static final int LEFT_TEXTPOSITION = 2;

    /**
     * 文字在图片的右边
     */
    public static final int RIGHT_TEXTPOSITION = 3;

    /**
     * 默认文字是在图片上面的
     */
    private int textPosition = BOTTOM_TEXTPOSITION;

    public MTabHost(Context context) {
        this(context, null);
    }

    public MTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //===================== 共有方法 start ==========================================

    /**
     * @param norImageRid
     * @param selectedImageRid
     * @param text
     */
    public void addTab(int norImageRid, int selectedImageRid, String text) {
        //创建单选按钮
        RadioButton rb = new RadioButton(getContext());
        //取消圆点
        rb.setButtonDrawable(null);
        //设置内容居中
        rb.setGravity(Gravity.CENTER);
        //声明布局参数对象
        LayoutParams lp;
        StateListDrawable drawable = new StateListDrawable();
        //添加默认的item
        drawable.addState(new int[]{-android.R.attr.state_checked},
                new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), norImageRid)));
        //添加选择后的item
        drawable.addState(new int[]{android.R.attr.state_checked},
                new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), selectedImageRid)));
        //设置大小
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        //获取组建的朝向
        int orientation = getOrientation();
        //如果是水平的
        if (orientation == LinearLayout.HORIZONTAL) {
            //水平的时候创建一个高度填充,但是宽度平分的布局参数
            lp = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            //判断文字是不是在图片下面
            if (textPosition == BOTTOM_TEXTPOSITION) {
                rb.setCompoundDrawables(null, drawable, null, null);
            } else { //文字再图片上面
                rb.setCompoundDrawables(null, null, null, drawable);
            }
        } else { //否则就是竖直的
            //竖直的时候创建一个宽度填充,但是高度平分的布局参数
            lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            //判断文字是不是在图片右边
            if (textPosition == RIGHT_TEXTPOSITION) {
                rb.setCompoundDrawables(drawable, null, null, null);
            } else { //文字在图片左边
                rb.setCompoundDrawables(null, null, drawable, null);
            }
        }
        //设置布局参数
        rb.setLayoutParams(lp);
        //设置文本
        rb.setText(text);
        //添加到组中
        this.addView(rb);
        //请求重新布局
        requestLayout();
    }

    /**
     * 设置文字的方位
     * 在方法{@link MTabHost#addTab(int, int, String)}之前调用
     * <p/>
     * {@link MTabHost#TOP_TEXTPOSITION}
     * {@link MTabHost#BOTTOM_TEXTPOSITION}
     * {@link MTabHost#LEFT_TEXTPOSITION}
     * {@link MTabHost#RIGHT_TEXTPOSITION}
     *
     * @param textPosition
     */
    public void setTextPosition(int textPosition) {
        this.textPosition = textPosition;
    }

    //===================== 共有方法 end ==========================================

}
