package com.kobe.light.api;


import com.kobe.light.request.BindRequest;
import com.kobe.light.request.LoginRequest;
import com.kobe.light.request.SubmitRequest;
import com.kobe.light.response.DeviceResponse;
import com.kobe.light.response.DictResponse;
import com.kobe.light.response.LoginResponse;
import com.kobe.light.response.SubmitResponse;
import com.kobe.light.response.UploadResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LightApi {

    String FINAL_URL = "http://140.246.81.58:8587/hongli-lc/api/";

    //登录
    @POST("sys/login")
    Observable<LoginResponse> login(@Body LoginRequest loginBean);

    //获取选项列表
    @GET("dict/data/list")
    Observable<DictResponse> getDict(@Query("dictType") String dictType);

    //扫码后获取信息
    @GET("lamp/pole/view/{key}")
    Observable<DeviceResponse> getPoleInfo(@Path("key") String key);

    //提交
    @POST("lamp/pole/edit")
    Observable<SubmitResponse> submit(@Body SubmitRequest submitRequest);

    @POST("common/upload/files")
    @Multipart
    Observable<UploadResponse> upload(@Part List<MultipartBody.Part> partList);

    //灯控器绑定
    @POST("lamp/install/add")
    Observable<SubmitResponse> bind(@Body BindRequest bindRequest);

}