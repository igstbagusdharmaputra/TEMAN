package com.ilkomhotspot.teman;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class Terjatuh extends AppCompatActivity {
    private int i = 0,timer = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi);
        final ImageView no = (ImageView) findViewById(R.id.no);
        final TextView waktu = (TextView) findViewById(R.id.timer);
        final ImageView yes = (ImageView) findViewById(R.id.yes);
        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        CountDownTimer time  = new CountDownTimer(7000, 1000) {

            public void onTick(long millisUntilFinished) {
                vibrator.vibrate(800);
                timer--;
                waktu.setText(""+timer);
                no.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        cancel();
                        finish();
                    }
                });

                yes.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View c){
                        finish();
                    }
                });
            }

            public void onFinish() {
                finish();
            }

        }.start();
    }

}