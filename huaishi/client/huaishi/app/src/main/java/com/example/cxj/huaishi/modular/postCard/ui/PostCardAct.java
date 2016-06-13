package com.example.cxj.huaishi.modular.postCard.ui;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cxj.huaishi.MyApp;
import com.example.cxj.huaishi.R;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xiaojinzi.activity.BaseActivity;
import xiaojinzi.annotation.Injection;

/**
 * 发帖的界面
 */
public class PostCardAct extends BaseActivity {

    @Injection(value = R.id.tv_act_post_card_post,click = "viewClick")
    private TextView tv_post = null;

    @Injection(value = R.id.tv_act_post_card_cancel,click = "viewClick")
    private TextView tv_cancel = null;

    @Override
    public int getLayoutId() {
        return R.layout.act_post_card;
    }

    public void viewClick(View v){
        int id = v.getId();
        switch (id) {
            case R.id.tv_act_post_card_post:
                post();
                break;
            case  R.id.tv_act_post_card_cancel:
                finish();
                break;
        }
    }

    public void post(){
        //需要上传的文件
        File f1 = new File(Environment.getExternalStorageDirectory(), "address.db");
        File f2 = new File(Environment.getExternalStorageDirectory(), "1.apk");

        //创建文件部分的请求体对象
        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), f1);
        RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), f2);

        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("files", f1.getName(), fileBody1)
                .addFormDataPart("files", f2.getName(), fileBody2)
                .addFormDataPart("user.id", "1")
                .addFormDataPart("content", "helloJni")
                .build();

        Call<String> call = MyApp.netWorkService.postCard(multipartBody);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                System.out.println("上传成功" + response.body());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("上传多文件失败" + t.getMessage() + "," + t.getCause());
            }
        });
    }

}
