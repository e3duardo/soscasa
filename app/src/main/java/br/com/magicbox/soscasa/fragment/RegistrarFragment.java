package br.com.magicbox.soscasa.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.magicbox.soscasa.EntrarActivity;
import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.Util;

import static android.content.ContentValues.TAG;

public class RegistrarFragment extends Fragment {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button registrar;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrar, container, false);

        nome = (EditText) view.findViewById(R.id.editText_registrar_nome);
        email = (EditText) view.findViewById(R.id.editText_registrar_email);
        senha = (EditText) view.findViewById(R.id.editText_registrar_senha);
        registrar = (Button) view.findViewById(R.id.button_registrar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm()) {
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), senha.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "createUsuario:onComplete:" + task.isSuccessful());

                                if (task.isSuccessful()) {
                                    Util.onAuthSuccess(getActivity(), mDatabase, task.getResult().getUser());
                                } else {
                                    Toast.makeText(getActivity(), "Sign Up Failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        return view;
    }

    private boolean validateForm() {
        boolean result = true;

        if (TextUtils.isEmpty(nome.getText().toString())) {
            nome.setError("Required");
            result = false;
        } else {
            nome.setError(null);
        }

        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Required");
            result = false;
        } else {
            email.setError(null);
        }

        if (TextUtils.isEmpty(senha.getText().toString())) {
            senha.setError("Required");
            result = false;
        } else {
            senha.setError(null);
        }

        return result;
    }

}
