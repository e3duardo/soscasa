package br.com.magicbox.soscasa.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
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

import br.com.magicbox.soscasa.EntrarActivity;
import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.Util;

import static android.content.ContentValues.TAG;


public class LoginFragment extends Fragment {

    private LoginButton entrarFacebook;
    private Button entrarEmail;
    private TextView cadastrar;
    private Button client;
    private Button prof;

    private ProgressBar progressBar;
    private FragmentActivity myContext;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private CallbackManager callbackManager;

    private EditText email;
    private EditText senha;
    private Button continuar;
    private TextView redefinirSenha;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        fragmentManager = myContext.getSupportFragmentManager();
        callbackManager = CallbackManager.Factory.create();


        email = (EditText) view.findViewById(R.id.editText_login_email);
        senha = (EditText) view.findViewById(R.id.editText_login_senha);
        continuar = (Button) view.findViewById(R.id.button_login_email);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        entrarFacebook = (LoginButton) view.findViewById(R.id.button_entrar_facebook);
        entrarFacebook.setReadPermissions("email");
        entrarFacebook.setFragment(this);
        entrarFacebook.setReadPermissions("email", "public_profile");
        entrarFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getContext(), "facebook login", Toast.LENGTH_LONG).show();

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        entrarEmail = (Button) view.findViewById(R.id.button_login_email);
        entrarEmail.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               if (!validateForm()) {
                                                   return;
                                               }

                                               mAuth.signInWithEmailAndPassword(email.getText().toString(), senha.getText().toString())
                                                       .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<AuthResult> task) {
                                                               Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());

                                                               if (task.isSuccessful()) {
                                                                   Util.onAuthSuccess(getActivity(), mDatabase, task.getResult().getUser());
                                                               } else {
                                                                   Toast.makeText(getActivity(), "Sign In Failed",
                                                                           Toast.LENGTH_SHORT).show();
                                                               }
                                                           }
                                                       });

                                           }
                                       }
        );

        client = (Button) view.findViewById(R.id.client);
        client.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

                                               mAuth.signInWithEmailAndPassword("msn@msn.com", "123123")
                                               .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<AuthResult> task) {
                                                               Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                                                                   progressBar.setVisibility(View.VISIBLE);

                                                               if (task.isSuccessful()) {
                                                                   Util.onAuthSuccess(getActivity(), mDatabase, task.getResult().getUser());
                                                               } else {
                                                                   Toast.makeText(getActivity(), "Sign In Failed",
                                                                           Toast.LENGTH_SHORT).show();
                                                               }
                                                           }
                                                       });

                                           }
                                       }
        );


        prof = (Button) view.findViewById(R.id.prof);
        prof.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {

                                          mAuth.signInWithEmailAndPassword("yahoo@yahoo.com", "123123")
                                                  .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                                      @Override
                                                      public void onComplete(@NonNull Task<AuthResult> task) {
                                                          Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                                                          progressBar.setVisibility(View.VISIBLE);
                                                          if (task.isSuccessful()) {
                                                              Util.onAuthSuccess(getActivity(), mDatabase, task.getResult().getUser());

                                                          } else {
                                                              Toast.makeText(getActivity(), "Sign In Failed",
                                                                      Toast.LENGTH_SHORT).show();
                                                          }
                                                      }
                                                  });

                                      }
                                  }
        );


        cadastrar = (TextView) view.findViewById(R.id.button_cadastrar);
        cadastrar.setOnClickListener(new View.OnClickListener() {
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

        redefinirSenha = (TextView) view.findViewById(R.id.redefinir_senha);
        redefinirSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Snackbar mySnackbar = Snackbar.make(getView(), "Sucesso", 200);
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View promptsView = inflater.inflate(R.layout.dialog_reset, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());

                alertDialogBuilder.setView(promptsView);

                final TextView userInput = (TextView) promptsView
                        .findViewById(R.id.email_reset);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mAuth
                                        .sendPasswordResetEmail( userInput.getText().toString() )
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()) {
//                                                            Toast.makeText(getActivity(),"Recuperação de acesso iniciada. Email enviado.", Toast.LENGTH_LONG).show();
                                                            mySnackbar.show();
                                                        }
                                                        else
                                                        Toast.makeText(
                                                                getActivity(),
                                                                "Falhou! Tente novamente",
                                                                Toast.LENGTH_SHORT
                                                        ).show();
                                                    }
                                                });
                                    }
                                });
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


        return view;
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Util.onAuthSuccess(getActivity(), mDatabase, task.getResult().getUser());
                } else {
                    Toast.makeText(getActivity(), "Sign In Failed",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    private boolean validateForm() {
        boolean result = true;
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
