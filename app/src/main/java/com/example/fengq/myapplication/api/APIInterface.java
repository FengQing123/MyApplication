package com.example.fengq.myapplication.api;

import com.example.fengq.myapplication.bean.PersonModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by fengq on 2017/5/10.
 */

public interface APIInterface {
    //    https://api.github.com/users/Guolei1130
    @GET("/users/{user}")
    Call<PersonModel> repo(@Path("user") String user);
}
