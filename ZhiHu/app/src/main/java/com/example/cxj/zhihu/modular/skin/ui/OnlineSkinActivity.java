package com.example.cxj.zhihu.modular.skin.ui;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cxj.zhihu.MyApp;
import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.common.tool.Msg;
import com.example.cxj.zhihu.config.Constant;
import com.example.cxj.zhihu.db.skin.SkinDb;
import com.example.cxj.zhihu.modular.skin.fragment.OnlineSkinViewPagerFragment;
import com.example.cxj.zhihu.modular.skin.entity.JsonSkin;
import com.example.cxj.zhihu.modular.skin.entity.SkinPaging;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import xiaojinzi.activity.BaseFragmentActivity;

import xiaojinzi.base.android.os.ProgressDialogUtil;
import xiaojinzi.base.android.os.T;
import xiaojinzi.json.android.JsonUtil;
import xiaojinzi.net.adapter.ResponseHandlerAdapter;
import xiaojinzi.net.filter.PdHttpRequest;
import xiaojinzi.viewAnnotation.Injection;


/**
 * 在线皮肤的界面,可以选择自己喜欢的皮肤
 */
public class OnlineSkinActivity extends BaseFragmentActivity {

    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == 666) {
                dialog.dismiss();
            }
        }
    };

    /**
     * pageSize的标识
     */
    public static final String PAGESIZEFLAG = "pageSize";

    /**
     * 类的标识
     */
    public static final String TAG = "OnlineSkinActivity";

    @Injection(value = R.id.iv_act_online_skin_back, click = "clickView")
    private ImageView iv_back = null;

    @Injection(value = R.id.iv_act_online_skin_download, click = "clickView")
    private ImageView iv_download = null;

    @Injection(R.id.rl_act_online_skin_container)
    private RelativeLayout rl_container = null;

    @Injection(R.id.rl_act_online_titlebar)
    private RelativeLayout rl_titleBar = null;

    @Injection(R.id.tv_act_online_skin_title)
    private TextView tv_title = null;

    @Injection(R.id.vp_act_online_skin)
    private ViewPager vp = null;

    /**
     * viewpager用的fragment集合
     */
    private List<Fragment> fragments = new ArrayList<Fragment>();

    /**
     * 返回的数据
     */
    private SkinPaging p = null;

    /**
     * 进度条对话框
     */
    private ProgressDialog dialog;

    @Override
    public int getLayoutId() {
        return R.layout.act_online_skin;
    }


    @Override
    public void initView() {
        loadStyle();
    }

    @Override
    public void initData() {

        popupDialog();

        //加载在线皮肤信息
        PdHttpRequest httpRequest = new PdHttpRequest(Constant.Url.skinAct.skinListUrl);
        httpRequest.setResponseHandler(new ResponseHandlerAdapter(){
            @Override
            public void handler(String data, Object[] p) throws Exception {
                //创建一个和相应的json一样的实体对象
                Msg m = new Msg();
                //这里矿建创建对象的时候会失败,因为这里用了泛型,所以手动帮框架创建了一个对象
                m.setData(new SkinPaging(0, 0));
                //转化json数据到实体对象
                JsonUtil.parseObjectFromJson(m, data);
                //如果返回的数据是正确的
                if (Msg.OK.equals(m.getMsg())) {
                    //处理数据u
                    handData(m);
                } else { //如果返回的数据失败,提示错误信息
                    T.showShort(OnlineSkinActivity.this, m.getMsgText());
                }
                //关闭对话框
                closeDialog();
            }

            @Override
            public void error(Exception e, Object[] p) {
                closeDialog();
                T.showShort(OnlineSkinActivity.this, "服务器正在打盹~~~~~");
            }
        });
        MyApp.ah.send(httpRequest);
    }

    @Override
    public void setOnlistener() {
    }

    /**
     * 处理数据
     *
     * @param m
     */
    private void handData(Msg m) throws JSONException {
        fragments.clear();
        //拿到返回的数据中的真的数据
        p = (SkinPaging) m.getData();
        List<JsonSkin> rows = p.getRows();
        //循环所有的皮肤对象
        for (int i = 0; rows != null && i < rows.size(); i++) {
            JsonSkin jsonSkin = rows.get(i);
            fragments.add(new OnlineSkinViewPagerFragment(jsonSkin));
        }
        //viewPager设置适配器
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }


    /**
     * 弹出对话框
     */
    private void popupDialog() {
        if (dialog == null) {
            dialog = ProgressDialogUtil.show(this, ProgressDialog.STYLE_SPINNER, false);
        } else {
            dialog.show();
        }
    }

    /**
     * 关闭对话框,延迟关闭,效果更佳
     */
    private void closeDialog() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }
                h.sendEmptyMessage(666);
            }
        }.start();
    }

    /**
     * View的点击事件集中处理
     *
     * @param v
     */
    public void clickView(View v) {
        int id = v.getId();
        if (id == R.id.iv_act_online_skin_back) { //如果是点击是返回的图标
            finish();
        } else if (id == R.id.iv_act_online_skin_download) { //如果点击的是下载的图标
            int currIndex = vp.getCurrentItem();
            if (currIndex < 0 || p == null || p.getRows() == null || p.getRows().size() == 0) {
                T.showShort(this, "请选择要下载的主题");
            } else {
                //拿到要下载的皮肤
                JsonSkin jsonSkin = p.getRows().get(currIndex);
                //根据皮肤的id查询数据库中有没有
                JsonSkin dbJsonSkin = SkinDb.getInstance(this).queryBySkinId(jsonSkin.getId());
                //如果没有,就去下载
                if (dbJsonSkin == null) {
                    SkinDb.getInstance(this).insert(jsonSkin);
                    T.showShort(this, "下载成功");
                } else { //如果有了,就提示已经下载
                    T.showShort(this, "已经下载");
                }
            }
        }
    }

    /**
     * 加载样式
     */
    private void loadStyle() {
        rl_container.setBackgroundColor(MyApp.skin.onLineSkinActSkin.getContainerBgColor());
        rl_titleBar.setBackgroundColor(MyApp.skin.onLineSkinActSkin.getTitleBarBgColor());
        vp.setBackgroundColor(MyApp.skin.onLineSkinActSkin.getViewPagerBgColor());
    }

    /**
     * 更改标题栏的文本
     * @param title
     */
    public void onEventChangeTitle(String title){
        tv_title.setText(title);
    }

}
