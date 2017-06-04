package br.com.magicbox.soscasa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.magicbox.soscasa.adapter.MensagemAdapter;
import br.com.magicbox.soscasa.adapter.NegociacaoAdapter;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.StatusNegociacao;
import br.com.magicbox.soscasa.models.StatusProblema;
import br.com.magicbox.soscasa.models.Usuario;

public class ProblemaClienteActivity extends BaseActivity {

    private String problemaUid;
    private Problema problema;

    private Menu menu;

    private TextView tvArea;
    private TextView tvDescricao;
    private TextView tvStatus;

    private RecyclerView rvNegociacoes;
    private LinearLayoutManager mManager;
    private TextView labelNegociacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problema_cliente);

        problemaUid = getIntent().getStringExtra("problemaUid");

        tvArea = (TextView) findViewById(R.id.text_problema_profissional_area);
        tvDescricao = (TextView) findViewById(R.id.text_problema_profissional_descricao);
        tvStatus = (TextView) findViewById(R.id.tv_problema_status);
        labelNegociacao = (TextView) findViewById(R.id.label_problema_cliente_negociacao);

        rvNegociacoes = (RecyclerView) findViewById(R.id.rv_problema_negociacoes);
        rvNegociacoes.setHasFixedSize(true);

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        rvNegociacoes.setLayoutManager(mManager);


        getDatabase().child("problemas").child(problemaUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                problema = dataSnapshot.getValue(Problema.class);
                problema.setUid(dataSnapshot.getKey());

                tvStatus.setText(problema.getStatus().getI18n());
                tvDescricao.setText(problema.getDescricao());
                tvArea.setText(getSessao().getAreaBy(problema.getAreaUid()).getNome());

                getDatabase().child("negociacoes")
                        .orderByChild("problema").equalTo(problema.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        List<Negociacao> negociacoes = new ArrayList<>();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Negociacao negociacao = data.getValue(Negociacao.class);
                            negociacao.setUid(data.getKey());
                            negociacao.setProblema(problema);
                            negociacoes.add(negociacao);
                        }

                        Negociacao negociacaoAprovada = null;
                        for (Negociacao n : negociacoes) {
                            if (StatusNegociacao.APROVADA.equals(n.getStatus())) {
                                negociacaoAprovada = n;
                            }
                        }

                        if (negociacaoAprovada != null && StatusProblema.PENDENTE.equals(problema.getStatus())) {
                            labelNegociacao.setVisibility(View.GONE);
                            rvNegociacoes.setVisibility(View.GONE);

                            findViewById(R.id.view_problema_cliente_negociacao2).setVisibility(View.VISIBLE);

                            ((TextView) findViewById(R.id.text_problema_cliente_valor))
                                    .setText(NumberFormat.getCurrencyInstance().format(negociacaoAprovada.getValor()));


                            getDatabase().child("usuarios").child(negociacaoAprovada.getProfissionalUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Usuario cliente = dataSnapshot.getValue(Usuario.class);
                                            ((TextView) findViewById(R.id.text_problema_cliente_profissional))
                                                    .setText(cliente.getNome());
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });

                            Query query = getDatabase().child("negociacoes").child(negociacaoAprovada.getUid()).child("mensagens");

                            ((RecyclerView) findViewById(R.id.recycler_mensagens_problema)).setAdapter(new MensagemAdapter(ProblemaClienteActivity.this, query));

                        } else {
                            findViewById(R.id.view_problema_cliente_negociacao2).setVisibility(View.GONE);

                            rvNegociacoes.setAdapter(new NegociacaoAdapter(ProblemaClienteActivity.this, negociacoes, getSessao()));
                        }

                        if(menu != null){
                            menu.findItem(R.id.action_aprovar_problema).setVisible(
                                    problema.getStatus() == StatusProblema.PENDENTE);
                        }
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_problema_cliente, menu);

//        menu.findItem(R.id.action_aprovar_problema).setVisible(
//                problema.getStatus() == StatusProblema.PENDENTE); // ver se tem negociacao aprovada

        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ProblemaClienteActivity.this);

        switch (item.getItemId()) {
            case R.id.action_aprovar_problema:
                dialog.setTitle(R.string.atencao)
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setMessage(R.string.deseja_aprovar_problema)
                        .setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                dialoginterface.cancel();
                            }
                        })
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {

                                StatusProblema statusAntigo = problema.getStatus();
                                if (problema.getStatus() != StatusProblema.CANCELADO && problema.getStatus() != StatusProblema.RESOLVIDO) {
                                    DatabaseReference ref = getDatabase().child("problemas").child(problema.getUid());
                                    ref.child("status").setValue(StatusProblema.RESOLVIDO);
                                    ref.child("resolvidoEm").setValue(new Date());

                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("problema", problema);
                                    returnIntent.putExtra("statusAntigo", statusAntigo);
                                    setResult(ClienteActivity.RESULT_PROBLEMA_APROVADO, returnIntent);
                                    finish();
                                }

                            }
                        }).show();
                return true;
            case R.id.action_cancelar_problema:
                dialog.setTitle(R.string.atencao)
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setMessage(R.string.deseja_cancelar_problema)
                        .setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                dialoginterface.cancel();
                            }
                        })
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {

                                StatusProblema statusAntigo = problema.getStatus();
                                if (problema.getStatus() != StatusProblema.CANCELADO && problema.getStatus() != StatusProblema.RESOLVIDO) {
                                    DatabaseReference ref = getDatabase().child("problemas").child(problema.getUid());
                                    ref.child("status").setValue(StatusProblema.CANCELADO);
                                    ref.child("canceladoEm").setValue(new Date());

                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("problema", problema);
                                    returnIntent.putExtra("statusAntigo", statusAntigo);
                                    setResult(ClienteActivity.RESULT_PROBLEMA_CANCELADO, returnIntent);
                                    finish();
                                }

                            }
                        }).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
