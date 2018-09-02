package com.planet.avocado.ui.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.planet.avocado.R;
import com.planet.avocado.managers.LoginManager;
import com.planet.avocado.ui.base.BaseActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 101;
    private CompositeDisposable mDisposables = new CompositeDisposable();
    private View mLayoutGoogleLoginBtn;
    private View mLayoutFacebookLoginBtn;
    private SimpleDraweeView mDraweeLogo;
    private CallbackManager mFacebookCallbackManager;
    private LoginButton mFacebookLoginBtn;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initFacebookLogin();
        initGoogleLogin();
        initLogoAnimation();
        checkLoginStatus();
    }

    private void initGoogleLogin() {
        Log.d(TAG, "initGoogleLogin: ");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mLayoutGoogleLoginBtn.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    private void initFacebookLogin() {
        Log.d(TAG, "initFacebookLogin: ");

        mFacebookLoginBtn = new LoginButton(this);
        mFacebookLoginBtn.setVisibility(View.GONE);
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginBtn.setReadPermissions("email", "public_profile");
        mFacebookLoginBtn.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                onLoginFailure();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                onLoginFailure();
            }
        });

        ViewGroup layout = findViewById(R.id.layout_login);
        layout.addView(mFacebookLoginBtn);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        com.facebook.login.LoginManager.getInstance().logOut();
                        onLoginSuccess();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        onLoginFailure();
                    }
                });
    }

    private void onLoginFailure() {
        Log.d(TAG, "onLoginFailure: ");
        Toast.makeText(this, "Login Failure", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }

    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.d(TAG, "handleGoogleSignInResult: ");
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            onGoogleLoginSuccess(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            onLoginFailure();
        }
    }

    private void onGoogleLoginSuccess(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        onLoginSuccess();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        onLoginFailure();
                    }
                });
    }

    private void initLogoAnimation() {
        Log.d(TAG, "initLogoAnimation: ");

        Context ctx = this;
        Resources resources = ctx.getResources();
        int resId = R.drawable.avocado;
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId));
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        mDraweeLogo.setController(controller);
    }

    private void initViews() {
        Log.d(TAG, "initViews: ");

        mLayoutGoogleLoginBtn = findViewById(R.id.layout_googleLoginBtn);
        mLayoutFacebookLoginBtn = findViewById(R.id.layout_facebookLoginBtn);
        mDraweeLogo = findViewById(R.id.drawee_logo);

        mLayoutFacebookLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFacebookLoginBtn != null) {
                    mFacebookLoginBtn.performClick();
                }
            }
        });
    }

    private void onLoginSuccess() {
        Log.d(TAG, "onLoginSuccess: ");
        gotoMainActivity();
    }

    private void checkLoginStatus() {
        Log.d(TAG, "checkLoginStatus: ");

        mDisposables.add(
                LoginManager.getInstance()
                        .isLoggedIn()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isLoggedIn -> {
                            if (isLoggedIn) {
                                gotoMainActivity();
                            } else {
                                showLoginButtons();
                            }
                        })
        );
    }

    private void showLoginButtons() {
        Log.d(TAG, "showLoginButtons: ");
        TranslateAnimation anim = new TranslateAnimation(0,
                0,
                200,
                0);
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        mLayoutFacebookLoginBtn.startAnimation(anim);
        mLayoutGoogleLoginBtn.startAnimation(anim);
        mLayoutFacebookLoginBtn.setVisibility(View.VISIBLE);
        mLayoutGoogleLoginBtn.setVisibility(View.VISIBLE);
    }

    private void gotoMainActivity() {
        Log.d(TAG, "gotoMainActivity: ");

        ContextCompat.startActivity(this,
                new Intent(this, MainActivity.class),
                null);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

        mDisposables.dispose();
    }
}
