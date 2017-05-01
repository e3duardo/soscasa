package br.com.magicbox.soscasa;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import br.com.magicbox.soscasa.models.Usuario;

/**
 * Created by eduardo on 01/05/17.
 */

public class Util {
    public static void onAuthSuccess(Context context, DatabaseReference mDatabase, FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(mDatabase, user.getUid(), username, user.getEmail());

        // Go to AntigaMainActivityAntiga
        context.startActivity(new Intent(context, ClienteActivity.class));
        //context.finish();
    }

    private static String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private static void writeNewUser(DatabaseReference mDatabase, String usuarioID, String nome, String email) {
        Usuario usuario = new Usuario();

        usuario.setNome(nome);
        usuario.setEmail(email);

        mDatabase.child("usuarios").child(usuarioID).setValue(usuario);
    }
}
