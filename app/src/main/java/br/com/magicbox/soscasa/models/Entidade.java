package br.com.magicbox.soscasa.models;

import java.util.Date;

/**
 * Created by pedro on 31/03/17.
 */

public class Entidade  {
    private Long id;
    private Date cadastradoEm;
    private Date alteradoEm;
    private Date excluidoEm;
    private Date excluido;

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

    public Date getExcluido() {
        return excluido;
    }

    public void setExcluido(Date excluido) {
        this.excluido = excluido;
    }



}
