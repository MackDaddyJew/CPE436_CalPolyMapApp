package edu.calpoly.mjew.cpe436_calpolymapapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;

/**
 * Created by Stefan on 28.11.16.
 */

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    private SignInButton mSignInGoogle;
    private Button mSignInGuest;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Log.d(TAG, "onCreate: " + this);

        Toast.makeText(SignInActivity.this, "load sign in", Toast.LENGTH_SHORT).show();

        mSignInGoogle = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInGuest = (Button) findViewById(R.id.sign_in_button_guest);

        mSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignInActivity.this, "Google login", Toast.LENGTH_SHORT).show();

            }
        });


        mSignInGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignInActivity.this, "Guest login", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SignInActivity.this, MainMapsActivity.class);
                //intent.putExtra("index", index);
                startActivity(intent);
            }
        });
    }
}
