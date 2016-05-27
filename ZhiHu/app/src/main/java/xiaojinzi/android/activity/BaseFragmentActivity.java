package xiaojinzi.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import xiaojinzi.android.activity.annotation.ViewInjectionUtil;
import xiaojinzi.android.message.EBus;

/**
 * 对activity的进行的简易封装
 *
 * @author xiaojinzi
 */
public abstract class BaseFragmentActivity extends FragmentActivity {

    // 上下文对象
    public Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isShowTitleBar()) {
            // 设置没有标题栏
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        if (isFullScreen()) {
            //设置全屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        context = this;

        //注册自己可以接受事件
        EBus.register(this);

        setContentView(getLayoutId());

        // 尝试注入属性
        ViewInjectionUtil.injectView(this);

        // 初始化控件
        initView();

        // 初始化数据
        initData();

        // 设置各种监听
        setOnlistener();

    }

    /**
     * activity所使用的布局文件的id
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 是否显示标题栏
     *
     * @return
     */
    public boolean isShowTitleBar() {
        return false;
    }

    /**
     * 是否全屏显示
     *
     * @return
     */
    public boolean isFullScreen() {
        return true;
    }

    /**
     * 初始化控件
     */
    public void initView() {
    }

    ;

    /**
     * 初始化数据
     */
    public void initData() {
    }

    ;

    /**
     * 设置各种监听
     */
    public void setOnlistener() {
    }

    ;

    /**
     * 简化findViewById方法
     *
     * @param <T>
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T f(int viewId) {
        return (T) findViewById(viewId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册自己
        EBus.unRegister(this);
    }
}
