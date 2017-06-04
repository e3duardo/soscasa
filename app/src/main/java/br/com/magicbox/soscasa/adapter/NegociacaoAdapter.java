package br.com.magicbox.soscasa.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import br.com.magicbox.soscasa.NegociacaoClienteActivity;
import br.com.magicbox.soscasa.NegociacaoProfissionalActivity;
import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.Sessao;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.viewholder.NegociacaoViewHolder;

/**
 * Criado por eduardo em 5/21/17.
 */

public class NegociacaoAdapter extends FirebaseRecyclerAdapter<Negociacao, NegociacaoViewHolder> {

    private final Activity activity;
    private Sessao sessao;
    private Problema problema;

    public NegociacaoAdapter(Activity activity, Query ref, Sessao sessao, Problema problema) {
        super(Negociacao.class, R.layout.item_negociacao, NegociacaoViewHolder.class, ref);
        this.activity = activity;
        this.sessao = sessao;
        this.problema = problema;
    }

    @Override
    protected Negociacao parseSnapshot(DataSnapshot snapshot) {
        Negociacao negociacao = super.parseSnapshot(snapshot);
        negociacao.setUid(snapshot.getKey());

        if (problema != null && negociacao.getProblemaUid().equals(problema.getUid()))
            negociacao.setProblema(problema);

        return negociacao;
    }

    @Override
    protected void populateViewHolder
            (final NegociacaoViewHolder viewHolder, final Negociacao model, final int position) {

        viewHolder.bindToView(model, sessao.usuarioEhProfissional());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                if (sessao.usuarioEhProfissional())
                    intent = new Intent(activity, NegociacaoProfissionalActivity.class);
                else
                    intent = new Intent(activity, NegociacaoClienteActivity.class);

                intent.putExtra("negociacao", model);
                intent.putExtra("sessao", sessao);
                activity.startActivity(intent);
            }
        });
    }
}
