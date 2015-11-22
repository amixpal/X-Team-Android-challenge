package com.xteam.sample.dagger;

import com.xteam.sample.APIWrapper.WareHouseAPIWrapper;
import com.xteam.sample.MainActivity;
import com.xteam.sample.adapter.StaggeredGridAdapter;
import com.xteam.sample.utility.CustomConverter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Amit on 10/26/15.
 */

@Singleton
@Component(modules = {AppConfModule.class})
public interface AppConfComponent {

    public void inject(MainActivity mainMainActivity);

    AppConf appConf();

}
