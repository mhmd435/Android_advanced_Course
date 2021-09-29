package com.example.androidadvancedcourse.MarketFragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidadvancedcourse.MainActivity;
import com.example.androidadvancedcourse.Models.cryptolistmodel.AllMarketModel;
import com.example.androidadvancedcourse.Models.cryptolistmodel.DataItem;
import com.example.androidadvancedcourse.R;
import com.example.androidadvancedcourse.RoomDb.Entites.MarketListEntity;
import com.example.androidadvancedcourse.databinding.FragmentMarketBinding;
import com.example.androidadvancedcourse.viewmodels.AppViewmodel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MarketFragment extends Fragment {


    FragmentMarketBinding fragmentMarketBinding;
    MainActivity mainActivity;
    CollapsingToolbarLayout collapsingToolbarLayout;

    AppViewmodel appViewmodel;

    List<DataItem> dataItemList;
    marketRV_Adapter marketRvAdapter;
    CompositeDisposable compositeDisposable;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar(view);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentMarketBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_market,container,false);
        compositeDisposable = new CompositeDisposable();

        setupViewModel();
        getMarketListDataFromDb();


        return fragmentMarketBinding.getRoot();
    }

    private void getMarketListDataFromDb() {
        Disposable disposable = appViewmodel.getAllMarketData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MarketListEntity>() {
                    @Override
                    public void accept(MarketListEntity marketListEntity) throws Throwable {
                        AllMarketModel allMarketModel = marketListEntity.getAllMarketModel();
                        dataItemList = allMarketModel.getRootData().getCryptoCurrencyList();

                        if (fragmentMarketBinding.marketRv.getAdapter() == null){
                            marketRvAdapter = new marketRV_Adapter((ArrayList<DataItem>) dataItemList);
                            fragmentMarketBinding.marketRv.setAdapter(marketRvAdapter);
                        }else {
                            marketRvAdapter = (marketRV_Adapter) fragmentMarketBinding.marketRv.getAdapter();
                            marketRvAdapter.updateData((ArrayList<DataItem>) dataItemList);
                        }


                    }
                });
        compositeDisposable.add(disposable);
    }

    private void setupViewModel() {
        appViewmodel = new ViewModelProvider(requireActivity()).get(AppViewmodel.class);
    }

    private void setupToolbar(View view){
        NavController navController = Navigation.findNavController(view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.marketFragment)
                .setOpenableLayout(mainActivity.drawerLayout)
                .build();

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_market_tb);



        NavigationUI.setupWithNavController(collapsingToolbarLayout,toolbar,navController,appBarConfiguration);


        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.marketFragment){
                    collapsingToolbarLayout.setTitleEnabled(false);
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_sort_24);
                    toolbar.setTitle("Market");
                    toolbar.setTitleTextColor(Color.WHITE);
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}