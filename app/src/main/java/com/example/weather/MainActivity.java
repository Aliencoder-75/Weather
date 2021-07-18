package com.example.weather;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.StateSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static TextView temperature;
    public static TextView weather;
    public static TextView weather_desc;
    public static TextView location;
    public static TextView wind_speed;
    public static TextView pressure;
    public static TextView real_feel;
    public static TextView humidity;
    public static ImageView icon;
    public static String location_name;
    public static String selected_unit;
    public static final String EXTRA_MESSAGE_UNIT = "com.example.Weather.MESSAGE_UNIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();

        if (!isConnected()) {
            Toast.makeText(this, "No Connection", Toast.LENGTH_LONG).show();
        }

        temperature = findViewById(R.id.temp);
        weather = findViewById(R.id.main);
        weather_desc = findViewById(R.id.desc);
        location = findViewById(R.id.location);
        wind_speed = findViewById(R.id.wind_speed);
        pressure = findViewById(R.id.pressure);
        real_feel = findViewById(R.id.real_feel);
        humidity = findViewById(R.id.humidity);
        icon = findViewById(R.id.icon);

        fetch process = new fetch();
        process.execute();

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.item1:
                Intent intent_refresh = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent_refresh);
                return true;

            case R.id.item2:
                Intent intent_setting = new Intent(MainActivity.this, Settings.class);
                startActivity(intent_setting);
                Intent intent = getIntent();
                selected_unit = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_UNIT);
                return true;

            case R.id.item3:
                Intent intent_About = new Intent(MainActivity.this, About_Us.class);
                startActivity(intent_About);
                return true;

            case R.id.item4:
                final AlertDialog.Builder loc_builder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.location_dialog, null);

                final EditText input = mView.findViewById(R.id.input);
                TextView btn_cancel = mView.findViewById(R.id.btn_cancel);
                TextView btn_ok = mView.findViewById(R.id.btn_ok);

                loc_builder.setView(mView);

                final AlertDialog alertDialog = loc_builder.create();
                alertDialog.setCanceledOnTouchOutside(false);

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        location_name = input.getText().toString();
                        saveData();
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("savePrefData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("locationValue", location_name);
        editor.putString("unitValue", selected_unit);
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("savePrefData", MODE_PRIVATE);
        location_name = sharedPreferences.getString("locationValue", "");
        selected_unit = sharedPreferences.getString("unitValue", "");
    }

}
