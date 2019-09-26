package com.example.b.niredplay.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.example.b.niredplay.ActivityManager.BaseActivity;
import com.example.b.niredplay.R;

import cn.bmob.v3.Bmob;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        Bmob.initialize(this, "e53cd95a95fc6de0fcbcff9a705ca0dc");
        RelativeLayout layoutsplash = findViewById(R.id.splash_layout);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.1f,1.0f);
        alphaAnimation.setDuration(1000);//设置动画播放时长1000毫秒（1秒）
        layoutsplash.startAnimation(alphaAnimation);
        //设置动画监听
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            //动画结束
            @Override
            public void onAnimationEnd(Animation animation) {
                //页面的跳转
                Intent intent=new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }
}
