package br.com.magicbox.soscasa;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import br.com.magicbox.soscasa.fragment.LoginFragment;

public class EntrarActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.entrar_container, new LoginFragment()).commit();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//       // Check auth on Activity start
//        if (mAuth.getCurrentUser() != null) {
//         onAuthSuccessFirebase(mAuth.getCurrentUser());
//       }
//    }

}
