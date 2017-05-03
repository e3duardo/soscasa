package br.com.magicbox.soscasa.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.Util;
import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Usuario;

import static android.content.ContentValues.TAG;

public class MeuPerfilFragment extends Fragment {

    EditText nome;
    EditText email;
   // EditText senha;
    EditText celular;
    Spinner area;
    Button botao;
    Button profissional;

    Usuario usuario;

    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_meu_perfil, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nome = (EditText) view.findViewById(R.id.editText_perfil_nome);
        email = (EditText) view.findViewById(R.id.editText_perfil_email);
        celular = (EditText) view.findViewById(R.id.editText_perfil_celular);
        area = (Spinner) view.findViewById(R.id.areaSpinner);
        botao = (Button) view.findViewById(R.id.button_perfil_ok);
        profissional = (Button) view.findViewById(R.id.button_perfil_profissional);

        final View camposProfissional = view.findViewById(R.id.campos_profissional);

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

                            if(usuario.getEhProfissional()){
                                camposProfissional.setVisibility(View.VISIBLE);
                                profissional.setVisibility(View.GONE);
                            }

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
                                    area.setAdapter(adapter);

                                    area.setSelection(adapter.getPosition(usuario.getArea()));

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
                usuario.setArea((Area) area.getSelectedItem());

                Util.writeNewUser(mDatabase, userId, usuario);

                Toast.makeText(getActivity(), "Usuário salvo!", Toast.LENGTH_SHORT).show();
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




        return view;
    }
}
