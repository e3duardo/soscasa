package br.com.magicbox.soscasa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Date;

import br.com.magicbox.soscasa.adapter.MensagemAdapter;
import br.com.magicbox.soscasa.models.Mensagem;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.StatusNegociacao;
import br.com.magicbox.soscasa.models.StatusProblema;
import br.com.magicbox.soscasa.models.Usuario;

public class NegociacaoClienteActivity extends BaseActivity {


    private Negociacao negociacao;

    private TextView tvProfissional;
    private TextView tvValor;
    private TextView tvNovaMensagem;

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private ImageButton bEnviarMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negociacao_cliente);

        NumberFormat format = NumberFormat.getCurrencyInstance();

        negociacao = (Negociacao) getIntent().getSerializableExtra("negociacao");

        tvProfissional = (TextView) findViewById(R.id.text_negociacao_cliente_profissional);
        tvNovaMensagem = (TextView) findViewById(R.id.text_negociacao_cliente_nova_mensagem);
        tvValor = (TextView) findViewById(R.id.text_negociacao_cliente_valor);
        bEnviarMensagem = (ImageButton) findViewById(R.id.button_negociacao_cliente_enviar_mensagem);

        tvNovaMensagem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    cadastrarMensagem(tvNovaMensagem.getText().toString());
                }
                return true;
            }
        });
        bEnviarMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarMensagem(tvNovaMensagem.getText().toString());
            }
        });
        if (negociacao.getValor() != null) {
            tvValor.setText(format.format(negociacao.getValor()));
        } else {
            tvValor.setVisibility(View.GONE);
            findViewById(R.id.negociacao_cliente_l_valor).setVisibility(View.GONE);
        }


        mManager = new LinearLayoutManager(this);
        mManager.setStackFromEnd(true);

        mRecycler = (RecyclerView) findViewById(R.id.negociacao_cliente_rv_mensagens);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(mManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        getDatabase().child("problemas").child(negociacao.getProblemaUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Problema problema = dataSnapshot.getValue(Problema.class);
                //tvDescricaoProblema.setText(problema.getDescricao());

                getDatabase().child("usuarios").child(problema.getClienteUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuario cliente = dataSnapshot.getValue(Usuario.class);
                        tvProfissional.setText(cliente.getNome());

                        //todo: loading
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

        final Query query = getDatabase().child("negociacoes").child(negociacao.getUid()).child("mensagens");

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
                AlertDialog.Builder dialog = new AlertDialog.Builder(NegociacaoClienteActivity.this);

                dialog.setTitle(R.string.atencao)
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setMessage(R.string.deseja_aprovar_negociacao)
                        .setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                dialoginterface.cancel();
                            }
                        })
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {

                                //StatusNegociacao statusProplemaAntigo = negociacao.getStatus();
                                if (negociacao.getProblema().getStatus() == StatusProblema.SOLICITADO &&
                                        negociacao.getStatus() == StatusNegociacao.ORCADA) {

                                    DatabaseReference ref = getDatabase().child("problemas").child(negociacao.getProblema().getUid());
                                    ref.child("status").setValue(StatusProblema.PENDENTE);
                                    ref.child("pendenteEm").setValue(new Date());

                                    DatabaseReference ref2 = getDatabase().child("negociacoes").child(negociacao.getUid());
                                    ref2.child("status").setValue(StatusNegociacao.APROVADA);

                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("negociacao", negociacao);
                                    setResult(ClienteActivity.RESULT_NEGOCIACAO_APROVADA, returnIntent);
                                    finish();
                                }

                            }
                        }).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cadastrarMensagem(String texto) {
        String key = getDatabase().child("negociacoes").child(negociacao.getUid()).child("mensagens").push().getKey();

        Mensagem mensagem = new Mensagem(key);
        mensagem.setUsuarioUid(getSessao().getUsuarioUid());
        mensagem.setData(new Date());
        mensagem.setMensagem(texto);

        getDatabase().child("negociacoes").child(negociacao.getUid()).child("mensagens").child(key).setValue(mensagem);

        Toast.makeText(NegociacaoClienteActivity.this, "nova mensagem", Toast.LENGTH_SHORT).show();

        mRecycler.smoothScrollToPosition(mRecycler.getAdapter().getItemCount());
    }

}
