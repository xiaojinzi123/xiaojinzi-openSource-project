package xiaojinzi.android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xiaojinzi.android.activity.annotation.ViewInjectionUtil;

/**
 * 基本的Fragment,可以防止多次加载数据
 *
 * @author cxj
 */
public abstract class BaseFragment1 extends Fragment {

    // 上下文对象
    public Context context = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
    }

    /**
     * fragment使用的试图
     */
    private View view = null;

    /**
     * 是否准备好视图
     */
    protected boolean isPrepareView;

    /**
     * 只要类没有销毁,就控制试图也只加载一次,防止多次加载浪费资源
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //如果视图没有加载过,就加载试图
        if (view == null)
            view = inflater.inflate(getLayoutId(), null);

        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }

        // 尝试注入属性,让注解起作用
        ViewInjectionUtil.injectView(this, view);

        //可能需要额外的初始化控件
        initView();

        isPrepareView = true;

        //初始化数据
        initData();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        isPrepareView = false;

    }

    /**
     * activity所使用的布局文件的id
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化试图
     */
    public void initView() {
    }

    /**
     * 初始化数据
     */
    public void initData() {
    }

    /**
     * 设置各种监听
     */
    public void setOnlistener() {
    }

    /**
     * 刷新UI
     */
    public void freshUI() {
    }

    /**
     * 简化findViewById方法
     *
     * @param <T>
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T f(int viewId) {
        return (T) view.findViewById(viewId);
    }

    /* 对于用户是否可见的变量 */
    protected boolean isVisibleToUser;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        //记录当前的可见状态
        this.isVisibleToUser = isVisibleToUser;

        if (isPrepareView && isVisibleToUser) {
            freshUI();
        }

    }

}
