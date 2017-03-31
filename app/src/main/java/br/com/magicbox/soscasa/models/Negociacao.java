package br.com.magicbox.soscasa.models;

import java.math.BigDecimal;

/**
 * Created by pedro on 31/03/17.
 */

public class Negociacao extends Entidade {
    private Problema problema;
    private Profissional profissional;

    private StatusServico statusServico;
    private BigDecimal valor;
}
