package br.com.magicbox.soscasa.adapter;

import android.app.Activity;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Mensagem;
import br.com.magicbox.soscasa.models.Usuario;
import br.com.magicbox.soscasa.viewholder.MensagemViewHolder;

/**
 * Created by eduardo on 5/21/17.
 */

public class MensagemAdapter extends FirebaseRecyclerAdapter<Mensagem, MensagemViewHolder> {


    private final Activity activity;
    private Usuario usuario;

    public MensagemAdapter(Activity activity, Query ref, Usuario usuario) {
        super(Mensagem.class, R.layout.item_mensagem, MensagemViewHolder.class, ref);
        this.activity = activity;
        this.usuario = usuario;
    }

    @Override
    protected Mensagem parseSnapshot(DataSnapshot snapshot) {
        Mensagem mensagem = super.parseSnapshot(snapshot);
        mensagem.setUid(snapshot.getKey());
        return mensagem;
    }

    @Override
    protected void populateViewHolder
            (final MensagemViewHolder viewHolder, final Mensagem model, final int position) {

        viewHolder.bindToView(model.getMensagem(), "eu");


    }
}
