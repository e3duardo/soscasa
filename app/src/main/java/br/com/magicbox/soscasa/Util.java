package br.com.magicbox.soscasa;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.magicbox.soscasa.models.Usuario;

/**
 * Created by eduardo on 01/05/17.
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


    public static void onAuthSuccessUsuario(Activity context, DatabaseReference mDatabase, FirebaseUser user, Usuario usuario) {
        writeNewUser(mDatabase, user.getUid(), usuario);

        Intent intent = null;
        if (usuario.getEhProfissional()) {
            intent = new Intent(context, ProfissionalActivity.class);

        } else {
            intent = new Intent(context, ClienteActivity.class);
        }
        intent.putExtra("usuario", usuario);
        context.startActivity(intent);
        context.finish();
    }




    public static void writeNewUser(DatabaseReference mDatabase, String usuarioId, Usuario usuario) {
        mDatabase.child("usuarios").child(usuarioId).setValue(usuario);
        usuario.setUid(usuarioId);
    }
}
