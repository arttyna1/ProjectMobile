package com.example.hp.carrent;


import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Insertcar extends Fragment {
private View view;
private StorageReference mStorageRef;
private Uri imgUri;
private ImageView img;
private EditText editText;
private Spinner spinner;
private Button btnup;
private  String type = "";
private MainActivity ma;
private Homefragment homefragment;
private Toolbar toolbar;
    public Insertcar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mStorageRef = FirebaseStorage.getInstance().getReference();
        view = inflater.inflate(R.layout.fragment_insertcar, container, false);
        img = view.findViewById(R.id.imageView4);
        editText = view.findViewById(R.id.editText);
        spinner = view.findViewById(R.id.spin);
        btnup = view.findViewById(R.id.buttonup);
        ma = new MainActivity();
        homefragment = new Homefragment();
        toolbar = view.findViewById(R.id.back);
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<String> arr = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_dropdown_item_1line,getResources().getStringArray(R.array.type));
        spinner.setAdapter(arr);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Select_photo();
            }
        });
        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imgUri != null && !editText.getText().toString().equalsIgnoreCase("") && !type.equalsIgnoreCase("")){
                    Upload();
                }
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.main,homefragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }
    private void Select_photo() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), 1234);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == -1 && data != null && data.getData() != null) {
            imgUri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imgUri);
                img.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getImageExt(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void Upload(){
        if (imgUri != null) {
            final StorageReference ref = mStorageRef.child("Profile" + System.currentTimeMillis() + "." + getImageExt(imgUri));
            ref.putFile(imgUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Toast.makeText(view.getContext(),"Upload Complete",Toast.LENGTH_LONG).show();
                        upcar(downloadUri.toString());
                    } else {
//                        Toast.makeText(Register.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void upcar(final String pathimg){
        String URL = ma.base_usr+"/mobile/insertcar.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("Success")){
                    FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.main,homefragment);
                    ft.addToBackStack(null);
                    ft.commit();

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
                Map<String,String> params = new HashMap<>();
                params.put("number",editText.getText().toString());
                params.put("type",type);
                params.put("url",pathimg);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);
    }
}
