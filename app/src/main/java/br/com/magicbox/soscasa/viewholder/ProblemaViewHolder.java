package br.com.magicbox.soscasa.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;

import br.com.magicbox.soscasa.ProblemaClienteActivity;
import br.com.magicbox.soscasa.ProblemaProfissionalActivity;
import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.Sessao;
import br.com.magicbox.soscasa.models.Problema;


public class ProblemaViewHolder extends RecyclerView.ViewHolder {

    private TextView tvArea;
    private TextView tvDescricao;
    private TextView tvStatus;
    private TextView tvLine2;
    private View item;
    private Activity activity;

    private Sessao sessao;


    public ProblemaViewHolder(Activity activity, View itemView, Sessao sessao) {
        super(itemView);

        this.tvArea = (TextView) itemView.findViewById(R.id.text_item_problema_area);
        this.tvDescricao = (TextView) itemView.findViewById(R.id.text_item_problema_descricao);
        this.tvStatus = (TextView) itemView.findViewById(R.id.text_item_problema_status);
        this.tvLine2 = (TextView) itemView.findViewById(R.id.text_item_problema_line2);
        this.item = itemView;
        this.sessao = sessao;
        this.activity = activity;
    }

    public void bindToView(final Problema problema) {

        tvArea.setText(sessao.getAreaBy(problema.getAreaUid()).getNome());
        tvDescricao.setText(problema.getDescricao());
        tvStatus.setText(problema.getStatus().getI18n());


        if (problema.getSolicitadoEm() != null)
            tvLine2.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(problema.getSolicitadoEm()));

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;

                if (sessao.usuarioEhProfissional())
                    intent = new Intent(activity, ProblemaProfissionalActivity.class);
                else
                    intent = new Intent(activity, ProblemaClienteActivity.class);

                intent.putExtra("problema", problema);
                intent.putExtra("sessao", sessao);
                activity.startActivityForResult(intent, 1);

            }
        });
    }
}
