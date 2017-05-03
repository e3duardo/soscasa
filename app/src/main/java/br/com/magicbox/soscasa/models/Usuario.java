package br.com.magicbox.soscasa.models;

import android.widget.EditText;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class Usuario {

    private String nome;
    private String email;
    private String celular;

    private Area area;
    private Boolean ehProfissional;

    public Usuario() {
        // Default constructor required for calls to DataSnapshot.getValue(Usuario.class)
    }

    public Usuario(String nome, String email, String celular) {
        this.area = null;
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


    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Boolean getEhProfissional() {
        return ehProfissional;
    }

    public void setEhProfissional(Boolean ehProfissional) {
        this.ehProfissional = ehProfissional;
    }
}

