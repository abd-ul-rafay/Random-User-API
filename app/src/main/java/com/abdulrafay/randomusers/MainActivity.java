package com.abdulrafay.randomusers;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String THE_URL = "https://randomuser.me/api";
    private Button button;
    private ConstraintLayout constraintLayoutForDataShowing;
    private TextView tvGender, tvName, tvEmail, tvAge, tvPhone, tvCity, tvCountry;
    private ImageView ivPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize(); // find all views by id

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                button.setVisibility(View.INVISIBLE);

                constraintLayoutForDataShowing.setVisibility(View.VISIBLE);

                dataMaking();
            }
        });

    }

    private void initialize() {
        button = (Button) findViewById(R.id.button);
        constraintLayoutForDataShowing = (ConstraintLayout) findViewById(R.id.constrainLayoutForDataShowing);
        tvGender = (TextView) findViewById(R.id.gender);
        tvName = (TextView) findViewById(R.id.name);
        tvEmail = (TextView) findViewById(R.id.email);
        tvAge = (TextView) findViewById(R.id.age);
        tvPhone = (TextView) findViewById(R.id.phone);
        tvCity = (TextView) findViewById(R.id.city);
        tvCountry = (TextView) findViewById(R.id.country);
        ivPicture = (ImageView) findViewById(R.id.picture);
    }

    private void dataMaking() {

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                loadJson();
            }
        }, 0, 5000);

    }

    private void loadJson() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, THE_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(MainActivity.this, "Ready", Toast.LENGTH_SHORT).show();
                        try {

                            JSONArray jsonArray = response.getJSONArray("results");
                            JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                            tvGender.setText("Gender: " + jsonObject2.getString("gender"));
                            JSONObject jsonObject3 = jsonObject2.getJSONObject("name");
                            tvName.setText("Name: " + jsonObject3.getString("first") + " " + jsonObject3.getString("last"));
                            JSONObject jsonObject4 = jsonObject2.getJSONObject("location");
                            tvCity.setText("City: " + jsonObject4.getString("city"));
                            tvCountry.setText("Country: " + jsonObject4.getString("country"));
                            tvEmail.setText("Email: " + jsonObject2.getString("email"));
                            JSONObject jsonObject5 = jsonObject2.getJSONObject("dob");
                            tvAge.setText("Age: " + jsonObject5.getString("age") + " yrs");
                            tvPhone.setText("Phone: " + jsonObject2.getString("phone"));
                            JSONObject jsonObject6 = jsonObject2.getJSONObject("picture");
                            String imgUrl = jsonObject6.getString("large");
                            Picasso.get().load(imgUrl).into(ivPicture);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

}
