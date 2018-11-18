package com.example.grzegorz.iskra2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.grzegorz.iskra2.Start;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public String message;
    public EditText log ;
    public EditText pass ;
    public TextView err;
    public Context cxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log = findViewById(R.id.editText);
        pass = findViewById(R.id.editText2);
        err = findViewById(R.id.textView);
        cxt = MainActivity.this;

        ConstraintLayout layout = findViewById(R.id.start);

        layout.setBackgroundColor(Color.rgb(46, 193, 242));

        Start.transfer(cxt, err);
        }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public void logowanie(View view)
    {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("login", log.getText().toString());
            jsonObject.put("haslo", pass.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message = jsonObject.toString();

        Start.Connection obiekt = new Start(). new Connection();
        obiekt.execute(message);

        Log.d("Droga", "A " + log.getText().toString());
    }


}
