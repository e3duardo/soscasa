package br.com.magicbox.soscasa.models;

import android.support.v4.media.session.PlaybackStateCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pedro on 31/03/17.
 */

@IgnoreExtraProperties
public class Problema implements Serializable{

    @Exclude
    private String uid;

    private String descricao;

    private StatusProblema status;

    private String areaUid;

    private String clienteUid;

    private Double latitude;

    private Double longitude;

    public Problema() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusProblema getStatus() {
        return status;
    }

    public void setStatus(StatusProblema status) {
        this.status = status;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @PropertyName(value = "area")
    public String getAreaUid() {
        return areaUid;
    }

    @PropertyName(value = "cliente")
    public String getClienteUid() {
        return clienteUid;
    }

    @PropertyName(value = "area")
    public void setAreaUid(String areaUid) {
        this.areaUid = areaUid;
    }

    @PropertyName(value = "cliente")
    public void setClienteUid(String clienteUid) {
        this.clienteUid = clienteUid;
    }


}
