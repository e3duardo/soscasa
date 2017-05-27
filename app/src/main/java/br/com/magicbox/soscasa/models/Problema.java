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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pedro on 31/03/17.
 */

@IgnoreExtraProperties
public class Problema implements Serializable{

    private String uid;
    private String descricao;
    private StatusProblema status;
    private Area area;
    private Usuario cliente;
    private Double latitude;
    private Double longitude;
    private Date solicitadoEm;
    private Date pendenteEm;
    private Date resolvidoEm;
    private Date canceladoEm;


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

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
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

    public Date getSolicitadoEm() {
        return solicitadoEm;
    }

    public void setSolicitadoEm(Date solicitadoEm) {
        this.solicitadoEm = solicitadoEm;
    }

    public Date getPendenteEm() {
        return pendenteEm;
    }

    public void setPendenteEm(Date pendenteEm) {
        this.pendenteEm = pendenteEm;
    }

    public Date getResolvidoEm() {
        return resolvidoEm;
    }

    public void setResolvidoEm(Date resolvidoEm) {
        this.resolvidoEm = resolvidoEm;
    }

    public Date getCanceladoEm() {
        return canceladoEm;
    }

    public void setCanceladoEm(Date canceladoEm) {
        this.canceladoEm = canceladoEm;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        //result.put("uid", uid);
        result.put("descricao", descricao);
        result.put("status", status);
        result.put("area", area.getUid());
        result.put("cliente", cliente.getUid());
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("solicitadoEm", String.valueOf(solicitadoEm));
        result.put("pendenteEm", String.valueOf(pendenteEm));
        result.put("resolvidoEm", String.valueOf(resolvidoEm));
        result.put("canceladoEm", String.valueOf(canceladoEm));

        return result;
    }

}
