package br.com.magicbox.soscasa.models;

import android.widget.EditText;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

// [START blog_user_class]
@IgnoreExtraProperties
public class Usuario {

    @Exclude
    private String uid;

    private String nome;
    private String email;
    private String celular;

    private String areaUid;

    private Boolean ehProfissional;

    public Usuario() {
        // Default constructor required for calls to DataSnapshot.getValue(Usuario.class)
    }

    public Usuario(String nome, String email, String celular) {
        this.areaUid = null;
        this.ehProfissional = false;
        this.nome = nome;
        this.email = email;
        this.celular = celular;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }


    @PropertyName(value = "area")
    public String getAreaUid() {
        return areaUid;
    }

    @PropertyName(value = "area")
    public void setAreaUid(String area) {
        this.areaUid = area;
    }

    public Boolean getEhProfissional() {
        return ehProfissional;
    }

    public void setEhProfissional(Boolean ehProfissional) {
        this.ehProfissional = ehProfissional;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String gui) {
        this.uid = gui;
    }
}

