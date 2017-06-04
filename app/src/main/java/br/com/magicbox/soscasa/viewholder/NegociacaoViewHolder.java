package br.com.magicbox.soscasa.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;

import br.com.magicbox.soscasa.NegociacaoActivity;
import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.Sessao;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.Usuario;


public class NegociacaoViewHolder extends RecyclerView.ViewHolder {

    private Activity activity;
    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvStatus;
    private ProgressBar progress;
    private Sessao sessao;

    public NegociacaoViewHolder(Activity activity, View itemView, Sessao sessao) {
        super(itemView);
        this.activity = activity;
        this.sessao = sessao;
        tvTitle = (TextView) itemView.findViewById(R.id.text_item_negociacao_title);
        tvDescription = (TextView) itemView.findViewById(R.id.text_item_negociacao_description);
        tvStatus = (TextView) itemView.findViewById(R.id.text_item_negociacao_status);
        progress = (ProgressBar) itemView.findViewById(R.id.progress_item_negociacao);

    }

    public void bindToView(final Negociacao negociacao) {

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        NumberFormat format = NumberFormat.getCurrencyInstance();

        tvStatus.setText(negociacao.getStatus().getI18n());

        if (!sessao.usuarioEhProfissional()) {
            progress.setVisibility(View.VISIBLE);
            if (negociacao.getValor() != null)
                tvDescription.setText(format.format(negociacao.getValor()));

            mDatabase.child("usuarios").child(negociacao.getProfissionalUid())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progress.setVisibility(View.GONE);
                    Usuario cliente = dataSnapshot.getValue(Usuario.class);
                    tvTitle.setText(cliente.getNome());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }


        if (sessao.usuarioEhProfissional()) {
            progress.setVisibility(View.VISIBLE);
            mDatabase.child("problemas").child(negociacao.getProblemaUid())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Problema problema = dataSnapshot.getValue(Problema.class);

                    tvDescription.setText(problema.getDescricao());
                    mDatabase.child("usuarios").child(problema.getClienteUid())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            progress.setVisibility(View.GONE);
                            Usuario cliente = dataSnapshot.getValue(Usuario.class);
                            tvTitle.setText(cliente.getNome());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(activity, NegociacaoActivity.class);
                intent.putExtra("negociacao", negociacao    );
                intent.putExtra("sessao", sessao);
                activity.startActivity(intent);
            }
        });
    }
}
