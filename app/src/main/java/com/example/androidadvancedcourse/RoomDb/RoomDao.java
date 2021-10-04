package com.example.androidadvancedcourse.RoomDb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.androidadvancedcourse.RoomDb.Entites.MarketDataEntity;
import com.example.androidadvancedcourse.RoomDb.Entites.MarketListEntity;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MarketListEntity marketListEntity);

    @Query("SELECT * FROM AllMarket")
    Flowable<MarketListEntity> getAllMarketData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MarketDataEntity marketDataEntity);

    @Query("SELECT * FROM CryptoData")
    Flowable<MarketDataEntity> getCryptoMarketData();

}
