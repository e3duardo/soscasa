package br.com.magicbox.soscasa;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

import br.com.magicbox.soscasa.adapter.MensagemAdapter;
import br.com.magicbox.soscasa.models.Mensagem;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.StatusNegociacao;
import br.com.magicbox.soscasa.models.StatusProblema;
import br.com.magicbox.soscasa.models.Usuario;

public class NegociacaoActivity extends BaseActivity {


    private Negociacao negociacao;

    private TextView textTitle;
    private TextView textStatus;
    private TextView textValor;
    private TextView textData;
    private TextView labelCliente;
    private TextView textNovaMensagem;

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private ImageButton buttonEnviarMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negociacao);

        NumberFormat format = NumberFormat.getCurrencyInstance();

        negociacao = (Negociacao) getIntent().getSerializableExtra("negociacao");

        textTitle = (TextView) findViewById(R.id.text_negociacao_title);
        textStatus = (TextView) findViewById(R.id.text_negociacao_status);
        textValor = (TextView) findViewById(R.id.text_negociacao_valor);
        textData = (TextView) findViewById(R.id.text_negociacao_data);
        textNovaMensagem = (TextView) findViewById(R.id.text_nova_mensagem);
        labelCliente = (TextView) findViewById(R.id.label_negociacao_cliente);
        buttonEnviarMensagem = (ImageButton) findViewById(R.id.button_enviar_mensagem);

        textStatus.setText(negociacao.getStatus().getI18n());

        if (getSessao().usuarioEhProfissional())
            labelCliente.setText(R.string.cliente);
        else
            labelCliente.setText(R.string.profissional);


        if (negociacao.getValor() != null) {
            textValor.setText(format.format(negociacao.getValor()));
        } else {
            textValor.setVisibility(View.GONE);
            findViewById(R.id.label_negociacao_valor).setVisibility(View.GONE);
        }

        switch (negociacao.getStatus()) {
            case ABERTA:
                textData.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(negociacao.getAbertoEm()));
                break;
            case ORCADA:
                textData.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(negociacao.getOrcadaEm()));
                break;
            case APROVADA:
                textData.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(negociacao.getAprovadaEm()));
                break;
            case CANCELADA:
                textData.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(negociacao.getCanceladaEm()));
                break;
        }

        getDatabase().child("problemas").child(negociacao.getProblemaUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Problema problema = dataSnapshot.getValue(Problema.class);

                getDatabase().child("usuarios").child(problema.getClienteUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuario cliente = dataSnapshot.getValue(Usuario.class);
                        textTitle.setText(cliente.getNome());

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

        mManager = new LinearLayoutManager(this);
        mManager.setStackFromEnd(true);

        mRecycler = (RecyclerView) findViewById(R.id.recycler_mensagens);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(mManager);

        Query query = getDatabase().child("negociacoes").child(negociacao.getUid()).child("mensagens");

        mRecycler.setAdapter(new MensagemAdapter(this, query));

        textNovaMensagem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    cadastrarMensagem(textNovaMensagem.getText().toString());
                }
                return true;
            }
        });
        buttonEnviarMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarMensagem(textNovaMensagem.getText().toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (getSessao().usuarioEhProfissional()) {
            inflater.inflate(R.menu.navigation_negociacao_profissional, menu);
        } else {
            inflater.inflate(R.menu.navigation_negociacao_cliente, menu);

            menu.findItem(R.id.action_cliente_aprovar_negociacao).setVisible(
                    negociacao.getProblema().getStatus() == StatusProblema.SOLICITADO &&
                            negociacao.getStatus() == StatusNegociacao.ORCADA);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profissional_enviar_valor_negociacao:

                LayoutInflater inflater = LayoutInflater.from(NegociacaoActivity.this);

                View promptsView = inflater.inflate(R.layout.dialog_precificar_negociacao, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        NegociacaoActivity.this);

                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.text_precificar_valor);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Date agora = new Date();
                                        Float valor = Float.valueOf(userInput.getText().toString());

                                        getDatabase().child("negociacoes").child(negociacao.getUid()).child("status").setValue(StatusNegociacao.ORCADA);
                                        getDatabase().child("negociacoes").child(negociacao.getUid()).child("orcadaEm").setValue(agora);
                                        getDatabase().child("negociacoes").child(negociacao.getUid()).child("valor").setValue(valor);

                                        textData.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(agora));
                                        textValor.setText(NumberFormat.getCurrencyInstance().format(valor));
                                        textStatus.setText(StatusNegociacao.ORCADA.getI18n());

                                        textValor.setVisibility(View.VISIBLE);
                                        findViewById(R.id.label_negociacao_valor).setVisibility(View.VISIBLE);
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
            case R.id.action_cliente_aprovar_negociacao:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(NegociacaoActivity.this);

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


                                    Date agora = new Date();

                                    DatabaseReference refProblemas = getDatabase().child("problemas").child(negociacao.getProblema().getUid());
                                    refProblemas.child("status").setValue(StatusProblema.PENDENTE);
                                    refProblemas.child("pendenteEm").setValue(agora);

                                    DatabaseReference refNegociacoes = getDatabase().child("negociacoes").child(negociacao.getUid());
                                    refNegociacoes.child("status").setValue(StatusNegociacao.APROVADA);
                                    refNegociacoes.child("aprovadaEm").setValue(agora);

                                    textData.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(agora));
                                    textStatus.setText(StatusNegociacao.APROVADA.getI18n());

                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("negociacao", negociacao);
                                    setResult(ClienteActivity.RESULT_NEGOCIACAO_APROVADA, returnIntent);
                                    finish();


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

        mRecycler.smoothScrollToPosition(mRecycler.getAdapter().getItemCount());

        textNovaMensagem.setText("");
    }

}
