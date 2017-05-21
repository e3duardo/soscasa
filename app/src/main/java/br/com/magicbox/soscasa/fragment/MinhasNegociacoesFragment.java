package br.com.magicbox.soscasa.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import br.com.magicbox.soscasa.ProfissionalActivity;
import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.adapter.NegociacaoAdapter;
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

        mManager = new LinearLayoutManager(activity);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        Query negociacoes = activity.getDatabase().child("negociacoes")
                .orderByChild("profissional").equalTo(activity.getUsuario().getUid());

        mRecycler.setAdapter(new NegociacaoAdapter(activity, negociacoes, activity.getUsuario()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }
}
