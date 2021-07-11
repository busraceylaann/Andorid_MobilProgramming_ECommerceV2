package com.example.sananelazimv2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.sananelazimv2.Adapters.ResimSliderAdapter;
import com.example.sananelazimv2.Model.IlanResimUpdateModel;
import com.example.sananelazimv2.Model.ResimDuzenleSliderModel;
import com.example.sananelazimv2.Retrofit.ApiClient;
import com.example.sananelazimv2.Retrofit.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IlanResimDuzenlemeActivity extends AppCompatActivity {

    String image;
    private String advertisementId,memberId;

    SwipeRefreshLayout swipeRefreshLayout;

    SharedPreferences sharedPreferences;

    Button btnResimsec, btnResimEkle, buttonAnasayfa;
    ImageView imageView;

    Bitmap bitmap;

    ApiInterface apiInterface;

    private ViewPager resimSlider; //Slider İçin.

    List<ResimDuzenleSliderModel> listResimSlider;
    ResimSliderAdapter sliderAdapter;

    CircleIndicator circleIndicator;//Sayfalama İçin
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilan_resim_duzenleme);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);


        Bundle intent = getIntent().getExtras();
        advertisementId = intent.getString("advertisementId"); //Fragmenttan buraya keyimizi aldık.

        sharedPreferences = getSharedPreferences("login", 0);
        memberId = sharedPreferences.getString("memberId", null);

        resimSlider = (ViewPager)findViewById(R.id.resimduzenlemeSlider);
        circleIndicator = (CircleIndicator)findViewById(R.id.IlanCircle);



        imageView = (ImageView)findViewById(R.id.imageview);

        btnResimsec = (Button)findViewById(R.id.btnResimSec);
        btnResimsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimGoster();
            }
        });

        btnResimEkle = (Button)findViewById(R.id.btnResimYukle);
        btnResimEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView.getDrawable()==null){
                    Toast.makeText(getApplicationContext(),"Lütfen Resimlerinizi Tekrar Seçiniz.",Toast.LENGTH_LONG).show();
                }else{
                    uploadImage();

                }
            }
        });

        buttonAnasayfa = (Button)findViewById(R.id.btnAnasayfa);
        buttonAnasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView.getDrawable()==null){
                    Toast.makeText(getApplicationContext(),"Lütfen Önce Resim Seçiniz.",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(IlanResimDuzenlemeActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        getImageResim();

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getImageResim();
                swipeRefreshLayout.setRefreshing(false);
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
        Call<IlanResimUpdateModel> request = apiInterface.ilanResimUpdate(memberId,advertisementId, image);
        request.enqueue(new Callback<IlanResimUpdateModel>() {
            @Override
            public void onResponse(Call<IlanResimUpdateModel> call, Response<IlanResimUpdateModel> response) {
                if (response.body().isTf()) {
                    Toast.makeText(getApplicationContext(), response.body().getSonuc(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), response.body().getSonuc(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<IlanResimUpdateModel> call, Throwable t) {
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
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), path);
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

    public void getImageResim(){
        Call<List<ResimDuzenleSliderModel>> request = apiInterface.resimduzenleSlider(advertisementId);
        request.enqueue(new Callback<List<ResimDuzenleSliderModel>>() {
            @Override
            public void onResponse(Call<List<ResimDuzenleSliderModel>> call, Response<List<ResimDuzenleSliderModel>> response) {
                listResimSlider = response.body();
                sliderAdapter = new ResimSliderAdapter(listResimSlider,getApplicationContext(),IlanResimDuzenlemeActivity.this);
                resimSlider.setAdapter(sliderAdapter);
                circleIndicator.setViewPager(resimSlider);
                circleIndicator.bringToFront();
            }

            @Override
            public void onFailure(Call<List<ResimDuzenleSliderModel>> call, Throwable t) {

            }
        });
    }
}