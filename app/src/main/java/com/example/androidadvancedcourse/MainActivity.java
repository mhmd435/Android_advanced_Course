package com.example.androidadvancedcourse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.PopupMenu;

import com.example.androidadvancedcourse.Models.cryptolistmodel.AllMarketModel;
import com.example.androidadvancedcourse.databinding.ActivityMainBinding;
import com.example.androidadvancedcourse.viewmodels.AppViewmodel;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.ibrahimsn.lib.OnItemReselectedListener;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Inject
    ConnectivityManager connectivityManager;

    @Inject
    NetworkRequest networkRequest;

    ActivityMainBinding activityMainBinding;

    NavHostFragment navHostFragment;
    NavController navController;

    AppBarConfiguration appBarConfiguration;
    AppViewmodel appViewModel;

    public DrawerLayout drawerLayout;
    CompositeDisposable compositeDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        drawerLayout = activityMainBinding.drawerlayout;

        compositeDisposable = new CompositeDisposable();
        setupViewModel();
        setupNavigationComponent();

        CheckConnection();


    }

    private void CheckConnection() {
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback(){
            @Override
            public void onAvailable(@androidx.annotation.NonNull Network network) {
                Log.e("TAG", "onAvailable: ");
                CallListApiRequest();
            }

            @Override
            public void onLost(@androidx.annotation.NonNull Network network) {
                Log.e("TAG", "onLost: ");
                Snackbar.make(activityMainBinding.mainCon,"Internet connection lost",2000).show();
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        }else{
            connectivityManager.registerNetworkCallback(networkRequest,networkCallback);
        }
    }

    private void setupViewModel() {
        appViewModel = new ViewModelProvider(this).get(AppViewmodel.class);
    }

    private void CallListApiRequest() {
        Observable.interval(20, TimeUnit.SECONDS)
                .flatMap(n -> appViewModel.MarketFutureCall().get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AllMarketModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull AllMarketModel allMarketModel) {
                        Log.e("TAG", "onNext: " + allMarketModel.getRootData().getCryptoCurrencyList().get(0).getName());
                        Log.e("TAG", "onNext: " + allMarketModel.getRootData().getCryptoCurrencyList().get(1).getName());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "onComplete: ");
                    }
                });
    }

    private void setupNavigationComponent() {
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment,R.id.marketFragment,R.id.watchlistfragment)
                .setOpenableLayout(activityMainBinding.drawerlayout)
                .build();

        NavigationUI.setupWithNavController(activityMainBinding.navigationView,navController);


        setupSmoothBottomMenu();
    }

    private void setupSmoothBottomMenu() {
        PopupMenu popupMenu = new PopupMenu(this,null);
        popupMenu.inflate(R.menu.bottom_navigation_menu);
        Menu menu = popupMenu.getMenu();

        activityMainBinding.bottomNavigation.setupWithNavController(menu,navController);
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}