package br.com.magicbox.soscasa.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Mensagem;


public class MensagemViewHolder extends RecyclerView.ViewHolder {

    //private TextView tvUsuario;
    private TextView tvMensagem;
    private View viewDestinatario = itemView.findViewById(R.id.l_mensagem_destinatatio);
    private View viewRemetente = itemView.findViewById(R.id.l_mensagem_remetente);

    public MensagemViewHolder(View itemView) {
        super(itemView);


    }

    public void bindToView(Mensagem mensagem) {

        if (mensagem.getUsuarioUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            //tvUsuario = (TextView) itemView.findViewById(R.id.tv_mensavem_usuario_remetente);
            tvMensagem = (TextView) itemView.findViewById(R.id.tv_mensagem_texto_remetente);
            viewRemetente.setVisibility(View.VISIBLE);
            viewDestinatario.setVisibility(View.GONE);

        } else {
            //tvUsuario = (TextView) itemView.findViewById(R.id.tv_mensavem_usuario_destinatario);
            tvMensagem = (TextView) itemView.findViewById(R.id.tv_mensagem_texto_destinatario);
            viewRemetente.setVisibility(View.GONE);
            viewDestinatario.setVisibility(View.VISIBLE);
        }

        //tvUsuario.setText(mensagem.getUsuarioUid());
        tvMensagem.setText(mensagem.getMensagem());

    }
}
