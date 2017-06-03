package br.com.magicbox.soscasa.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.Util;
import br.com.magicbox.soscasa.models.Usuario;

public class RegistrarFragment extends Fragment {

    private EditText textNome;
    private EditText textEmail;
    private EditText textCelular;
    private EditText textSenha;
    private TextView textError;
    private Button buttonRegistrar;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private Usuario usuario;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrar, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        textNome = (EditText) view.findViewById(R.id.text_registrar_nome);
        textEmail = (EditText) view.findViewById(R.id.text_registrar_email);
        textCelular = (EditText) view.findViewById(R.id.text_registrar_celular);
        textSenha = (EditText) view.findViewById(R.id.text_registrar_senha);
        textError = (TextView) view.findViewById(R.id.text_registrar_error);

        buttonRegistrar = (Button) view.findViewById(R.id.button_registrar);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_registrar);
        progressBar.setVisibility(View.GONE);

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm()) {
                    return;
                }

                lock();

                usuario = new Usuario(textNome.getText().toString(), textEmail.getText().toString(), textCelular.getText().toString());

                mAuth.createUserWithEmailAndPassword(textEmail.getText().toString(), textSenha.getText().toString())
                        .addOnCompleteListener(getActivity(), fazerLogin());

            }
        });

        return view;
    }

    @NonNull
    private OnCompleteListener<AuthResult> fazerLogin() {
        return new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Util.onAuthSuccess(getActivity(), mDatabase, task.getResult().getUser(), usuario);
                } else {
                    unlock();
                    textError.setText(R.string.cadastrar_error);
                    textError.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private boolean validateForm() {
        boolean result = true;

        if (TextUtils.isEmpty(textNome.getText().toString())) {
            textNome.setError(getString(R.string.camporequerido));
            result = false;
        } else {
            textNome.setError(null);
        }

        if (TextUtils.isEmpty(textEmail.getText().toString())) {
            textEmail.setError(getString(R.string.camporequerido));
            result = false;
        } else {
            textEmail.setError(null);
        }
        if (TextUtils.isEmpty(textCelular.getText().toString())) {
            textCelular.setError(getString(R.string.camporequerido));
            result = false;
        } else {
            textCelular.setError(null);
        }

        if (TextUtils.isEmpty(textSenha.getText().toString())) {
            textSenha.setError(getString(R.string.camporequerido));
            result = false;
        } else {
            textSenha.setError(null);
        }

        return result;
    }

    private void lock() {
        textError.setText("");
        textError.setVisibility(View.GONE);

        progressBar.setVisibility(View.VISIBLE);

        textNome.setEnabled(false);
        textEmail.setEnabled(false);
        textCelular.setEnabled(false);
        textSenha.setEnabled(false);

        buttonRegistrar.setEnabled(false);
    }

    private void unlock() {
        progressBar.setVisibility(View.GONE);

        textNome.setEnabled(true);
        textEmail.setEnabled(true);
        textCelular.setEnabled(true);
        textSenha.setEnabled(true);

        buttonRegistrar.setEnabled(true);

    }

}
