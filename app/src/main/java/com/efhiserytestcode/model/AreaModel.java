package com.efhiserytestcode.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "tblOptionArea")
public class AreaModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int id;

    @SerializedName("province")
    @Expose
    String province = "";

    @SerializedName("city")
    @Expose
    String city = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
