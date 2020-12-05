package edu.ucsb.cs.cs184.mli01.videotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.ListObjectsV2Result;
//import com.amazonaws.services.s3.model.S3ObjectSummary;

import com.danikula.videocache.HttpProxyCacheServer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 videosViewPager;
    private HttpProxyCacheServer proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videosViewPager = findViewById(R.id.videosViewPager);

//        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();
//        ListObjectsV2Result result = s3.listObjectsV2("moo123moo125");
//        List<S3ObjectSummary> objects = result.getObjectSummaries();
//        for (S3ObjectSummary os : objects) {
//            Log.i("item", "* " + os.getKey());
//        }

        List<VideoItem> videoItems = new ArrayList<>();

        VideoItem test9 = new VideoItem();
        test9.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/cuba18.mp4";
        test9.videoTitle = "Big Spike MP4";
        test9.videoDescription = "Made a big shot against the opposing team. Work hard, play hard.";
        videoItems.add(test9);

        VideoItem test10 = new VideoItem();
        test10.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/tik1.mp4";
        test10.videoTitle = "Bolley Ball";
        test10.videoDescription = "Living my best life.";
        videoItems.add(test10);

        VideoItem test1 = new VideoItem();
        test1.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/mckib1.mp4";
        test1.videoTitle = "Big Spike";
        test1.videoDescription = "Made a big shot against the opposing team. Work hard, play hard.";
        videoItems.add(test1);

        VideoItem test2 = new VideoItem();
        test2.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/portrait1.mp4";
        test2.videoTitle = "ball hit in my face";
        test2.videoDescription = "Scott Sterling in the house";
        videoItems.add(test2);

        VideoItem test3 = new VideoItem();
        test3.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/mckib3.mp4";
        test3.videoTitle = "Ball Hit in my Face";
        test3.videoDescription = "Made a big shot against the opposing team. Work hard, play hard.";
        videoItems.add(test3);

        proxy = newProxy();
        videosViewPager.setAdapter(new VideosAdapter(videoItems, getApplicationContext(), proxy));
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(8L * 1024L * 1024L * 1024L)
                .build();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // somehow pause all videos
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        proxy.shutdown();
    }
}