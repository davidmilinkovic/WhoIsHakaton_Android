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

    @ColumnInfo(name = "status")
    private Boolean status;

    @ColumnInfo(name = "datum_isteka")
    private Date datumIsteka;

    @ColumnInfo(name = "datum_registracije")
    private Date datumRegistracije;

    @ColumnInfo(name = "poslednji_put_pretrazivan")
    private Date poslednjiPutPretrazivan;

    @ColumnInfo(name = "registar")
    private String registar;

    public Domen(String naziv, Boolean omiljeni, Boolean status, Date datumIsteka, Date datumRegistracije, Date poslednjiPutPretrazivan, String registar) {
        this.naziv = naziv;
        this.omiljeni = omiljeni;
        this.status = status;
        this.datumIsteka = datumIsteka;
        this.datumRegistracije = datumRegistracije;
        this.poslednjiPutPretrazivan = poslednjiPutPretrazivan;
        this.registar = registar;
    }

    public String getNaziv() {
        return naziv;
    }

    public Boolean getOmiljeni() {
        return omiljeni;
    }

    public Boolean getStatus() {
        return status;
    }

    public Date getDatumIsteka() {
        return datumIsteka;
    }

    public Date getDatumRegistracije() {
        return datumRegistracije;
    }

    public Date getPoslednjiPutPretrazivan() {
        return poslednjiPutPretrazivan;
    }

    public String getRegistar() {
        return registar;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setOmiljeni(Boolean omiljeni) {
        this.omiljeni = omiljeni;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setDatumIsteka(Date datumIsteka) {
        this.datumIsteka = datumIsteka;
    }

    public void setDatumRegistracije(Date datumRegistracije) {
        this.datumRegistracije = datumRegistracije;
    }

    public void setPoslednjiPutPretrazivan(Date poslednjiPutPretrazivan) {
        this.poslednjiPutPretrazivan = poslednjiPutPretrazivan;
    }

    public void setRegistar(String registar) {
        this.registar = registar;
    }

    public static Domen FromJSONObject(JSONObject obj) throws JSONException {
        return new Domen(obj.getString("Domain Name"), false, null, null, null, null, obj.getString("Registrar"));
    }
}
