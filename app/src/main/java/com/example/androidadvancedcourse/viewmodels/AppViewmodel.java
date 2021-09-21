package com.example.androidadvancedcourse.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidadvancedcourse.AppRepository;
import com.example.androidadvancedcourse.Models.cryptolistmodel.AllMarketModel;
import com.example.androidadvancedcourse.R;
import com.example.androidadvancedcourse.RoomDb.Entites.MarketListEntity;

import java.util.ArrayList;
import java.util.concurrent.Future;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

@HiltViewModel
public class AppViewmodel extends AndroidViewModel {

    MutableLiveData<ArrayList<Integer>> mutableLiveData = new MutableLiveData<>();

    @Inject
    AppRepository appRepository;

    @Inject
    public AppViewmodel(@NonNull Application application) {
        super(application);
        getViewPagerData();

    }

    public Future<Observable<AllMarketModel>> MarketFutureCall(){
        return appRepository.marketListFutureCall();
    }

    MutableLiveData<ArrayList<Integer>> getViewPagerData(){
        ArrayList<Integer> pics = new ArrayList();
        pics.add(R.drawable.p1);
        pics.add(R.drawable.p2);
        pics.add(R.drawable.p3);
        pics.add(R.drawable.p4);
        pics.add(R.drawable.p5);

        mutableLiveData.postValue(pics);

        return mutableLiveData;
    }

    public MutableLiveData<ArrayList<Integer>> getMutableLiveData() {
        return mutableLiveData;
    }

    public void insertAllMarket(AllMarketModel allMarketModel){
        appRepository.InsertAllMarket(allMarketModel);
    }

    public Flowable<MarketListEntity> getAllMarketData(){
        return appRepository.getAllMarketData();
    }


}
