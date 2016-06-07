package br.com.amaro.manoel.homeautomation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

/**
 * Created by manoel on 29/05/16.
 */

public class AuthActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    @Inject
    FirebaseAuth mAuth;

    private FirebaseUser mUser;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getApplication()).getComponent().inject(this);

        mAuth.addAuthStateListener(this);
        mUser = mAuth.getCurrentUser();

        if (mUser == null)
            startLogin();
        else
            onCreateAuthenticated(savedInstanceState);
    }

    protected void onCreateAuthenticated(Bundle savedInstanceState) {

    }

    private void startLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        mUser = firebaseAuth.getCurrentUser();
        if (mUser == null)
            startLogin();

    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public FirebaseUser getmUser() {
        return mUser;
    }

}
