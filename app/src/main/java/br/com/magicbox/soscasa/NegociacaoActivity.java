package br.com.magicbox.soscasa;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Date;

import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Mensagem;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.StatusProblema;
import br.com.magicbox.soscasa.models.Usuario;
import br.com.magicbox.soscasa.viewholder.MensagemViewHolder;
import br.com.magicbox.soscasa.viewholder.NegociacaoViewHolder;
import br.com.magicbox.soscasa.viewholder.ProblemaViewHolder;

public class NegociacaoActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private Negociacao negociacao;

    private TextView text1;
    private TextView text2;
    private TextView inserir;

    private FirebaseRecyclerAdapter<Mensagem, MensagemViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negociacao);

        text1 = (TextView) findViewById(R.id.negociacao_detail_cliente);
        text2 = (TextView) findViewById(R.id.negociacao_detail_more);
        inserir = (TextView) findViewById(R.id.inserir_mensagem);
        inserir.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL  && event.getAction() == KeyEvent.ACTION_DOWN) {
                    writeNewMensagem(inserir.getText().toString());
                }
                return true;
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        negociacao = (Negociacao) getIntent().getSerializableExtra("negociacao");

        mDatabase.child("problemas").child(negociacao.getProblemaUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Problema problema = dataSnapshot.getValue(Problema.class);
                text2.setText(problema.getDescricao());

                mDatabase.child("usuarios").child(problema.getClienteUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuario cliente = dataSnapshot.getValue(Usuario.class);
                        text1.setText(cliente.getNome());
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



        mRecycler = (RecyclerView) findViewById(R.id.mensagem_list);
        mRecycler.setHasFixedSize(true);

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        final Query postsQuery = mDatabase.child("negociacoes").child(negociacao.getUid()).child("mensagens");

        mAdapter = new FirebaseRecyclerAdapter<Mensagem, MensagemViewHolder>(Mensagem.class, R.layout.item_mensagem,
                MensagemViewHolder.class, postsQuery) {

            @Override
            protected Mensagem parseSnapshot(DataSnapshot snapshot) {
                Mensagem mensagem = super.parseSnapshot(snapshot);
                mensagem.setUid(snapshot.getKey());
                return mensagem;
            }

            @Override
            protected void populateViewHolder(final MensagemViewHolder viewHolder, final Mensagem model, final int position) {
                viewHolder.bindToPost("eu", model.getMensagem());
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

    private void writeNewMensagem(String texto) {
        String key = mDatabase.child("negociacoes").child(negociacao.getUid()).child("mensagens").push().getKey();

        Mensagem mensagem = new Mensagem();
        //mensagem.setNegociacaoUid(negociacao.getUid());
        mensagem.setUsuarioUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mensagem.setData(new Date());
        mensagem.setMensagem(texto);


        mDatabase.child("negociacoes").child(negociacao.getUid()).child("mensagens").child(key).setValue(mensagem);

        Toast.makeText(NegociacaoActivity.this, "nova mensagem: " , Toast.LENGTH_SHORT).show();
    }

}
