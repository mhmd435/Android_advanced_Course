package com.example.androidadvancedcourse.RoomDb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.androidadvancedcourse.RoomDb.Entites.MarketListEntity;
import com.example.androidadvancedcourse.RoomDb.converters.AllMarketModelConverter;

@TypeConverters({AllMarketModelConverter.class})
@Database(entities = {MarketListEntity.class},version = 1)
public abstract class AppDataBase extends RoomDatabase {

    private static final String Db_Name = "AppDb";
    private static AppDataBase instance;
    public abstract RoomDao roomDao();

    public static synchronized AppDataBase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,Db_Name)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
