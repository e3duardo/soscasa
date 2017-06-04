package br.com.magicbox.soscasa.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.Sessao;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.viewholder.NegociacaoViewHolder;

/**
 * Criado por eduardo em 5/21/17.
 */

public class NegociacaoAdapter extends RecyclerView.Adapter {

    private final Activity activity;
    private List<Negociacao> negociacoes;
    private Sessao sessao;

    public NegociacaoAdapter(Activity activity, List<Negociacao> negociacoes, Sessao sessao) {
        this.activity = activity;
        this.negociacoes = negociacoes;
        this.sessao = sessao;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.item_negociacao, parent, false);

        return new NegociacaoViewHolder(activity, view, sessao);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        NegociacaoViewHolder holder = (NegociacaoViewHolder) viewHolder;

        Negociacao negociacao = negociacoes.get(position);

        holder.bindToView(negociacao);

    }

    @Override
    public int getItemCount() {
        return negociacoes.size();
    }

}
