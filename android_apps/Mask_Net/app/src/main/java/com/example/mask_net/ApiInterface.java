package com.example.mask_net;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

interface ApiInterface {
    @FormUrlEncoded
    @POST("/penalty_user")
    Call<List<Task>>getstatus(@Field("user_name") String user_name);

}

