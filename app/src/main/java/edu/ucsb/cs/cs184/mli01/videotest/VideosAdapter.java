package edu.ucsb.cs.cs184.mli01.videotest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

//    public static final Integer batchCount = 3;
//    private List<String> videoKeys;

    public List<VideoItem> videoItems;
    public int currentIndex;
    public static SimpleExoPlayer exoPlayer;
    private static Context context;
    private static HttpProxyCacheServer proxy;
    public static ViewPager2 videosViewPager;

    public VideosAdapter(List<VideoItem> videoItems, Context context) {
        this.videoItems = videoItems;
        this.currentIndex = 0;
        this.context = context;
        this.exoPlayer = createExoplayer();
        this.proxy = new HttpProxyCacheServer.Builder(context).build();
        this.videosViewPager = null;

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

    public void setPager(ViewPager2 videosViewPager) {
        this.videosViewPager=videosViewPager;
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
        exoPlayer.setPlaybackParameters(new PlaybackParameters(1f));

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
        // make it look infinite
        return Integer.MAX_VALUE;
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {

        PlayerView playerView;
        TextView textVideoTitle, textVideoDescription;
        FloatingActionButton slowMoFab, likeFab, commentFab;
        boolean isLiked;
        float currentVolume;

        @SuppressLint("ClickableViewAccessibility")
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.videoView);
            textVideoTitle = itemView.findViewById(R.id.textVideoTitle);
            textVideoDescription = itemView.findViewById(R.id.textVideoDescription);
            isLiked = false;
            currentVolume = 0;

            slowMoFab = itemView.findViewById(R.id.slowButton);
            slowMoFab.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_POINTER_DOWN: {
                            // prevent recycler view from scrolling if fab click drags
                            if (videosViewPager != null) {
                                videosViewPager.setUserInputEnabled(false);
                            }

                            currentVolume = exoPlayer.getVolume();
                            exoPlayer.setVolume(0f);
                            slowMoFab.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.buttonSloMoPress)));
                            exoPlayer.setPlaybackParameters(new PlaybackParameters(0.2f));
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_POINTER_UP: {
                            exoPlayer.setPlaybackParameters(new PlaybackParameters(1f));
                            slowMoFab.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.buttonSloMo)));
                            exoPlayer.setVolume(currentVolume);

                            // re-enable scrolling
                            if (videosViewPager != null) {
                                videosViewPager.setUserInputEnabled(true);
                            }
                            break;
                        }
                    }
                    return false;
                }
            });

            likeFab = itemView.findViewById(R.id.likeButton);
            likeFab.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_POINTER_DOWN: {
                            // prevent recycler view from scrolling if fab click drags
                            if (videosViewPager != null) {
                                videosViewPager.setUserInputEnabled(false);
                            }
                            isLiked = !isLiked;
                            int color = isLiked ? R.color.buttonLike : R.color.white;
                            likeFab.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(color)));
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_POINTER_UP: {
                            // re-enable scrolling
                            if (videosViewPager != null) {
                                videosViewPager.setUserInputEnabled(true);
                            }
                            break;
                        }
                    }
                    return false;
                }
            });

            // click screen to alternate pause/play
            playerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (exoPlayer.isPlaying()) {
                        exoPlayer.pause();
                    } else {
                        exoPlayer.play();
                    }
                }
            });

            // fill screen with video by height/width depending on orientation
            int orientation = context.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            } else {
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
            }

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
            // add red accent backslash to front of titles
            String titleString = "<font color=#e74c3c>/</font> " + videoItem.videoTitle;
            textVideoTitle.setText(Html.fromHtml(titleString));
            textVideoDescription.setText(videoItem.videoDescription);

            isLiked = videoItem.isLiked;
            if (isLiked) {
                likeFab.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.buttonLike)));
            }
            Log.i("YOLO", "SET DATA");
        }
    }
}