package com.example.gideon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpScreen extends AppCompatActivity {

    private ImageButton backBtn;
    private EditText nameText, emailText, passwordText, confirmPasswordText;
    Button createAccount;

    private FirebaseAuth mAuth;
    private final String TAG = SignUpScreen.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        initializeComponents();
        onListeners();
    }

    private void initializeComponents(){
        backBtn = findViewById(R.id.backBtn);
        nameText = findViewById(R.id.name);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        confirmPasswordText = findViewById(R.id.confirmPassword);
        createAccount = findViewById(R.id.createAccount);
        mAuth = FirebaseAuth.getInstance();
    }

    private void onListeners(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUI();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordText.getText().toString().equals(confirmPasswordText.getText().toString())){
                    mAuth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                            .addOnCompleteListener(SignUpScreen.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignUpScreen.this, "Incorrect Email or Password format.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void updateUI(){
        Intent intent = new Intent(SignUpScreen.this, MainActivity.class);
        startActivity(intent);
    }
}
