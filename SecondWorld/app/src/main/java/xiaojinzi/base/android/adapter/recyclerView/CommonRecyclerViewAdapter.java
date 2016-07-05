package xiaojinzi.base.android.adapter.recyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by cxj on 2016/4/28.
 */
public abstract class CommonRecyclerViewAdapter<T> extends RecyclerView.Adapter<CommonRecyclerViewHolder> {

    /**
     * 条目的点击事件的监听接口
     */
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    /**
     * 上下文对象
     */
    protected Context context;

    /**
     * 要显示的数据
     */
    protected List<T> data;

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param data    显示的数据
     */
    public CommonRecyclerViewAdapter(Context context, List<T> data) {
        this.data = data;
        this.context = context;
    }

    /**
     * viewType 是通过{@link RecyclerView.Adapter#getItemViewType(int)}获取到的
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, getLayoutViewId(viewType), null);
        CommonRecyclerViewHolder vh = new CommonRecyclerViewHolder(view);
        viewCreated(vh, viewType);
        return vh;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new MyClickListenerAdapter(position));
        convert(holder, data.get(position), position);
    }

    /**
     * 实现列表的显示
     *
     * @param h        RecycleView的ViewHolder
     * @param entity   实体对象
     * @param position 当前的下标
     */
    public abstract void convert(CommonRecyclerViewHolder h, T entity, int position);

    /**
     * 布局文件被转化成View的时候调用
     *
     * @param vh
     * @param viewType
     */
    public void viewCreated(CommonRecyclerViewHolder vh, int viewType) {
    }

    /**
     * @param viewType 返回值就是根据这个值进行判断返回的
     * @return
     */
    public abstract int getLayoutViewId(int viewType);

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 设置监听事件
     *
     * @param onRecyclerViewItemClickListener
     */
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    /**
     * 条目的点击事件
     */
    public interface OnRecyclerViewItemClickListener {

        /**
         * 回调的方法
         *
         * @param v
         * @param position
         */
        public void onItemClick(View v, int position);

    }

    /**
     * 实现点击的接口,每一个ViewItem都对应一个这个类,每一个都不一样的对象
     */
    private class MyClickListenerAdapter implements View.OnClickListener {

        /**
         * 条目的下标
         */
        private int postion;

        public MyClickListenerAdapter(int postion) {
            this.postion = postion;
        }

        @Override
        public void onClick(View v) {
            if (onRecyclerViewItemClickListener != null) {
                //回调方法
                onRecyclerViewItemClickListener.onItemClick(v, postion);
            }
        }

    }

}
