package br.com.magicbox.soscasa.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.Usuario;
import br.com.magicbox.soscasa.viewholder.ProblemaViewHolder;

/**
 * Created by eduardo on 5/21/17.
 */

public class ProblemaAdapter extends RecyclerView.Adapter {

    private Usuario usuario;
    private List<Problema> problemas;
    private Activity activity;

    public ProblemaAdapter(Activity activity, List<Problema> problemas, Usuario usuario){
        this.problemas = problemas;
        this.activity = activity;
        this.usuario = usuario;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.item_problema, parent, false);
        return new ProblemaViewHolder(activity, view, usuario);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ProblemaViewHolder holder = (ProblemaViewHolder) viewHolder;

        Problema livro  = problemas.get(position) ;

        holder.bindToView(livro);


    }

    @Override
    public int getItemCount() {
        return problemas.size();
    }
}
