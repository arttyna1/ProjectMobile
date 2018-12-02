package com.example.hp.carrent;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
private ImageView img_profile;
private Uri imgUri;
private Button btnregis;
private MainActivity ma;
private StorageReference mStorageRef;
private EditText username,password,name,tel,address,passcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ma = new MainActivity();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        bind();
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Select_photo();
            }
        });
        btnregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imgUri == null){
                    Toast.makeText(getApplicationContext(),"กรุณาเลือกภาพโปรไฟล์",Toast.LENGTH_LONG).show();
                }
                if(!passcon.getText().toString().equalsIgnoreCase(password.getText().toString())){
                    Toast.makeText(getApplicationContext(),"กรุณาใส่ password ให้ตรงกัน",Toast.LENGTH_LONG).show();
                }
                if(passcon.getText().toString().equalsIgnoreCase(password.getText().toString()) && imgUri != null && !username.getText().toString().equalsIgnoreCase("") && !password.getText().toString().equalsIgnoreCase("") && !passcon.getText().toString().equalsIgnoreCase("") && !tel.getText().toString().equalsIgnoreCase("") && !address.getText().toString().equalsIgnoreCase("") && !name.getText().toString().equalsIgnoreCase("")){
                    Upload();
                }else{
                    Toast.makeText(getApplicationContext(),"กรุณาใส่ กรอกข้อมูลให้ครบ",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    private void bind(){
        img_profile = findViewById(R.id.profile);
        btnregis = findViewById(R.id.btn_signUp);
        username = findViewById(R.id.nsuserbane);
        password = findViewById(R.id.nspassword);
        name = findViewById(R.id.nsname);
        tel = findViewById(R.id.nstel);
        address = findViewById(R.id.nsadd);
        passcon = findViewById(R.id.nsconpass);
    }
    private void Select_photo() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), 1234);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                img_profile.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
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
                        Toast.makeText(getApplicationContext(),"Upload Complete",Toast.LENGTH_LONG).show();
                        Register(downloadUri.toString());
                    } else {
                        Toast.makeText(Register.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void Register(final String pathimg){
        bind();
        String URL = ma.base_usr+"/mobile/insert.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                params.put("username",username.getText().toString());
                params.put("password",password.getText().toString());
                params.put("url",pathimg);
                params.put("tel",tel.getText().toString());
                params.put("address",address.getText().toString());
                params.put("name",name.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



}
