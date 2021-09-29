package com.example.weather;

import android.os.AsyncTask;
import android.util.Log;
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

public class fetch extends AsyncTask<Void, Void, Void> {

    String data = "";
    String parse_temp = "";
    String parse_feels_like = "";
    String parse_humidity = "";
    String parse_pressure = "";
    String parse_location_str = "";
    String parse_wind_speed = "";
    String parse_icon_str = "";
    String parse_main_str = "";
    String parse_msg = "";
    String parse_description_str = "";
    String icon_str = "";
    String main_str = "";
    String description_str = "";
    String location = MainActivity.location_name;
    String units = MainActivity.selected_unit;

    @Override
    protected Void doInBackground(Void... voids) {

        Log.d("target", "doInBackground: "+ location);
        Log.d("target", "doInBackground: "+ units);

        if(units == "") {
            units = "metric";
        }

        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q="+location+"&appid=ebc169fdc97c2e4d0f7c40bdfb1dab04&units="+units);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONObject locationCheck;
            locationCheck = (new JSONObject(data));
            String message = locationCheck.getString("cod");

            JSONObject Curr_weather;
            Curr_weather = (new JSONObject(data)).getJSONObject("main");
            int temp = Curr_weather.getInt("temp");
            int feels_like = Curr_weather.getInt("feels_like");
            int humidity = Curr_weather.getInt("humidity");
            int pressure = Curr_weather.getInt("pressure");

            JSONObject location;
            location = (new JSONObject(data)).getJSONObject("sys");
            String country = location.getString("country");

            JSONObject city = new JSONObject(data);
            String city_name = city.getString("name");

            String location_str = city_name + ", " + country;

            JSONObject wind = (new JSONObject(data)).getJSONObject("wind");
            String speed = wind.getString("speed");

            JSONObject weather_arr = new JSONObject(data);
            JSONArray jsonArray = weather_arr.optJSONArray("weather");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String icon = jsonObject.optString("icon");
                String main = jsonObject.optString("main");
                String description = jsonObject.optString("description");
                icon_str = icon;
                main_str = main;
                description_str = description;
            }

            if (units.equals("metric")) {
                String temp_str = temp + "°C";
                String feels_str = feels_like + "°C";
                String pressure_str = pressure + " mb";
                String speed_str = speed + " KPH";
                String humidity_str = humidity + "%";

                parse_temp = temp_str;
                parse_feels_like = feels_str;
                parse_humidity = humidity_str;
                parse_pressure = pressure_str;
                parse_location_str = location_str;
                parse_msg = message;
                parse_wind_speed = speed_str;
                parse_icon_str = icon_str;
                parse_main_str = main_str;
                parse_description_str = description_str;
            }

            if (units.equals("imperial")) {
                String temp_str = temp + "°F";
                String feels_str = feels_like + "°F";
                String pressure_str = pressure + " in";
                String speed_str = speed + " MPH";
                String humidity_str = humidity + "%";

                parse_temp = temp_str;
                parse_feels_like = feels_str;
                parse_humidity = humidity_str;
                parse_pressure = pressure_str;
                parse_location_str = location_str;
                parse_msg = message;
                parse_wind_speed = speed_str;
                parse_icon_str = icon_str;
                parse_main_str = main_str;
                parse_description_str = description_str;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        MainActivity.temperature.setText(this.parse_temp);
        MainActivity.weather.setText(this.parse_main_str);
        MainActivity.weather_desc.setText(this.parse_description_str);
        MainActivity.wind_speed.setText(this.parse_wind_speed);
        MainActivity.pressure.setText(this.parse_pressure);
        MainActivity.real_feel.setText(this.parse_feels_like);
        MainActivity.humidity.setText(this.parse_humidity);

        Log.d("msg", "onPostExecute: "+parse_msg);

        if(parse_msg.equals("200")) {
            MainActivity.location.setText(this.parse_location_str);
        } else {
            MainActivity.location.setText("Place Not Found");
        }


        if (parse_icon_str.equals("01d")) {
            MainActivity.icon.setImageResource(R.drawable.clear_day);
        }
        if (parse_icon_str.equals("01n")) {
            MainActivity.icon.setImageResource(R.drawable.clear_night);
        }
        if (parse_icon_str.equals("02d")) {
            MainActivity.icon.setImageResource(R.drawable.day_cloudy);
        }
        if (parse_icon_str.equals("02n")) {
            MainActivity.icon.setImageResource(R.drawable.night_cloudy);
        }
        if ((parse_icon_str.equals("03d")) || parse_icon_str.equals("03n")) {
            MainActivity.icon.setImageResource(R.drawable.clouds);
        }
        if ((parse_icon_str.equals("04d")) || parse_icon_str.equals("04n")) {
            MainActivity.icon.setImageResource(R.drawable.broken_clouds);
        }
        if (parse_icon_str.equals("09d")) {
            MainActivity.icon.setImageResource(R.drawable.shower_day);
        }
        if (parse_icon_str.equals("09n")) {
            MainActivity.icon.setImageResource(R.drawable.shower_night);
        }
        if (parse_icon_str.equals("10d")) {
            MainActivity.icon.setImageResource(R.drawable.rain_day);
        }
        if (parse_icon_str.equals("10n")) {
            MainActivity.icon.setImageResource(R.drawable.rain_night);
        }
        if (parse_icon_str.equals("11d")) {
            MainActivity.icon.setImageResource(R.drawable.thunder_day);
        }
        if (parse_icon_str.equals("11n")) {
            MainActivity.icon.setImageResource(R.drawable.thunder_night);
        }
        if ((parse_icon_str.equals("13d")) || parse_icon_str.equals("13n")) {
            MainActivity.icon.setImageResource(R.drawable.snow);
        }
        if ((parse_icon_str.equals("50d")) || parse_icon_str.equals("50n")) {
            MainActivity.icon.setImageResource(R.drawable.thunder_day);
        }
    }

}

