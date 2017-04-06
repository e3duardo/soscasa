package br.com.magicbox.soscasa.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pedro on 31/03/17.
 */

public class Problema  {
    private String descricao;

    private Area area;

    private StatusProblema status;

    private Cliente cliente;


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public StatusProblema getStatus() {
        return status;
    }

    public void setStatus(StatusProblema status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("descricao", descricao);
        result.put("area", area);
        result.put("status", status);
        result.put("cliente", cliente);

        return result;
    }
}
