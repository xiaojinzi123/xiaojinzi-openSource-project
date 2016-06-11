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
     * 上下文对象
     */
    protected Context context;

    /**
     * 要显示的数据
     */
    protected List<T> data;

    public CommonRecyclerViewAdapter(Context context, List<T> data) {
        this.data = data;
        this.context = context;
    }

    /**
     * viewType 是通过{@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}获取到的
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
     * @param vh
     * @param viewType
     */
    public void viewCreated(CommonRecyclerViewHolder vh, int viewType){
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

}
