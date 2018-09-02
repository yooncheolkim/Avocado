package com.planet.avocado.managers;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.planet.avocado.data.local.User;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.subjects.SingleSubject;

public class LoginManager {
    private static final String TAG = "LoginManager";
    private static LoginManager sInstance = null;

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        if (sInstance == null) {
            synchronized (LoginManager.class) {
                if (sInstance == null) {
                    sInstance = new LoginManager();
                }
            }
        }

        return sInstance;
    }

    public Single<Boolean> isLoggedIn() {
        Log.d(TAG, "isLoggedIn: ");

        SingleSubject<Boolean> result = SingleSubject.create();

        FirebaseUser currentUser = FirebaseAuth.getInstance()
                .getCurrentUser();

        result.onSuccess(currentUser != null);

        return result.delay(1, TimeUnit.SECONDS);
    }

    public void signout() {
        Log.d(TAG, "signout: ");
        FirebaseAuth.getInstance().signOut();
    }

    public User getUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance()
                .getCurrentUser();
        return new User(currentUser.getDisplayName(), currentUser.getPhotoUrl().toString());
    }
}
