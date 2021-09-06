package com.example.androidadvancedcourse.Hilt.Modules;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(ActivityComponent.class)
public class HiltAppModule {

    @Provides
    @Named("besenior")
    String ProvideName(){
        return "besenior academy";
    }

    @Provides
    @Named("myname")
    String Providelastname(){
        return "aghajani";
    }

}
