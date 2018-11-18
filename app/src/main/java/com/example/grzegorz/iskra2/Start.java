package com.example.grzegorz.iskra2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.Editable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import java.lang.Thread;

public class Start extends AppCompatActivity{

    public static TextView err;
    public static Context cxt;
    public static Context cxt_people;
    public static String key;
    public static TextView mTextView;
    public static  TextView namePlace ;
    public static TextView subnamePlace ;
    public static TextView telPlace ;
    public static TextView unPlace ;
    public static TextView notePlace ;
    public static TextView keyPlace ;
    public static ConstraintLayout layout;
    String name ;
    String subname ;
    String tel ;
    String un ;
    String note;


        public void decision (String info){
            Log.d("Serwer", "Odpowiedz "+ info);

             if (info.equals("\"Permission for access\"")) {
                 Intent intent = new Intent(cxt, People.class);
                 cxt.startActivity(intent);
             }
             else if(info.equals("\"No permission for access\"")) {
                 err.setText(R.string.error_log);
             }
             else if(info.equals("\"Key not found\"")) {
                 mTextView.setText("Nie znaleziono klucza");

                 namePlace.setText(" ");
                 subnamePlace.setText(" ");
                 telPlace.setText(" ");
                 unPlace.setText(" ");
                 notePlace.setText(" ");
                 keyPlace.setText(key);

                 layout.setBackgroundColor(Color.rgb(227, 234, 42));
             }
             else{
                 // ta funkcja musi obslugiwac wyjatko logowania i ludzi
                 try{
                     JSONObject data  = new JSONObject(info);
                     name = data.getString("imie");
                     subname = data.getString("nazwisko");
                     tel = data.getString("tel");
                     un = data.getString("uniwersytet");
                     note = data.getString("notka");


                     mTextView.setText(" ");
                     namePlace.setText(name);
                     subnamePlace.setText(subname);
                     telPlace.setText(tel);
                     unPlace.setText(un);
                     notePlace.setText("Notka: "+note);
                     keyPlace.setText(key);

                     if(note.equals("")){
                         layout.setBackgroundColor(Color.rgb(74, 234, 42));
                     }
                     else {
                         layout.setBackgroundColor(Color.rgb(234, 170, 42));
                     }

                 }catch (JSONException e){
                     e.printStackTrace();
                    // err.setText(R.string.bigerror_log);

                 }
            }
        }

    public class Connect extends Service {

        public IBinder onBind(Intent intent) {
            return null;
        }

        public String Log(final String message) {


            OutputStream os = null;
            InputStream is = null;
            HttpURLConnection conn = null;
            String str="";
            try {
                //constants
                URL url = new URL("http://sktt.cba.pl/sktt/pages/android.php");

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /*milliseconds*/);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(message.getBytes().length);

                //make some HTTP header nicety
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                //open
                conn.connect();

                //setup send
                os = new BufferedOutputStream(conn.getOutputStream());
                os.write(message.getBytes());
                //clean up
                os.flush();

                //do somehting with response
                is = new BufferedInputStream(conn.getInputStream());
                str = IOUtils.toString(is, StandardCharsets.UTF_8);
                

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //clean up
                try {
                    os.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                conn.disconnect();
            }
            return str;
        }

    }



    public class Connection extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Connect polaczenie = new Connect();
            return polaczenie.Log(strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            decision(result);
        }
    }

    public static void transfer(Context con, TextView er)
    {
        cxt=con;
        err = er;
    }

    public static void transfer_people(Context con, TextView[] x, ConstraintLayout back)
    {
        cxt_people=con;
        mTextView = x[0];
        namePlace = x[1];
        subnamePlace = x[2];
        telPlace= x[3];
        unPlace = x[4];
        notePlace = x[5];
        keyPlace = x[6];
        layout = back;

    }
    public static void transfer_numer(String k){
        key = k;
    }

}

