package com.example.hp.carrent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.MyViewHolder> {
    private View itemView;
    private Context context;
    private List<Reservation> Listreser;
    private final String statusna = MainActivity.MyBumdle.getString("status");
    private  final String id = MainActivity.MyBumdle.getString("id");
    private MainActivity ma;


    public ReservationAdapter(Context context, List<Reservation> listreser) {
        this.context = context;
        Listreser = listreser;
    }

    private ImageView imgprofile,car;
    private TextView name,date,dateback,time,timeback,status,agent;
    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgprofile,car;
        private TextView name,date,datego,time,timeback,status,confirm,agent;
        public MyViewHolder(@NonNull View view) {
            super(view);
            imgprofile = view.findViewById(R.id.rentProfile);
            car = view.findViewById(R.id.imgrent);
            name = view.findViewById(R.id.carrentpost);
            date = view.findViewById(R.id.textdate);
            datego = view.findViewById(R.id.texttogo);
            time = view.findViewById(R.id.texttimego);
            timeback = view.findViewById(R.id.texttimeback);
            status = view.findViewById(R.id.textreserstatus);
            confirm = view.findViewById(R.id.textView5);
            agent = view.findViewById(R.id.textagent);
            ma = new MainActivity();
        }
    }

    @NonNull
    @Override
    public ReservationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_rent,parent,false);
        context = parent.getContext();
        return new ReservationAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Reservation rv = Listreser.get(position);
        Glide.with(itemView.getContext()).load(rv.getUrl_user()).into(holder.imgprofile);
        Glide.with(itemView.getContext()).load(rv.getUrl_car()).into(holder.car);
        holder.name.setText(rv.getName());
        holder.datego.setText(rv.getBackdate());
        holder.date.setText(rv.getDate());
        holder.time.setText(rv.getGotime());
        holder.timeback.setText(rv.getBacktime());
        holder.status.setText(rv.getStatus());
        if(statusna.equalsIgnoreCase("admin")){
            holder.confirm.setText("ยืนยันการจอง");
            holder.confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setMessage("ต้องการยืนยันการจองหรือไหม?");
                    builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Update(String.valueOf(rv.getId()));
                        }
                    });
                    builder.setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            });
        }else{
            holder.confirm.setText("ยกเลิกการจอง");
            holder.confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setMessage("ต้องการยกเลิกการจองไหม?");
                    builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Del(String.valueOf(rv.getId()));
                        }
                    });
                    builder.setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            });
        }
        holder.agent.setText(rv.getAgent());


    }
    @Override
    public int getItemCount() {
        return Listreser.size();
    }

    public void Update(final String id_car){
        String load = ma.base_usr+"/mobile/updaterequest.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                load, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.toString().equalsIgnoreCase("Success")){
                    Intent i = new Intent(itemView.getContext(),Home.class);
                    i.putExtra("sad", "true");
                    i.putExtra("status","admin");
                    i.putExtra("id",id);
                    context.startActivity(i);
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
                param.put("id",id_car);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
        requestQueue.add(stringRequest);
    }
    public void Del(final String id_car){
        String load = ma.base_usr+"/mobile/cancle.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                load, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.toString().equalsIgnoreCase("Success")){
                    Intent i = new Intent(itemView.getContext(),Home.class);
                    i.putExtra("hat", "true");
                    i.putExtra("status","admin");
                    i.putExtra("id",id);
                    context.startActivity(i);
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
                param.put("id",id_car);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
        requestQueue.add(stringRequest);
    }
}
