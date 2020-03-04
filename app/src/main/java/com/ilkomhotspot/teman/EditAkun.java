package com.ilkomhotspot.teman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class EditAkun extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        ImageView img = findViewById(R.id.back);
        img.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

        ImageView simpan = findViewById(R.id.simpan);
        simpan.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
                Toast.makeText(getApplicationContext(),"Data berhasil disimpan",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
