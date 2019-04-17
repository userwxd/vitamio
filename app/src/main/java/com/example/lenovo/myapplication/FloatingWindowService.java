package com.example.lenovo.myapplication;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;


import java.io.IOException;

import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;


/**
 * Created by lenovo on 2019/4/16.
 */

public class FloatingWindowService extends Service {
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;

    public FloatingWindowService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        Log.d("悬浮窗", "Build.VERSION.SDK_INT" + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            // android 8.0及以后使用
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            // android 8.0以前使用
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        //该flags描述的是窗口的模式，是否可以触摸，可以聚焦等
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 设置视频的播放窗口大小
        layoutParams.width = 800;
        layoutParams.height = 450;
        layoutParams.x = 300;
        layoutParams.y = 300;
        Vitamio.isInitialized(this);
        VideoView videoView = new VideoView(this);
        //放入网址
        videoView.setVideoURI(Uri.parse("https://raw.githubusercontent.com/dongzhong/ImageAndVideoStore/master/Bruno%20Mars%20-%20Treasure.mp4"));
        //设置控制栏
        videoView.setMediaController(new MediaController(this));
        //获取焦点
        videoView.requestFocus();
        videoView.setOnPreparedListener(new io.vov.vitamio.MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(io.vov.vitamio.MediaPlayer mp) {
                mp.setPlaybackSpeed(1.0f);
            }
        });

        windowManager.addView(videoView, layoutParams);
        videoView.start();
        videoView.setOnTouchListener(new FloatingOnTouchListener());
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showFloatingWindow(){

        if (Settings.canDrawOverlays(this)) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
         /*   display = layoutInflater.inflate(R.layout.video_display, null);
            surfaceView = display.findViewById(R.id.videoplayer_display);*/
            // 获取surfaceView的sourfaceHolder
            surfaceHolder = surfaceView.getHolder();
            final MediaPlayer mediaPlayer = new MediaPlayer();
            // 设置视频播放流类型
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 视频资源网址
            Uri uri = Uri.parse("https://raw.githubusercontent.com/dongzhong/ImageAndVideoStore/master/Bruno%20Mars%20-%20Treasure.mp4");

            try {
                // 设置视频播放资源，这里如果前面调用了MediaPlayer.create(contex, R.raw.video),就不用再次调用了
                mediaPlayer.setDataSource(this,uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    // 视频播放设置
                    mediaPlayer.setDisplay(surfaceHolder);
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 待视频资源准备好了，回调中播放视频资源，
                    mediaPlayer.start();
                    //循环播放
                    mediaPlayer.setLooping(true);
                }
            });
          /*  windowManager.addView(display, layoutParams);
            display.setOnTouchListener(new FloatingOnTouchListener());*/

        }
    }

    // touch移动视频窗口
    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    Log.d("悬浮窗", "movedX = " + movedX + ", movedY =" + movedY);
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

}
