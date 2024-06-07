package com.example.easypepg.retrofit_setup;

import com.example.easypepg.modal.SDKData;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetDataService {
    @POST("paymentrequest")
    Call<ResponseBody> initPg(@Body MultipartBody data);
    @POST("transact")
    Call<ResponseBody> checkStatus(@Body SDKData data);
}
