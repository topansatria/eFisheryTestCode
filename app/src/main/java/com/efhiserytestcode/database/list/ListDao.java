package com.efhiserytestcode.database.list;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.efhiserytestcode.model.ListModel;

import java.util.List;

@Dao
public interface ListDao {

    @Query("delete from sqlite_sequence where name='tblList'")
    void resetAI();

    @Query("SELECT * FROM tblList ORDER BY id DESC")
    List<ListModel> getDataAll();

    @Query("SELECT * FROM tblList WHERE komoditas LIKE :komoditas ORDER BY id DESC")
    List<ListModel> getDataAllWhereKomoditas(String komoditas);

    @Query("SELECT * FROM tblList WHERE areaProvinsi LIKE :areaProvinsi ORDER BY id DESC")
    List<ListModel> getDataAllWhereProv(String areaProvinsi);

    @Query("SELECT * FROM tblList WHERE komoditas LIKE :areaKota ORDER BY id DESC")
    List<ListModel> getDataAllWhereCity(String areaKota);

    @Insert
    void insertData(ListModel... listModels);

    @Query("DELETE FROM tblList")
    void deleteAllData();

}
