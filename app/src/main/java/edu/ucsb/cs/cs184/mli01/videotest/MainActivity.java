package edu.ucsb.cs.cs184.mli01.videotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private VideosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<VideoItem> videoItems = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        JsonReader.loadAWSKeys();
        JSONArray awsKeys = null;
        JSONArray awsTitles = null;
        JSONArray awsDescriptions = null;
        List<String> videoKeys = new ArrayList<>();
        List<String> videoTitles = new ArrayList<>();
        List<String> videoDescriptions = new ArrayList<>();
        try {
            awsKeys = (JSONArray) JsonReader.awsKeys.get("cs184keys");
            awsTitles = (JSONArray) JsonReader.awsKeys.get("cs184titles");
            awsDescriptions = (JSONArray) JsonReader.awsKeys.get("cs184descriptions");
            for (int i = 0; i < awsKeys.length(); i++){
                videoKeys.add( (String)awsKeys.get(i)  );
                videoTitles.add( (String)awsTitles.get(i)  );
                videoDescriptions.add( (String)awsDescriptions.get(i)  );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        VideoItem itema = new VideoItem();
//        itema.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/cdm9.mp4";
//        itema.videoTitle = "Title";
//        itema.videoDescription = "Description";
//        videoItems.add(itema);

        for (int i = 0; i < VideosAdapter.batchCount; i++) {
            VideoItem item = new VideoItem();
            item.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/" + videoKeys.get(i);
            item.videoTitle = videoTitles.get(i);
            item.videoDescription = videoDescriptions.get(i);
            videoItems.add(item);
        }

        adapter = new VideosAdapter(videoItems, getApplicationContext(), videoKeys, videoTitles, videoDescriptions);

        ViewPager2 videosViewPager = findViewById(R.id.videosViewPager);
        videosViewPager.setAdapter(adapter);

        Log.i("YOLO", "CREATED");
    }

    @Override
    protected void onDestroy() {
        adapter.releasePlayer();
        super.onDestroy();
    }
}