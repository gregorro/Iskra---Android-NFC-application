package com.example.grzegorz.iskra2;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import 	android.nfc.NfcEvent;
import 	android.nfc.NfcAdapter;
import android.telecom.Connection;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.nfc.*;
import com.example.grzegorz.iskra2.MainActivity;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class People extends AppCompatActivity{

    private static final String TAG = "NFC";
    private NfcAdapter nfcAdapter;
    private PendingIntent nfcPendingIntent;
    public static  TextView mTextView;
    public static  TextView namePlace ;
    public static TextView subnamePlace ;
    public static  TextView telPlace ;
    public static TextView unPlace ;
    public static TextView notePlace ;
    public static TextView keyPlace ;
    public Context cxt;
    public TextView[] tab;
    public ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        layout = findViewById(R.id.back);
        mTextView = findViewById(R.id.textView2);
        namePlace =findViewById(R.id.textView3);
        subnamePlace =findViewById(R.id.textView4);
        telPlace =findViewById(R.id.textView5);
        unPlace =findViewById(R.id.textView6);
        notePlace =findViewById(R.id.textView7);
        keyPlace =findViewById(R.id.textView8);

        tab = new TextView[]{mTextView, namePlace, subnamePlace, telPlace, unPlace, notePlace,keyPlace};
        cxt = People.this;

        Start.transfer_people(cxt, tab, layout);

        // initialize NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "enableForegroundMode");

        // foreground mode gives the current active application priority for reading scanned tags
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED); // filter for tags
        IntentFilter[] writeTagFilters = new IntentFilter[] {tagDetected};
        nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, writeTagFilters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        byte[] extraID = tag.getId();

        StringBuilder sb = new StringBuilder();
        for (byte b : extraID) {
            sb.append(String.format("%02X", b));
            };

        String nfcTagSerialNum = sb.toString();
        Log.e("nfc ID", nfcTagSerialNum);


        JSONObject jsonObject = new JSONObject();

        try {
        jsonObject.put("key", nfcTagSerialNum);
        } catch (JSONException e) {
        e.printStackTrace();
        }

        String message = jsonObject.toString();

        Start.Connection obiekt = new Start(). new Connection();
        obiekt.execute(message);

        Start.transfer_numer(nfcTagSerialNum);

    }
    }




