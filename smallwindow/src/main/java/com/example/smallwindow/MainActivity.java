package com.example.smallwindow;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

public class MainActivity extends AppCompatActivity {

    private NiceVideoPlayer mNicevideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNicevideo = findViewById(R.id.nicevideo);
        mNicevideo.setPlayerType(NiceVideoPlayer.TYPE_IJK); // IjkPlayer or MediaPlayer
        String url = "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4";
        String drawableurl="http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-30-43.jpg";
        playvideoandsmaller(url,this,drawableurl);
    }

    public void onc(View view) {
          if (mNicevideo.isIdle()){
              Toast.makeText(this, "要点击播放后才能进入小窗口", Toast.LENGTH_SHORT).show();
          }else{
              mNicevideo.enterTinyWindow();
          }

    }
    public void playvideoandsmaller( String url, Context context,String drawableurl){
        if (mNicevideo != null&&url!=null) {
            mNicevideo.setUp(url,null);
            TxVideoPlayerController controller = new TxVideoPlayerController(context);
            controller.setLenght(98000);
            controller.setTitle("办公室小野开番外了，居然在办公室开澡堂！老板还点赞？");

            Glide.with(this)
                    .load("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-30-43.jpg")
                   /* .placeholder(R.drawable.img_default)
                    .crossFade()*/
                    .into(controller.imageView());
                    mNicevideo.setController(controller);
        }else{
            Toast.makeText(this, "nicevideoplayer与url不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }
}
