package br.com.magicbox.soscasa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

public class PerfilActivity extends AppCompatActivity {

    private EditText textNome;
    private EditText textEmail;
    private EditText textCelular;
    private Spinner spinnerArea;
    private Button buttonSalvar;

    private View viewProfissional;
    private MenuItem menuProfissional;
    private Usuario usuario;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        textNome = (EditText) findViewById(R.id.text_perfil_nome);
        textEmail = (EditText) findViewById(R.id.text_perfil_email);
        textCelular = (EditText) findViewById(R.id.text_perfil_celular);
        spinnerArea = (Spinner) findViewById(R.id.spinner_perfil_area);
        buttonSalvar = (Button) findViewById(R.id.button_perfil_salvar);

        viewProfissional = findViewById(R.id.view_profissional);

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase.child("usuarios").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        usuario = dataSnapshot.getValue(Usuario.class);

                        if (usuario != null) {
                            usuario.setUid(dataSnapshot.getKey());

                            textNome.setText(usuario.getNome());
                            textEmail.setText(usuario.getEmail());
                            textCelular.setText(usuario.getCelular());

                            if (usuario.getEhProfissional()) {
                                menuProfissional.setVisible(false);
                                viewProfissional.setVisibility(View.VISIBLE);
                            } else {
                                menuProfissional.setVisible(true);
                                viewProfissional.setVisibility(View.GONE);
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
                                    spinnerArea.setAdapter(adapter);

                                    spinnerArea.setSelection(adapter.getPosition(new Area(usuario.getAreaUid())));

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario.setNome(textNome.getText().toString());
                usuario.setEmail(textEmail.getText().toString());
                usuario.setCelular(textCelular.getText().toString());

                Area areaAtual = (Area) spinnerArea.getSelectedItem();
                if (areaAtual != null)
                    usuario.setAreaUid(areaAtual.getUid());

                Util.writeNewUser(mDatabase, userId, usuario);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", usuario);
                setResult(ClienteActivity.RESULT_USUARIO_ALTERADO, returnIntent);
                finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_perfil, menu);

        menuProfissional = menu.findItem(R.id.action_sou_profissional);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sou_profissional:


                new AlertDialog.Builder(PerfilActivity.this)
                        .setTitle(R.string.atencao)
                        .setMessage(R.string.confirmacao_sou_profissional)
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        usuario.setEhProfissional(true);

                                        Util.writeNewUser(mDatabase, usuario.getUid(), usuario);

                                        menuProfissional.setVisible(false);
                                        viewProfissional.setVisibility(View.VISIBLE);
                                    }
                                })
                        .setNegativeButton(R.string.cancelar,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .create().show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
