package com.example.benjamin.postup;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface heroku {
    @POST("/predict")
    Call<Posted> analyze (@Field("phrase") String phrase);
}
