package br.com.magicbox.soscasa.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.Date;

/**
 * Criado por pedro em 31/03/17.
 */

public class Negociacao implements Serializable {

    @Exclude
    private String uid;

    private String problemaUid;

    private String profissionalUid;

    private StatusNegociacao status;

    private Float valor;

    @Exclude
    private Problema problema;

    private Date abertoEm;
    private Date orcadaEm;
    private Date aprovadaEm;
    private Date canceladaEm;

    public Negociacao() {

    }

    public Negociacao(String uid) {
        this.uid = uid;
    }

    @Exclude
    public Problema getProblema() {
        return problema;
    }

    @Exclude
    public void setProblema(Problema problema) {
        this.problema = problema;
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    @Exclude
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

    public Date getAbertoEm() {
        return abertoEm;
    }

    public void setAbertoEm(Date abertoEm) {
        this.abertoEm = abertoEm;
    }

    public Date getOrcadaEm() {
        return orcadaEm;
    }

    public void setOrcadaEm(Date orcadaEm) {
        this.orcadaEm = orcadaEm;
    }

    public Date getAprovadaEm() {
        return aprovadaEm;
    }

    public void setAprovadaEm(Date aprovadaEm) {
        this.aprovadaEm = aprovadaEm;
    }

    public Date getCanceladaEm() {
        return canceladaEm;
    }

    public void setCanceladaEm(Date canceladaEm) {
        this.canceladaEm = canceladaEm;
    }
}
