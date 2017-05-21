package br.com.magicbox.soscasa.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.magicbox.soscasa.R;


public class MensagemViewHolder extends RecyclerView.ViewHolder {

    public TextView tvUsuario;
    public TextView tvMensagem;

    public MensagemViewHolder(View itemView) {
        super(itemView);

        tvUsuario = (TextView) itemView.findViewById(R.id.tv_mensavem_usuario);
        tvMensagem = (TextView) itemView.findViewById(R.id.tv_mensagem_texto);
    }

    public void bindToView(String usuario, String text) {
        tvUsuario.setText(usuario);
        tvMensagem.setText(text);
    }
}
