package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by Stefan on 28.11.16.
 */

public class SignInActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = SignInActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private SignInButton mSignInGoogle;
    private Button mSignInGuest;
    private Button mSignOutGoogle;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Log.d(TAG, "onCreate: " + this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("xxxxx")
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Toast.makeText(SignInActivity.this, "load sign in", Toast.LENGTH_SHORT).show();

        mSignInGoogle = (SignInButton) findViewById(R.id.sign_in_button);
        mSignOutGoogle = (Button) findViewById(R.id.sign_out_button_google);
        mSignInGuest = (Button) findViewById(R.id.sign_in_button_guest);

        setGooglePlusButtonText(mSignInGoogle, "Sign in with Google");

        mSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Google sign in" + this);
                Toast.makeText(SignInActivity.this, "Hello [GOOGLE ACCOUNT]", Toast.LENGTH_SHORT).show();
                signIn();
            }
        });

        mSignOutGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Google sign out" + this);
                Toast.makeText(SignInActivity.this, "Goodbye [GOOGLE ACCOUNT]", Toast.LENGTH_SHORT).show();
                signOut();
            }
        });

        mSignInGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Guest sign in" + this);
                Toast.makeText(SignInActivity.this, "Hello Guest!", Toast.LENGTH_SHORT).show();
                loadMainActivity();
            }
        });
    }

    private void signIn() {
        Log.d(TAG, "signIn: " + this);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            //Toast.makeText(SignInActivity.this, "signed", Toast.LENGTH_SHORT).show();
            loadMainActivity();

            /*mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);*/
        } else {
            // Signed out, show unauthenticated UI.
            //Toast.makeText(SignInActivity.this, "not signed", Toast.LENGTH_SHORT).show();
            //loadMainActivity();
            //updateUI(false);
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public void loadMainActivity() {
        Intent intent = new Intent(SignInActivity.this, MainMapsActivity.class);
        //intent.putExtra("index", index);
        startActivity(intent);
    }

    protected void setGooglePlusButtonText(SignInButton signInButton,
                                           String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextSize(15);
                tv.setTypeface(null, Typeface.NORMAL);
                tv.setText(buttonText);
                return;
            }
        }
    }
}
