package xiaojinzi.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import xiaojinzi.android.view.common.MyHorizontalScrollView;

/**
 * Created by cxj on 2016/4/1.
 */
public class MyLinearHorizontalView extends MyHorizontalScrollView {

    public MyLinearHorizontalView(Context context) {
        super(context);
    }

    public MyLinearHorizontalView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearHorizontalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("the childCount must be two");
        }
        mWidth = getWidth();
        mHeight = getHeight();
        View view = getChildAt(0);
        view.layout(0, 0, mWidth, mHeight);
        view = getChildAt(1);
        view.layout(mWidth, 0, mWidth + view.getMeasuredWidth(), mHeight);
    }

    @Override
    public int getRightBound() {
        return getChildAt(1).getMeasuredWidth();
    }

    @Override
    public int getCenterBound() {
        return getChildAt(1).getMeasuredWidth() / 2;
    }

    private boolean menuIsOpened = false;

    public boolean isMenuIsOpened() {
        return menuIsOpened;
    }

    @Override
    protected void smoothTo(int finalX) {
        super.smoothTo(finalX);
        if (onMenuStateListener != null) {
            if (finalX == getRightBound() && menuIsOpened == false) {
                onMenuStateListener.menuStateChange(true);
                menuIsOpened = true;
            } else if (finalX == getLeftBound() && menuIsOpened) {
                onMenuStateListener.menuStateChange(false);
                menuIsOpened = false;
            }
        }
    }

    //公有方法的开发 start

    /**
     * 打开菜单
     *
     * @param isShowAnimation 是否展示动画
     */
    public void openMenu(boolean isShowAnimation) {
        if (getScrollX() == getRightBound() || menuIsOpened) {
            return;
        }
        int finalX = getRightBound();
        if (isShowAnimation) {
            smoothTo(finalX);
        } else {
            scrollTo(finalX, 0);
            if (onMenuStateListener != null) {
                if (finalX == getRightBound() && menuIsOpened == false) {
                    onMenuStateListener.menuStateChange(true);
                    menuIsOpened = true;
                } else if (finalX == getLeftBound() && menuIsOpened) {
                    onMenuStateListener.menuStateChange(false);
                    menuIsOpened = false;
                }
            }
        }
    }

    /**
     * 关闭菜单
     *
     * @param isShowAnimation 是否展示动画
     */
    public void closeMenu(boolean isShowAnimation) {
        if (getScrollX() == getLeftBound() || !menuIsOpened) {
            return;
        }
        int finalX = getLeftBound();
        if (isShowAnimation) {
            smoothTo(finalX);
        } else {
            scrollTo(finalX, 0);
            if (onMenuStateListener != null) {
                if (finalX == getRightBound() && menuIsOpened == false) {
                    onMenuStateListener.menuStateChange(true);
                    menuIsOpened = true;
                } else if (finalX == getLeftBound() && menuIsOpened) {
                    onMenuStateListener.menuStateChange(false);
                    menuIsOpened = false;
                }
            }
        }
    }
    //公有方法的开发 end

    //提供的接口 start

    /**
     * 菜单状态的监听
     */
    public interface OnMenuStateListener {
        /**
         * 当菜单展开和关闭的时候调用
         *
         * @param isOpen
         */
        public void menuStateChange(boolean isOpen);
    }

    private OnMenuStateListener onMenuStateListener = null;

    /**
     * 设置菜单的监听
     *
     * @param onMenuStateListener 菜单的监听接口
     */
    public void setOnMenuStateListener(OnMenuStateListener onMenuStateListener) {
        this.onMenuStateListener = onMenuStateListener;
    }

    //提供的接口 end

}
