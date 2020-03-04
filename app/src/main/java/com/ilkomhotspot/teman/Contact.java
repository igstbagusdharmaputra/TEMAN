package com.ilkomhotspot.teman;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;




//keperluan untuk google signIn

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
//end

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Contact extends AppCompatActivity implements SensorEventListener{
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private static final int SHAKE_THRESHOLD = 0;
    Queue<Double> move = new LinkedList<Double>();
    private float x,y,z;
    private FirebaseAuth mAuth;
    private ProgressBar progressbar;
    private ImageView lingkaran;
    private ListView listView;
    private List<String> personList;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);
        listView = (ListView)findViewById(R.id.list_person);
        personList = new ArrayList<>();
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        ImageView img =  (ImageView) findViewById(R.id.settings);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings();
            }
        });

        ImageView tambah = (ImageView) findViewById(R.id.tambah);
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(Contact.this);
                AlertDialog.Builder a_builder = new AlertDialog.Builder(Contact.this);
                a_builder.setMessage("Masukkan username kontak yang akan menerima notifikasi")
                        .setCancelable(false)
                        .setView(input)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                ImageView image = new ImageView(Contact.this);
                                RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.activity_akun);
                                image.setBackgroundResource(R.drawable.foto_kecil);
                                myLayout.addView(image);

                                personList.add(input.getText().toString());
                                adapter.notifyDataSetChanged();
                                dialog.cancel();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = a_builder.create();
                alert.show();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        TextView nama = findViewById(R.id.nama);
        lingkaran = findViewById(R.id.lingkaran);
        nama.setText(mAuth.getCurrentUser().getDisplayName());
        progressbar = findViewById(R.id.progressbar);

        new DownloadImageFromInternet(lingkaran).execute(mAuth.getCurrentUser().getPhotoUrl().toString());

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, personList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    protected void settings(){
        Intent intent = new Intent(this, EditAkun.class);
        startActivity(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
        }
        double magnitude = Math.sqrt(x * x + y * y + z * z);
        if (move.size() < 15)
            move.add(magnitude);
        else
        {
            double magnitudeAverage = 0;
            for(Double temp : move){
                magnitudeAverage += temp;
            }
            magnitudeAverage = magnitudeAverage/move.size();

            if(magnitude/magnitudeAverage > 3.5){
                //TextView text = (TextView)findViewById(R.id.hasil);
                //text.setText(""+magnitudeAverage);
                move.clear();
                Intent intent = new Intent(this, Terjatuh.class);
                startActivity(intent);
            }
            else{
                move.remove();
                move.add(magnitude);
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            //Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();


        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
            progressbar.setVisibility(View.INVISIBLE);
            lingkaran.setVisibility(View.VISIBLE);
        }
    }

}
