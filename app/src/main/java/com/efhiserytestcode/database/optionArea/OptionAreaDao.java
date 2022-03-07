package com.efhiserytestcode.database.optionArea;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.efhiserytestcode.model.AreaModel;
import com.efhiserytestcode.model.ListModel;

import java.util.List;

@Dao
public interface OptionAreaDao {

    @Query("delete from sqlite_sequence where name='tblOptionArea'")
    void resetAI();

    @Query("SELECT * FROM tblOptionArea")
    List<AreaModel> getDataAll();

    @Insert
    void insertData(AreaModel... areaModels);

    @Query("DELETE FROM tblOptionArea")
    void deleteAllData();

}
