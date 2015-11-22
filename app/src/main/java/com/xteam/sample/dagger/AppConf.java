package com.xteam.sample.dagger;

import android.app.Activity;
import android.content.Context;

import com.xteam.sample.AppDelegate;
import com.xteam.sample.adapter.StaggeredGridAdapter;

/**
 * Created by Amit on 10/26/15.
 */

/*
Global object on Application level configured by Dagger2
For more details : AppConfComponent and AppConfModule
 */
public class AppConf extends AppDelegate {

    public static final String BASE_URL = "http://74.50.59.155:5000/api";

    public static final String NO_RESPONSE = "oops! Something went wrong. Please try again later!";

    public static final String NO_RESULT = "No result found";

    public static final int RESPONSE_LIMIT = 10;

    public static boolean getIsResponseForTags() {
        return isResponseForTags;
    }

    public static void setIsResponseForTags(boolean isResponseForTags) {
        AppConf.isResponseForTags = isResponseForTags;
    }

    public static  boolean isResponseForTags;

    public static StaggeredGridAdapter adapter = null;

    public static StaggeredGridAdapter getAdapter() {
        return adapter;
    }

    public static void setAdapter(Context context, AppConf appConf, Activity activity){
        adapter = new StaggeredGridAdapter(context, appConf, activity);
    }


    public static int getInStockLimit() {
        return inStockLimit;
    }

    public static boolean getIsStock() {
        return isStock;
    }

    public static void setIsStock(boolean isStock) {
        AppConf.isStock = isStock;
    }

    public static boolean isStock;

    public static final int inStockLimit = 0;

    public boolean isGolbalData() {
        return golbalData;
    }

    public void setGolbalData(boolean golbalData) {
        this.golbalData = golbalData;
    }

    public boolean getGolbalData() {
        return this.golbalData;
    }

    public int getStockItems() {
        return stockItems;
    }

    public void setStockItems(int stockItems) {
        this.stockItems = stockItems;
    }

    public int getPaginationLimit() {
        return paginationLimit;
    }

    public void setPaginationLimit(int paginationLimit) {
        this.paginationLimit = paginationLimit;
    }

    public int getPaginationSkip() {
        return paginationSkip;
    }

    public void setPaginationSkip(int paginationSkip) {
        this.paginationSkip = paginationSkip;
    }

    private boolean golbalData;
    private int stockItems;
    private static int paginationLimit;
    private static int paginationSkip;



}
