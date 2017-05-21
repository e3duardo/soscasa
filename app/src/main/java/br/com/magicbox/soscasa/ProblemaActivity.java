package br.com.magicbox.soscasa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import br.com.magicbox.soscasa.adapter.NegociacaoAdapter;
import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.Usuario;
import br.com.magicbox.soscasa.viewholder.NegociacaoViewHolder;

public class ProblemaActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private Problema problema;

    private Usuario usuario;

    private TextView tvArea;
    private TextView tvDescricao;
    private TextView tvStatus;

    private RecyclerView rvNegociacoes;
    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<Negociacao, NegociacaoViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problema);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        problema = (Problema) getIntent().getSerializableExtra("problema");
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        tvArea = (TextView) findViewById(R.id.tv_problema_area);
        tvDescricao = (TextView) findViewById(R.id.tv_problema_descricao);
        tvStatus = (TextView) findViewById(R.id.tv_problema_status);

        rvNegociacoes = (RecyclerView) findViewById(R.id.rv_problema_negociacoes);
        rvNegociacoes.setHasFixedSize(true);

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        rvNegociacoes.setLayoutManager(mManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(ProblemaActivity.this, "sequence1", Toast.LENGTH_SHORT).show();

        tvStatus.setText(problema.getStatus().getI18n());
        tvDescricao.setText(problema.getDescricao());

        mDatabase.child("areas").child(problema.getAreaUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvArea.setText(dataSnapshot.getValue(Area.class).getNome());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query negociacoes = mDatabase.child("negociacoes")
                .orderByChild("problema").equalTo(problema.getUid());

        rvNegociacoes.setAdapter(new NegociacaoAdapter(this, negociacoes, usuario));

        Toast.makeText(ProblemaActivity.this, "sequence2", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

}
