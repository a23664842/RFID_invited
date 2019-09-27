package com.example.invited;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;

public class CheckActivity extends AppCompatActivity {
    private String item = "";
    private String[] item1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        final String number = bundle.getString("number");
        LinearLayout additem = (LinearLayout) findViewById(R.id.check);
        String fileName = "event";
        TextView tv=new TextView(this);
        tv.setText("ffsdf");
        additem.addView(tv);
        try{
            FileInputStream inputStream = openFileInput(fileName);
            byte[] bytes = new byte[1024];
            StringBuffer sb = new StringBuffer();
            while (inputStream.read(bytes) != -1){
                sb.append(new String(bytes));
            }
            item = sb.toString();
            item1 = item.split( ",");
            tv=new TextView(this);
            tv.setText(item1[item1.length-2]);
            additem.addView(tv);
            for (int i=0;i<=item1.length;i++) {
                final Button btn = new Button(this);
                additem.addView(btn);
                btn.setText(item1[i]);
                btn.setId(i);
                btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(CheckActivity.this, checkcontentctivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("ename",item1[v.getId()]);//傳遞Double
                        bundle.putString("rfidnum",number);
                        intent.putExtras(bundle);
                        CheckActivity.this.finish();
                        startActivity(intent);
                    }
                });

            }
            inputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }


}
