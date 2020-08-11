package com.example.gideon;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapter.ClipsViewAdapter;
import model.EventsDataModel;
import network.GetDataService;
import network.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScreen extends AppCompatActivity {

    private Intent intent;
    private TextView signOutBtn;
    private VideoView videoView;

    private ListView clipsListView;
    private ProgressDialog progressDialog;
    private Handler handler;
    private Runnable runnable;
    private GetDataService service;
    private ArrayList<EventsDataModel> clipsData, allClipsData;
    private SwitchMaterial eventToggle;
    private TextView eventTitleText;
    private NotificationCompat.Builder builder;
    private  NotificationChannel channel;

    private int latestEventSerial;
    private int delay = 5000;
    private boolean checked = false;

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
        clipsListView = findViewById(R.id.recentClips);
        videoView = findViewById(R.id.videoFeed);
        eventToggle = findViewById(R.id.event_toggle);
        eventTitleText = findViewById(R.id.event_title_text);

        MediaController mediaController = new MediaController(videoView.getContext());
        videoView.setMediaController(mediaController);

        allClipsData = new ArrayList<>();
        clipsData = new ArrayList<>();
        handler = new Handler();

        // Notification Builder
        builder = new NotificationCompat.Builder(this, "1")
                    .setSmallIcon(R.drawable.ic_next)
                    .setContentTitle("Alert")
                    .setContentText("New Event Occurred.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alert..";
            String description = "New Event..";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void onListeners() {
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

        clipsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventsDataModel event = clipsData.get(i);
                videoView.setVideoPath(event.getClip_url());
                videoView.start();
            }
        });

        eventToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    eventTitleText.setText("All Events");
                    generateDataList(allClipsData);
                }
                else {
                    eventTitleText.setText("Recent");
                    checked = true;
                    generateDataList(allClipsData);
                }
            }
        });
    }

    private void setRetrofitInstance(){
        progressDialog = new ProgressDialog(MainScreen.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

//        Create handle for the RetrofitInstance interface
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<EventsDataModel>> call = service.getAllData();
        call.enqueue(new Callback<List<EventsDataModel>>() {
            @Override
            public void onResponse(Call<List<EventsDataModel>> call, Response<List<EventsDataModel>> response) {
                progressDialog.dismiss();
                if(response.body() != null){
                    allClipsData.addAll(response.body());
                    generateDataList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<EventsDataModel>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("MainScreen", Objects.requireNonNull(t.getMessage()));
                Toast.makeText(MainScreen.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(List<EventsDataModel> dataList){
        if(dataList.size() != 0) {
            clipsData.clear();

            latestEventSerial = dataList.get(dataList.size()-1).getEvent_serial();

            if(eventToggle.isChecked())
                clipsData.addAll(dataList);
            else {
                if(allClipsData.size() > 5)
                    for (int i = allClipsData.size() - 1; i >= allClipsData.size() - 5; i--)
                        clipsData.add(allClipsData.get(i));
                else
                    for (int i = allClipsData.size() - 1; i >= 0; i--)
                        clipsData.add(allClipsData.get(i));
            }

            ArrayAdapter<EventsDataModel> clipAdapter = new ClipsViewAdapter(this, clipsData);
            clipsListView.setAdapter(null);
            clipsListView.setAdapter(clipAdapter);
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
        Call<List<EventsDataModel>> call;

        call = service.getUpdatedDataList("/api/events/"+latestEventSerial+"?fetchUpdated=true");

        call.enqueue(new Callback<List<EventsDataModel>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<EventsDataModel>> call, Response<List<EventsDataModel>> response) {
                progressDialog.dismiss();
                if(response.body() != null){

                    if(response.body().size() > 0){
                        if(response.body().get(0).getEvent_serial() > latestEventSerial) {
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainScreen.this);
                            notificationManager.notify(Integer.parseInt(channel.getId()), builder.build());
                        }

                        allClipsData.addAll(response.body());
                        generateDataList(allClipsData);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<EventsDataModel>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("MainScreen", Objects.requireNonNull(t.getMessage()));
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
    public void onBackPressed() {
        // Disable Back Button.
    }
}
