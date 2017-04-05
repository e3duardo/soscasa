package br.com.magicbox.soscasa.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
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
        okButton = (Button)view.findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get usuario value
                                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                                // [START_EXCLUDE]
                                if (usuario == null) {
                                    // Usuario is null, error out
                                    Log.e(TAG, "Usuario " + userId + " is unexpectedly null");
//                                    Toast.makeText(NewPostActivity.this,
//                                            "Error: could not fetch usuario.",
//                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // Write new post
                                    writeNewPost(userId, problemaEditText.getText().toString());
                                }

                                // Finish this Activity, back to the stream
                                //setEditingEnabled(true);
                                //dsafinish();
                                // [END_EXCLUDE]
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                                // [START_EXCLUDE]
                                //setEditingEnabled(true);
                                // [END_EXCLUDE]
                            }
                        });
            }
        });



        mDatabase = FirebaseDatabase.getInstance().getReference();

        return view;

    }

    private void writeNewPost(String userId, String descricao) {
        String key = mDatabase.child("areas").push().getKey();

//        Problema problema = new Problema();
//        problema.setStatus(StatusProblema.PENDENTE);
//        problema.setDescricao(descricao);
//        problema.setCadastradoEm(new Date());
//        problema.setExcluido(false);

        Area problema = new Area();
        problema.setNome(descricao);

        Map<String, Object> postValues = problema.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/areas/" + key, postValues);
        //childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
}
