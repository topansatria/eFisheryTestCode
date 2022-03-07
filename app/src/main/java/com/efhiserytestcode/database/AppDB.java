package com.efhiserytestcode.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.efhiserytestcode.database.list.ListDao;
import com.efhiserytestcode.database.optionArea.OptionAreaDao;
import com.efhiserytestcode.database.optionSize.OptionSizeDao;
import com.efhiserytestcode.model.AreaModel;
import com.efhiserytestcode.model.ListModel;
import com.efhiserytestcode.model.SizeModel;

@Database(
        entities = {
                ListModel.class,
                AreaModel.class,
                SizeModel.class
        }, version = 1, exportSchema = false)
public abstract class AppDB extends RoomDatabase {

        // List
        public abstract ListDao listDao();

        // List Area
        public abstract OptionAreaDao optionAreaDao();

        // List Size
        public abstract OptionSizeDao optionSizeDao();

}
