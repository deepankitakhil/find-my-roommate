package com.akhil.findmyroommate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

/**
 * Created by akhil on 12/26/2016.
 */

public class LoginActivity extends AppCompatActivity {

    public static final String USER_NAME = "";
    private static int RC_SIGN_IN = 0;
    private static String TAG = "MAIN_ACTIVITY";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                    Log.d("AUTH", "user logged in: " + user.getEmail());
                else
                    Log.d("AUTH", "user logged out.");
            }
        };
        login();
    }

    private void login() {
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
                            ))
                            .build(), RC_SIGN_IN);
        } else {
            Log.d(TAG, firebaseAuth.getCurrentUser().getEmail());
            startActivityIntent(WelcomeUserActivity.class);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    private void startActivityIntent(Class className) {
        Intent intent = new Intent(this, className);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            intent.putExtra(USER_NAME, FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null)
            firebaseAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            startActivityIntent(WelcomeUserActivity.class);
        }

    }
}
