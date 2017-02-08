package com.example.shubhamgarg.groupplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.Call;
import android.text.Layout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Shubham on 30-06-2016.
 */
public class MySplash extends Activity {

    private static int sec = 2000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashlayout);

        startAnimations();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MySplash.this, MainActivity.class));
                finish();
                }
        },sec);
    }
    private void startAnimations(){
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.alpha);
        anim.reset();
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout);
        layout.clearAnimation();
        layout.startAnimation(anim);


        anim = AnimationUtils.loadAnimation(this,R.anim.alpha);
        anim.reset();
        ImageView splash = (ImageView)findViewById(R.id.splash);
        splash.clearAnimation();
        splash.startAnimation(anim);
            }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(this.isFinishing()){
//        Animation anim = AnimationUtils.loadAnimation(this,R.anim.translate);
//        anim.reset();
//        RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout);
//        layout.clearAnimation();
//        layout.startAnimation(anim);
//    }}
}
