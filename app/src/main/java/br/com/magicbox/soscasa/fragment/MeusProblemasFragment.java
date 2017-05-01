package br.com.magicbox.soscasa.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.viewholder.ProblemaViewHolder;

public class MeusProblemasFragment extends Fragment {

    private DatabaseReference mDatabase;

    private FirebaseRecyclerAdapter<Problema, ProblemaViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meus_problemas, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = (RecyclerView) view.findViewById(R.id.problemas_list);
        mRecycler.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        Query postsQuery = getQuery(mDatabase);

        mAdapter = new FirebaseRecyclerAdapter<Problema, ProblemaViewHolder>(Problema.class, R.layout.item_problema,
                ProblemaViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final ProblemaViewHolder viewHolder, final Problema model, final int position) {
                final DatabaseReference postRef = getRef(position);

                viewHolder.bindToPost(model);
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

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("problemas");
    }

}
