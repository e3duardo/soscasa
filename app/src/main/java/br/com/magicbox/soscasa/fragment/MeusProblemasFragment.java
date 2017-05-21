package br.com.magicbox.soscasa.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import br.com.magicbox.soscasa.ClienteActivity;
import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.adapter.ProblemaAdapter;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.viewholder.ProblemaViewHolder;

public class MeusProblemasFragment extends Fragment {

    private FirebaseRecyclerAdapter<Problema, ProblemaViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    private ClienteActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meus_problemas, container, false);

        activity = (ClienteActivity) getActivity();

        mManager = new LinearLayoutManager(activity);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);

        mRecycler = (RecyclerView) view.findViewById(R.id.problemas_list);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(mManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        final Query query = activity.getDatabase().child("problemas")
                .orderByChild("cliente").equalTo(activity.getUsuario().getUid());

        mRecycler.setAdapter(new ProblemaAdapter(activity, query, activity.getUsuario()));

//        Toast.makeText(getActivity(), "sequence1", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

}
