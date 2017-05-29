package br.com.magicbox.soscasa.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Problema;


public class ProblemaViewHolder extends RecyclerView.ViewHolder {

    public TextView tvArea;
    public TextView tvDescricao;
    public TextView tvStatus;
    public TextView tvLine2;


    public ProblemaViewHolder(View itemView) {
        super(itemView);

        tvArea = (TextView) itemView.findViewById(R.id.tv_item_problema_area);
        tvDescricao = (TextView) itemView.findViewById(R.id.tv_item_problema_descricao);
        tvStatus = (TextView) itemView.findViewById(R.id.tv_item_problema_status);
        tvLine2 = (TextView) itemView.findViewById(R.id.tv_item_problema_line2);
    }

    public void bindToView(Problema problema) {

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

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        if(problema.getSolicitadoEm() != null)
            tvLine2.setText(sdf.format(problema.getSolicitadoEm()));
    }
}
