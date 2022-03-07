package com.efhiserytestcode.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "tblList")
public class ListModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int id;

    @SerializedName("uuid")
    @Expose
    String uuid = "";

    @SerializedName("komoditas")
    @Expose
    String komoditas = "";

    @SerializedName("area_provinsi")
    @Expose
    String areaProvinsi = "";

    @SerializedName("area_kota")
    @Expose
    String areaKota = "";

    @SerializedName("size")
    @Expose
    String size = "";

    @SerializedName("price")
    @Expose
    String price = "";

    @SerializedName("tgl_parsed")
    @Expose
    String tglParsed = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getKomoditas() {
        return komoditas;
    }

    public void setKomoditas(String komoditas) {
        this.komoditas = komoditas;
    }

    public String getAreaProvinsi() {
        return areaProvinsi;
    }

    public void setAreaProvinsi(String areaProvinsi) {
        this.areaProvinsi = areaProvinsi;
    }

    public String getAreaKota() {
        return areaKota;
    }

    public void setAreaKota(String areaKota) {
        this.areaKota = areaKota;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTglParsed() {
        return tglParsed;
    }

    public void setTglParsed(String tglParsed) {
        this.tglParsed = tglParsed;
    }
}
