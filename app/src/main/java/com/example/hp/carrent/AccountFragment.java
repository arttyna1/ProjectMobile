package com.example.hp.carrent;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
private ImageView img;
private TextView name,tel,address,edit;
private View itemview;
private MainActivity ma;
private AccountFragment accountFragment;
private final String id = MainActivity.MyBumdle.getString("id");

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemview =  inflater.inflate(R.layout.fragment_account, container, false);
        img = itemview.findViewById(R.id.ResStdreqProfile);
        name = itemview.findViewById(R.id.textacename);
        tel = itemview.findViewById(R.id.textactel);
        address = itemview.findViewById(R.id.textacadress);
        edit = itemview.findViewById(R.id.textEdit);
        accountFragment = new AccountFragment();
        ma = new MainActivity();
        return itemview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        load();
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater layoutInflater = getLayoutInflater();
                View viewnaja = layoutInflater.inflate(R.layout.dialogedit, null);
                builder.setView(viewnaja);

                final EditText names = viewnaja.findViewById(R.id.editacname);
                final EditText tels = viewnaja.findViewById(R.id.editactel);
                final EditText adds = viewnaja.findViewById(R.id.editacadd);

                names.setText(name.getText().toString());
                tels.setText(tel.getText().toString());
                adds.setText(address.getText().toString());
                builder.setNegativeButton("แก้ไข", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        update(names.getText().toString(),tels.getText().toString(),adds.getText().toString());
                    }
                });
                builder.setPositiveButton("ปิด", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
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
                               name.setText(posts.getString("name"));
                               tel.setText(posts.getString("tel"));
                               address.setText(posts.getString("address"));
                        Glide.with(itemview.getContext()).load(posts.getString("url")).into(img);

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
    public void update(final String name,final String tel,final String address){
        String load = ma.base_usr+"/mobile/updateaccount.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                load, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("Success")){
                    FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.main,accountFragment);
                    ft.addToBackStack(null);
                    ft.commit();
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
                param.put("name",name);
                param.put("tel",tel);
                param.put("address",address);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
