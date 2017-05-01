package br.com.magicbox.soscasa.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class AntigaRecentPostsFragment extends AntigaPostListFragment {

    public AntigaRecentPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child("posts")
                .limitToFirst(100);
        // [END recent_posts_query]

        return recentPostsQuery;
    }
}
