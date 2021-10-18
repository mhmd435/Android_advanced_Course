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
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.example.androidadvancedcourse.Models.cryptolistmodel.AllMarketModel;
import com.example.androidadvancedcourse.Models.cryptolistmodel.CryptoMarketDataModel;
import com.example.androidadvancedcourse.databinding.ActivityMainBinding;
import com.example.androidadvancedcourse.viewmodels.AppViewmodel;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.ibrahimsn.lib.OnItemReselectedListener;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    DateTimeFormatter dtf;
    LocalDateTime now;

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

        setuptime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("time", "oncreate time: " + dtf.format(LocalDateTime.now()) );
        }
        drawerLayout = activityMainBinding.drawerlayout;

        compositeDisposable = new CompositeDisposable();
        setupViewModel();
        setupNavigationComponent();

        CheckConnection();


    }

    private void setuptime() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        }
    }

    private void CheckConnection() {
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback(){
            @Override
            public void onAvailable(@androidx.annotation.NonNull Network network) {
                Log.e("TAG", "onAvailable: ");
                CallListApiRequest();
                CallCryptoMarketApiRequest();
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

    private void CallCryptoMarketApiRequest() {
        Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    Document pageSrc = Jsoup.connect("https://coinmarketcap.com/").get();

                    // Scraping Market Data like (marketCap,Dominance,...)
                    Elements ScrapeMarketData= pageSrc.getElementsByClass("cmc-link");
                    //for spliting BTC and ETH dominance in txt
                    String[] dominance_txt = ScrapeMarketData.get(4).text().split(" ");

                    // Scraping Market number of changes like (MarketcapChange,volumeChange,...)
                    Elements ScrapeMarketChange = pageSrc.getElementsByClass("sc-27sy12-0 gLZJFn");
                    String[] changePercent = ScrapeMarketChange.text().split(" ");

                    // Scraping All span Tag
                    Elements ScrapeChangeIcon = pageSrc.getElementsByTag("span");

                    // get all span Tag wth Icon (class= caretUp and caretDown)
                    ArrayList<String> IconList = new ArrayList();
                    for (Element i : ScrapeChangeIcon){
                        if (i.hasClass("icon-Caret-down") || i.hasClass("icon-Caret-up")){
                            IconList.add(i.attr("class"));
                        }
                    }

                    // matching - or + element of PercentChanges
                    ArrayList<String> finalchangePercent = new ArrayList<>();
                    for (int i = 0;i < 3;i++){
                        if (IconList.get(i).equals("icon-Caret-up")){
                            finalchangePercent.add(changePercent[i]);
                        }else{
                            finalchangePercent.add("-" + changePercent[i]);
                        }
                    }

                    // initialize all data
                    String Cryptos = ScrapeMarketData.get(0).text();
                    String Exchanges = ScrapeMarketData.get(1).text();
                    String MarketCap = ScrapeMarketData.get(2).text();
                    String Vol_24h = ScrapeMarketData.get(3).text();

                    String BTC_Dominance = dominance_txt[1];
                    String ETH_Dominance = dominance_txt[3];

                    String MarketCap_change = finalchangePercent.get(0);
                    String vol_change = finalchangePercent.get(1);
                    String BTCD_change = finalchangePercent.get(2);

                    CryptoMarketDataModel cryptoMarketDataModel = new CryptoMarketDataModel(Cryptos,Exchanges,MarketCap,Vol_24h,BTC_Dominance,ETH_Dominance,MarketCap_change,vol_change,BTCD_change);
                    // insert model class to RoomDatabase
                    appViewModel.insertCryptoDataMarket(cryptoMarketDataModel);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "onComplete: jsoup done");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("TAG", "onError:"+ e.getMessage());
                    }
                });
    }

    private void setupViewModel() {
        appViewModel = new ViewModelProvider(this).get(AppViewmodel.class);
    }

    private void CallListApiRequest() {
        Observable.interval(20, TimeUnit.SECONDS)
                .flatMap(n -> appViewModel.MarketFutureCall())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AllMarketModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Log.e("time", "onSubscribe time: " + dtf.format(LocalDateTime.now()) );
                        }
                    }

                    @Override
                    public void onNext(@NonNull AllMarketModel allMarketModel) {
//                        Log.e("TAG", "onNext: " + allMarketModel.getRootData().getCryptoCurrencyList().get(0).getName());
//                        Log.e("TAG", "onNext: " + allMarketModel.getRootData().getCryptoCurrencyList().get(1).getName());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Log.e("time", "onNext time: " + dtf.format(LocalDateTime.now()) );
                        }
                        appViewModel.insertAllMarket(allMarketModel);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Log.e("time", "onComplete time: " + dtf.format(LocalDateTime.now()) );
                        }
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

        activityMainBinding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem item) {
                if (item.getItemId() == R.id.exit){
                    finish();
                }else {
                    NavigationUI.onNavDestinationSelected(item, navController);
                    activityMainBinding.drawerlayout.closeDrawers();
                }
                return false;
            }
        });


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