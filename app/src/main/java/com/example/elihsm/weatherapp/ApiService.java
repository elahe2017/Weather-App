package com.example.elihsm.weatherapp;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiService  {
    private Context context;
    public ApiService(Context context){
        this.context=context;

    }
    public void getWeather(String cityName, final OnWeatherResultReceived onWeatherResultRecieved){
        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, "http://api.openweathermap.org/data/2.5/weather?q="+cityName+"&APPID=cba4232d1c010643b37b3f6d7f793786", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                           JSONArray weatherJsonArray= response.getJSONArray("weather");
                           String weather=weatherJsonArray.getJSONObject(0).getString("main");

                           JSONObject mainJsonObject=response.getJSONObject("main");
                           float temp= (float) mainJsonObject.getDouble("temp");
                           int pressure=mainJsonObject.getInt("pressure");
                           int humidity=mainJsonObject.getInt("humidity");
                           onWeatherResultRecieved.onReceived(weather,temp,pressure,humidity);
                        } catch (JSONException e) {
                            onWeatherResultRecieved.onError(e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onWeatherResultRecieved.onError(error.toString());

            }
        });
      RequestQueue requestQueue= RequestQueueContainer.getRequestQueue(context);
      request.setRetryPolicy(new DefaultRetryPolicy(18000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      requestQueue.add(request);
    }
    public interface OnWeatherResultReceived{
        void onReceived(String weather,float temp,int pressure,int humidity);
        void onError(String message);
    }
}
