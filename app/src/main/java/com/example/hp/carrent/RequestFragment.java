package com.example.hp.carrent;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class RequestFragment extends Fragment {
private View view;
private RecyclerView recyclerView;
private LinearLayoutManager linearLayoutManager;
private List<Reservation> Listreser;
private MainActivity ma;
private final String id = MainActivity.MyBumdle.getString("id");


    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_request, container, false);
        Listreser = new ArrayList<>();
        ma = new MainActivity();
        recyclerView = view.findViewById(R.id.recycle_myrent);
        linearLayoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadReq();
    }
    public void loadReq(){
        String load = ma.base_usr+"/mobile/getMyRent.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                load, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject posts = array.getJSONObject(i);
                        Listreser.add(new Reservation(
                                posts.getInt("id"),
                                posts.getString("name"),
                                posts.getString("url"),
                                posts.getString("car"),
                                posts.getString("date"),
                                posts.getString("rentdate"),
                                posts.getString("timego"),
                                posts.getString("timeback"),
                                posts.getString("status"),
                                posts.getString("agent")
                        ));
                        ReservationAdapter rvt = new ReservationAdapter(view.getContext(),Listreser);
                        recyclerView.setAdapter(rvt);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
