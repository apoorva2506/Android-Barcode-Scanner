package com.example.apoor.barcodescanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShowDetails extends AppCompatActivity {

  //  RequestQueue requestQueue;
    //String showUrl = "http://localhost/test.php?serialno=123";
     //TextView result;
  RequestQueue requestQueue;
  TextView txt;
  TextView txt2;
    Button search;



    //txt.setText(str);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        search = (Button)findViewById(R.id.button);
        txt = (TextView)findViewById(R.id.textView);
       // txt2 = (TextView)findViewById(R.id.textView);
        Intent i = getIntent();
        final String str = i.getStringExtra("scansno");


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        //txt.setText(str);

        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String showUrl= "http://192.168.1.100/test.php?serialno="+str;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                        showUrl, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       // System.out.println(response.toString());
                        try {
                            JSONArray stocks = response.getJSONArray("stocks");
                            for (int i = 0; i < stocks.length(); i++) {
                                JSONObject stock = stocks.getJSONObject(i);

                                String name = stock.getString("item_name");
                                String location = stock.getString("location");
                                String staff = stock.getString("staff");

                                txt.append(name + " " + location + " " + staff + " \n");
                            }
                            txt.append("===\n");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.append(error.getMessage());

                    }
                });
                requestQueue.add(jsonObjectRequest);
            }
        });
    }
}
