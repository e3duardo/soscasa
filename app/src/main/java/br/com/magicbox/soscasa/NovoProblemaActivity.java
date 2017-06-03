package br.com.magicbox.soscasa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.StatusProblema;

public class NovoProblemaActivity extends BaseActivity {

    private Spinner spinnerArea;
    private AutoCompleteTextView textProblema;
    private Button buttonCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_problema);

        spinnerArea = (Spinner) findViewById(R.id.spinner_novo_problema_area);
        textProblema = (AutoCompleteTextView) findViewById(R.id.text_novo_problema_descricao);
        buttonCadastrar = (Button) findViewById(R.id.button_novo_problema);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarProblema(textProblema.getText().toString(), (Area) spinnerArea.getSelectedItem());
            }
        });

        getDatabase().child("areas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Area> areas = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Area area = postSnapshot.getValue(Area.class);
                    area.setUid(postSnapshot.getKey());
                    areas.add(area);
                }

                ArrayAdapter<Area> adapter = new ArrayAdapter<>(NovoProblemaActivity.this, android.R.layout.simple_spinner_item, areas);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerArea.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void cadastrarProblema(String descricao, Area area) {
        String key = getDatabase().child("problemas").push().getKey();

        Problema problema = new Problema();
        problema.setUid(key);
        problema.setStatus(StatusProblema.SOLICITADO);
        problema.setDescricao(descricao);
        problema.setAreaUid(area.getUid());
        problema.setClienteUid(getUsuario().getUid());
        problema.setLatitude(latitude);
        problema.setLongitude(longitude);
        problema.setSolicitadoEm(new Date());

        getDatabase().child("problemas").child(key).setValue(problema);


        //Toast.makeText(getActivity(), "novo problema: " + problema.getDescricao() + " " + area.getNome(), Toast.LENGTH_SHORT).show();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",problema);
        setResult(ClienteActivity.RESULT_PROBLEMA_CRIADO,returnIntent);
        finish();
    }
}
