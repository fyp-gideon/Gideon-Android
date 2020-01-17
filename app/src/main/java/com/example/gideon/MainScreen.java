package com.example.gideon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainScreen extends AppCompatActivity {

    Button signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        initializeComponents();
        onListeners();
    }

    private void initializeComponents() {
        signOutBtn = findViewById(R.id.signOutBtn);
    }

    private void onListeners() {
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
