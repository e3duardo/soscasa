package br.com.magicbox.soscasa.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.Usuario;


public class NegociacaoViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTitle;
    public TextView tvDescription;

    public NegociacaoViewHolder(View itemView) {
        super(itemView);

        tvTitle = (TextView) itemView.findViewById(R.id.tv_item_negociacao_title);
        tvDescription = (TextView) itemView.findViewById(R.id.tv_item_negociacao_description);
    }

    public void bindToView(Negociacao negociacao, final boolean ehProfissional) {

        Toast.makeText(itemView.getContext(), "sequence10", Toast.LENGTH_SHORT).show();

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        if (!ehProfissional) {
            mDatabase.child("usuarios").child(negociacao.getProfissionalUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Usuario cliente = dataSnapshot.getValue(Usuario.class);
                    tvTitle.setText(cliente.getNome());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        mDatabase.child("problemas").child(negociacao.getProblemaUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Problema problema = dataSnapshot.getValue(Problema.class);
                tvDescription.setText(problema.getDescricao());

                if (ehProfissional) {
                    mDatabase.child("usuarios").child(problema.getClienteUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario cliente = dataSnapshot.getValue(Usuario.class);
                            tvTitle.setText(cliente.getNome());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
