package br.com.magicbox.soscasa.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import br.com.magicbox.soscasa.ProblemaActivity;
import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.Usuario;
import br.com.magicbox.soscasa.viewholder.NegociacaoViewHolder;
import br.com.magicbox.soscasa.viewholder.ProblemaViewHolder;

/**
 * Created by eduardo on 5/21/17.
 */

public class ProblemaAdapter extends FirebaseRecyclerAdapter<Problema, ProblemaViewHolder> {

    private final Activity activity;
    private Usuario usuario;

    public ProblemaAdapter(Activity activity, Query ref, Usuario usuario) {
        super(Problema.class, R.layout.item_problema, ProblemaViewHolder.class, ref);
        this.activity = activity;
        this.usuario = usuario;

//        Toast.makeText(activity, "sequence2", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Problema parseSnapshot(DataSnapshot snapshot) {
        Problema problema = super.parseSnapshot(snapshot);
        problema.setUid(snapshot.getKey());
//        Toast.makeText(activity, "sequence3", Toast.LENGTH_SHORT).show();
        return problema;
    }

    @Override
    protected void populateViewHolder(final ProblemaViewHolder viewHolder, final Problema model, final int position) {
        viewHolder.bindToView(model);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(activity, "sequence4", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(activity, ProblemaActivity.class);
                intent.putExtra("problema", model);
                intent.putExtra("usuario", usuario);
                activity.startActivity(intent);

            }
        });
    }
}
