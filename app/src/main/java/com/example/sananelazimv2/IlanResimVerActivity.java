package com.example.sananelazimv2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sananelazimv2.Model.AddImageModel;
import com.example.sananelazimv2.Retrofit.ApiClient;
import com.example.sananelazimv2.Retrofit.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IlanResimVerActivity extends AppCompatActivity {
    String image;

    Button btnResimsec, btnResimEkle,btnAnasayfa;
    ImageView imageView;

    Bitmap bitmap;

    ApiInterface apiInterface;

    String memberId, advertisementId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilan_resim_ver);


        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Bundle bundle  =  getIntent().getExtras();
        memberId = String.valueOf(bundle.getInt("uyeid"));
        advertisementId = bundle.getString("ilanid");


        imageView = (ImageView) findViewById(R.id.imageview);

        btnResimsec = findViewById(R.id.btnResimSec);
        btnResimEkle = findViewById(R.id.btnResimYukle);
        btnAnasayfa = findViewById(R.id.btnAnasayfa);

        btnResimsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimGoster();
            }
        });

        btnResimEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView.getDrawable()==null){
                    Toast.makeText(IlanResimVerActivity.this,"Lütfen Önce Resim Seçiniz.",Toast.LENGTH_LONG).show();
                }else{
                    uploadImage();
                }
            }
        });

        btnAnasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IlanResimVerActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }



    //Galeriyi Açar.
    public void resimGoster() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 777);
    }

    public void uploadImage() {
        image = imageToString();
        Call<AddImageModel> addImageModelCall = apiInterface.addImage(memberId, advertisementId, image);
        addImageModelCall.enqueue(new Callback<AddImageModel>() {
            @Override
            public void onResponse(Call<AddImageModel> call, Response<AddImageModel> response) {
                if (response.body().isTf()) {
                    Toast.makeText(IlanResimVerActivity.this, response.body().getSonuc(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(IlanResimVerActivity.this, response.body().getSonuc(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AddImageModel> call, Throwable t) {
                Log.i("test 1 ","hata : "+t);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777 && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(IlanResimVerActivity.this.getContentResolver(), path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String imageToString = Base64.encodeToString(bytes, Base64.DEFAULT);
        return imageToString;
    }
}