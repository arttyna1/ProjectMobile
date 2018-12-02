package com.example.hp.carrent;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.Map;

public class Home extends AppCompatActivity {
private NavigationView navigationView;
private DrawerLayout drawerLayout;
private ActionBarDrawerToggle actionBarDrawerToggle;
private Homefragment homefragment;
private AccountFragment accountFragment;
private  RequestFragment requestFragment;
private Insertcar insertcar;
private TextView tv;
private ImageView img;
    final String status = MainActivity.MyBumdle.getString("status");
//final String id = MainActivity.MyBumdle.getString("id");
private String id,url = "";
private MainActivity ma;
private confirm cn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Bundle bd = getIntent().getExtras();
        if(bd != null){
//          status = bd.getString("status");
            id = bd.getString("id");
        }
        Toast.makeText(getApplicationContext(),status,Toast.LENGTH_SHORT).show();
        ma = new MainActivity();
        load();
        navigationView = findViewById(R.id.nav_start);
        navigationView.setItemIconTintList(null);
        drawerLayout = findViewById(R.id.drawer);
        homefragment = new Homefragment();
        accountFragment = new AccountFragment();
        requestFragment = new RequestFragment();
        cn =  new confirm();
        insertcar = new Insertcar();
        if(getIntent().hasExtra("lock")){
            id = bd.getString("id");
            setFragment(homefragment);
        }
        if(getIntent().hasExtra("sad")){
            id = bd.getString("id");
            setFragment(cn);
        }
        if(getIntent().hasExtra("sad")){
            id = bd.getString("id");
            setFragment(requestFragment);
        }

        View hView =  navigationView.inflateHeaderView(R.layout.navigator_header);
        img = hView.findViewById(R.id.imageView3);
        tv = hView.findViewById(R.id.textView2);
        setupDrawerContent(navigationView);

        Bundle hbd = new Bundle();
        hbd.putString("id",id);
        homefragment.setArguments(hbd);
        setFragment(homefragment);

        ImageView pen = findViewById(R.id.pencil);

        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(insertcar);
            }
        });
        if(status.equalsIgnoreCase("admin")) {
            navigationView.getMenu().getItem(2).setTitle("ยินยันการจอง");
        }else{
            navigationView.getMenu().getItem(2).setTitle("หน้าจอง");
            pen.setEnabled(false);
        }

    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.nav_homena:{
                                setFragment(homefragment);

                                Bundle hbd = new Bundle();
                                hbd.putString("id",id);
                                homefragment.setArguments(hbd);
                                return true;
                            }
                            case R.id.nav_account:{
                                setFragment(accountFragment);
                                Bundle hbd = new Bundle();
                                hbd.putString("id",id);
                                accountFragment.setArguments(hbd);
                                return true;
                            }
                            case R.id.nav_regis:{
                                if(status.equalsIgnoreCase("admin")){
                                    setFragment(cn);
                                    Bundle hbd = new Bundle();
                                    hbd.putString("id",id);
                                    cn.setArguments(hbd);
                                    return true;
                                }else{
                                    setFragment(requestFragment);
                                    Bundle hbd = new Bundle();
                                    hbd.putString("id",id);
                                    requestFragment.setArguments(hbd);
                                    return true;
                                }
                            }
                            case R.id.nav_logout:{
                                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(i);
                            }
                            default:{
                                return false;
                            }
                        }
                    }
                });
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void load(){
        String load = ma.base_usr+"/mobile/loadData.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                load, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject posts = array.getJSONObject(i);
                        Glide.with(getApplicationContext()).load(posts.getString("url")).into(img);
                        tv.setText(posts.getString("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("id",id);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}
