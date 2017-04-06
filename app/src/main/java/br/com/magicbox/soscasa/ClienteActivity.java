package br.com.magicbox.soscasa;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import br.com.magicbox.soscasa.fragment.DefinirProblemaFragment;
import br.com.magicbox.soscasa.fragment.MeuPerfilFragment;
import br.com.magicbox.soscasa.fragment.MeusProblemasFragment;

public class ClienteActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        fragmentManager = getSupportFragmentManager();
        fragment = new DefinirProblemaFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container, fragment).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.definir_problema:
                                fragment = new DefinirProblemaFragment();
                                break;
                            case R.id.meus_problemas:
                                fragment = new MeusProblemasFragment();
                                break;
                            case R.id.meu_perfil:
                                fragment = new MeuPerfilFragment();
                                break;
                        }
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_container, fragment).commit();
                        return true;
                    }
                });
    }
}

