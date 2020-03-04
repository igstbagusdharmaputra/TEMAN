package com.ilkomhotspot.teman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilkomhotspot.teman.R;



public class Splash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        this.splash();
    }

    protected void splash(){
        TextView tv;
        TextView tv1;
        ImageView iv;

        tv = (TextView) findViewById(R.id.tv);
        tv1 = (TextView) findViewById(R.id.tv1);
        iv = (ImageView)  findViewById(R.id.iv);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);

        tv.startAnimation(myanim);
        iv.startAnimation(myanim);
        tv1.startAnimation(myanim);

        final Intent  i = new Intent(this,MainActivity.class);
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };

        timer.start();
    }

}
