package com.example.cxj.huaishi.service;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by cxj on 2016/6/10.
 * 所有的网络请求
 */
public interface NetWorkService {

    /**
     * 注册
     *
     * @param phoneNumber 电话号码
     * @param password    密码
     * @return
     */
    @FormUrlEncoded
    @POST("client/user/register")
    public Call<String> register(@Field("phoneNumber") String phoneNumber, @Field("password") String password);

    /**
     * 登陆
     *
     * @param phoneNumber 电话号码
     * @param name        用户名
     * @param password    密码
     * @return
     */
    @FormUrlEncoded
    @POST("client/user/login")
    public Call<String> login(@Field("phoneNumber") String phoneNumber, @Field("name") String name, @Field("password") String password);

    /**
     * 发帖的时候发出的网络请求
     * 参数是一个多部分的请求体
     *
     * @param multipartBody
     * @return
     */
    @POST("client/card/insertCardWithImage")
    public Call<String> postCardWithImage(@Body MultipartBody multipartBody);

    @POST("client/card/insertNormalCard")
    public Call<String> postNormalCard(@Body MultipartBody multipartBody);

    /**
     * 获取说说列表
     *
     * @param beforDate
     * @return
     */
    @FormUrlEncoded
    @POST("client/card/listCard")
    public Call<String> listCard(@Field("date")Long beforDate);

}
