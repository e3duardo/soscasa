package br.com.magicbox.soscasa.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import br.com.magicbox.soscasa.ProblemaClienteActivity;
import br.com.magicbox.soscasa.ProblemaProfissionalActivity;
import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.Usuario;


public class ProblemaViewHolder extends RecyclerView.ViewHolder {

    private TextView tvArea;
    private TextView tvDescricao;
    private TextView tvStatus;
    private TextView tvLine2;
    private View item;
    private Activity activity;

    private Usuario usuario;


    public ProblemaViewHolder(Activity activity, View itemView, Usuario usuario) {
        super(itemView);

        this.tvArea = (TextView) itemView.findViewById(R.id.text_item_problema_area);
        this.tvDescricao = (TextView) itemView.findViewById(R.id.text_item_problema_descricao);
        this.tvStatus = (TextView) itemView.findViewById(R.id.text_item_problema_status);
        this.tvLine2 = (TextView) itemView.findViewById(R.id.text_item_problema_line2);
        this.item = itemView;
        this.usuario = usuario;
        this.activity = activity;
    }

    public void bindToView(final Problema problema) {

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("areas").child(problema.getAreaUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvArea.setText(dataSnapshot.getValue(Area.class).getNome());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tvDescricao.setText(problema.getDescricao());
        tvStatus.setText(problema.getStatus().getI18n());



        if (problema.getSolicitadoEm() != null)
            tvLine2.setText(DateFormat.getDateInstance().format(problema.getSolicitadoEm()));

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = null;


                if (usuario.getEhProfissional())
                    intent = new Intent(activity, ProblemaProfissionalActivity.class);
                else
                    intent = new Intent(activity, ProblemaClienteActivity.class);

                intent.putExtra("problema", problema);
                intent.putExtra("usuario", usuario);
                activity.startActivityForResult(intent, 1);

            }
        });
    }
}
