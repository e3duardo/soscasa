package br.com.magicbox.soscasa.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.StatusProblema;
import br.com.magicbox.soscasa.models.Usuario;

import static android.content.ContentValues.TAG;


public class DefinirProblemaFragment extends Fragment {

    private Spinner areaSpinner;
    private EditText problemaEditText;
    private Button okButton;

    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_definir_problema, container, false);
        areaSpinner = (Spinner) view.findViewById(R.id.areaSpinner);
        problemaEditText = (EditText) view.findViewById(R.id.problemaEditText);
        okButton = (Button) view.findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabase.child("usuarios").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);

                        if (usuario == null) {
                            Log.e(TAG, "Usuario " + userId + " is unexpectedly null");
                            Toast.makeText(getActivity(), "Error: could not fetch usuario.", Toast.LENGTH_SHORT).show();
                        } else {
                            writeNewProblema(problemaEditText.getText().toString(), (Area) areaSpinner.getSelectedItem(), usuario);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("areas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Area> areas =  new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Area area = postSnapshot.getValue(Area.class);
                    areas.add(area);
                }

                ArrayAdapter<Area> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, areas);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getAreas:onCancelled", databaseError.toException());
            }
        });

        return view;
    }

    private void writeNewProblema(String descricao, Area area, Usuario cliente) {
        String key = mDatabase.child("problemas").push().getKey();

        Problema problema = new Problema();
        problema.setStatus(StatusProblema.PENDENTE);
        problema.setDescricao(descricao);
        problema.setArea(area);
        problema.setCliente(cliente);

        mDatabase.child("problemas").child(key).setValue(problema);

        Toast.makeText(getActivity(), "novo problema: " + problema.getDescricao() + " " + problema.getArea().getNome(), Toast.LENGTH_SHORT).show();

//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/problemas/" + key, problema.toMap());

//        mDatabase.updateChildren(childUpdates);
    }
}
