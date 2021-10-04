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
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidadvancedcourse.MainActivity;
import com.example.androidadvancedcourse.Models.cryptolistmodel.AllMarketModel;
import com.example.androidadvancedcourse.Models.cryptolistmodel.CryptoMarketDataModel;
import com.example.androidadvancedcourse.Models.cryptolistmodel.DataItem;
import com.example.androidadvancedcourse.R;
import com.example.androidadvancedcourse.RoomDb.Entites.MarketDataEntity;
import com.example.androidadvancedcourse.RoomDb.Entites.MarketListEntity;
import com.example.androidadvancedcourse.databinding.FragmentMarketBinding;
import com.example.androidadvancedcourse.viewmodels.AppViewmodel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    ArrayList<DataItem> filteredList;
    ArrayList<DataItem> UpdatedDataList;


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

        UpdatedDataList = new ArrayList<>();
        filteredList = new ArrayList<>();

        setupSearchBox();
        setupViewModel();
        getMarketListDataFromDb();
        getCryptoMarketDataFromDb();


        return fragmentMarketBinding.getRoot();
    }

    private void setupSearchBox(){
        fragmentMarketBinding.searchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void filter(String name){
        filteredList.clear();
        for (DataItem item : dataItemList){
            if (item.getSymbol().toLowerCase().contains(name.toLowerCase()) || item.getName().toLowerCase().contains(name.toLowerCase())){
                filteredList.add(item);
            }
        }

        marketRvAdapter.updateData(filteredList);
        marketRvAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }

            void checkEmpty(){
                if (marketRvAdapter.getItemCount() == 0){
                    fragmentMarketBinding.itemnotFoundTxt.setVisibility(View.VISIBLE);
                }else {
                    fragmentMarketBinding.itemnotFoundTxt.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getCryptoMarketDataFromDb() {
        Disposable disposable = appViewmodel.getCryptoMarketData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MarketDataEntity>() {
                    @Override
                    public void accept(MarketDataEntity roomMarketEntity) throws Throwable {
                        CryptoMarketDataModel cryptoMarketDataModel = roomMarketEntity.getCryptoMarketDataModel();

                        //set BTC.D on UI
                        fragmentMarketBinding.CryptoBTCD.setText(cryptoMarketDataModel.getBtc_dominance());
                        String[] str3 = cryptoMarketDataModel.getBtcd_change().split("%");
                        if (Float.parseFloat(str3[0]) > 0){
                            fragmentMarketBinding.BTCDIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24);
                            fragmentMarketBinding.BTCChange.setTextColor(Color.GREEN);
                        }else if (Float.parseFloat(str3[0]) < 0){
                            fragmentMarketBinding.BTCDIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24);
                            fragmentMarketBinding.BTCChange.setTextColor(Color.RED);
                        }else {
                            fragmentMarketBinding.BTCDIcon.setBackgroundResource(R.drawable.ic_baseline_horizontal_rule_24);
                            fragmentMarketBinding.BTCChange.setTextColor(Color.WHITE);
                        }
                        fragmentMarketBinding.BTCChange.setText(cryptoMarketDataModel.getBtcd_change());


                        //set market cap  on UI
                        fragmentMarketBinding.CryptoMarketCap.setText(cryptoMarketDataModel.getMarketCap());
                        // get marketcap without %
                        String[] str = cryptoMarketDataModel.getMarketCap_change().split("%");
                        Log.e("TAG", "accept: " + str[0]);
                        if (Float.parseFloat(str[0]) > 0){
                            fragmentMarketBinding.marketcapIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24);
                            fragmentMarketBinding.MarketCapChange.setTextColor(Color.GREEN);
                        }else if (Float.parseFloat(str[0]) < 0){
                            fragmentMarketBinding.marketcapIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24);
                            fragmentMarketBinding.MarketCapChange.setTextColor(Color.RED);
                        }else {
                            fragmentMarketBinding.marketcapIcon.setBackgroundResource(R.drawable.ic_baseline_horizontal_rule_24);
                            fragmentMarketBinding.MarketCapChange.setTextColor(Color.WHITE);
                        }
                        fragmentMarketBinding.MarketCapChange.setText(cryptoMarketDataModel.getMarketCap_change());


                        //set market Vol on UI
                        fragmentMarketBinding.CryptoVolume.setText(cryptoMarketDataModel.getVol_24h());
                        //get VolumeChange without %
                        String[] str2 = cryptoMarketDataModel.getVol_change().split("%");
                        if (Float.parseFloat(str2[0]) > 0){
                            fragmentMarketBinding.VolumeIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24);
                            fragmentMarketBinding.VolumeChange.setTextColor(Color.GREEN);
                        }else if (Float.parseFloat(str2[0]) < 0){
                            fragmentMarketBinding.VolumeIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24);
                            fragmentMarketBinding.VolumeChange.setTextColor(Color.RED);
                        }else {
                            fragmentMarketBinding.VolumeIcon.setBackgroundResource(R.drawable.ic_baseline_horizontal_rule_24);
                            fragmentMarketBinding.VolumeChange.setTextColor(Color.WHITE);
                        }
                        fragmentMarketBinding.VolumeChange.setText(cryptoMarketDataModel.getVol_change());




                    }
                });
        compositeDisposable.add(disposable);
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

                            if (filteredList.isEmpty() || filteredList.size() == 1000){
                                marketRvAdapter.updateData((ArrayList<DataItem>) dataItemList);
                            }else {
                                //get All new Data when user searching and filtering
                                marketRvAdapter.updateData((ArrayList<DataItem>) filteredList);
                            }
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