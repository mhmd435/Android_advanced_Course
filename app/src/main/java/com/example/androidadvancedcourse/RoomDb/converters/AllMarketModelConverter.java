package com.example.androidadvancedcourse.RoomDb.converters;

import androidx.room.TypeConverter;

import com.example.androidadvancedcourse.Models.cryptolistmodel.AllMarketModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class AllMarketModelConverter {

    @TypeConverter
    public String tojson(AllMarketModel allMarketModel) {
        if (allMarketModel == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<AllMarketModel>() {}.getType();
        String json = gson.toJson(allMarketModel, type);
        return json;
    }

    @TypeConverter
    public AllMarketModel toDataClass(String allMarket) {
        if (allMarket == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<AllMarketModel>() {}.getType();
        AllMarketModel allMarketModel = gson.fromJson(allMarket, type);
        return allMarketModel;
    }
}
