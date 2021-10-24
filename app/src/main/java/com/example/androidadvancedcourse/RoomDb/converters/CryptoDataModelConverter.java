package com.example.androidadvancedcourse.RoomDb.converters;

import androidx.room.TypeConverter;

import com.example.androidadvancedcourse.Models.cryptolistmodel.CryptoMarketDataModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class CryptoDataModelConverter {

    @TypeConverter
    public String tojson(CryptoMarketDataModel cryptoMarketDataModel) {
        if (cryptoMarketDataModel == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CryptoMarketDataModel>() {}.getType();
        String json = gson.toJson(cryptoMarketDataModel, type);
        return json;
    }

    @TypeConverter
    public CryptoMarketDataModel toDataClass(String data) {
        if (data == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CryptoMarketDataModel>() {}.getType();
        CryptoMarketDataModel cryptoMarketDataModel = gson.fromJson(data, type);
        return cryptoMarketDataModel;
    }
}
