package com.example.invited;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.StringTokenizer;

public class checkcontentctivity extends AppCompatActivity {

    private String item = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkcontentctivity);
        Bundle bundle = getIntent().getExtras();
        String filename = bundle.getString("ename");
        String number = bundle.getString("rfidnum");
        LinearLayout additem = (LinearLayout) findViewById(R.id.printcheck);
        try{
            FileInputStream inputStream = openFileInput(filename);
            byte[] bytes = new byte[1024];
            StringBuffer sb = new StringBuffer();
            while (inputStream.read(bytes) != -1){
                sb.append(new String(bytes));
            }
            item = sb.toString();
            inputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        StringTokenizer item1 = new StringTokenizer(item,",");
        String num = item1.nextToken();
        for (;item1.hasMoreTokens();) {
            String jname = item1.nextToken();
            String NT = item1.nextToken();
            if(true)
            {

                TextView tv = new TextView(this);
                tv.setText(jname+"有到");
                additem.addView(tv);
                try {
                    FileOutputStream outputStream = openFileOutput(filename, Context.MODE_APPEND);
                    outputStream.write(("get,"+jname+",").getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(jname.equals("get"))
            {
                TextView tv = new TextView(this);
                tv.setText(NT+"有到");
                additem.addView(tv);
            }
        }
        ((Button) findViewById(R.id.button4))
                .setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(checkcontentctivity.this, MainActivity.class);
                            checkcontentctivity.this.finish();
                            startActivity(intent);
                    }
                });
    }
}
