package br.com.magicbox.soscasa.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.Usuario;


public class MensagemViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;

    public MensagemViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.mensagem_usuario);
        authorView = (TextView) itemView.findViewById(R.id.mensagem_texto);
    }

    public void bindToPost(String usuario, String text) {

        titleView.setText(usuario);

        authorView.setText(text);

    }
}
