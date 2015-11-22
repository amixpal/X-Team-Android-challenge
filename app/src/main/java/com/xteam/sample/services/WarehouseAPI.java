package com.xteam.sample.services;

import com.squareup.okhttp.Response;
import com.xteam.sample.models.Warehouse;

import org.json.JSONObject;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Amit on 10/24/15.
 */

/*
Retrofit API Endpoints
 */
public interface WarehouseAPI {

    @GET("/search")
    public Observable<List<Warehouse>> getWarehouseWithLimit (@Query("limit") int limit);

    @GET("/search")
    public Observable<List<Warehouse>> getWarehouseWithSkip (@Query("skip") int limit);

    @GET("/search")
    public Observable<List<Warehouse>> searchWithTags(@Query("q") String tags);


}
