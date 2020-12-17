package edu.ucsb.cs.cs184.mli01.videotest;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.Html;
import android.util.Log;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

//    public static final Integer batchCount = 3;
//    private List<String> videoKeys;

    public List<VideoItem> videoItems;
    public int currentIndex;
    public static SimpleExoPlayer exoPlayer;
    private static Context context;
    private static HttpProxyCacheServer proxy;

    public VideosAdapter(List<VideoItem> videoItems, Context context) {
        this.videoItems = videoItems;
        this.currentIndex = 0;
        this.context = context;
        this.exoPlayer = createExoplayer();
        this.proxy = new HttpProxyCacheServer.Builder(context).build();

        Log.i("YOLO", "ADAPTER CREATED");
    }

    private SimpleExoPlayer createExoplayer() {
        SimpleExoPlayer player = new SimpleExoPlayer.
                Builder(context).
                setTrackSelector(new DefaultTrackSelector(context)).
                setLoadControl(new DefaultLoadControl()).
                build();
        player.setRepeatMode(Player.REPEAT_MODE_ONE);
        return player;
    }

    public void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_video,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VideoViewHolder holder) {
        super.onViewAttachedToWindow(holder);

//        int position = holder.getAdapterPosition();
        currentIndex = holder.getAdapterPosition() % videoItems.size();
        VideoItem item = videoItems.get(currentIndex);

        holder.setVideoData(item);
        holder.playerView.setPlayer(exoPlayer);

        exoPlayer.setMediaItem(MediaItem.fromUri(proxy.getProxyUrl(item.videoURL)));
        exoPlayer.prepare();
        if (item.seek > 0) {
            exoPlayer.seekTo(item.seek);
            item.seek = 0;
        }
        exoPlayer.setPlayWhenReady(true);

        Log.i("YOLO", "ATTACH");
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VideoViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.playerView.setPlayer(null);

        Log.i("YOLO", "DETACH");
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
//        if ((position + 1)%VideosAdapter.batchCount == 0){
//            newBatch(position+1);
//        }
    }

//    public void newBatch(int position) {
//        for (int i = position; i < position + VideosAdapter.batchCount; i++) {
//            VideoItem item = new VideoItem();
//            item.videoURL = "https://moo123moo125.s3-us-west-2.amazonaws.com/" + videoKeys.get(i);
//            item.videoTitle = "Title";
//            item.videoDescription = "Description";
//            videoItems.add(item);
//        }
//    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {

        PlayerView playerView;
        TextView textVideoTitle, textVideoDescription;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.videoView);
            textVideoTitle = itemView.findViewById(R.id.textVideoTitle);
            textVideoDescription = itemView.findViewById(R.id.textVideoDescription);

            playerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });

            // hide controller
            playerView.hideController();
            playerView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
                @Override
                public void onVisibilityChange(int visibility) {
                    playerView.hideController();
                }
            });
        }

        void setVideoData(VideoItem videoItem) {
            String titleString = "<font color=#e74c3c>/</font> " + videoItem.videoTitle;
            textVideoTitle.setText(Html.fromHtml(titleString));
            textVideoDescription.setText(videoItem.videoDescription);
            Log.i("YOLO", "SET DATA");
        }
    }
}