package br.com.magicbox.soscasa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Usuario;

public class PerfilActivity extends BaseActivity {

    private EditText textNome;
    private EditText textEmail;
    private EditText textCelular;
    private Spinner spinnerArea;
    private Button buttonSalvar;

    private View viewProfissional;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        textNome = (EditText) findViewById(R.id.text_perfil_nome);
        textEmail = (EditText) findViewById(R.id.text_perfil_email);
        textCelular = (EditText) findViewById(R.id.text_perfil_celular);
        spinnerArea = (Spinner) findViewById(R.id.spinner_novo_problema_area);
        buttonSalvar = (Button) findViewById(R.id.button_perfil_salvar);

        viewProfissional = findViewById(R.id.view_profissional);

        final Usuario usuario = getSessao().getUsuario();

        textNome.setText(usuario.getNome());
        textEmail.setText(usuario.getEmail());
        textCelular.setText(usuario.getCelular());

        if (usuario.getEhProfissional()) {
            viewProfissional.setVisibility(View.VISIBLE);
        } else {
            viewProfissional.setVisibility(View.GONE);
        }

        ArrayAdapter<Area> adapter = new ArrayAdapter<>(PerfilActivity.this, android.R.layout.simple_spinner_item, getSessao().getAreas());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArea.setAdapter(adapter);

        spinnerArea.setSelection(adapter.getPosition(new Area(usuario.getAreaUid())));


        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario.setNome(textNome.getText().toString());
                usuario.setEmail(textEmail.getText().toString());
                usuario.setCelular(textCelular.getText().toString());

                Area areaAtual = (Area) spinnerArea.getSelectedItem();
                if (areaAtual != null)
                    usuario.setAreaUid(areaAtual.getUid());

                Util.cadastrarUsuario(getDatabase(), getSessao().getUsuarioUid(), usuario);

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

        MenuItem menuProfissional = menu.findItem(R.id.action_sou_profissional);

        if (getSessao().getUsuario().getEhProfissional()) {
            menuProfissional.setVisible(false);
        } else {
            menuProfissional.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sou_profissional:


                new AlertDialog.Builder(PerfilActivity.this)
                        .setTitle(R.string.atencao)
                        .setMessage(R.string.confirmacao_sou_profissional)
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        final Usuario usuario = getSessao().getUsuario();

                                        usuario.setEhProfissional(true);

                                        Util.cadastrarUsuario(getDatabase(), usuario.getUid(), usuario);

                                        item.setVisible(false);
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
