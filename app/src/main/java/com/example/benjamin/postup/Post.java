package com.example.benjamin.postup;

import com.google.gson.annotations.SerializedName;

public class Post {
    private String phrase;
    @SerializedName("accuracy")
    private Double accuracy;
    @SerializedName("polarity")
    private String polarity;

    public Post(String phrase) {
        this.phrase = phrase;
    }

    public Double getAcc() {
        return accuracy;
    }

    public String getPolarity() {
        return polarity;
    }
}
