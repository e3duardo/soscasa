package br.com.magicbox.soscasa.models;

import java.util.Date;

/**
 * Created by pedro on 31/03/17.
 */

public class Entidade  {
    protected Long id;
    protected Date cadastradoEm;
    protected Date alteradoEm;
    protected Date excluidoEm;
    protected Boolean excluido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCadastradoEm() {
        return cadastradoEm;
    }

    public void setCadastradoEm(Date cadastradoEm) {
        this.cadastradoEm = cadastradoEm;
    }

    public Date getAlteradoEm() {
        return alteradoEm;
    }

    public void setAlteradoEm(Date alteradoEm) {
        this.alteradoEm = alteradoEm;
    }

    public Date getExcluidoEm() {
        return excluidoEm;
    }

    public void setExcluidoEm(Date excluidoEm) {
        this.excluidoEm = excluidoEm;
    }

    public Boolean getExcluido() {
        return excluido;
    }

    public void setExcluido(Boolean excluido) {
        this.excluido = excluido;
    }



}
