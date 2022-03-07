package com.efhiserytestcode.network.service;

import com.efhiserytestcode.model.AreaModel;
import com.efhiserytestcode.model.ListModel;
import com.efhiserytestcode.model.SizeModel;

import org.json.JSONObject;

import io.reactivex.Observable;

import java.util.ArrayList;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // Get List
    @GET("list")
    Observable<ArrayList<ListModel>> getList(
            @Query("search") JSONObject search
    );

    // Add Row
    @POST("list")
    Observable<JSONObject> addRow(
            @Body ArrayList<ListModel> listModel
    );

    // Get Area
    @GET("option_area")
    Observable<ArrayList<AreaModel>> getArea();

    // Get Size
    @GET("option_size")
    Observable<ArrayList<SizeModel>> getSize();

}
