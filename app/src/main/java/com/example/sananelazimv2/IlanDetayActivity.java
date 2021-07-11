package com.example.sananelazimv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sananelazimv2.Adapters.SliderAdapter;
import com.example.sananelazimv2.Model.CallPhoneModel;
import com.example.sananelazimv2.Model.FavoriIslemModel;
import com.example.sananelazimv2.Model.FavoriModel;
import com.example.sananelazimv2.Model.IlanDetayModel;
import com.example.sananelazimv2.Model.IlanDetaySliderModel;
import com.example.sananelazimv2.Retrofit.ApiClient;
import com.example.sananelazimv2.Retrofit.ApiInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IlanDetayActivity extends AppCompatActivity {

    private TextView lblIlanBaslikDetay,lblIlanDetayFiyat,lblIlanDetayKategori,lblIlanDetayDurunu,lblIlanDetayAciklama;
    private FloatingActionButton btnIlanDetayMesajlasma,btnIlanDetayFavoriEkle,btnOpen,btnCall;
    private ViewPager ilanDetaySlider; //Slider İçin.
    String advertisementId;
    ApiInterface apiInterface;

    List<IlanDetaySliderModel> list;
    SliderAdapter sliderAdapter;

    CircleIndicator circleIndicator;//Sayfalama İçin

    SharedPreferences sharedPreferences;
    String memberId, otherId, id;

    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;

    private Boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilan_detay);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Bundle bundle  =  getIntent().getExtras();
        advertisementId = bundle.getString("ilanid");

        sharedPreferences = getApplicationContext().getSharedPreferences("login",0);
        memberId = sharedPreferences.getString("memberId",null);


        lblIlanBaslikDetay = (TextView)findViewById(R.id.lblIlanBaslikDetay);
        lblIlanDetayAciklama = (TextView)findViewById(R.id.lblIlanDetayAciklama);
        lblIlanDetayFiyat = (TextView)findViewById(R.id.lblIlanDetayFiyat);
        lblIlanDetayKategori = (TextView)findViewById(R.id.lblIlanDetayKategori);
        lblIlanDetayDurunu = (TextView)findViewById(R.id.lblIlanDetayDurunu);

        ilanDetaySlider = (ViewPager)findViewById(R.id.ilanDetaySlider);
        circleIndicator = (CircleIndicator)findViewById(R.id.sliderCircle);

        btnIlanDetayFavoriEkle = (FloatingActionButton) findViewById(R.id.favoriEkle);
        btnIlanDetayMesajlasma = (FloatingActionButton)findViewById(R.id.mesajAt);

        btnCall = (FloatingActionButton)findViewById(R.id.call);
        btnOpen = (FloatingActionButton)findViewById(R.id.open);

        getIlanDetay();
        getImage();
        getText();
        action();

        rotateOpen = AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonClicked();
            }
        });


    }

    private void onAddButtonClicked(){
        setVisibility(clicked);
        setAnimation(clicked);
        setClickable(clicked);

        clicked = !clicked;
    }

    private void setVisibility(Boolean clicked){
        if(!clicked){
            btnCall.setVisibility(View.VISIBLE);
            btnIlanDetayFavoriEkle.setVisibility(View.VISIBLE);
            btnIlanDetayMesajlasma.setVisibility(View.VISIBLE);
        }
        else{
            btnCall.setVisibility(View.INVISIBLE);
            btnIlanDetayFavoriEkle.setVisibility(View.INVISIBLE);
            btnIlanDetayMesajlasma.setVisibility(View.INVISIBLE);
        }

    }

    private void setAnimation(Boolean clicked){
        if(!clicked){
            btnOpen.startAnimation(rotateOpen);
            btnCall.startAnimation(fromBottom);
            btnIlanDetayFavoriEkle.startAnimation(fromBottom);
            btnIlanDetayMesajlasma.startAnimation(fromBottom);
        }
        else{
            btnOpen.startAnimation(rotateClose);
            btnCall.startAnimation(toBottom);
            btnIlanDetayFavoriEkle.startAnimation(toBottom);
            btnIlanDetayMesajlasma.startAnimation(toBottom);
        }

    }

    private void setClickable(Boolean clicked){
        if(!clicked){
            btnCall.setClickable(true);
            btnIlanDetayFavoriEkle.setClickable(true);
            btnIlanDetayMesajlasma.setClickable(true);
        }
        else{
            btnCall.setClickable(false);
            btnIlanDetayFavoriEkle.setClickable(false);
            btnIlanDetayMesajlasma.setClickable(false);
        }

    }

    public void getIlanDetay(){
        Call<IlanDetayModel> request = apiInterface.ilanDetay(advertisementId);
        request.enqueue(new Callback<IlanDetayModel>() {
            @Override
            public void onResponse(Call<IlanDetayModel> call, Response<IlanDetayModel> response) {
                lblIlanBaslikDetay.setText((String)response.body().getTitle());
                lblIlanDetayAciklama.setText((String)response.body().getDescription());
                lblIlanDetayFiyat.setText((String)response.body().getPrice());
                lblIlanDetayKategori.setText((String)response.body().getCategory());
                lblIlanDetayDurunu.setText((String)response.body().getState());

                otherId = String.valueOf(response.body().getMemberId());
            }

            @Override
            public void onFailure(Call<IlanDetayModel> call, Throwable t) {

            }
        });
    }

    public void getImage(){
        Call<List<IlanDetaySliderModel>> request = apiInterface.ilanDetaySlider(advertisementId);
        request.enqueue(new Callback<List<IlanDetaySliderModel>>() {
            @Override
            public void onResponse(Call<List<IlanDetaySliderModel>> call, Response<List<IlanDetaySliderModel>> response) {
                list = response.body();
                sliderAdapter = new SliderAdapter(list,getApplicationContext());
                ilanDetaySlider.setAdapter(sliderAdapter);
                circleIndicator.setViewPager(ilanDetaySlider);
                circleIndicator.bringToFront();
            }

            @Override
            public void onFailure(Call<List<IlanDetaySliderModel>> call, Throwable t) {

            }
        });
    }

    public void getText(){
        Call<FavoriModel> request = apiInterface.getButtonText(memberId,advertisementId);
        request.enqueue(new Callback<FavoriModel>() {
            @Override
            public void onResponse(Call<FavoriModel> call, Response<FavoriModel> response) {
                if (response.body().isTf()){
                    btnIlanDetayFavoriEkle.setImageResource(R.drawable.ic_red_favorite_24);
                }else{
                    btnIlanDetayFavoriEkle.setImageResource(R.drawable.ic_un_favorite_border_24);
                }
            }

            @Override
            public void onFailure(Call<FavoriModel> call, Throwable t) {
                Log.d("Hata Tag", "Hata:"+t);
            }
        });
    }

    public void action(){
        btnIlanDetayFavoriEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<FavoriIslemModel> request = apiInterface.favoriIslemler(memberId,advertisementId);
                request.enqueue(new Callback<FavoriIslemModel>() {
                    @Override
                    public void onResponse(Call<FavoriIslemModel> call, Response<FavoriIslemModel> response) {
                        if (response.body().isTf()){
                            Toast.makeText(IlanDetayActivity.this,response.body().getText(),Toast.LENGTH_LONG).show();
                            getText();
                        }else{
                            Toast.makeText(IlanDetayActivity.this,response.body().getText(),Toast.LENGTH_LONG).show();
                            getText();
                        }
                    }

                    @Override
                    public void onFailure(Call<FavoriIslemModel> call, Throwable t) {

                    }
                });
            }
        });

        btnIlanDetayMesajlasma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherId.equals(memberId)){
                    Toast.makeText(IlanDetayActivity.this,"Kendi İlanınıza Mesaj Gönderemezsiniz",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(IlanDetayActivity.this,ActivityChat.class);
                    OtherId.setOtherId(otherId);
                    startActivity(intent);
                }
            }
        });


        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherId.equals(memberId)){
                    Toast.makeText(IlanDetayActivity.this,"Kendi Numaranızı Arayamazsınız",Toast.LENGTH_LONG).show();
                }else{
                    id = otherId;
                    Call<CallPhoneModel> request = apiInterface.callNumber(id);
                    request.enqueue(new Callback<CallPhoneModel>() {
                        @Override
                        public void onResponse(Call<CallPhoneModel> call, Response<CallPhoneModel> response) {
                            if (response.body()!=null){
                                String numara = String.valueOf(response.body().getPhone());
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+numara));
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<CallPhoneModel> call, Throwable t) {

                        }
                    });
                }


            }
        });

    }
}