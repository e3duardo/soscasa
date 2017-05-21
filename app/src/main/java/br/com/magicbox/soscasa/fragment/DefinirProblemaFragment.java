package br.com.magicbox.soscasa.fragment;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.magicbox.soscasa.ClienteActivity;
import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.StatusProblema;

import static android.content.ContentValues.TAG;


public class DefinirProblemaFragment extends Fragment {

    private Spinner sArea;
    private AutoCompleteTextView actvProblema;
    private Button bCadastrar;

    private ClienteActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (ClienteActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_definir_problema, container, false);

        sArea = (Spinner) view.findViewById(R.id.areaSpinner);
        actvProblema = (AutoCompleteTextView) view.findViewById(R.id.problemaEditText);
        bCadastrar = (Button) view.findViewById(R.id.okButton);

        bCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarProblema(actvProblema.getText().toString(), (Area) sArea.getSelectedItem());
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        activity.getDatabase().child("areas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Area> areas = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Area area = postSnapshot.getValue(Area.class);
                    area.setUid(postSnapshot.getKey());
                    areas.add(area);
                }

                ArrayAdapter<Area> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, areas);
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
        String key = activity.getDatabase().child("problemas").push().getKey();

        Problema problema = new Problema();
        problema.setStatus(StatusProblema.SOLICITADO);
        problema.setDescricao(descricao);
        problema.setAreaUid(area.getUid());
        problema.setClienteUid(activity.getUsuario().getUid());
        problema.setLatitude(activity.latitude);
        problema.setLongitude(activity.longitude);

        activity.getDatabase().child("problemas").child(key).setValue(problema);

        Toast.makeText(getActivity(), "novo problema: " + problema.getDescricao() + " " + area.getNome(), Toast.LENGTH_SHORT).show();
    }


}
