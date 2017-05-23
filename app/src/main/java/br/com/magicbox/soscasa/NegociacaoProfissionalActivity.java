package br.com.magicbox.soscasa;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import br.com.magicbox.soscasa.adapter.MensagemAdapter;
import br.com.magicbox.soscasa.models.Mensagem;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.Usuario;

public class NegociacaoProfissionalActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private Negociacao negociacao;

    private TextView tvCliente;
    private TextView tvValor;
    private EditText etNovaMensagem;
    private ImageButton bEnviarMensagem;

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negociacao_profissional);

        NumberFormat format = NumberFormat.getCurrencyInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        negociacao = (Negociacao) getIntent().getSerializableExtra("negociacao");

        tvCliente = (TextView) findViewById(R.id.negociacao_profissional_l_cliente);
        tvValor = (TextView) findViewById(R.id.negociacao_profissional_l_valor);
        etNovaMensagem = (EditText) findViewById(R.id.negociacao_profissional_et_nova_mensagem);
        bEnviarMensagem = (ImageButton) findViewById(R.id.negociacao_profissional_b_enviar_mensagem);

        etNovaMensagem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL  && event.getAction() == KeyEvent.ACTION_DOWN) {
                    cadastrarMensagem(etNovaMensagem.getText().toString());
                }
                return true;
            }
        });
        bEnviarMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarMensagem(etNovaMensagem.getText().toString());
            }
        });

        if(negociacao.getValor() != null) {
            tvValor.setText(String.valueOf(format.format(negociacao.getValor())));
        }else{
            tvValor.setVisibility(View.GONE);
            findViewById(R.id.negociacao_profissional_l_valor).setVisibility(View.GONE);
        }

        mManager = new LinearLayoutManager(this);
        //mManager.set
        //mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);

        mRecycler = (RecyclerView) findViewById(R.id.negociacao_profissional_rv_mensagens);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_negociacao_profissional, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_enviar_valor_negociacao:
                Toast.makeText(NegociacaoProfissionalActivity.this, "criar logica de enviar valor aqui", Toast.LENGTH_SHORT).show();

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

        Toast.makeText(NegociacaoProfissionalActivity.this, "nova mensagem" , Toast.LENGTH_SHORT).show();

        mRecycler.smoothScrollToPosition(mRecycler.getAdapter().getItemCount());

        etNovaMensagem.setText("");
    }

}