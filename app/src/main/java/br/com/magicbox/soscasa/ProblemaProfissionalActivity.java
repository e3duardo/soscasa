package br.com.magicbox.soscasa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.StatusNegociacao;
import br.com.magicbox.soscasa.models.Usuario;

import static android.content.ContentValues.TAG;

public class ProblemaProfissionalActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private Problema problema;
    private Usuario usuario;

    private TextView problemaArea;
    private TextView problemaDescricao;
    private TextView problemaCliente;
    private Button negociarBotao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problema_profissional);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        problema = (Problema) getIntent().getSerializableExtra("problema");

        problemaArea = (TextView) findViewById(R.id.problema_detail_area);
        problemaDescricao = (TextView) findViewById(R.id.problema_detail_descricao);
        problemaCliente = (TextView) findViewById(R.id.problema_detail_cliente);

        negociarBotao = (Button) findViewById(R.id.negociar_profissional_button);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("usuarios").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        usuario = dataSnapshot.getValue(Usuario.class);
                        usuario.setUid(dataSnapshot.getKey());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });

        populateView();
    }

    private void populateView() {
        problemaDescricao.setText(problema.getDescricao());
        mDatabase.child("areas").child(problema.getAreaUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                problemaArea.setText(dataSnapshot.getValue(Area.class).getNome());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child("usuarios").child(problema.getClienteUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                problemaCliente.setText(dataSnapshot.getValue(Usuario.class).getNome());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        negociarBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewProblema();
            }
        });
    }

    private void writeNewProblema() {
        String key = mDatabase.child("negociacoes").push().getKey();

        Negociacao negociacao = new Negociacao();
        negociacao.setProblemaUid(problema.getUid());
        negociacao.setProfissionalUid(usuario.getUid());
        negociacao.setStatus(StatusNegociacao.ABERTA);

        mDatabase.child("negociacoes").child(key).setValue(negociacao);

        Toast.makeText(ProblemaProfissionalActivity.this, "nova negociacao: ", Toast.LENGTH_SHORT).show();
    }
}
