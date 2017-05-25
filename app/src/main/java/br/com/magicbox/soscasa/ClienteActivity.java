package br.com.magicbox.soscasa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;

import br.com.magicbox.soscasa.adapter.ProblemaAdapter;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.viewholder.ProblemaViewHolder;

public class ClienteActivity extends BaseActivity  {


    private Fragment fragment;
    private FragmentManager fragmentManager;


    private FirebaseRecyclerAdapter<Problema, ProblemaViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    CoordinatorLayout layout;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        mManager = new LinearLayoutManager(ClienteActivity.this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);

        mRecycler = (RecyclerView) findViewById(R.id.problemas_list);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(mManager);

        layout = (CoordinatorLayout) findViewById(R.id.activity_client);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent  intent = new Intent(v.getContext(), NovoProblemaActivity.class);
                startActivityForResult(intent, 1);


                //todo:mensagem certa na snackbar
                Snackbar.make(layout,  "This is a simple Snackbar", Snackbar.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onResume();

        final Query query = getDatabase().child("problemas")
                .orderByChild("cliente").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());


        mRecycler.setAdapter(new ProblemaAdapter(ClienteActivity.this, query, getUsuario()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_cliente, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                //todo: acao de sair

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, EntrarActivity.class));
                finish();
                return true;
            case R.id.action_profile:
                //todo: acao de sair

                Intent  intent = new Intent(ClienteActivity.this, PerfilActivity.class);
                startActivityForResult(intent, 1);


                //todo:mensagem certa na snackbar
                Snackbar.make(layout,  "This is a simple Snackbar", Snackbar.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}

