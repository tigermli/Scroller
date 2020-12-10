package edu.ucsb.cs.cs184.mli01.videotest;

import android.content.Context;
import android.util.Log;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

public class CacheDataSourceFactory implements DataSource.Factory {
    private final Context context;
    private final DefaultDataSourceFactory defaultDatasourceFactory;
    private final long maxFileSize, maxCacheSize;

    private DataSource source;

    public CacheDataSourceFactory(Context context, long maxCacheSize, long maxFileSize) {
        super();
        this.context = context;
        this.maxCacheSize = maxCacheSize;
        this.maxFileSize = maxFileSize;
        this.source = null;

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(this.context).build();
        defaultDatasourceFactory = new DefaultDataSourceFactory(this.context,
                bandwidthMeter,
                new DefaultHttpDataSourceFactory("Scroller"));
    }

    @Override
    public DataSource createDataSource() {
        Log.i("YOLO", "CREATE\t"+String.valueOf(source == null));

        if (source == null) {
            LeastRecentlyUsedCacheEvictor evictor = new LeastRecentlyUsedCacheEvictor(maxCacheSize);
            SimpleCache simpleCache = new SimpleCache(new File(context.getCacheDir(), "media"), evictor);
            source = new CacheDataSource(simpleCache, defaultDatasourceFactory.createDataSource(),
                    new FileDataSource(), new CacheDataSink(simpleCache, maxFileSize),
                    CacheDataSource.FLAG_BLOCK_ON_CACHE | CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null);
        }
        return source;
    }
}