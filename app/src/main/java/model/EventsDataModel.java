package model;

import com.google.gson.annotations.SerializedName;

public class EventsDataModel {
    @SerializedName("_id")
    private String id;
    @SerializedName("event_label")
    private String event_label;
    @SerializedName("event_serial")
    private int event_serial;
    @SerializedName("date_time")
    private String date_time;
    @SerializedName("normal_probability")
    private float normal_probability;
    @SerializedName("abnormal_probability")
    private float abnormal_probability;
    @SerializedName("image_url")
    private String image_url;
    @SerializedName("clip_url")
    private String clip_url;

    public EventsDataModel(String id, String event_label, int event_serial, String date_time, float normal_probability, float abnormal_probability, String image_url, String clip_url) {
        this.id = id;
        this.event_label = event_label;
        this.event_serial = event_serial;
        this.date_time = date_time;
        this.normal_probability = normal_probability;
        this.abnormal_probability = abnormal_probability;
        this.image_url = image_url;
        this.clip_url = clip_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent_label() {
        return event_label;
    }

    public void setEvent_label(String event_label) {
        this.event_label = event_label;
    }

    public int getEvent_serial() {
        return event_serial;
    }

    public void setEvent_serial(int event_serial) {
        this.event_serial = event_serial;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public float getNormal_probability() {
        return normal_probability;
    }

    public void setNormal_probability(float normal_probability) {
        this.normal_probability = normal_probability;
    }

    public float getAbnormal_probability() {
        return abnormal_probability;
    }

    public void setAbnormal_probability(float abnormal_probability) {
        this.abnormal_probability = abnormal_probability;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getClip_url() {
        return clip_url;
    }

    public void setClip_url(String clip_url) {
        this.clip_url = clip_url;
    }

    @Override
    public String toString() {
        return  "event_serial = " + event_serial + '\n' +
                "date_time = " + date_time + '\n' +
                "normal_probability = " + normal_probability + '\n' +
                "abnormal_probability = " + abnormal_probability + '\n';
    }
}
