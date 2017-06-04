package br.com.magicbox.soscasa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import br.com.magicbox.soscasa.fragment.ProcurarProblema;

public class ProfissionalActivity extends BaseLocationActivity {


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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_profissional, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profissional_logout:
                logout();
                return true;
            case R.id.action_profissional_negociacoes:
                Intent intent = new Intent(ProfissionalActivity.this, MinhasNegociacoesActivity.class);
                intent.putExtra("usuario", getUsuario());
                startActivityForResult(intent, 1);
                return true;
            case R.id.action_profissional_profile:
                editProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
