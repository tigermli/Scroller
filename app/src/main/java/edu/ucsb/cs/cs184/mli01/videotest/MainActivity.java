package edu.ucsb.cs.cs184.mli01.videotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private VideosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<VideoItem> videoItems = new ArrayList<>();

        VideoItem test1 = new VideoItem();
        test1.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/cuba18.mp4";
        test1.videoTitle = "Big Spike MP4";
        test1.videoDescription = "Made a big shot against the opposing team. Work hard, play hard.";
        videoItems.add(test1);

        VideoItem test2 = new VideoItem();
        test2.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/tik1.mp4";
        test2.videoTitle = "Bolley Ball";
        test2.videoDescription = "Living my best life.";
        videoItems.add(test2);

        VideoItem test3 = new VideoItem();
        test3.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/portrait1.mp4";
        test3.videoTitle = "ball hit in my face";
        test3.videoDescription = "Scott Sterling in the house";
        videoItems.add(test3);

        VideoItem test4 = new VideoItem();
        test4.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/tik1.mp4";
        test4.videoTitle = "Bolley Ball";
        test4.videoDescription = "Living my best life.";
        videoItems.add(test4);

        VideoItem test5 = new VideoItem();
        test5.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/portrait1.mp4";
        test5.videoTitle = "ball hit in my face";
        test5.videoDescription = "Scott Sterling in the house";
        videoItems.add(test5);

        VideoItem test6 = new VideoItem();
        test6.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/cuba18.mp4";
        test6.videoTitle = "Big Spike MP4";
        test6.videoDescription = "Made a big shot against the opposing team. Work hard, play hard.";
        videoItems.add(test6);

        VideoItem test7 = new VideoItem();
        test7.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/tik1.mp4";
        test7.videoTitle = "Bolley Ball";
        test7.videoDescription = "Living my best life.";
        videoItems.add(test7);

        VideoItem test8 = new VideoItem();
        test8.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/portrait1.mp4";
        test8.videoTitle = "ball hit in my face";
        test8.videoDescription = "Scott Sterling in the house";
        videoItems.add(test8);

//
//        VideoItem test1 = new VideoItem();
//        test1.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/mckib1.mp4";
//        test1.videoTitle = "Big Spike";
//        test1.videoDescription = "Made a big shot against the opposing team. Work hard, play hard.";
//        videoItems.add(test1);
//
//        VideoItem test3 = new VideoItem();
//        test3.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/mckib3.mp4";
//        test3.videoTitle = "Ball Hit in my Face";
//        test3.videoDescription = "Made a big shot against the opposing team. Work hard, play hard.";
//        videoItems.add(test3);

        adapter = new VideosAdapter(videoItems, getApplicationContext());

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