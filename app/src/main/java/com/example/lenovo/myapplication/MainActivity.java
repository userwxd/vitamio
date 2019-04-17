package com.example.lenovo.myapplication;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.CenterLayout;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;


public class MainActivity extends AppCompatActivity {


    private CenterLayout mDd;
    private TextView netSpeedTv;
    private TextView percentTv;
    private VideoView videoView;
    private Button mChangeLayout;
    private String url1 = "http://112.253.22.157/17/z/z/y/u/zzyuasjwufnqerzvyxgkuigrkcatxr/hc.yinyuetai.com/D046015255134077DDB3ACA0D7E68D45.flv";
    private String url2 = "http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4";
    private String url3 = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";
    private String url4 = "http://42.96.249.166/live/388.m3u8";
    private String url5 = "http://live.3gv.ifeng.com/zixun.m3u8";
    private int mVideoLayout = 0;
    private CustomMediaController customMediaController;
    private TextView downloadRateView;
    private TextView loadRateView;
    private ProgressBar pb;
    private View anchorView;
    private Activity activity;
    private TextView mDownloadRate;
    private TextView mLoadRate;
    private VideoView mVitamio;
    private ProgressBar mProbar;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(this);
        // 小窗口播放 https://www.jianshu.com/p/d50a67ba253c?nomobile=yes
        //小窗口播放2 https://blog.csdn.net/lisiwei1994/article/details/83509715
        //小窗口播放3 https://blog.csdn.net/dongzhong1990/article/details/80512706
        setContentView(R.layout.main);

        // 获取权限。如果当前获取了权限，直接就启动悬浮窗画面FloatingWindowService.class
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 1111);
        } else {
            startService(new Intent(MainActivity.this, FloatingWindowService.class));
        }

    /*     mDownloadRate = findViewById(R.id.download_rate);
       mLoadRate = findViewById(R.id.load_rate);
        videoView = findViewById(R.id.vitamio);
        mProbar = findViewById(R.id.probar);
        videoView.setMediaController(customMediaController);
        videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);//高画质
        //放入网址
        videoView.setVideoURI(Uri.parse(url2));
        //设置控制栏
        videoView.setMediaController(new MediaController(this));
        //获取焦点
        videoView.requestFocus();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setPlaybackSpeed(1.0f);
            }
        });*/
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 判断获取权限是否成功
        if (requestCode == 1111) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                startService(new Intent(MainActivity.this, FloatingWindowService.class));
            }
        }
    }

    //初始化控件
    private void initView() {
        customMediaController = new CustomMediaController(this, videoView, this);
        customMediaController.show(5000);
        customMediaController.setVideoName("白火锅 x 红火锅");
        videoView.setMediaController(customMediaController);
        videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);//高画质
        //放入网址
        videoView.setVideoURI(Uri.parse(url2));
        //设置控制栏
        videoView.setMediaController(new MediaController(this));
        //获取焦点
        videoView.requestFocus();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setPlaybackSpeed(1.0f);
            }
        });

    }


}
