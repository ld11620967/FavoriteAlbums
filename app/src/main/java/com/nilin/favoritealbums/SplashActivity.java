package com.nilin.favoritealbums;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/4 0004.
 */
public class SplashActivity extends Activity {

    private final static int TIME = 1200;

    private final static int GO_HOME = 101;

    ImageView welcomeImg,imageLove;

    TextView textView;

    private Animation imgAnimation;

    private Animation textAnimation;

//    View view;

    private Intent intent = new Intent();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
                    goHome();
        }
    };

    /**
     * 进入主页
     */
    private void goHome() {
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        //启动服务
        startService(new Intent(this,PlayService.class));


        mHandler.sendEmptyMessageDelayed(GO_HOME, TIME);

        welcomeImg= (ImageView) findViewById(R.id.welcomeImg);
        imageLove= (ImageView) findViewById(R.id.welcomeLove);
        textView= (TextView) findViewById(R.id.welcomeText);

        imgAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        textAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
        welcomeImg.startAnimation(imgAnimation);
        imageLove.startAnimation(imgAnimation);
        textView.startAnimation(textAnimation);

    }
}