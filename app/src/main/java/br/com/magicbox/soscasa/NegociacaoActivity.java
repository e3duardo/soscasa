package br.com.magicbox.soscasa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
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

import java.util.Date;

import br.com.magicbox.soscasa.adapter.MensagemAdapter;
import br.com.magicbox.soscasa.models.Mensagem;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.Usuario;
import br.com.magicbox.soscasa.viewholder.MensagemViewHolder;

public class NegociacaoActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private Negociacao negociacao;

    private TextView tvCliente;
    private TextView tvDescricaoProblema;
    private TextView tvNovaMensagem;

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negociacao);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        negociacao = (Negociacao) getIntent().getSerializableExtra("negociacao");

        tvCliente = (TextView) findViewById(R.id.tv_negociacao_cliente);
        tvDescricaoProblema = (TextView) findViewById(R.id.tv_negociacao_descricao_problema);
        tvNovaMensagem = (TextView) findViewById(R.id.tv_negociacao_nova_mensagem);

        tvNovaMensagem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL  && event.getAction() == KeyEvent.ACTION_DOWN) {
                    cadastrarMensagem(tvNovaMensagem.getText().toString());
                    tvNovaMensagem.setText("");
                }
                return true;
            }
        });

        mManager = new LinearLayoutManager(this);
        //mManager.set
        //mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);

        mRecycler = (RecyclerView) findViewById(R.id.mensagem_list);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(mManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabase.child("problemas").child(negociacao.getProblemaUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Problema problema = dataSnapshot.getValue(Problema.class);
                tvDescricaoProblema.setText(problema.getDescricao());

                mDatabase.child("usuarios").child(problema.getClienteUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuario cliente = dataSnapshot.getValue(Usuario.class);
                        tvCliente.setText(cliente.getNome());
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

        final Query query = mDatabase.child("negociacoes").child(negociacao.getUid()).child("mensagens");

        mRecycler.setAdapter(new MensagemAdapter(this, query));
    }

    private void cadastrarMensagem(String texto) {
        String key = mDatabase.child("negociacoes").child(negociacao.getUid()).child("mensagens").push().getKey();

        Mensagem mensagem = new Mensagem();
        mensagem.setUsuarioUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mensagem.setData(new Date());
        mensagem.setMensagem(texto);

        mDatabase.child("negociacoes").child(negociacao.getUid()).child("mensagens").child(key).setValue(mensagem);

        Toast.makeText(NegociacaoActivity.this, "nova mensagem" , Toast.LENGTH_SHORT).show();

        mRecycler.smoothScrollToPosition(mRecycler.getAdapter().getItemCount());
    }

}
