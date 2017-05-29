package br.com.magicbox.soscasa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import static android.content.ContentValues.TAG;

public class NovoProblemaActivity extends BaseActivity {

    private Spinner sArea;
    private AutoCompleteTextView actvProblema;
    private Button bCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_problema);

        sArea = (Spinner) findViewById(R.id.areaSpinner);
        actvProblema = (AutoCompleteTextView) findViewById(R.id.problemaEditText);
        bCadastrar = (Button) findViewById(R.id.okButton);

        bCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarProblema(actvProblema.getText().toString(), (Area) sArea.getSelectedItem());
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
                sArea.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getAreas:onCancelled", databaseError.toException());
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
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
