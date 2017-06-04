package br.com.magicbox.soscasa.models;

public enum StatusProblema {
    SOLICITADO("Solicitado"),
    PENDENTE("Pendente"),
    RESOLVIDO("Resolvido"),
    CANCELADO("Cancelado");

    /**
     * Criado por pedro em 31/03/17.
     */
    private String i18n;

    StatusProblema(String i18n) {

        this.i18n = i18n;
    }

    public String getI18n() {
        return i18n;
    }

    public void setI18n(String i18n) {
        this.i18n = i18n;
    }

}
