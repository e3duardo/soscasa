package br.com.magicbox.soscasa;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.magicbox.soscasa.adapter.NegociacaoAdapter;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.StatusProblema;
import br.com.magicbox.soscasa.models.Usuario;

/**
 * Criado por eduardo em 03/06/17.
 */

public class MinhasNegociacoesActivity extends BaseActivity {

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_negociacoes);

        mRecycler = (RecyclerView) findViewById(R.id.negociacoes_profissional_list);
        mRecycler.setHasFixedSize(true);

        mManager = new LinearLayoutManager(MinhasNegociacoesActivity.this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        getDatabase().child("negociacoes")
                .orderByChild("profissional").equalTo(getSessao().getUsuarioUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<Negociacao> negociacoes = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    final Negociacao negociacao = data.getValue(Negociacao.class);
                    negociacao.setUid(data.getKey());

                    getDatabase().child("problemas").child(negociacao.getProblemaUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot data2) {

                            Problema problema = data2.getValue(Problema.class);
                            problema.setUid(data2.getKey());


                            if(!problema.getStatus().equals(StatusProblema.CANCELADO)){
                                negociacao.setProblema(problema);
                                negociacoes.add(negociacao);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }

                mRecycler.setAdapter(new NegociacaoAdapter(MinhasNegociacoesActivity.this, negociacoes, getSessao()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}
