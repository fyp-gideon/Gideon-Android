package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gideon.R;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.EventsDataModel;

public class ClipsViewAdapter extends ArrayAdapter<EventsDataModel> {

    private Context context;
    private List<EventsDataModel> dataList;

    public ClipsViewAdapter(Context context, ArrayList<EventsDataModel> dataList){
        super(context, 0 , dataList);
        this.context = context;
        this.dataList = dataList;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.clip_view_item, parent, false);
        }

        EventsDataModel event = dataList.get(position);

        ImageView eventThumbnail = listItem.findViewById(R.id.thumbnail);
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(event.getImage_url())
                .placeholder(R.drawable.transparent_background)
                .error(R.drawable.transparent_background)
                .into(eventThumbnail);

        TextView eventID = listItem.findViewById(R.id.event_serial);
        eventID.setText(String.format("ID: %s", event.getEvent_serial()));

        String dateTime = event.getDate_time();
        String date = dateTime.substring(0, dateTime.indexOf("T"));
        String time = dateTime.substring(dateTime.indexOf("T")+1, dateTime.indexOf("."));

        TextView eventDate = listItem.findViewById(R.id.date);
        eventDate.setText(String.format("Date: %s", date));

        TextView eventTime = listItem.findViewById(R.id.time);
        eventTime.setText(String.format("Time: %s", time));

        return listItem;
    }
}
