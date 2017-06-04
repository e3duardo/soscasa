package br.com.magicbox.soscasa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Criado por eduardo em 5/20/17.
 */

public class BaseActivity extends AppCompatActivity {


    public double latitude = -21.1947618;
    public double longitude = -41.9047604;

    private DatabaseReference mDatabase;
    private Sessao sessao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessao = (Sessao) getIntent().getSerializableExtra("sessao");
        if (sessao == null)
            throw new NullPointerException("Esqueceu de passar o usuário como extra no intent?");

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    protected void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, EntrarActivity.class));
        finish();
    }

    protected void editProfile() {
        Intent intent = new Intent(BaseActivity.this, PerfilActivity.class);
        intent.putExtra("usuario", getSessao());
        startActivityForResult(intent, 1);
    }


    public Sessao getSessao() {
        return sessao;
    }

    public DatabaseReference getDatabase() {
        return mDatabase;
    }


}
