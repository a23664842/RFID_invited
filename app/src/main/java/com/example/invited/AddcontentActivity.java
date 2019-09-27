package com.example.invited;

import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentFilter;
import android.nfc.tech.NfcF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.app.AlertDialog;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class AddcontentActivity extends AppCompatActivity {

    private NfcAdapter mAdapter;
    private AlertDialog mDialog;
    private TextView Uid;
    private String rfidn = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontent);
        Bundle bundle = getIntent().getExtras();
        final int number = bundle.getInt("number");
        final int cnum = bundle.getInt("cnum");
        rfidn = "";
        Uid =(TextView)findViewById(R.id.uid);
        resolveIntent(getIntent());
        mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null).create();
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            showMessage(R.string.error, R.string.no_nfc);
            finish();
            return;
        }
        ((Button) findViewById(R.id.button))
                .setOnClickListener(new OnClickListener() {
                    @Override

                    public void onClick(View v) {
                        if(!rfidn.equals("")&&(number>cnum)){
                        try {
                            writename(rfidn);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                            Intent intent = new Intent();
                            intent.setClass(AddcontentActivity.this, AddcontentActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("number",number);//傳遞Double
                            bundle.putInt("cnum",cnum+1);
                            intent.putExtras(bundle);
                            AddcontentActivity.this.finish();
                            startActivity(intent);
                    }
                        else if (number<=cnum)
                        {
                            try {
                                writename(rfidn);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent();
                            intent.setClass(AddcontentActivity.this, MainActivity.class);
                            AddcontentActivity.this.finish();
                            startActivity(intent);
                        }
                    }
                });
        ((Button) findViewById(R.id.button3))
                .setOnClickListener(new OnClickListener() {
                    @Override

                    public void onClick(View v) {
                        if(!rfidn.equals("")){
                            Intent intent = new Intent();
                            intent.setClass(AddcontentActivity.this, CheckActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("number",rfidn);//傳遞Double
                            intent.putExtras(bundle);
                            AddcontentActivity.this.finish();
                            startActivity(intent);
                        }

                    }
                });
    }
    private void showMessage(int title, int message) {
        mDialog.setTitle(title);
        mDialog.setMessage(getText(message));
        mDialog.show();
    }

    public void writename(String rfidnum) throws IOException {
        Context C = getApplicationContext();
        File file = new File(C.getFilesDir(), "event");
        String lastevent="";
        try{
            FileInputStream inputStream = openFileInput("event");
            byte[] bytes = new byte[1024];
            StringBuffer sb = new StringBuffer();
            while (inputStream.read(bytes) != -1){
                sb.append(new String(bytes));
            }
            lastevent = sb.toString();
            String[] item1 = lastevent.split( ",");
            lastevent = item1[item1.length-2];
            inputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        String jname = ((TextView) findViewById(R.id.textview)).getText().toString()+","+rfidnum+",";
        try {
            FileOutputStream outputStream = openFileOutput(lastevent, Context.MODE_APPEND);
            outputStream.write(jname.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                //byte[] empty = new byte[0];
                //byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                //NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                //NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                //msgs = new NdefMessage[] { msg };
            }
            // Setup the views
            // buildTagViews(msgs);
        }
    }

    private String dumpTagData(Parcelable p) {
        StringBuilder sb = new StringBuilder();
        Tag tag = (Tag) p;
        byte[] id = tag.getId();
        //sb.append("Tag ID (hex): ").append(getHex(id)).append("\n");
        // sb.append("Tag ID (dec): ").append(getDec(id)).append("\n");
        sb.append("ID (reversed): ").append(getReversed(id)).append("\n");
        Uid.setText(String.valueOf(getReversed(id)));
        rfidn = String.valueOf(getReversed(id));
      /*  String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                MifareClassic mifareTag = MifareClassic.get(tag);
                String type = "Unknown";
                switch (mifareTag.getType()) {
                case MifareClassic.TYPE_CLASSIC:
                    type = "Classic";
                    break;
                case MifareClassic.TYPE_PLUS:
                    type = "Plus";
                    break;
                case MifareClassic.TYPE_PRO:
                    type = "Pro";
                    break;
                }
                sb.append("Mifare Classic type: ");
                sb.append(type);
                sb.append('\n');

                sb.append("Mifare size: ");
                sb.append(mifareTag.getSize() + " bytes");
                sb.append('\n');

                sb.append("Mifare sectors: ");
                sb.append(mifareTag.getSectorCount());
                sb.append('\n');

                sb.append("Mifare blocks: ");
                sb.append(mifareTag.getBlockCount());*/
        //  }

          /*  if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                case MifareUltralight.TYPE_ULTRALIGHT:
                    type = "Ultralight";
                    break;
                case MifareUltralight.TYPE_ULTRALIGHT_C:
                    type = "Ultralight C";
                    break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);*/
        // }
        //   }

        return sb.toString();
    }


    private long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

}
