package com.example.nimbus.ui.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.nimbus.Constants;
import com.example.nimbus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static com.example.nimbus.MainActivity.longitude;
import static com.example.nimbus.MainActivity.latitude;
import static com.example.nimbus.MainActivity.city;

public class WeatherFragment extends Fragment {

    private String temperature;
    private String humidity;
    private String wind;
    private String precipIntensity;
    private String precipProbability;
    private Double precipProb;
    private String weather;
    private String summary;

    private TextView weatherTitle;
    private TextView weatherText;
    private TextView weatherTemp;
    private TextView weatherStatus;

    public WeatherFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_weather, container, false);
        weatherText = root.findViewById(R.id.text_weather_extra);
        weatherTitle = root.findViewById(R.id.text_weather_title);
        weatherTemp = root.findViewById(R.id.text_weather_temp);
        weatherStatus = root.findViewById(R.id.text_weather);

        String url = "https://api.darksky.net/forecast/" + Constants.NIGHT_SKY_API_KEY + "/"
                + latitude + "," + longitude;
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    final String myResponse = response.body().string();

                    try {
                        JSONObject reader = new JSONObject(myResponse);
                        JSONObject currently = reader.getJSONObject("currently");
                        summary = currently.getString("summary");
                        temperature = currently.getString("temperature");
                        humidity = currently.getString("humidity");
                        wind = currently.getString("windSpeed");
                        precipIntensity = currently.getString("precipIntensity");
                        precipProbability = currently.getString("precipProbability");
                        precipProb = Double.parseDouble(precipProbability) * 100.00;
                        precipProbability = Double.toString(precipProb);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (city == null) {
                                weatherTitle.setText(R.string.text_invalid_weather);
                            } else {
                                weatherTitle.setText(city);
                                weatherStatus.setText(summary);
                                weatherTemp.setText(temperature + "°F");
                                weather =
                                        "Humidity: " + humidity + "g/m"+ "\u00b3" + "\n" +
                                        "Wind Speed: " + wind + "mph\n" +
                                        "Precipitation Intensity: " + precipIntensity + "mm/h\n" +
                                        "Precipitation Probability: " + precipProbability + "%\n";
                                weatherText.setText(weather);
                            }


                        }
                    });
                }

            }
        });

        return root;
    }
}