package br.com.magicbox.soscasa;

import java.io.Serializable;
import java.util.List;

import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Usuario;

/**
 * Criado por eduardo em 03/06/17.
 */

public class Sessao implements Serializable {

    private Usuario usuario;

    private List<Area> areas;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public Area getAreaBy(String uid) {
        for (Area area : areas) {
            return area;
        }
        return null;
    }

    public String getUsuarioUid() {
        return usuario.getUid();
    }

    public Boolean usuarioEhProfissional() {
        return usuario.getEhProfissional();
    }
}
