package com.example.hp.carrent;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Homefragment extends Fragment {
private View view;
private RecyclerView recyclerView;
private List<car> Listcar;
private String url = "http://192.168.88.83";
private String id;
private Spinner spinner;

    public Homefragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homefragment, container, false);
        recyclerView = view.findViewById(R.id.recycle);
        Listcar = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        if(getArguments() != null){
            id = getArguments().getString("id");
        }
        return view;

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadPost();
        Toast.makeText(view.getContext(),id,Toast.LENGTH_LONG).show();




    }
    public void loadPost(){
        String load = url+"/mobile/car.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                load, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject posts = array.getJSONObject(i);
                        Listcar.add(new car (
                                posts.getInt("id_car"),
                                posts.getString("num"),
                                posts.getString("url"),
                                posts.getString("status")

                        ));
                        Caradapter pdpt = new Caradapter(Listcar,getContext());
                        recyclerView.setAdapter(pdpt);
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

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    public void Search(final String type ){
        String load = url+"/mobile/getCar.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                load, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject posts = array.getJSONObject(i);
                        Listcar.add(new car (
                                posts.getInt("id_car"),
                                posts.getString("num"),
                                posts.getString("url"),
                                posts.getString("status")

                        ));
                        Caradapter pdpt = new Caradapter(Listcar,getContext());
                        recyclerView.setAdapter(pdpt);
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
                Map<String,String> params = new HashMap<>();
                params.put("id",type);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}
