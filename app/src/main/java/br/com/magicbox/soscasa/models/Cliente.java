package br.com.magicbox.soscasa.models;

import java.util.List;

/**
 * Created by pedro on 31/03/17.
 */

public class Cliente extends Usuario{
    private List<Negociacao> negociacoes;

    private List<Problema> problemas;
}
