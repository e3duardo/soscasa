package br.com.magicbox.soscasa;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;

import br.com.magicbox.soscasa.adapter.NegociacaoAdapter;

/**
 * Created by eduardo on 03/06/17.
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



    }

    @Override
    protected void onResume() {
        super.onResume();

        Query negociacoes = getDatabase().child("negociacoes")
                .orderByChild("profissional").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //.orderByChild("profissional").equalTo(getUsuario().getUid());

        mRecycler.setAdapter(new NegociacaoAdapter(MinhasNegociacoesActivity.this, negociacoes, getUsuario(), null));
    }
}
