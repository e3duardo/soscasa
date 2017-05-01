package br.com.magicbox.soscasa.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import br.com.magicbox.soscasa.AntigaNewPostActivityAntiga;
import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Usuario;

public class MeuPerfilFragment extends Fragment {

    EditText nome;
    EditText email;
    EditText senha;
    Button botao;

    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meu_perfil, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nome = (EditText) view.findViewById(R.id.editText_perfil_nome);
        email = (EditText) view.findViewById(R.id.editText_perfil_email);
        senha = (EditText) view.findViewById(R.id.editText_perfil_senha);
        botao = (Button) view.findViewById(R.id.button_perfil_ok);

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase.child("usuarios").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);

                        if (usuario != null) {
                            nome.setText(usuario.getNome());
                            email.setText(usuario.getEmail());
                            senha.setText(usuario.getSenha());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return view;
    }
}
