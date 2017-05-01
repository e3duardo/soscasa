package br.com.magicbox.soscasa.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Post;
import br.com.magicbox.soscasa.models.Problema;


public class ProblemaViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;

    public ProblemaViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.problema_area);
        authorView = (TextView) itemView.findViewById(R.id.problema_text);
    }

    public void bindToPost(Problema problema) {
        titleView.setText(problema.getArea().getNome());
        authorView.setText(problema.getDescricao());
    }
}
