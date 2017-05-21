package br.com.magicbox.soscasa;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.Usuario;

public class ProblemaActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private Problema problema;

    private TextView problemaArea;
    private TextView problemaDescricao;
    private TextView problemaCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problema);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        problema = (Problema) getIntent().getSerializableExtra("problema");

        problemaArea = (TextView) findViewById(R.id.problema_detail_area);
        problemaDescricao = (TextView) findViewById(R.id.problema_detail_text);
        problemaCliente = (TextView) findViewById(R.id.problema_detail_cliente);


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

    }

}
