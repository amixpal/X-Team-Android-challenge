package com.xteam.sample;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.xteam.sample.APIWrapper.WareHouseAPIWrapper;
import com.xteam.sample.dagger.AppConf;
import com.xteam.sample.utility.DeviceDensity;
import com.xteam.sample.utility.GridItemSpaces;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by Amit on 24-10-2015.
 */

public class MainActivity extends AppCompatActivity {

    @Inject
    AppConf mAppConf;

    Toolbar mToolbar;
    private String tags;
    private WareHouseAPIWrapper wareHouseAPIWrapper;
    private RecyclerView mRecyclerView;
    private CircleProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDelegate.getComponent(getApplicationContext()).inject(this);    // Intiating Dependecy Injection Graph
        new DeviceDensity(getApplicationContext(), mAppConf).setLimitForDevice();   // Finding Device Screen density
        wareHouseAPIWrapper = new WareHouseAPIWrapper(getApplicationContext(), MainActivity.this, mAppConf);

        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.staggering_grid);
        progressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("X-Team");
        }


        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCache(getApplicationContext());
                CircleProgressBar circle = (CircleProgressBar) findViewById(R.id.progress_bar);
                circle.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                mAppConf.setPaginationSkip(0);
                mAppConf.setIsResponseForTags(false);
                wareHouseAPIWrapper.getWarehouseDetails(progressBar, mRecyclerView, MainActivity.this);
            }
        });

        // API endpoint call using Retrofit
        wareHouseAPIWrapper.getWarehouseDetails(progressBar, mRecyclerView, MainActivity.this);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.staggering_grid);
        GridItemSpaces decoration = new GridItemSpaces(10);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Implementing Search Bar at Application Toolbar
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(onQueryTextListener()); // text changed listener
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    private SearchView.OnQueryTextListener onQueryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                CircleProgressBar circle = (CircleProgressBar) findViewById(R.id.progress_bar);
                circle.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                tags = s;
                // API endpoint call for search data using tags
                wareHouseAPIWrapper.getWareHouseWithTags(s, progressBar, mRecyclerView, MainActivity.this);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.no_result);
                relativeLayout.setVisibility(View.INVISIBLE);
                return false;
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_checkbox:
                CircleProgressBar circle = (CircleProgressBar) findViewById(R.id.progress_bar);
                circle.setVisibility(View.GONE);
                if (item.isChecked()) {
                    item.setIcon(R.drawable.ic_action_checkbox);
                    mAppConf.setGolbalData(false);
                } else {
                    item.setIcon(R.drawable.ic_action_clicked_checbox);
                    mAppConf.setGolbalData(true);
                }
                progressBar.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                if (mAppConf.getIsResponseForTags())
                    wareHouseAPIWrapper.getWareHouseWithTags(tags, progressBar, mRecyclerView, MainActivity.this);
                else
                    wareHouseAPIWrapper.getWarehouseWithInStock(progressBar, mRecyclerView, MainActivity.this);
                item.setChecked(!item.isChecked());

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy(){
        mAppConf.setGolbalData(false);
    }

    // Implementing Cache delete option when user clicks on refresh FLoating button Icon
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        else if(dir!= null && dir.isFile())
            return dir.delete();
        else {
            return false;
        }
    }

}
