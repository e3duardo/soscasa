package br.com.magicbox.soscasa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.Usuario;
import br.com.magicbox.soscasa.viewholder.NegociacaoViewHolder;

public class ProblemaActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private Problema problema;

    private TextView problemaArea;
    private TextView problemaDescricao;
    private TextView problemaCliente;
    private TextView problemaStatus;

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<Negociacao, NegociacaoViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problema);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        problema = (Problema) getIntent().getSerializableExtra("problema");

        problemaArea = (TextView) findViewById(R.id.problema_detail_area);
        problemaDescricao = (TextView) findViewById(R.id.problema_detail_descricao);
        problemaCliente = (TextView) findViewById(R.id.problema_detail_cliente);
        problemaStatus = (TextView) findViewById(R.id.problema_detail_status);

        mRecycler = (RecyclerView) findViewById(R.id.negociacoes_list);
        mRecycler.setHasFixedSize(true);

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        populateView();
    }

    private void populateView() {
        problemaStatus.setText(problema.getStatus().getI18n());
        problemaDescricao.setText(problema.getDescricao());
        mDatabase.child("areas").child(problema.getAreaUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                problemaArea.setText(dataSnapshot.getValue(Area.class).getNome());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        mDatabase.child("usuarios").child(problema.getClienteUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                problemaCliente.setText(dataSnapshot.getValue(Usuario.class).getNome());
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        Query negociacoes = mDatabase.child("negociacoes")
                .orderByChild("problema").equalTo(problema.getUid());

        mAdapter = new FirebaseRecyclerAdapter<Negociacao, NegociacaoViewHolder>
                (Negociacao.class, R.layout.item_negociacao, NegociacaoViewHolder.class, negociacoes) {

            @Override
            protected Negociacao parseSnapshot(DataSnapshot snapshot) {
                Negociacao negociacao = super.parseSnapshot(snapshot);
                negociacao.setUid(snapshot.getKey());
                return negociacao;
            }

            @Override
            protected void populateViewHolder
                    (final NegociacaoViewHolder viewHolder, final Negociacao model, final int position) {


                viewHolder.bindToPost(mDatabase, model);

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProblemaActivity.this, NegociacaoActivity.class);
                        intent.putExtra("negociacao", model);
                        startActivity(intent);
                    }
                });
            }
        };

        mRecycler.setAdapter(mAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

}
