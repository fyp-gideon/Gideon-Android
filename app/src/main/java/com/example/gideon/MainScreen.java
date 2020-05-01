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

import adapter.ClipsViewAdapter;
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
    private ArrayList<EventsDataModel> eventsDataList, recentClipsData;
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
        recentClipsView = findViewById(R.id.recentClips);
        recentClipsData = new ArrayList<>();
        eventsDataList = new ArrayList<>();
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
            recentClipsData.clear();
            latestEventSerial = dataList.get(dataList.size() - 1).getEvent_serial();

            eventsDataList.addAll(dataList);

            for(int i = eventsDataList.size()-1; i > eventsDataList.size()-6; i--) {
                recentClipsData.add(eventsDataList.get(i));
            }

            ArrayAdapter clipAdapter = new ClipsViewAdapter(this, recentClipsData);

            recentClipsView.setAdapter(null);
            recentClipsView.setAdapter(clipAdapter);
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
