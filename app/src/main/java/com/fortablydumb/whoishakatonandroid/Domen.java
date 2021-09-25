package com.fortablydumb.whoishakatonandroid;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

@Entity
public class Domen {
    @PrimaryKey
    @ColumnInfo(name = "naziv")
    @NonNull
    private String naziv;

    @ColumnInfo(name = "omiljeni")
    private Boolean omiljeni;

    @ColumnInfo(name = "poslednji_put_pretrazivan")
    private Date poslednjiPutPretrazivan;

    @ColumnInfo(name = "raw_data")
    private String rawData;

    public Domen(@NonNull String naziv, Boolean omiljeni, Date poslednjiPutPretrazivan,String rawData) {
        this.naziv = naziv;
        this.omiljeni = omiljeni;
        this.poslednjiPutPretrazivan = poslednjiPutPretrazivan;
        this.rawData = rawData;
    }

    public void setNaziv(@NonNull String naziv) {
        this.naziv = naziv;
    }

    public void setOmiljeni(Boolean omiljeni) {
        this.omiljeni = omiljeni;
    }

    public void setPoslednjiPutPretrazivan(Date poslednjiPutPretrazivan) {
        this.poslednjiPutPretrazivan = poslednjiPutPretrazivan;
    }



    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    @NonNull
    public String getNaziv() {
        return naziv;
    }

    public Boolean getOmiljeni() {
        return omiljeni;
    }

    public Date getPoslednjiPutPretrazivan() {
        return poslednjiPutPretrazivan;
    }


    public String getRawData() {
        return rawData;
    }
}
