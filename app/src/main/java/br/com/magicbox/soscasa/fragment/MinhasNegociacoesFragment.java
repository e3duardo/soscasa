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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import br.com.magicbox.soscasa.NegociacaoActivity;
import br.com.magicbox.soscasa.ProfissionalActivity;
import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.viewholder.NegociacaoViewHolder;

public class MinhasNegociacoesFragment extends Fragment {

    private FirebaseRecyclerAdapter<Negociacao, NegociacaoViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    private ProfissionalActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (ProfissionalActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_minhas_negociacoes, container, false);

        mRecycler = (RecyclerView) view.findViewById(R.id.negociacoes_profissional_list);
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

        Query postsQuery = getQuery(activity.getDatabase());

        mAdapter = new FirebaseRecyclerAdapter<Negociacao, NegociacaoViewHolder>(Negociacao.class, R.layout.item_negociacao,
                NegociacaoViewHolder.class, postsQuery) {

            @Override
            protected Negociacao parseSnapshot(DataSnapshot snapshot) {
                Negociacao negociacao = super.parseSnapshot(snapshot);
                negociacao.setUid(snapshot.getKey());
                return negociacao;
            }

            @Override
            protected void populateViewHolder(final NegociacaoViewHolder viewHolder, final Negociacao model, final int position) {

                viewHolder.bindToPost(activity.getDatabase(), model);

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), NegociacaoActivity.class);
                        intent.putExtra("negociacao", model);
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
        return databaseReference.child("negociacoes")
                .orderByChild("profissional").equalTo(activity.getUsuario().getUid());
    }
}
