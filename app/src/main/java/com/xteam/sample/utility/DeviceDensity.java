package com.xteam.sample.utility;

import android.content.Context;
import android.util.DisplayMetrics;

import com.xteam.sample.dagger.AppConf;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Amit on 10/28/15.
 */

/*
Set the limit for grid view as per Device density
 */
public class DeviceDensity {

    private Context mContext;
    private AppConf mAppConf;

    public DeviceDensity (Context context, AppConf appConf){
        this.mContext = context;
        this.mAppConf = appConf;
    }

    public void setLimitForDevice() {

        switch (mContext.getResources().getDisplayMetrics().densityDpi) {

            case DisplayMetrics.DENSITY_LOW:
                mAppConf.setPaginationLimit(20);
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                mAppConf.setPaginationLimit(20);
                break;
            case DisplayMetrics.DENSITY_HIGH:
                mAppConf.setPaginationLimit(20);
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                mAppConf.setPaginationLimit(26);
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                mAppConf.setPaginationLimit(26);
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                mAppConf.setPaginationLimit(26);
                break;
            case DisplayMetrics.DENSITY_TV:
                mAppConf.setPaginationLimit(50);
            case DisplayMetrics.DENSITY_280:
            default:
                mAppConf.setPaginationLimit(25);
        }
    }
}
