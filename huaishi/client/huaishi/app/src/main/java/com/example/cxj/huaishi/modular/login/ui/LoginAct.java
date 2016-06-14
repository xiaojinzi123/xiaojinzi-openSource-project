package com.example.cxj.huaishi.modular.login.ui;

import android.app.ProgressDialog;


import android.system.ErrnoException;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cxj.huaishi.MyApp;
import com.example.cxj.huaishi.R;
import com.example.cxj.huaishi.common.Msg;
import com.example.cxj.huaishi.common.entity.User;
import com.example.cxj.huaishi.modular.main.main.ui.MainAct;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xiaojinzi.activity.BaseActivity;
import xiaojinzi.annotation.Injection;
import xiaojinzi.base.android.activity.ActivityUtil;
import xiaojinzi.base.android.os.ProgressDialogUtil;
import xiaojinzi.base.android.os.T;

/**
 * A login screen that offers login via email/password.
 */
public class LoginAct extends BaseActivity {

    @Injection(R.id.et_act_login_name_or_phone)
    private EditText et_name_or_phoneNumber;

    @Injection(R.id.et_act_login_password)
    private EditText et_password;

    @Injection(value = R.id.bt_act_login_login, click = "viewClick")
    private Button bt_login;

    @Injection(value = R.id.tv_act_login_forget_password, click = "viewClick")
    private TextView tv_forget_password;

    @Injection(value = R.id.tv_act_login_register, click = "viewClick")
    private TextView tv_register;

    @Override
    public int getLayoutId() {
        return R.layout.act_login;
    }

    @Override
    public void initView() {
        //初始化进度条对话框
        MyApp.dialog = ProgressDialogUtil.show(this, ProgressDialog.STYLE_SPINNER, false);
        MyApp.dialog.dismiss();
    }

    @Override
    public void initData() {
    }

    /**
     * 点击事件的集中处理
     *
     * @param v
     */
    public void viewClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.bt_act_login_login: // 登陆逻辑实现
                login();
                break;
            case R.id.tv_act_login_forget_password: //跳转到找回密码界面
                T.showShort(context, "跳转到找回密码界面");
                break;
            case R.id.tv_act_login_register: //跳转到注册界面
                T.showShort(context, "跳转到注册界面");
                break;
        }


    }

    /**
     * 登陆的逻辑
     */
    private void login() {

        String nameOrPhoneNumber = et_name_or_phoneNumber.getText().toString();
        String password = et_password.getText().toString();

        MyApp.dialog.setMessage("正在登录");
        MyApp.dialog.show();

        Call<String> call = MyApp.netWorkService.login(nameOrPhoneNumber, nameOrPhoneNumber, password);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    //返回的字符串json数据
                    String body = response.body();
                    Msg m = MyApp.gson.fromJson(body, Msg.class);
                    if (Msg.OK.equals(m.getMsg())) { //登陆成功
                        T.showShort(context, "登录成功");
                        MyApp.dialog.dismiss();
                        org.json.JSONObject jo = new org.json.JSONObject(body);
                        String userJson = jo.getString("data");
                        MyApp.u = MyApp.gson.fromJson(userJson, User.class);
                        if (MyApp.u.getId() != null) {
                            ActivityUtil.startActivity(context, MainAct.class);
                            finish();
                        }
                    }
                } catch (Exception e) {
                    MyApp.dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyApp.dialog.dismiss();
            }
        });

    }

}

