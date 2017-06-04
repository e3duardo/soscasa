package br.com.magicbox.soscasa;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
import br.com.magicbox.soscasa.models.StatusNegociacao;
import br.com.magicbox.soscasa.models.Usuario;

public class NegociacaoProfissionalActivity extends BaseActivity {

    private DatabaseReference mDatabase;

    private Negociacao negociacao;

    private TextView tvCliente;
    private TextView tvValor;
    private TextView tvValorLabel;
    private TextView tvStatus;
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

        tvCliente = (TextView) findViewById(R.id.text_negociacao_profissional_cliente);
        tvValor = (TextView) findViewById(R.id.text_negociacao_profissional_valor);
        tvValorLabel = (TextView) findViewById(R.id.negociacao_profissional_l_valor);
        tvStatus = (TextView) findViewById(R.id.text_negociacao_profissional_status);
        etNovaMensagem = (EditText) findViewById(R.id.text_negociacao_profissional_nova_mensagem);
        bEnviarMensagem = (ImageButton) findViewById(R.id.button_negociacao_profissional_enviar_mensagem);

        etNovaMensagem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
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

        tvStatus.setText(negociacao.getStatus().getI18n());

        if (negociacao.getValor() != null && negociacao.getValor() != 0) {
            tvValor.setText(String.valueOf(format.format(negociacao.getValor())));
        } else {
            tvValorLabel.setVisibility(View.GONE);
            tvValor.setVisibility(View.GONE);
            findViewById(R.id.text_negociacao_profissional_valor).setVisibility(View.GONE);
        }

        mManager = new LinearLayoutManager(this);
        mManager.setStackFromEnd(true);

        mRecycler = (RecyclerView) findViewById(R.id.negociacao_profissional_rv_mensagens);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(mManager);

        mDatabase.child("problemas").child(negociacao.getProblemaUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Problema problema = dataSnapshot.getValue(Problema.class);

                mDatabase.child("usuarios").child(problema.getClienteUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuario cliente = dataSnapshot.getValue(Usuario.class);
                        tvCliente.setText(cliente.getNome());

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

                LayoutInflater inflater = LayoutInflater.from(NegociacaoProfissionalActivity.this);

                View promptsView = inflater.inflate(R.layout.dialog_precificar_negociacao, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        NegociacaoProfissionalActivity.this);

                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.text_precificar_valor);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mDatabase.child("negociacoes").child(negociacao.getUid()).child("status").setValue(StatusNegociacao.ORCADA);
                                        mDatabase.child("negociacoes").child(negociacao.getUid()).child("valor").setValue(new Float(userInput.getText().toString()));
                                    }
                                })
                        .setNegativeButton(R.string.cancelar,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cadastrarMensagem(String texto) {
        String key = mDatabase.child("negociacoes").child(negociacao.getUid()).child("mensagens").push().getKey();

        Mensagem mensagem = new Mensagem(key);
        mensagem.setUsuarioUid(getSessao().getUsuarioUid());
        mensagem.setData(new Date());
        mensagem.setMensagem(texto);

        mDatabase.child("negociacoes").child(negociacao.getUid()).child("mensagens").child(key).setValue(mensagem);

        mRecycler.smoothScrollToPosition(mRecycler.getAdapter().getItemCount());

        etNovaMensagem.setText("");
    }

}
