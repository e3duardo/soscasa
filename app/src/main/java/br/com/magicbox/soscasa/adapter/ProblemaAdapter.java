package br.com.magicbox.soscasa.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.Sessao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.viewholder.ProblemaViewHolder;

/**
 * Criado por eduardo em 5/21/17.
 */

public class ProblemaAdapter extends RecyclerView.Adapter {

    private Sessao sessao;
    private List<Problema> problemas;
    private Activity activity;

    public ProblemaAdapter(Activity activity, List<Problema> problemas, Sessao sessao) {
        this.problemas = problemas;
        this.activity = activity;
        this.sessao = sessao;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.item_problema, parent, false);

        return new ProblemaViewHolder(activity, view, sessao);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ProblemaViewHolder holder = (ProblemaViewHolder) viewHolder;

        Problema problema = problemas.get(position);

        holder.bindToView(problema);


    }

    @Override
    public int getItemCount() {
        return problemas.size();
    }
}
