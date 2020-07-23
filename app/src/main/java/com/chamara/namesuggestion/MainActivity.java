package com.chamara.namesuggestion;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView txtAuto;
    ProgressBar progressBar3;
    List<String> stringList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtAuto = findViewById(R.id.txtAutoComplete);
        progressBar3 = (ProgressBar) findViewById(R.id.spin_kit2);
        Sprite wave3 = new FadingCircle();
        progressBar3.setIndeterminateDrawable(wave3);

        progressBar3.setVisibility(View.VISIBLE);


        stringList = new ArrayList<String>();
        stringList.clear();
        getListData();
    }

    private void getListData() {
        String url = "https://namesuggestion.000webhostapp.com/viewname.php";
        RequestQueue requestQueue =  Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("users");

                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String nm=jsonObject.getString("name");
                        stringList.add(nm);
                    }
                    progressBar3.setVisibility(View.INVISIBLE);

                    AutoSuggestAdapter adapter = new AutoSuggestAdapter(getApplicationContext(), android.R.layout.simple_list_item_activated_1, stringList);

                    txtAuto.setAdapter(adapter);
                    // specify the minimum type of characters before drop-down list is shown
                    txtAuto.setThreshold(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Json Exception : " + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error Response : " +error , Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }
}