package com.example.benjamin.postup;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Heroku {
    @POST("/predict")
    Call<Post> analyze (@Body Post post);
}
