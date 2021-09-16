package com.example.androidadvancedcourse.Hilt.Modules;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.qualifiers.ActivityContext;
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

    @Provides
    ConnectivityManager ProvideConnectivityManager(@ActivityContext Context context){
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    NetworkRequest ProvideNetworkRequest(){
        return new NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
    }

}
