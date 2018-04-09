package com.example.benjamin.postup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Posted {
    @SerializedName("accuracy")
    @Expose
    private Double accuracy;
    @SerializedName("polarity")
    @Expose
    private String polarity;

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public String getPolarity() {
        return polarity;
    }

    public void setPolarity(String polarity) {
        this.polarity = polarity;
    }

}
