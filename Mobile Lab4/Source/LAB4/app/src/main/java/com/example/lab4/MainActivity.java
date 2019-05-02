package com.example.lab4;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "LogActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    //This method will call when the user clicks on login button
    public void login(View view) {
        TextView txtEmail = findViewById(R.id.txtUsername);
        String email = txtEmail.getText().toString();
        TextView txtPassword = findViewById(R.id.txtPassword);
        String password = txtPassword.getText().toString();

        TextView lblUsernameError = (TextView) findViewById(R.id.lblUsernameError);
        TextView lblPasswordError = (TextView) findViewById(R.id.lblPasswordError);
        //Setting empty string to the error labels.
        lblUsernameError.setText("");
        lblPasswordError.setText("");

        //Checking the username is empty or not.
        if(email.isEmpty())
        {
            lblUsernameError.setText("Please enter the email.");
        }
        //Checking the password is empty or not.
        else if(password.isEmpty())
        {
            lblPasswordError.setText("Please enter password");
        }
        //Validating the username and password.
        else
        {
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent redirect = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(redirect);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            lblPasswordError.setText("Invalid Username/Password.");
                        }
                    });
        }
    }

    public void register(View view) {
        Intent redirect = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(redirect);
    }
}

