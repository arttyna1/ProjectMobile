package com.example.hp.carrent;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Caradapter extends RecyclerView.Adapter<Caradapter.MyViewHolder> {
  List<car> Listcar;
  View itemview;
  ImageView imgcar,imgmenu;
  private String nauy,adddate;
    private  DatePickerDialog picker;
    private TimePickerDialog timepic;
    private TimePickerDialog timepic2;
    private  MainActivity ma;
  final String id = MainActivity.MyBumdle.getString("id");
    final String status = MainActivity.MyBumdle.getString("status");

    public Caradapter(List<car> listcar, Context context) {
        Listcar = listcar;
        this.context = context;
    }

    TextView textnumcar,texttype;
  private Context context;
    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgcar,imgmenu;
        TextView textnumcar,texttype;
        private CardView card;
        public MyViewHolder(@NonNull View view) {
            super(view);
            imgcar = view.findViewById(R.id.imgpost);
            textnumcar = view.findViewById(R.id.textnumcar);
            texttype = view.findViewById(R.id.texttype);
            card = view.findViewById(R.id.cardmain);
            imgmenu = view.findViewById(R.id.menuna);
            ma = new MainActivity();

        }
    }
    @NonNull
    @Override
    public Caradapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_show,parent,false);
        context = parent.getContext();
        return new Caradapter.MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final car c = Listcar.get(position);
        Glide.with(itemview.getContext()).load(c.getUrl_car()).into(holder.imgcar);
        holder.textnumcar.setText("ทะเบียนรถ"+c.getNum_car());
        holder.texttype.setText("ประเภทรถ"+c.getStatus_car());
        if(!status.equalsIgnoreCase("admin")){
            holder.imgmenu.setVisibility(View.GONE);
        }
        holder.imgmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(itemview.getContext(), holder.imgmenu);

                popupMenu.getMenuInflater().inflate(R.menu.car, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.nav_edit:{
                                return true;
                            }
                            case R.id.nav_del:{
                                Del(String.valueOf(c.getId_car()));
                                return true;
                            }
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        holder.imgcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemview.getContext());
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewnaja = layoutInflater.inflate(R.layout.deatil_car, null);
                builder.setView(viewnaja);
                ImageView imgcar = viewnaja.findViewById(R.id.imageView);
                TextView txtnum = viewnaja.findViewById(R.id.textView);
                TextView txttype = viewnaja.findViewById(R.id.textView3);

                Glide.with(viewnaja.getContext()).load(c.getUrl_car()).into(imgcar);
                txtnum.setText(c.getNum_car());
                txttype.setText(c.getStatus_car());

                builder.setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemview.getContext());
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View viewnaja2 = layoutInflater.inflate(R.layout.date_select, null);
                final TextView text = viewnaja2.findViewById(R.id.textDate);
                final EditText editText = viewnaja2.findViewById(R.id.etnuay);
                final Button btn = viewnaja2.findViewById(R.id.buttonTime);
                final Button btnT1 = viewnaja2.findViewById(R.id.btnTime1);
                final Button btnT2 = viewnaja2.findViewById(R.id.btnTime2);
                final TextView text1 = viewnaja2.findViewById(R.id.textTime);
                final TextView text2 = viewnaja2.findViewById(R.id.textTime2);
                final ImageView img = viewnaja2.findViewById(R.id.carRev);
                Glide.with(viewnaja2.getContext()).load(c.getUrl_car()).into(img);
                builder.setView(viewnaja2);
                builder.setPositiveButton("ปิด", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        picker = new DatePickerDialog(viewnaja2.getContext(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        text.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                }, year, month, day);
                        picker.show();
                    }
                });
                btnT1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();
                        int  mHour = c.get(Calendar.HOUR_OF_DAY);
                        int mMinute = c.get(Calendar.MINUTE);
                        timepic = new TimePickerDialog(viewnaja2.getContext(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        text1.setText(hourOfDay + ":" + minute);
                                    }
                                },mHour,mMinute,false);
                        timepic.show();
                    }
                });
                btnT2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();
                        int  mHour = c.get(Calendar.HOUR_OF_DAY);
                        int mMinute = c.get(Calendar.MINUTE);
                        timepic2 = new TimePickerDialog(viewnaja2.getContext(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        text2.setText(hourOfDay + ":" + minute);
                                    }
                                },mHour,mMinute,false);
                        timepic2.show();
                    }
                });

                builder.setNegativeButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Reser(String.valueOf(c.getId_car()),adddate,editText.getText().toString(),text.getText().toString(),text1.getText().toString(),text2.getText().toString());
                    }
                });
                builder.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return Listcar.size();
    }
    public void Reser(final String id_car,final String date,final String agen,final String rent,final String time1,final String time2){
        String URL = "http://192.168.88.83/mobile/insertrequest.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(itemview.getContext(),response,Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id_car",id_car);
                params.put("id_user",id);
                params.put("agent",agen);
                params.put("rent",rent);
                params.put("timeto",time1);
                params.put("timeback",time2);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(itemview.getContext());
        requestQueue.add(stringRequest);
    }
    public void Del(final String id_car){
        String load = ma.base_usr+"/mobile/delcar.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                load, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            if(response.toString().equalsIgnoreCase("Success")){
                Intent i = new Intent(itemview.getContext(),Home.class);
                i.putExtra("lock", "true");
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
        RequestQueue requestQueue = Volley.newRequestQueue(itemview.getContext());
        requestQueue.add(stringRequest);
    }
}
