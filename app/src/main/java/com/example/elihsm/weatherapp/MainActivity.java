package com.example.elihsm.weatherapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ApiService.OnWeatherResultReceived{
    private ApiService apiService;
    private TextView weatherTv;
    private TextView tempTv;
    private TextView humidityTv;
    private TextView pressureTv;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiService=new ApiService(this);
        weatherTv=findViewById(R.id.tv_main_weather);
        tempTv=findViewById(R.id.tv_main_temp);
        humidityTv=findViewById(R.id.tv_main_humidity);
        pressureTv=findViewById(R.id.tv_main_pressure);
        progressBar=findViewById(R.id.progressBar_main);




        final EditText editText=findViewById(R.id.et_main_cityName);
        Button sendRequestButton=findViewById(R.id.button_main_sendRequest);
        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.length()>0) {


                    progressBar.setVisibility(View.VISIBLE);
                    apiService.getWeather(editText.getText().toString(), MainActivity.this);
                }else {
                    editText.setError("edit text is empty");
                }
            }
        });
    }

    @Override
    public void onReceived(String weather, float temp, int pressure, int humidity) {
        weatherTv.setText(weather);
        tempTv.setText(String.valueOf(temp));
        pressureTv.setText(String.valueOf(pressure));
        humidityTv.setText(String .valueOf(humidity));
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onError(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }
}
