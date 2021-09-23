package com.example.androidadvancedcourse.HomeFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.androidadvancedcourse.HomeFragment.adapters.TopCoinRvAdapter;
import com.example.androidadvancedcourse.HomeFragment.adapters.TopGainLosersAdapter;
import com.example.androidadvancedcourse.Models.cryptolistmodel.AllMarketModel;
import com.example.androidadvancedcourse.Models.cryptolistmodel.DataItem;
import com.example.androidadvancedcourse.RoomDb.Entites.MarketListEntity;
import com.example.androidadvancedcourse.viewmodels.AppViewmodel;
import com.example.androidadvancedcourse.HomeFragment.adapters.sliderImageAdapter;
import com.example.androidadvancedcourse.MainActivity;
import com.example.androidadvancedcourse.R;
import com.example.androidadvancedcourse.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    FragmentHomeBinding fragmentHomeBinding;
    MainActivity mainActivity;
    AppViewmodel appViewModel;

    @Inject
    @Named("myname")
    String name;

    public List<String> top_wants = Arrays.asList("BTC","ETH","BNB","ADA","XRP","DOGE","DOT","UNI","LTC","LINK");
    TopCoinRvAdapter topCoinRvAdapter;
    CompositeDisposable compositeDisposable;

    TopGainLosersAdapter topGainLosersAdapter;



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

        fragmentHomeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewmodel.class);
        compositeDisposable = new CompositeDisposable();

        setupViewPager2();
        getAllMarketDataFromDb();
        setupTablayoutTopGainLose(fragmentHomeBinding.topGainIndicator,fragmentHomeBinding.topLoseIndicator);



        // Inflate the layout for this fragment
        return fragmentHomeBinding.getRoot();
    }

    private void setupTablayoutTopGainLose(View topGainIndicator, View topLoseIndicator){
        topGainLosersAdapter = new TopGainLosersAdapter(this);
        fragmentHomeBinding.viewPager2.setAdapter(topGainLosersAdapter);

        Animation gainAnimIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_from_left);
        Animation gainAnimOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_out_left);
        Animation loseAnimIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_from_right);
        Animation loseAnimOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_out_right);

        fragmentHomeBinding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == 0){
                    topLoseIndicator.startAnimation(loseAnimOut);
                    topLoseIndicator.setVisibility(View.GONE);
                    topGainIndicator.setVisibility(View.VISIBLE);
                    topGainIndicator.startAnimation(gainAnimIn);

                }else {
                    topGainIndicator.startAnimation(gainAnimOut);
                    topGainIndicator.setVisibility(View.GONE);
                    topLoseIndicator.setVisibility(View.VISIBLE);
                    topLoseIndicator.startAnimation(loseAnimIn);
                }
            }
        });

        new TabLayoutMediator(fragmentHomeBinding.tablayout, fragmentHomeBinding.viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0){
                    tab.setText("TopGainers");
                }else {
                    tab.setText("TopLosers");
                }
            }
        }).attach();

    }

    private void setupViewPager2() {
        appViewModel.getMutableLiveData().observe((LifecycleOwner) getActivity(), new Observer<ArrayList<Integer>>() {
            @Override
            public void onChanged(ArrayList<Integer> pics) {
                fragmentHomeBinding.viewPagerImageSlider.setAdapter(new sliderImageAdapter(pics));
                fragmentHomeBinding.viewPagerImageSlider.setOffscreenPageLimit(3);
//                fragmentHomeBinding.viewPagerImageSlider.setVisibility(View.VISIBLE);
            }
        });

    }

    private void setupToolbar(View view){
        NavController navController = Navigation.findNavController(view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment)
                .setOpenableLayout(mainActivity.drawerLayout)
                .build();

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.homeFragment){
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_sort_24);
                    toolbar.setTitle("CryptoBs");
                }
            }
        });


    }

    private void getAllMarketDataFromDb(){
        Disposable disposable = appViewModel.getAllMarketData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MarketListEntity>() {
                    @Override
                    public void accept(MarketListEntity marketListEntity) throws Throwable {
                        AllMarketModel allMarketModel = marketListEntity.getAllMarketModel();

                        ArrayList<DataItem> top10 = new ArrayList<>();
                        for (int i = 0; i < allMarketModel.getRootData().getCryptoCurrencyList().size(); i++) {
                            for (int j = 0; j < top_wants.size(); j++) {
                                String coin_name = top_wants.get(j);
                                if (allMarketModel.getRootData().getCryptoCurrencyList().get(i).getSymbol().equals(coin_name)) {
                                    DataItem dataItem = allMarketModel.getRootData().getCryptoCurrencyList().get(i);
                                    top10.add(dataItem);
                                }
                            }
                        }


                        if (fragmentHomeBinding.TopCoinRv.getAdapter() != null) {
                            topCoinRvAdapter = (TopCoinRvAdapter) fragmentHomeBinding.TopCoinRv.getAdapter();
                            topCoinRvAdapter.updateData(top10);
                        } else {
                            topCoinRvAdapter = new TopCoinRvAdapter(top10);
                            fragmentHomeBinding.TopCoinRv.setAdapter(topCoinRvAdapter);
                        }
                    }
                });
        compositeDisposable.add(disposable);

    }
}