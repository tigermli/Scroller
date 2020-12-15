package edu.ucsb.cs.cs184.mli01.videotest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private List<VideoItem> videoItems;
    public static SimpleExoPlayer exoPlayer;
    private static Context context;
    private static HttpProxyCacheServer proxy;

    public VideosAdapter(List<VideoItem> videoItems, Context context) {
        this.videoItems = videoItems;
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
        player.setPlayWhenReady(false);

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

        int position = holder.getAdapterPosition();
        VideoItem item = videoItems.get(position);

        holder.setVideoData(item);

        holder.playerView.setPlayer(exoPlayer);
        exoPlayer.setMediaItem(MediaItem.fromUri(proxy.getProxyUrl(item.videoURL)));
        exoPlayer.prepare();
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
        Log.i("YOLO", "BIND");
    }

    @Override
    public void onViewRecycled(@NonNull VideoViewHolder holder) {
        Log.i("YOLO", "RELEASE");
        super.onViewRecycled(holder);
    }

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

//            playerView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mp.setLooping(true);
//                }
//            });

//            playerView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    float videoRatio = mp.getVideoHeight() / (float) mp.getVideoWidth();
//                    float screenRatio = playerView.getHeight() / (float)
//                            playerView.getWidth();
//                    float scaleY = videoRatio / screenRatio;
//
//                    playerView.setScaleY(scaleY);
//
////                    if (scaleY < 1f) {
////                        playerView.setScaleY(scaleY);
////                    } else {
////                        playerView.setScaleX(1f / scaleY);
////                    }
//                }
//            });

            playerView.setControllerHideOnTouch(false);
            playerView.setControllerShowTimeoutMs(0);
        }


        void setVideoData(VideoItem videoItem) {
            textVideoTitle.setText(videoItem.videoTitle);
            textVideoDescription.setText(videoItem.videoDescription);

            Log.i("YOLO", "SET DATA");
        }
    }
}