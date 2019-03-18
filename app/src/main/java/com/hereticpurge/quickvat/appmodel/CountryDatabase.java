package com.hereticpurge.quickvat.appmodel;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.hereticpurge.quickvat.database.CountryDao;

@Database(entities = {CountryObject.class}, version = 1)
public abstract class CountryDatabase extends RoomDatabase{

    private static final String DATABASE_NAME = "quick_vat_database";

    private static CountryDatabase countryDatabase;

    public abstract CountryDao countryDao();

    public static CountryDatabase getCountryDatabase(Context context) {
        if (countryDatabase == null) {
            countryDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    CountryDatabase.class,
                    DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build();
        }

        return countryDatabase;

    }

    public static void destroyInstance() {
        countryDatabase = null;
    }

}
