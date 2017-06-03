package br.com.magicbox.soscasa.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.Util;


public class LoginFragment extends Fragment {

    private AutoCompleteTextView textEmail;
    private AutoCompleteTextView textSenha;

    private TextView textError;

    private LoginButton buttonEntrarFacebook;
    private Button buttonEntrarEmail;
    private Button buttonRedefinirSenha;
    private Button buttonCadastrar;

    private ProgressBar progressBar;
    private FragmentActivity myContext;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private CallbackManager callbackManager;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        fragmentManager = myContext.getSupportFragmentManager();
        callbackManager = CallbackManager.Factory.create();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        textEmail = (AutoCompleteTextView) view.findViewById(R.id.text_login_email);
        textSenha = (AutoCompleteTextView) view.findViewById(R.id.text_login_senha);
        textError = (TextView) view.findViewById(R.id.text_login_error);

        buttonEntrarEmail = (Button) view.findViewById(R.id.button_login_entrar_email);
        buttonEntrarFacebook = (LoginButton) view.findViewById(R.id.button_login_entrar_facebook);
        buttonRedefinirSenha = (Button) view.findViewById(R.id.button_login_redefinir_senha);
        buttonCadastrar = (Button) view.findViewById(R.id.button_login_cadastrar);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_login);
        unlock();


        buttonEntrarFacebook.setReadPermissions("textEmail");
        buttonEntrarFacebook.setFragment(this);
        buttonEntrarFacebook.setReadPermissions("textEmail", "public_profile");
        buttonEntrarFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AuthCredential credential =
                        FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());

                mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), fazerLogin());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                unlock();
                textError.setText(R.string.login_error);
                textError.setVisibility(View.VISIBLE);
            }
        });


        buttonEntrarEmail.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if (!validateForm()) {
                                                         return;
                                                     }

                                                     lock();

                                                     mAuth.signInWithEmailAndPassword(textEmail.getText().toString(), textSenha.getText().toString())
                                                             .addOnCompleteListener(getActivity(), fazerLogin());

                                                 }
                                             }
        );


        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   fragment = new RegistrarFragment();

                                                   final FragmentTransaction transaction = fragmentManager.beginTransaction();
                                                   transaction.replace(R.id.entrar_container, fragment);
                                                   transaction.addToBackStack(fragment.getClass().getName());
                                                   transaction.commit();
                                               }
                                           }
        );


        buttonRedefinirSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View promptsView = inflater.inflate(R.layout.dialog_reset, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());

                alertDialogBuilder.setView(promptsView);

                final TextView userInput = (TextView) promptsView
                        .findViewById(R.id.text_reset_email);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.redefinir,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        lock();

                                        mAuth.sendPasswordResetEmail(userInput.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        unlock();
                                                        if (task.isSuccessful()) {
                                                            new AlertDialog.Builder(
                                                                    getActivity())
                                                                    .setTitle(R.string.redefinir_success_title)
                                                                    .setMessage(R.string.redefinir_success_message)
                                                                    .setNegativeButton(R.string.cancelar,
                                                                            new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                    dialog.cancel();
                                                                                }
                                                                            })
                                                                    .create().show();
                                                        } else {
                                                            new AlertDialog.Builder(
                                                                    getActivity())
                                                                    .setTitle(R.string.redefinir_error_title)
                                                                    .setMessage(R.string.redefinir_error_message)
                                                                    .setNegativeButton(R.string.ok,
                                                                            new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                    dialog.cancel();
                                                                                }
                                                                            })
                                                                    .create().show();
                                                        }

                                                    }
                                                });
                                    }
                                }).setNegativeButton(R.string.cancelar,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                ;
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Util.onAuthSuccess(getActivity(), mDatabase, user);
            }
        };


        //to exclude
        Button client = (Button) view.findViewById(R.id.client);
        client.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {

                                          lock();

                                          mAuth.signInWithEmailAndPassword("msn@msn.com", "123123")
                                                  .addOnCompleteListener(getActivity(), fazerLogin());

                                      }
                                  }
        );


        Button prof = (Button) view.findViewById(R.id.prof);
        prof.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        lock();

                                        mAuth.signInWithEmailAndPassword("yahoo@yahoo.com", "123123")
                                                .addOnCompleteListener(getActivity(), fazerLogin());

                                    }
                                }
        );
        /////end to exclude


        return view;
    }

    @NonNull
    private OnCompleteListener<AuthResult> fazerLogin() {
        return new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Util.onAuthSuccess(getActivity(), mDatabase, task.getResult().getUser());
                } else {
                    unlock();
                    textError.setText(R.string.login_error);
                    textError.setVisibility(View.VISIBLE);
                }
            }
        };
    }


    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(textEmail.getText().toString())) {
            textEmail.setError(getString(R.string.camporequerido));
            result = false;
        } else {
            textEmail.setError(null);
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

        textEmail.setEnabled(false);
        textSenha.setEnabled(false);

        buttonEntrarFacebook.setEnabled(false);
        buttonEntrarEmail.setEnabled(false);
        buttonRedefinirSenha.setEnabled(false);
        buttonCadastrar.setEnabled(false);
    }

    private void unlock() {
        progressBar.setVisibility(View.GONE);

        textEmail.setEnabled(true);
        textSenha.setEnabled(true);

        buttonEntrarFacebook.setEnabled(true);
        buttonEntrarEmail.setEnabled(true);
        buttonRedefinirSenha.setEnabled(true);
        buttonCadastrar.setEnabled(true);
    }


}
