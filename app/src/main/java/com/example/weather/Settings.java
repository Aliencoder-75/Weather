package com.example.weather;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    public static final String EXTRA_MESSAGE_UNIT = "com.example.Weather.MESSAGE_UNIT";
    CharSequence unit_name[] = {"Metric °C", "Imperial °F"};
    public static String unit = "";
    TextView units_text;
    int checkedItem;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        LinearLayout linearLayout2 = findViewById(R.id.units_category);

        units_text = findViewById(R.id.units_text);

        linearLayout2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final AlertDialog.Builder unit_builder = new AlertDialog.Builder(Settings.this);

                unit_builder.setTitle("Units");
                unit_builder.setSingleChoiceItems(unit_name, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                unit = "metric";
                                units_text.setText("Metric °C");
                                checkedItem = 0;
                                break;
                            case 1:
                                unit = "imperial";
                                units_text.setText("Imperial °F");
                                checkedItem = 1;
                                break;
                        }
                    }
                })

                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("item", "onClick: " + checkedItem);
                                SharedPreferences sharedPreferences = getSharedPreferences("savePrefData", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("itemValue", checkedItem);
                                editor.apply();
                            }
                        });
                final AlertDialog alertDialog = unit_builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(Settings.this, MainActivity.class);
                intent.putExtra(EXTRA_MESSAGE_UNIT, unit);
                startActivity(intent);
            }
        });

        loadData();
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("savePrefData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("unitValue", unit);
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("savePrefData", MODE_PRIVATE);
        unit = sharedPreferences.getString("unitValue", "");
        checkedItem = sharedPreferences.getInt("itemValue",MODE_PRIVATE);

        if (unit.equals("metric")) {
            units_text.setText("Metric °C");
        } else if (unit.equals("imperial")) {
            units_text.setText("Imperial °F");
        } else {
            units_text.setText("Metric °C");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }
}
