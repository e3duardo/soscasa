package br.com.magicbox.soscasa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import br.com.magicbox.soscasa.fragment.LoginFragment;

public class EntrarActivity extends AppCompatActivity /*implements View.OnClickListener */{

//    private static final String TAG = "EntrarActivity";
//
//    private DatabaseReference mDatabase;
//    private FirebaseAuth mAuth;
//
//    private EditText mEmailField;
//    private EditText mPasswordField;
//    private Button mSignInButton;
//    private Button mSignUpButton;

    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar);

        fragmentManager = getSupportFragmentManager();
        fragment = new LoginFragment();

        final FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.add(R.id.entrar_container, fragment).commit();

//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mAuth = FirebaseAuth.getInstance();
//
//        // Views
//        mEmailField = (EditText) findViewById(R.id.field_email);
//        mPasswordField = (EditText) findViewById(R.id.field_password);
//        mSignInButton = (Button) findViewById(R.id.button_sign_in);
//        mSignUpButton = (Button) findViewById(R.id.button_sign_up);
//
//        // Click listeners
//        mSignInButton.setOnClickListener(this);
//        mSignUpButton.setOnClickListener(this);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // Check auth on Activity start
//       // if (mAuth.getCurrentUser() != null) {
//       //     onAuthSuccessFirebase(mAuth.getCurrentUser());
//      //e3duardo  }
//    }
//
//    private void signIn() {
//        Log.d(TAG, "signIn");
//        if (!validateForm()) {
//            return;
//        }
//
//        showProgressDialog();
//        String email = mEmailField.getText().toString();
//        String password = mPasswordField.getText().toString();
//
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
//                        hideProgressDialog();
//
//                        if (task.isSuccessful()) {
//                            onAuthSuccessFirebase(task.getResult().getUser());
//                        } else {
//                            Toast.makeText(EntrarActivity.this, "Sign In Failed",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//    private void signUp() {
//        Log.d(TAG, "signUp");
//        if (!validateForm()) {
//            return;
//        }
//
//        showProgressDialog();
//        String email = mEmailField.getText().toString();
//        String password = mPasswordField.getText().toString();
//
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "createUsuario:onComplete:" + task.isSuccessful());
//                        hideProgressDialog();
//
//                        if (task.isSuccessful()) {
//                            onAuthSuccessFirebase(task.getResult().getUser());
//                        } else {
//                            Toast.makeText(EntrarActivity.this, "Sign Up Failed",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//    private void onAuthSuccessFirebase(FirebaseUser user) {
//        String username = usernameFromEmail(user.getEmail());
//
//        // Write new user
//        writeNewUser(user.getUid(), username, user.getEmail());
//
//        // Go to AntigaMainActivityAntiga
//        startActivity(new Intent(EntrarActivity.this, ClienteActivity.class));
//        finish();
//    }
//
//    private String usernameFromEmail(String email) {
//        if (email.contains("@")) {
//            return email.split("@")[0];
//        } else {
//            return email;
//        }
//    }
//
//    private boolean validateForm() {
//        boolean result = true;
//        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
//            mEmailField.setError("Required");
//            result = false;
//        } else {
//            mEmailField.setError(null);
//        }
//
//        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
//            mPasswordField.setError("Required");
//            result = false;
//        } else {
//            mPasswordField.setError(null);
//        }
//
//        return result;
//    }
//
//    // [START basic_write]
//    private void writeNewUser(String usuarioID, String nome, String email) {
//        Usuario usuario = new Usuario();
//
//        usuario.setNome(nome);
//        usuario.setEmail(email);
//
//        mDatabase.child("usuarios").child(usuarioID).setValue(usuario);
//    }
//    // [END basic_write]
//
//    @Override
//    public void onClick(View v) {
//        int i = v.getId();
//        if (i == R.id.button_sign_in) {
//            signIn();
//        } else if (i == R.id.button_sign_up) {
//            signUp();
//        }
//    }
}
