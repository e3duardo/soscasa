package br.com.magicbox.soscasa.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.fragment.dummy.DummyContent;
import br.com.magicbox.soscasa.fragment.dummy.DummyContent.DummyItem;

public class MeusProblemasFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problema_list, container, false);


        return view;
    }

}
