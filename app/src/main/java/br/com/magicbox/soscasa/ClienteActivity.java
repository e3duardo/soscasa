package br.com.magicbox.soscasa;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.magicbox.soscasa.adapter.ProblemaAdapter;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.StatusProblema;

public class ClienteActivity extends BaseLocationActivity {

    public static int RESULT_PROBLEMA_APROVADO = 5;
    public static int RESULT_PROBLEMA_CRIADO = 1;
    public static int RESULT_PROBLEMA_CANCELADO = 2;
    public static int RESULT_USUARIO_ALTERADO = 3;
    //public static int RESULT_VOLTAR = Activity.RESULT_CANCELED;
    public static int RESULT_NEGOCIACAO_APROVADA = 4;

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private List<Problema> problemas;
    private CoordinatorLayout layout;
    private FloatingActionButton fab;

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
                Intent intent = new Intent(v.getContext(), NovoProblemaActivity.class);
                intent.putExtra("sessao", getSessao());
                startActivityForResult(intent, 1);
            }
        });

        problemas = new ArrayList<>();

        getDatabase().child("problemas")
                .orderByChild("cliente").equalTo(getSessao().getUsuarioUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Problema problema = data.getValue(Problema.class);
                    problema.setUid(data.getKey());
                    if (StatusProblema.SOLICITADO.equals(problema.getStatus()) || StatusProblema.PENDENTE.equals(problema.getStatus())) {
                        if (!problemas.contains(problema)) {
                            problemas.add(problema);
                            mRecycler.setAdapter(new ProblemaAdapter(ClienteActivity.this, problemas, getSessao()));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        getDatabase().child("problemas")
//                .orderByChild("cliente").equalTo(getSessao().getUsuarioUid()).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Problema problema = dataSnapshot.getValue(Problema.class);
//                problema.setUid(dataSnapshot.getKey());
//                if (StatusProblema.SOLICITADO.equals(problema.getStatus()) || StatusProblema.PENDENTE.equals(problema.getStatus())) {
//                    if (!problemas.contains(problema)) {
//                        problemas.add(problema);
//                        mRecycler.setAdapter(new ProblemaAdapter(ClienteActivity.this, problemas, getSessao()));
//                    }
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Problema problema = dataSnapshot.getValue(Problema.class);
//                problema.setUid(dataSnapshot.getKey());
//                if (!StatusProblema.SOLICITADO.equals(problema.getStatus()) && !StatusProblema.PENDENTE.equals(problema.getStatus())) {
//                    problemas.remove(problema);
//                } else {
//                    if (!problemas.contains(problema)) {
//                        problemas.add(problema);
//                    }else{
//                        problemas.remove(problema);
//                        problemas.add(problema);
//                    }
//                }
//                mRecycler.setAdapter(new ProblemaAdapter(ClienteActivity.this, problemas, getSessao()));
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
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
                logout();
                return true;
            case R.id.action_profile:
                editProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == ClienteActivity.RESULT_PROBLEMA_CRIADO) {
                Snackbar.make(layout, R.string.problema_criado, Snackbar.LENGTH_LONG).show();
            }
            if (resultCode == ClienteActivity.RESULT_PROBLEMA_CANCELADO) {
                final Problema problema1 = (Problema) data.getSerializableExtra("problema");
                final StatusProblema statusAntigo = (StatusProblema) data.getSerializableExtra("statusAntigo");

                Snackbar.make(layout, R.string.problema_cancelado, Snackbar.LENGTH_LONG).setAction(R.string.desfazer, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDatabase().child("problemas").child(problema1.getUid()).child("status").setValue(statusAntigo);
                    }
                }).show();
            }
            if (resultCode == ClienteActivity.RESULT_PROBLEMA_APROVADO) {
                final Problema problema1 = (Problema) data.getSerializableExtra("problema");
                final StatusProblema statusAntigo = (StatusProblema) data.getSerializableExtra("statusAntigo");

                Snackbar.make(layout, R.string.problema_aprovado, Snackbar.LENGTH_LONG).setAction(R.string.desfazer, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDatabase().child("problemas").child(problema1.getUid()).child("status").setValue(statusAntigo);
                    }
                }).show();
            }
            if (resultCode == ClienteActivity.RESULT_USUARIO_ALTERADO) {
                Snackbar.make(layout, R.string.perfil_atualizado, Snackbar.LENGTH_LONG).show();
            }
//            if (resultCode == ClienteActivity.RESULT_VOLTAR) {
//
//            }
        }
    }
}

