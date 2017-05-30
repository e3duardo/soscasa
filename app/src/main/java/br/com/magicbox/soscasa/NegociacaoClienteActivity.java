package br.com.magicbox.soscasa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import br.com.magicbox.soscasa.adapter.MensagemAdapter;
import br.com.magicbox.soscasa.models.Mensagem;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.Usuario;

public class NegociacaoClienteActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private Negociacao negociacao;

    private TextView tvProfissional;
    //private TextView tvDescricaoProblema;
    private TextView tvValor;
    private TextView tvNovaMensagem;

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negociacao_cliente);

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        mDatabase = FirebaseDatabase.getInstance().getReference();

        negociacao = (Negociacao) getIntent().getSerializableExtra("negociacao");

        tvProfissional = (TextView) findViewById(R.id.negociacao_cliente_tv_profissional);
        //tvDescricaoProblema = (TextView) findViewById(R.id.tv_negociacao_cliente_descricao_problema);
        tvNovaMensagem = (TextView) findViewById(R.id.negociacao_cliente_et_nova_mensagem);

        tvValor = (TextView) findViewById(R.id.negociacao_cliente_tv_valor);
        if (negociacao.getValor() != null) {
            tvValor.setText(format.format(negociacao.getValor()));
        } else {
            //findViewById(R.id.action_aprovar_negociacao).setVisibility(View.INVISIBLE);
            tvValor.setVisibility(View.GONE);
            findViewById(R.id.negociacao_cliente_l_valor).setVisibility(View.GONE);
        }


        tvNovaMensagem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    cadastrarMensagem(tvNovaMensagem.getText().toString());
                    tvNovaMensagem.setText("");
                }
                return true;
            }
        });

        mManager = new LinearLayoutManager(this);
        mManager.setStackFromEnd(true);

        mRecycler = (RecyclerView) findViewById(R.id.negociacao_cliente_rv_mensagens);
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
                //tvDescricaoProblema.setText(problema.getDescricao());

                mDatabase.child("usuarios").child(problema.getClienteUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuario cliente = dataSnapshot.getValue(Usuario.class);
                        tvProfissional.setText(cliente.getNome());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_negociacao_cliente, menu);
        if (negociacao.getValor() == null)
            menu.findItem(R.id.action_aprovar_negociacao).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_aprovar_negociacao:
                Toast.makeText(NegociacaoClienteActivity.this, "criar logica de aprovacao aqui", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cadastrarMensagem(String texto) {
        String key = mDatabase.child("negociacoes").child(negociacao.getUid()).child("mensagens").push().getKey();

        Mensagem mensagem = new Mensagem();
        mensagem.setUsuarioUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mensagem.setData(new Date());
        mensagem.setMensagem(texto);

        mDatabase.child("negociacoes").child(negociacao.getUid()).child("mensagens").child(key).setValue(mensagem);

        Toast.makeText(NegociacaoClienteActivity.this, "nova mensagem", Toast.LENGTH_SHORT).show();

        mRecycler.smoothScrollToPosition(mRecycler.getAdapter().getItemCount());
    }

}
