package com.example.gideon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import model.EventsDataModel;
import network.GetDataService;
import network.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScreen extends AppCompatActivity {

    private Intent intent;
    private TextView personalBtn, addCameraBtn, signOutBtn;

    private ListView recentClipsView;
    private ProgressDialog progressDialog;
    private Handler handler;
    private Runnable runnable;
    private int delay = 5000;
    private GetDataService service;
    private List<String> dataString;
    private int latestEventSerial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        initializeComponents();
        onListeners();
        setRetrofitInstance();
    }

    private void initializeComponents() {
        signOutBtn = findViewById(R.id.signOut);
        personalBtn = findViewById(R.id.personal);
        addCameraBtn = findViewById(R.id.addCamera);
        recentClipsView = findViewById(R.id.recentClips);
        dataString = new ArrayList<>();
        handler = new Handler();
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

    private void setRetrofitInstance(){
        progressDialog = new ProgressDialog(MainScreen.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        /*Create handle for the RetrofitInstance interface*/
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<EventsDataModel>> call = service.getAllData();
        call.enqueue(new Callback<List<EventsDataModel>>() {
            @Override
            public void onResponse(Call<List<EventsDataModel>> call, Response<List<EventsDataModel>> response) {
                progressDialog.dismiss();
                if(response.body() != null)
                    generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<EventsDataModel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainScreen.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(List<EventsDataModel> dataList){
        if(dataList.size() != 0) {
            latestEventSerial = dataList.get(dataList.size() - 1).getEvent_serial();
            for (EventsDataModel i : dataList) {
                dataString.add(i.toString());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataString);
            recentClipsView.setAdapter(arrayAdapter);
        }
    }

    private void setTimerData(){
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, delay);
                getUpdatedData();
            }
        }, delay);
    }

    private void getUpdatedData(){
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<EventsDataModel>> call = service.getUpdatedDataList("/api/events/"+latestEventSerial+"?fetchUpdated=true");
        call.enqueue(new Callback<List<EventsDataModel>>() {
            @Override
            public void onResponse(Call<List<EventsDataModel>> call, Response<List<EventsDataModel>> response) {
                progressDialog.dismiss();
                if(response.body() != null)
                    generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<EventsDataModel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainScreen.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, delay);
                getUpdatedData();
            }
        }, delay);
        super.onResume();
    }
    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        // Disable Back Button.
    }
}
