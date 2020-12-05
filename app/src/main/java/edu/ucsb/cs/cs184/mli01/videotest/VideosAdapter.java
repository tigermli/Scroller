package edu.ucsb.cs.cs184.mli01.videotest;

import android.content.Context;
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
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private List<VideoItem> videoItems;
    private static Context context;
    private static HttpProxyCacheServer proxy;
    private static HashMap<String, MediaItem> oldToProxyURL;

    public VideosAdapter(List<VideoItem> videoItems, Context context, HttpProxyCacheServer proxy) {
        this.videoItems = videoItems;
        this.context = context;
        this.proxy = proxy;
        this.oldToProxyURL = new HashMap<String, MediaItem>();
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

        holder.player.seekTo(0);
        holder.player.play();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VideoViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.player.pause();
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.setVideoData(videoItems.get(position));
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {

        PlayerView playerView;
        TextView textVideoTitle, textVideoDescription;
        SimpleExoPlayer player;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.videoView);
            textVideoTitle = itemView.findViewById(R.id.textVideoTitle);
            textVideoDescription = itemView.findViewById(R.id.textVideoDescription);

            DefaultTrackSelector trackSelector = new DefaultTrackSelector(context);
            DefaultLoadControl loadControl = new DefaultLoadControl
                    .Builder()
                    .setBufferDurationsMs(25000, 100000, 1500, 2000)
                    .build();
            player = new SimpleExoPlayer.
                    Builder(context).
                    setTrackSelector(trackSelector).
                    setLoadControl(loadControl).
                    build();

            player.setRepeatMode(Player.REPEAT_MODE_ONE);
            player.setPlayWhenReady(false);
            playerView.setPlayer(player);

            // video fill entire screen
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);

            // tap to pause/play
            playerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (player.isPlaying()) {
                        player.pause();
                    } else {
                        player.play();
                    }
                }
            });

            // hide controller
            playerView.hideController();
            playerView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
                @Override
                public void onVisibilityChange(int i) {
                    if(i == 0) {
                        playerView.hideController();
                    }
                }
            });
        }

        void setVideoData(VideoItem videoItem) {
            String oldURL = videoItem.videoURL;

            if (!oldToProxyURL.containsKey(oldURL)) {
                oldToProxyURL.put(oldURL, MediaItem.fromUri(proxy.getProxyUrl(oldURL)));
            }
            player.setMediaItem(oldToProxyURL.get(oldURL));
            player.prepare();

            textVideoTitle.setText(videoItem.videoTitle);
            textVideoDescription.setText(videoItem.videoDescription);
        }
    }
}
