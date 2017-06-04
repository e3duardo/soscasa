package br.com.magicbox.soscasa;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Usuario;

/**
 * Criado por eduardo em 01/05/17.
 */

public class Util {

    public static void onAuthSuccessFirebase(final Activity context, final DatabaseReference mDatabase, final FirebaseUser user) {

        mDatabase.child("usuarios")
                .child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        onAuthSuccessUsuario(context, mDatabase, user, dataSnapshot.getValue(Usuario.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }


    public static void onAuthSuccessUsuario(final Activity context, final DatabaseReference mDatabase, final FirebaseUser user, final Usuario usuario) {
        cadastrarUsuario(mDatabase, user.getUid(), usuario);

        final Intent intent;
        if (usuario.getEhProfissional()) {
            intent = new Intent(context, ProfissionalActivity.class);

        } else {
            intent = new Intent(context, ClienteActivity.class);
        }


        mDatabase.child("areas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Area> areas = new ArrayList<>();

                for (DataSnapshot area : dataSnapshot.getChildren()) {
                    areas.add(area.getValue(Area.class));
                }

                Sessao sessao = new Sessao();
                sessao.setUsuario(usuario);
                sessao.setAreas(areas);

                intent.putExtra("sessao", sessao);
                context.startActivity(intent);
                context.finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void cadastrarUsuario(DatabaseReference mDatabase, String usuarioId, Usuario usuario) {
        mDatabase.child("usuarios").child(usuarioId).setValue(usuario);
        usuario.setUid(usuarioId);
    }
}
