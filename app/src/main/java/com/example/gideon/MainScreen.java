package com.example.gideon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainScreen extends AppCompatActivity {

    private Intent intent;
    private TextView personalBtn, addCameraBtn, signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        initializeComponents();
        onListeners();
    }

    private void initializeComponents() {
        signOutBtn = findViewById(R.id.signOut);
        personalBtn = findViewById(R.id.personal);
        addCameraBtn = findViewById(R.id.addCamera);
    }

    private void onListeners() {
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

        personalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainScreen.this, PersonalScreen.class);
                startActivity(intent);
            }
        });

        addCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainScreen.this, AddCameraScreen.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Disable Back Button.
    }
}
