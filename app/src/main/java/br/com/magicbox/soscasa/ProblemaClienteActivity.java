package br.com.magicbox.soscasa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import br.com.magicbox.soscasa.adapter.NegociacaoAdapter;
import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.StatusProblema;
import br.com.magicbox.soscasa.models.Usuario;
import br.com.magicbox.soscasa.viewholder.NegociacaoViewHolder;

public class ProblemaClienteActivity extends BaseActivity {

    private Problema problema;
    private TextView tvArea;
    private TextView tvDescricao;
    private TextView tvStatus;

    private RecyclerView rvNegociacoes;
    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<Negociacao, NegociacaoViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problema_cliente);


        problema = (Problema) getIntent().getSerializableExtra("problema");

        tvArea = (TextView) findViewById(R.id.text_problema_profissional_area);
        tvDescricao = (TextView) findViewById(R.id.text_problema_profissional_descricao);
        tvStatus = (TextView) findViewById(R.id.tv_problema_status);

        rvNegociacoes = (RecyclerView) findViewById(R.id.rv_problema_negociacoes);
        rvNegociacoes.setHasFixedSize(true);

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        rvNegociacoes.setLayoutManager(mManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(ProblemaClienteActivity.this, "sequence1", Toast.LENGTH_SHORT).show();

        tvStatus.setText(problema.getStatus().getI18n());
        tvDescricao.setText(problema.getDescricao());

        getDatabase().child("areas").child(problema.getAreaUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvArea.setText(dataSnapshot.getValue(Area.class).getNome());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query negociacoes = getDatabase().child("negociacoes")
                .orderByChild("problema").equalTo(problema.getUid());

        rvNegociacoes.setAdapter(new NegociacaoAdapter(this, negociacoes, getUsuario(), problema));

        Toast.makeText(ProblemaClienteActivity.this, "sequence2", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_problema_cliente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancelar_problema:
                AlertDialog.Builder dialog = new AlertDialog.Builder(ProblemaClienteActivity.this);

                dialog.setTitle(R.string.atencao)
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setMessage(R.string.deseja_cancelar_problema)
                        .setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                dialoginterface.cancel();
                            }
                        })
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {

                                StatusProblema statusAntigo = problema.getStatus();
                                if (problema.getStatus() != StatusProblema.CANCELADO && problema.getStatus() != StatusProblema.RESOLVIDO) {
                                    DatabaseReference ref = getDatabase().child("problemas").child(problema.getUid());
                                    ref.child("status").setValue(StatusProblema.CANCELADO);
                                    ref.child("canceladoEm").setValue(new Date());

                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("problema", problema);
                                    returnIntent.putExtra("statusAntigo", statusAntigo);
                                    setResult(ClienteActivity.RESULT_PROBLEMA_CANCELADO, returnIntent);
                                    finish();
                                }

                            }
                        }).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

}
