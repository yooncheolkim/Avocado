package com.planet.avocado.repos;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseUtils {
    public static DatabaseReference getProductRef() {
        return FirebaseDatabase.getInstance().getReference("products");
    }

    public static DatabaseReference getUserRef() {
        return FirebaseDatabase.getInstance().getReference("users");
    }

    public static DatabaseReference getCommentRef() {
        return FirebaseDatabase.getInstance().getReference("comments");
    }
}
