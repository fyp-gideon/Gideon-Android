package com.example.gideon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class AddCameraScreen extends AppCompatActivity {

    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camera_screen);

        initializeComponents();
        onListeners();
    }

    private void initializeComponents(){
        backBtn = findViewById(R.id.backBtn);
    }

    private void onListeners(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCameraScreen.this, MainScreen.class);
                startActivity(intent);
            }
        });
    }
}
