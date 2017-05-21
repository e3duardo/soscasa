package br.com.magicbox.soscasa;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import br.com.magicbox.soscasa.fragment.MeuPerfilFragment;
import br.com.magicbox.soscasa.fragment.MinhasNegociacoesFragment;
import br.com.magicbox.soscasa.fragment.ProcurarProblema;

public class ProfissionalActivity extends BaseActivity {


    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profissional);

        fragmentManager = getSupportFragmentManager();
        fragment = new ProcurarProblema();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container_profissional, fragment).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation_profissional);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.procurar_problema:
                                fragment = new ProcurarProblema();
                                break;
                            case R.id.meus_problemas_profissional:
                                fragment = new MinhasNegociacoesFragment();
                                break;
                            case R.id.meu_perfil_profissional:
                                fragment = new MeuPerfilFragment();
                                break;
                        }
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_container_profissional, fragment).commit();
                        return true;
                    }
                });
    }

}
