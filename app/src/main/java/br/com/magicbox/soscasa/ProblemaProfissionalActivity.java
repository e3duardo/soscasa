package br.com.magicbox.soscasa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.StatusNegociacao;
import br.com.magicbox.soscasa.models.Usuario;

public class ProblemaProfissionalActivity extends BaseActivity {

    private Problema problema;

    private TextView problemaData;
    private TextView problemaDescricao;
    private TextView problemaCliente;
    private Button negociarBotao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problema_profissional);

        problema = (Problema) getIntent().getSerializableExtra("problema");

        problemaData = (TextView) findViewById(R.id.text_problema_profissional_data);
        problemaDescricao = (TextView) findViewById(R.id.text_problema_profissional_descricao);
        problemaCliente = (TextView) findViewById(R.id.text_problema_profissional_cliente);
        negociarBotao = (Button) findViewById(R.id.button_problema_profissional_negociar);

        problemaData.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(problema.getSolicitadoEm()));
        problemaDescricao.setText(problema.getDescricao());

        getDatabase().child("usuarios").child(problema.getClienteUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                problemaCliente.setText(dataSnapshot.getValue(Usuario.class).getNome());
                //todo: loading
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        negociarBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarNovoProblema();
            }
        });
    }

    private void cadastrarNovoProblema() {
        String key = getDatabase().child("negociacoes").push().getKey();

        Negociacao negociacao = new Negociacao(key);
        negociacao.setProblemaUid(problema.getUid());
        negociacao.setProfissionalUid(getSessao().getUsuarioUid());
        negociacao.setStatus(StatusNegociacao.ABERTA);
        negociacao.setAbertoEm(new Date());

        getDatabase().child("negociacoes").child(key).setValue(negociacao);

        Intent intent = new Intent(ProblemaProfissionalActivity.this, NegociacaoActivity.class);
        intent.putExtra("sessao", getSessao());
        intent.putExtra("negociacao", negociacao);
        startActivityForResult(intent, 1);
        finish();
    }
}
