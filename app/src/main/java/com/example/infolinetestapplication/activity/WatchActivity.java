package com.example.infolinetestapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.infolinetestapplication.R;
import com.example.infolinetestapplication.utils.Variables;

/**
 * Created by süleyman on 31.03.2016.
 * Listede seçilmiş video yu izlediğimiz activity
 */
public class WatchActivity extends Activity {

    String videoPath;
    SurfaceView videoSurface;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            videoPath = extras.getString(Variables.VIDEO_URL_TAG);
        }

        videoSurface = (SurfaceView)findViewById(R.id.videoSurface);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(videoPath);
        } catch(Exception e) {
            e.printStackTrace();
        }

        videoSurface.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
//                System.out.println("surfaceCreated");
                mediaPlayer.setDisplay(videoSurface.getHolder());
                mediaPlayer.prepareAsync();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                System.out.println("surfaceChanged");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
//                System.out.println("surfaceDestroyed");
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

    }


    // List activity e geri yönlendirme
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(WatchActivity.this, ListActivity.class);
        startActivity(intent);
        finish();
    }
}
