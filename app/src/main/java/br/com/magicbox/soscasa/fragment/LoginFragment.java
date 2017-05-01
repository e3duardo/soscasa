package br.com.magicbox.soscasa.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.SignInButton;

import br.com.magicbox.soscasa.R;


public class LoginFragment extends Fragment {


    private LoginButton entrarFacebook;
    private SignInButton entrarGoogle;
    private Button entrarEmail;
    private Button cadastrar;

    private FragmentActivity myContext;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private CallbackManager callbackManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        fragmentManager = myContext.getSupportFragmentManager();
        callbackManager = CallbackManager.Factory.create();

        entrarFacebook = (LoginButton) view.findViewById(R.id.button_entrar_facebook);
        entrarFacebook.setReadPermissions("email");
        entrarFacebook.setFragment(this);
        entrarFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getContext(), "facebook login", Toast.LENGTH_LONG).show();
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

        entrarGoogle = (SignInButton) view.findViewById(R.id.button_entrar_google);
        entrarGoogle.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {


                                                  Toast.makeText(v.getContext(), "google login", Toast.LENGTH_LONG).show();
                                              }
                                          }
        );

        entrarEmail = (Button) view.findViewById(R.id.button_entrar_email);
        entrarEmail.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  fragment = new LoginEmailFragment();

                                                  final FragmentTransaction transaction = fragmentManager.beginTransaction();
                                                  transaction.replace(R.id.entrar_container, fragment);
                                                  transaction.addToBackStack(fragment.getClass().getName());
                                                  transaction.commit();
                                              }
                                          }
        );

        cadastrar = (Button) view.findViewById(R.id.button_cadastrar);
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

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }



}
