package br.com.magicbox.soscasa.models;

/**
 * Created by pedro on 31/03/17.
 */

public class Endereco {
    private String rua;
    private String numero;
    private String cidade;
    private String bairro;
    private String cep;
    private String complemente;

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getComplemente() {
        return complemente;
    }

    public void setComplemente(String complemente) {
        this.complemente = complemente;
    }
}
