package br.com.magicbox.soscasa.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

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
    private String areaUid;
    private String clienteUid;
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

    @PropertyName("area")
    public String getAreaUid() {
        return areaUid;
    }

    @PropertyName("area")
    public void setAreaUid(String areaUid) {
        this.areaUid = areaUid;
    }

    @PropertyName("cliente")
    public String getClienteUid() {
        return clienteUid;
    }

    @PropertyName("cliente")
    public void setClienteUid(String clienteUid) {
        this.clienteUid = clienteUid;
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
        result.put("areaUid", areaUid);
        result.put("clienteUid", clienteUid);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("solicitadoEm", String.valueOf(solicitadoEm));
        result.put("pendenteEm", String.valueOf(pendenteEm));
        result.put("resolvidoEm", String.valueOf(resolvidoEm));
        result.put("canceladoEm", String.valueOf(canceladoEm));

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Problema problema = (Problema) o;

        return uid != null ? uid.equals(problema.uid) : problema.uid == null;

    }

    @Override
    public int hashCode() {
        return uid != null ? uid.hashCode() : 0;
    }
}
