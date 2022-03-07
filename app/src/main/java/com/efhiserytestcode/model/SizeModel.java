package com.efhiserytestcode.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "tblOptionSize")
public class SizeModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int id;

    @SerializedName("size")
    @Expose
    String size = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
