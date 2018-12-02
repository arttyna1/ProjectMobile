package com.example.hp.carrent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button nextreg, btnsignin;
    private CheckBox save;
    private EditText etusername, etpassword;
    private SharedPreferences mRef;
    private static final String PREF_NAME = "Preffile";
    public final String base_usr = "http://10.51.5.70";
    public  static Bundle MyBumdle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRef = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        bind();
        getPreference();
        nextreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });
        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(etusername.getText().toString(),etpassword.getText().toString());
                if (save.isChecked()) {
                    Boolean saveboolean = save.isChecked();
                    SharedPreferences.Editor editor = mRef.edit();
                    editor.putString("username", etusername.getText().toString());
                    editor.putString("password", etpassword.getText().toString());
                    editor.putBoolean("boolean", saveboolean);
                    editor.apply();
                } else {
                    mRef.edit().clear().apply();
                }
                etusername.getText().clear();
                etpassword.getText().clear();
                save.setChecked(false);
            }
        });
    }

    private void bind() {
        nextreg = findViewById(R.id.btnnxtrgt);
        save = findViewById(R.id.check_save);
        etusername = findViewById(R.id.et_login_username);
        etpassword = findViewById(R.id.et_login_password);
        btnsignin = findViewById(R.id.button_signin);
    }

    private void getPreference() {
        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if (sp.contains("username")) {
            String u = sp.getString("username", "not found.");
            etusername.setText(u);
        }
        if (sp.contains("password")) {
            String u = sp.getString("password", "not found.");
            etpassword.setText(u);
        }
        if (sp.contains("boolean")) {
            Boolean b = sp.getBoolean("boolean", false);
            save.setChecked(b);
        }

    }

    private void login(final String user,final String pass) {
        String url = base_usr + "/mobile/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equalsIgnoreCase("Failed")){
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject posts = array.getJSONObject(i);
                            Intent ig = new Intent(getApplicationContext(),Home.class);
                             ig.putExtra("id",String.valueOf(posts.getInt("id")));
                            MainActivity.MyBumdle.putString("id",String.valueOf(posts.getInt("id")));
                            MainActivity.MyBumdle.putString("status",posts.getString("status"));
                            ig.putExtra("status",posts.getString("status"));
                            startActivity(ig);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



//
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param =  new HashMap<>();
                param.put("username", user);
                param.put("password", pass);
                return param;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}