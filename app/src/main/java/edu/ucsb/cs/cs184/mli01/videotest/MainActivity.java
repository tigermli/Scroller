package edu.ucsb.cs.cs184.mli01.videotest;

import android.os.Bundle;
import android.os.StrictMode;
import com.google.android.exoplayer2.SimpleExoPlayer;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends AppCompatActivity {

    private VideosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        long savedSeek = 0;
        int savedIndex = 0;
        if (savedInstanceState != null) {
            savedSeek = savedInstanceState.getLong("seek");
            savedIndex = savedInstanceState.getInt("index");
        }

        List<VideoItem> videoItems = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        JsonReader.loadAWSKeys();
        try {
            JSONArray awsKeys = (JSONArray) JsonReader.awsKeys.get("cs184keys");
            JSONArray awsTitles = (JSONArray) JsonReader.awsKeys.get("cs184titles");
            JSONArray awsDescriptions = (JSONArray) JsonReader.awsKeys.get("cs184descriptions");

            for (int i = 0; i < awsKeys.length(); i++){
                VideoItem item = new VideoItem();
                item.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/" + awsKeys.get(i);
                item.videoTitle = ((String) awsTitles.get(i)).trim();
                item.videoDescription = ((String) awsDescriptions.get(i)).trim();
                item.seek = (i+2 == savedIndex) ? savedSeek : 0;
                item.isLiked = false;
                videoItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new VideosAdapter(videoItems, getApplicationContext());
        ViewPager2 videosViewPager = findViewById(R.id.videosViewPager);
        videosViewPager.setAdapter(adapter);

        adapter.setPager(videosViewPager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SimpleExoPlayer player = adapter.exoPlayer;
        if (player != null) {
            long seek = player.getCurrentPosition();
            int index = adapter.currentIndex;

            outState.putLong("seek", seek);
            outState.putInt("index", index);
        }
    }

    @Override
    protected void onDestroy() {
        adapter.releasePlayer();
        super.onDestroy();
    }
}