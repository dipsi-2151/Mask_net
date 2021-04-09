package com.example.mask_net;

import com.google.gson.annotations.SerializedName;

public class Remove_penalty_POJO {
    @SerializedName("response") public String response;

    public Remove_penalty_POJO(String response) {
        this.response=response;
    }

    public String getResponse(){ return this.response; }
}
