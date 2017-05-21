package br.com.magicbox.soscasa.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.Usuario;


public class ProblemaViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;

    public ProblemaViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.problema_area);
        authorView = (TextView) itemView.findViewById(R.id.problema_text);
    }

    public void bindToPost(DatabaseReference mDatabase, Problema problema) {

        mDatabase.child("areas").child(problema.getAreaUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                titleView.setText(dataSnapshot.getValue(Area.class).getNome());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        authorView.setText(problema.getDescricao());
    }
}
