package br.com.magicbox.soscasa.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class AntigaMyPostsFragment extends AntigaPostListFragment {

    public AntigaMyPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        return databaseReference.child("user-posts")
                .child(getUid());
    }
}
