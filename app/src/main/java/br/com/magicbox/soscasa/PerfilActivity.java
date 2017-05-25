package br.com.magicbox.soscasa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Usuario;

import static android.content.ContentValues.TAG;

public class PerfilActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    // EditText senha;
    private EditText celular;
    private Spinner area;
    private Button botao;
    private Button profissional;

    private Usuario usuario;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        nome = (EditText) findViewById(R.id.editText_perfil_nome);
        email = (EditText) findViewById(R.id.editText_perfil_email);
        celular = (EditText) findViewById(R.id.editText_perfil_celular);
        area = (Spinner) findViewById(R.id.areaSpinner);
        botao = (Button) findViewById(R.id.button_perfil_ok);
        profissional = (Button) findViewById(R.id.button_perfil_profissional);

        final View camposProfissional = findViewById(R.id.campos_profissional);

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase.child("usuarios").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        usuario = dataSnapshot.getValue(Usuario.class);

                        if (usuario != null) {
                            nome.setText(usuario.getNome());
                            email.setText(usuario.getEmail());
                            celular.setText(usuario.getCelular());

                            if (usuario.getEhProfissional()) {
                                camposProfissional.setVisibility(View.VISIBLE);
                                profissional.setVisibility(View.GONE);
                            }

                            mDatabase.child("areas").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    List<Area> areas = new ArrayList<>();
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        Area area = postSnapshot.getValue(Area.class);
                                        area.setUid(postSnapshot.getKey());
                                        areas.add(area);
                                    }

                                    ArrayAdapter<Area> adapter = new ArrayAdapter<>(PerfilActivity.this, android.R.layout.simple_spinner_item, areas);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    area.setAdapter(adapter);

                                    area.setSelection(adapter.getPosition(new Area(usuario.getAreaUid())));

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w(TAG, "getAreas:onCancelled", databaseError.toException());
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setCelular(celular.getText().toString());

                Area areaAtual = (Area) area.getSelectedItem();
                if(areaAtual!=null)
                    usuario.setAreaUid(areaAtual.getUid());

                Util.writeNewUser(mDatabase, userId, usuario);

                //Toast.makeText(getActivity(), "Usuário salvo!", Toast.LENGTH_SHORT).show();

                Intent returnIntent = new Intent();
                //returnIntent.putExtra("result",problema);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
        profissional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario.setEhProfissional(true);

                Util.writeNewUser(mDatabase, userId, usuario);

                camposProfissional.setVisibility(View.VISIBLE);
                profissional.setVisibility(View.GONE);
            }
        });
    }
}
