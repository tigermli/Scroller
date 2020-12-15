package edu.ucsb.cs.cs184.mli01.videotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 videosViewPager;
    private VideosAdapter adapter;
    private static Bundle mBundleRecyclerViewState;

    private final String KEY_RECYCLER_STATE = "recycler_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<VideoItem> videoItems = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        JsonReader.loadAWSKeys();
        JSONArray awsKeys = null;
        List<String> videoKeys = new ArrayList<>();
        System.out.println("mouse" + JsonReader.awsKeys);
        try {
            awsKeys = (JSONArray) JsonReader.awsKeys.get("cs184keys");
            for (int i = 0; i < awsKeys.length(); i++){
                videoKeys.add( (String)awsKeys.get(i)  );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 7; i++) {
            VideoItem item = new VideoItem();
            item.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/" + videoKeys.get(i);
            item.videoTitle = "Title";
            item.videoDescription = "Description";
            videoItems.add(item);
        }

        adapter = new VideosAdapter(videoItems, getApplicationContext());
        videosViewPager = findViewById(R.id.videosViewPager);
        videosViewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // restore RecyclerView state
//        if (mBundleRecyclerViewState != null) {
//
//        }
        Log.i("SWOLO", "RESUME");
        Log.i("SWOLO", ""+(adapter.exoPlayer == null));
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("SWOLO", "PAUSE");
    }

    @Override
    protected void onDestroy() {
        adapter.releasePlayer();
        super.onDestroy();
    }
}