package br.com.magicbox.soscasa.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.Date;

/**
 * Criado por pedro em 31/03/17.
 */

public class Mensagem {

    @Exclude
    private String uid;

    private String usuarioUid;

    private Date data;

    private String mensagem;


    public Mensagem() {

    }

    public Mensagem(String uid) {
        this.uid = uid;
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    @Exclude
    public void setUid(String uid) {
        this.uid = uid;
    }

    @PropertyName(value = "usuario")
    public String getUsuarioUid() {
        return usuarioUid;
    }

    @PropertyName(value = "usuario")
    public void setUsuarioUid(String usuarioUid) {
        this.usuarioUid = usuarioUid;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
