package com.xteam.sample.dagger;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Amit on 10/26/15.
 */

@Module
public class AppConfModule {

    @Provides
    AppConf provideAppConf() {
        return new AppConf();
    }

}
