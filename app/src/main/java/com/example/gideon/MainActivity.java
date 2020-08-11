package com.example.gideon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button signInbtn;
    EditText emailText, passwordText;
    private TextView createAccount;
    private FirebaseAuth mAuth;

    private final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeComponents();
        onListeners();
    }

    private void initializeComponents() {
        signInbtn = findViewById(R.id.signIn);
        createAccount = findViewById(R.id.createAccount);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);

        mAuth = FirebaseAuth.getInstance();
    }

    private void onListeners() {
        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!emailText.getText().toString().equals("")) && (!passwordText.getText().toString().equals("")))
                    mAuth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Incorrect Email/Password combination.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });
                else
                    Toast.makeText(MainActivity.this, "Please provide email and password.", Toast.LENGTH_SHORT).show();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpScreen.class);
                startActivity(intent);
            }
        });
    }

    private void updateUI(FirebaseUser user){
        if(user != null) {
            Intent intent = new Intent(MainActivity.this, MainScreen.class);
            startActivity(intent);
        }
    }
}
