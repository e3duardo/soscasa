package br.com.magicbox.soscasa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import br.com.magicbox.soscasa.ClienteActivity;
import br.com.magicbox.soscasa.ProblemaActivity;
import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.Usuario;
import br.com.magicbox.soscasa.viewholder.ProblemaViewHolder;

public class MeusProblemasFragment extends Fragment {

    private FirebaseRecyclerAdapter<Problema, ProblemaViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    private ClienteActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (ClienteActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_meus_problemas, container, false);

        mRecycler = (RecyclerView) view.findViewById(R.id.problemas_list);
        mRecycler.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mManager = new LinearLayoutManager(activity);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        final Query postsQuery = getQuery(activity.getDatabase());

        mAdapter = new FirebaseRecyclerAdapter<Problema, ProblemaViewHolder>(Problema.class, R.layout.item_problema,
                ProblemaViewHolder.class, postsQuery) {

            @Override
            protected Problema parseSnapshot(DataSnapshot snapshot) {
                Problema problema = super.parseSnapshot(snapshot);
                problema.setUid(snapshot.getKey());
                return problema;
            }

            @Override
            protected void populateViewHolder(final ProblemaViewHolder viewHolder, final Problema model, final int position) {
                viewHolder.bindToPost(activity.getDatabase(), model);

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ProblemaActivity.class);
                        intent.putExtra("problema", model);
                        startActivity(intent);
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("problemas").orderByChild("cliente").equalTo(activity.getUsuario().getUid());
    }

}
