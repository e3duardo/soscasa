package br.com.magicbox.soscasa.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import br.com.magicbox.soscasa.NegociacaoClienteActivity;
import br.com.magicbox.soscasa.NegociacaoProfissionalActivity;
import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Usuario;
import br.com.magicbox.soscasa.viewholder.NegociacaoViewHolder;

/**
 * Created by eduardo on 5/21/17.
 */

public class NegociacaoAdapter extends FirebaseRecyclerAdapter<Negociacao, NegociacaoViewHolder> {


    private final Activity activity;
    private Usuario usuario;

    public NegociacaoAdapter(Activity activity, Query ref, Usuario usuario) {
        super(Negociacao.class, R.layout.item_negociacao, NegociacaoViewHolder.class, ref);
        this.activity = activity;
        this.usuario = usuario;
        Toast.makeText(activity, "sequence4", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Negociacao parseSnapshot(DataSnapshot snapshot) {
        Negociacao negociacao = super.parseSnapshot(snapshot);
        negociacao.setUid(snapshot.getKey());
        Toast.makeText(activity, "sequence5", Toast.LENGTH_SHORT).show();
        return negociacao;
    }

    @Override
    protected void populateViewHolder
            (final NegociacaoViewHolder viewHolder, final Negociacao model, final int position) {

        viewHolder.bindToView(model, usuario.getEhProfissional());

        Toast.makeText(activity, "sequence6", Toast.LENGTH_SHORT).show();

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;

                if (usuario.getEhProfissional())
                    intent = new Intent(activity, NegociacaoProfissionalActivity.class);
                else
                    intent = new Intent(activity, NegociacaoClienteActivity.class);

                intent.putExtra("negociacao", model);
                activity.startActivity(intent);
            }
        });
    }
}
