package com.example.cxj.huaishi.modular.postCard.ui;

import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cxj.huaishi.MyApp;
import com.example.cxj.huaishi.R;
import com.example.cxj.huaishi.modular.imageSelect.ui.SelectLocalImageAct;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xiaojinzi.activity.BaseActivity;
import xiaojinzi.annotation.Injection;
import xiaojinzi.base.android.os.T;

/**
 * 发帖的界面
 */
public class PostCardAct extends BaseActivity {

    @Injection(value = R.id.tv_act_post_card_post, click = "viewClick")
    private TextView tv_post = null;

    @Injection(value = R.id.tv_act_post_card_cancel, click = "viewClick")
    private TextView tv_cancel = null;

    @Injection(value = R.id.iv_act_post_card_more, click = "viewClick")
    private ImageView iv_more = null;

    @Injection(R.id.et_act_post_card_content)
    private EditText et_content = null;

    @Override
    public int getLayoutId() {
        return R.layout.act_post_card;
    }

    /**
     * 点击事件的集中处理
     *
     * @param v
     */
    public void viewClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_act_post_card_post:
                post();
                break;
            case R.id.tv_act_post_card_cancel:
                finish();
                break;
            case R.id.iv_act_post_card_more: //弹出底部的菜单,选择是拍照还是选择图片
                filePaths = null;
                Intent i = new Intent(this, SelectLocalImageAct.class);
                startActivityForResult(i, 0);
                break;
        }
    }

    /**
     * 要上传的文件的路径数据
     */
    private String[] filePaths = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            //获取到返回的文件路径数组的数据
            filePaths = data.getStringArrayExtra(SelectLocalImageAct.RETURN_DATA_FLAG);
        }
    }

    /**
     * 提交说说
     */
    public void post() {

        //说说的内容
        String content = et_content.getText().toString();
        if (TextUtils.isEmpty(content)) {
            T.showShort(context, "说说的内容不能为空");
            return;
        }

        //创建一个问部分的表单构造器
        MultipartBody.Builder builder = new MultipartBody.Builder();

        if (filePaths != null) { //加入文件的请求体部分
            for (int i = 0; i < filePaths.length; i++) {
                String filePath = filePaths[i];
                File file = new File(filePath);
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                builder.addFormDataPart("files", file.getName(), fileBody);
            }
        }

        builder.addFormDataPart("content", content).addFormDataPart("user.id", MyApp.u.getId() + "");

        MultipartBody multipartBody = builder.build();

        Call<String> call;

        if (filePaths == null) {
            call = MyApp.netWorkService.postNormalCard(multipartBody);
        }else{
            call = MyApp.netWorkService.postCardWithImage(multipartBody);
        }

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
