package com.example.api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Activity2 extends AppCompatActivity {
    EditText editTextVille;
    private TextView nTextViewResult;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        editTextVille=(EditText)findViewById(R.id.editText);
        nTextViewResult = findViewById(R.id.text_view_result);
        Button buttonParseV = findViewById(R.id.button_parse_V);

        mQueue = Volley.newRequestQueue(this);

        buttonParseV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ville = editTextVille.getText().toString();
                jsonParse(ville);
            }
        });
    }

    private void jsonParse(String ville) {
        String url = "http://192.168.1.70/server/apiLoclisation.php?Ville=" + ville;

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