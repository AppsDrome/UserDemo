package com.appsdrome.innofiedDemo.service;

import com.appsdrome.innofiedDemo.model.Post;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetUserPostServiceData {

    @GET("api/users")
    Call<Post> getResults(@Query("page") int page,@Query("per_page") int per_page);
}
