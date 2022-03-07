package com.efhiserytestcode.database.optionSize;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.efhiserytestcode.model.AreaModel;
import com.efhiserytestcode.model.SizeModel;

import java.util.List;

@Dao
public interface OptionSizeDao {

    @Query("delete from sqlite_sequence where name='tblOptionSize'")
    void resetAI();

    @Query("SELECT * FROM tblOptionSize")
    List<SizeModel> getDataAll();

    @Insert
    void insertData(SizeModel... sizeModels);

    @Query("DELETE FROM tblOptionSize")
    void deleteAllData();

}
