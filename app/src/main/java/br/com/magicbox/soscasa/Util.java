package br.com.magicbox.soscasa;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.magicbox.soscasa.models.Usuario;

/**
 * Created by eduardo on 01/05/17.
 */

public class Util {
    public static void onAuthSuccess(Context context, DatabaseReference mDatabase, FirebaseUser user, Usuario usuario) {
        writeNewUser(mDatabase, user.getUid(), usuario);

        if (usuario.getEhProfissional()) {
            context.startActivity(new Intent(context, ProfissionalActivity.class));
        } else {
            context.startActivity(new Intent(context, ClienteActivity.class));
        }
        //context.finish();
    }

    public static void onAuthSuccess(final Context context, final DatabaseReference mDatabase, final FirebaseUser user) {

        mDatabase.child("usuarios")
                .child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        onAuthSuccess(context, mDatabase, user, dataSnapshot.getValue(Usuario.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }


    public static void writeNewUser(DatabaseReference mDatabase, String usuarioID, Usuario usuario) {
        mDatabase.child("usuarios").child(usuarioID).setValue(usuario);
    }
}
