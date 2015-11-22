package com.xteam.sample.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.xteam.sample.APIWrapper.WareHouseAPIWrapper;
import com.xteam.sample.R;
import com.xteam.sample.WareHouseDetailActivity;
import com.xteam.sample.dagger.AppConf;
import com.xteam.sample.models.Warehouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amit on 26-10-2015.
 */
public class StaggeredGridAdapter extends RecyclerView.Adapter<StaggeredGridAdapter.StaggeredGridView> {

    int size;
    private AppConf mAppConf;
    private Context context;
    private  Activity mActivity;
    private String INTENT_VALUE = "warehouse";
    private static StaggeredGridAdapter mAdapter;
    private List<Warehouse> warehouses = new ArrayList<Warehouse>();

    public StaggeredGridAdapter(Context context, AppConf appConf, Activity activity) {

        this.context = context;
        this.mAppConf = appConf;
        this.mActivity = activity;
    }

    public void addItems(List<Warehouse> response) {
        size = warehouses.size() + response.size();
        warehouses.addAll(response);

    }

    public void setAdapter(StaggeredGridAdapter mAdapter){
        this.mAdapter = mAdapter;
    }

    public StaggeredGridAdapter getAdapter(){
        return this.mAdapter;
    }

    @Override
    public StaggeredGridView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        StaggeredGridView staggeredGridView = new StaggeredGridView(layoutView);
        return staggeredGridView;
    }

    @Override
    public void onBindViewHolder(StaggeredGridView holder, int position) {
        int limit=0;
        CircleProgressBar circle = (CircleProgressBar) mActivity.findViewById(R.id.progress_bar);
        WareHouseAPIWrapper wareHouseAPIWrapper = new WareHouseAPIWrapper(this.context, mAppConf);
        holder.textView.setText(warehouses.get(position).getFace());
        // Appending New Items on the basis of position in StaggeredGridView
        if (position == warehouses.size() -1 && !(mAppConf.isGolbalData()) && (!mAppConf.getIsResponseForTags())){
            circle.setVisibility(View.VISIBLE);
            circle.setShowArrow(true);
            wareHouseAPIWrapper.getWareHouseWithNextPagination(mAppConf, null, mActivity);
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    class StaggeredGridView extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        public StaggeredGridView(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.img_name);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, WareHouseDetailActivity.class);
            intent.putExtra(INTENT_VALUE, warehouses.get(getAdapterPosition()));
            v.getContext().startActivity(intent);
        }
    }

}