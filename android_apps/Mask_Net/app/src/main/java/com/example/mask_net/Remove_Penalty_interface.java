package com.example.mask_net;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Remove_Penalty_interface {

    @FormUrlEncoded
    @POST("/remove_penalty")
    Call<List<Remove_penalty_POJO>>getID(@Field("id") String id);
}
