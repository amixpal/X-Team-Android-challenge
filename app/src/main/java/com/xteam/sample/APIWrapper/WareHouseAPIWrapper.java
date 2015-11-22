package com.xteam.sample.APIWrapper;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.xteam.sample.MainActivity;
import com.xteam.sample.R;
import com.xteam.sample.dagger.AppConf;
import com.xteam.sample.models.Warehouse;
import com.xteam.sample.services.WarehouseAPI;
import com.xteam.sample.utility.CachingRequest;

import java.util.List;

import retrofit.RestAdapter;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Amit on 10/26/15.
 */

/*
Wrapper class: WareHouseAPIWrapper intermediator of Endpoints and Activity
 */
public class WareHouseAPIWrapper {

    private AppConf mAppConf;
    private Context mContext;
    private Activity mActivity;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    public WareHouseAPIWrapper(Context context, Activity activity, AppConf appConf) {
        this.mContext = context;
        this.mActivity = activity;
        this.mAppConf = appConf;
    }

    public WareHouseAPIWrapper(Context mContext, AppConf appConf) {
        this.mContext = mContext;
        this.mAppConf = appConf;
    }

    public void getWareHouseWithTags(String s, final CircleProgressBar progressBar, final RecyclerView mRecyclerView,
                                     final Activity mActivity) {

        CachingRequest mCachingRequestAdapter = new CachingRequest(mContext, mAppConf.BASE_URL,
                this.mAppConf);
        RestAdapter adapter = mCachingRequestAdapter.init();

        adapter.create(WarehouseAPI.class).searchWithTags(s)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Warehouse>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(this.getClass().getName(), "OnCompleted ()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(this.getClass().getName(), "Error:" + e.toString());

                    }

                    @Override
                    public void onNext(List<Warehouse> response) {
                        Log.d(this.getClass().getName(), "OnResponse ()");
                        if (response.size() == 0) {
                            RelativeLayout relativeLayout = (RelativeLayout) mActivity.findViewById(R.id.no_result);
                            progressBar.setVisibility(View.GONE);
                            relativeLayout.setVisibility(View.VISIBLE);
                        } else {
                            mAppConf.setIsResponseForTags(true);
                            progressBar.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            fillRecyclerStaggeredView(response, mRecyclerView, mAppConf);
                        }
                    }
                });
    }

    public void getWarehouseDetails(final CircleProgressBar progressBar, final RecyclerView mRecyclerView, final Activity mActivity) {

        RelativeLayout recyclerView = (RelativeLayout) mActivity.findViewById(R.id.no_result);
        recyclerView.setVisibility(View.INVISIBLE);
        CachingRequest mCachingRequestAdapter = new CachingRequest(mContext, mAppConf.BASE_URL, mAppConf);
        RestAdapter adapter = mCachingRequestAdapter.init();

        //Making request to API
        adapter.create(WarehouseAPI.class).getWarehouseWithLimit(mAppConf.getPaginationLimit())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Warehouse>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(this.getClass().getName(), "OnCompleted ()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(this.getClass().getName(), "Error:" + e.toString());
                    }

                    @Override
                    public void onNext(List<Warehouse> response) {
                        Log.d(this.getClass().getName(), "OnResponse ()");
                        progressBar.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        fillRecyclerStaggeredView(response, mRecyclerView, mAppConf);
                    }

                });
    }

    public void getWareHouseWithNextPagination(final AppConf mAppConf,
                                               final CircleProgressBar progressBar, final Activity mActivity) {
        {
            CachingRequest mCachingRequestAdapter = new CachingRequest(mContext, mAppConf.BASE_URL, mAppConf);
            RestAdapter adapter = mCachingRequestAdapter.init();
            RelativeLayout recyclerView = (RelativeLayout) mActivity.findViewById(R.id.no_result);
            recyclerView.setVisibility(View.INVISIBLE);
            //Making request to API
            adapter.create(WarehouseAPI.class).getWarehouseWithSkip(mAppConf.getPaginationSkip())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Warehouse>>() {
                        @Override
                        public void onCompleted() {
                            Log.d(this.getClass().getName(), "OnCompleted ()");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(this.getClass().getName(), "Error:" + e.toString());
                        }

                        @Override
                        public void onNext(List<Warehouse> response) {
                            Log.d(this.getClass().getName(), "OnResponse ()");
                            if (response.size() != 0) {
                                if (mAppConf.getPaginationSkip() == 0)
                                    mAppConf.setPaginationSkip(mAppConf.getPaginationLimit());
                                if (!mAppConf.getIsStock())
                                    mAppConf.setPaginationSkip(response.size() + mAppConf.getPaginationSkip());
                                mAppConf.getAdapter().addItems(response);
                                mAppConf.getAdapter().notifyDataSetChanged();

                            }
                            CircleProgressBar circle = (CircleProgressBar) mActivity.findViewById(R.id.progress_bar);
                            circle.setVisibility(View.GONE);
                        }

                    });
        }
    }


    public void getWarehouseWithInStock(final CircleProgressBar progressBar, final RecyclerView mRecyclerView, final MainActivity mainActivity) {
        int limitSet = mAppConf.getPaginationSkip();
        CachingRequest mCachingRequestAdapter = new CachingRequest(mContext, mAppConf.BASE_URL, mAppConf);
        RestAdapter adapter = mCachingRequestAdapter.init();
        RelativeLayout recyclerView = (RelativeLayout) mActivity.findViewById(R.id.no_result);
        recyclerView.setVisibility(View.INVISIBLE);
        if(mAppConf.getPaginationSkip()==0)
            limitSet = mAppConf.getPaginationLimit();

        //Making request to API
        adapter.create(WarehouseAPI.class).getWarehouseWithLimit(limitSet)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Warehouse>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(this.getClass().getName(), "OnCompleted ()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(this.getClass().getName(), "OnError ()");
                    }

                    @Override
                    public void onNext(List<Warehouse> response) {
                        Log.d(this.getClass().getName(), "OnResponse ()");
                        progressBar.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        fillRecyclerStaggeredView(response, mRecyclerView, mAppConf);
                    }
                });
    }

    public void fillRecyclerStaggeredView(final List<Warehouse> response, RecyclerView mRecyclerView, final AppConf mAppConf) {
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mAppConf.setAdapter(mContext, mAppConf, mActivity);
        mAppConf.getAdapter().setAdapter(this.mAppConf.getAdapter());
        mAppConf.getAdapter().addItems(response);
        mRecyclerView.setAdapter(this.mAppConf.getAdapter());

    }
}

