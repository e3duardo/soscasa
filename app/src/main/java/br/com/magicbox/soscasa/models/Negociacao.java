package br.com.magicbox.soscasa.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by pedro on 31/03/17.
 */

public class Negociacao implements Serializable {

    @Exclude
    private String uid;

    private String problemaUid;

    private String profissionalUid;

    private StatusNegociacao status;

    private Float valor;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @PropertyName("problema")
    public String getProblemaUid() {
        return problemaUid;
    }

    @PropertyName("problema")
    public void setProblemaUid(String problemaUid) {
        this.problemaUid = problemaUid;
    }

    @PropertyName("profissional")
    public String getProfissionalUid() {
        return profissionalUid;
    }

    @PropertyName("profissional")
    public void setProfissionalUid(String profissionalUid) {
        this.profissionalUid = profissionalUid;
    }

    public StatusNegociacao getStatus() {
        return status;
    }

    public void setStatus(StatusNegociacao status) {
        this.status = status;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }
}
