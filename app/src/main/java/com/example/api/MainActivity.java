package com.example.api;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextView nTextViewResult;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nTextViewResult = findViewById(R.id.text_view_result);
        Button buttonParse = findViewById(R.id.button_parse);

        mQueue = Volley.newRequestQueue(this);

        Button nextact = findViewById(R.id.button_activity);

        nextact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(getApplicationContext(), Activity2.class);
                startActivity(nextScreen);
            }
        });

        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("DEVE0304", "MainActivity:onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("DEVE0304", "MainActivity:onStop()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("DEVE0304", "MainActivity:onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("DEVE0304", "MainActivity:onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("DEVE0304", "MainActivity:onDestroy()");
    }

    private void jsonParse() {
        String url = "http://192.168.1.70/server/apiLoclisation.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("location");

                            for(int i = 0; i <  jsonArray.length(); i++){
                                JSONObject results = jsonArray.getJSONObject(i);

                                int id = results.getInt("id");
                                String Ville = results.getString("Ville");
                                String Lat = results.getString("Lat");
                                String Longi = results.getString("Longi");

                                nTextViewResult.append(String.valueOf(id) + ", " + Ville + ", " + Lat + ", " + Longi + "\n\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }
}