package com.yoursecondworld.secondworld.modular.forgetPassword;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yoursecondworld.secondworld.R;
import com.yoursecondworld.secondworld.common.BaseAct;
import com.yoursecondworld.secondworld.modular.selectGame.ui.SelectGameAct;

import xiaojinzi.annotation.Injection;
import xiaojinzi.base.android.activity.ActivityUtil;
import xiaojinzi.base.android.os.T;

/**
 * 忘记密码的界面
 */
public class ForgetPasswordAct extends BaseAct implements Runnable {


    //短信发送的时候.这里是表示还要等多久才能再一次返送,默认是60
    private int leaveSecond = 0;

    private boolean isClickGetCheckCodeButton = false;

    /**
     * 消息的传送
     */
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;

            switch (what) {
                case 0: //表示在等待
                    bt_getCheckCode.setEnabled(false); //让用户不能点击
                    bt_getCheckCode.setText("重新发送(" + leaveSecond + ")");
                    //设置灰色的背景,表示现在不是能点击的
                    bt_getCheckCode.setBackgroundResource(R.drawable.invalid_getcheckcode_bt_bg);
                    break;
                case 1:
                    //可以响应用户的点击了
                    bt_getCheckCode.setEnabled(true);
                    bt_getCheckCode.setBackgroundResource(R.drawable.getcheckcode_bt_bg);
                    //如果已经点过按钮了,那么这里显示的就是再次发送
                    if (isClickGetCheckCodeButton) {
                        bt_getCheckCode.setText("重新发送");
                    } else {
                        bt_getCheckCode.setText("发送验证码");
                    }
                    break;
            }

        }
    };

    /**
     * 控制线程是否存活,界面开启,线程就启动
     */
    private boolean controlThread = true;

    @Injection(R.id.rl_act_titlebar)
    private RelativeLayout rl_titlebar = null;

    @Injection(value = R.id.iv_back, click = "clickView")
    private ImageView iv_back = null;

    @Injection(value = R.id.bt_act_forget_password_getcheckcode, click = "clickView")
    private Button bt_getCheckCode = null;

    @Injection(value = R.id.bt_act_forget_password_submit,click = "clickView")
    private Button bt_submit = null;

    @Override
    public int getLayoutId() {
        return R.layout.act_forget_password;
    }


    @Override
    public void initView() {
        super.initView();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rl_titlebar.getLayoutParams();
        lp.topMargin = statusHeight;

        //启动线程
        new Thread(this).start();

    }


    /**
     * 点击事件的集中处理
     *
     * @param v
     */
    public void clickView(View v) {

        //获取控件的id
        int id = v.getId();

        //对id的筛选然后做处理
        switch (id) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_act_forget_password_getcheckcode:
                leaveSecond = 5;
                isClickGetCheckCodeButton = true;
                break;
            case R.id.bt_act_forget_password_submit:

                ActivityUtil.startActivity(context, SelectGameAct.class);

                break;
        }

    }

    @Override
    public boolean isRegisterEvent() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controlThread = false;
    }

    /**
     * 用来实现短信发送的倒计时
     */
    @Override
    public void run() {
        while (controlThread) {
            //休眠一秒钟
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            if (leaveSecond > 0) {
                leaveSecond--;
                h.sendEmptyMessage(0);
            } else {
                h.sendEmptyMessage(1);
            }
        }
    }
}
