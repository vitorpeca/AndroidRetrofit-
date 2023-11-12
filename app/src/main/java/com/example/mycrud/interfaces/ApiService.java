package com.example.mycrud.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiService {


            @GET("/add")
            Call<Double> add(@Query("num1") String num1, @Query("num2") String num2);


    }

