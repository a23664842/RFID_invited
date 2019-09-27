package com.example.invited;

import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.Button;
import java.io.IOException;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import android.content.Context;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((Button) findViewById(R.id.button2))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        writeevent();
                        Intent intent = new Intent();
                        intent.setClass(AddActivity.this  ,AddcontentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("number",Integer.valueOf(((TextView) findViewById(R.id.jnum)).getText().toString()) );//傳遞Double
                        bundle.putInt("cnum",0);
                        intent.putExtras(bundle);
                        AddActivity.this.finish();
                        startActivity(intent);
                    }

    });
    }

    public void writeevent()
    {
        String eve = ((TextView) findViewById(R.id.event)).getText().toString()+",";
        String num = ((TextView) findViewById(R.id.jnum)).getText().toString()+",";
        try {
            FileOutputStream outputStream = openFileOutput("event", Context.MODE_APPEND);
            outputStream.write(eve.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream outputStream1 = openFileOutput(eve, Context.MODE_APPEND);
            outputStream1.write(num.getBytes());
            outputStream1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

