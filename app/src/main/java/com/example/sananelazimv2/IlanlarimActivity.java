package com.example.sananelazimv2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sananelazimv2.Adapters.IlanlarimAdapter;
import com.example.sananelazimv2.Model.AdvertisementModel;
import com.example.sananelazimv2.Model.IlanSilModel;
import com.example.sananelazimv2.Model.IlanlarimModel;
import com.example.sananelazimv2.Retrofit.ApiClient;
import com.example.sananelazimv2.Retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IlanlarimActivity extends AppCompatActivity {

    ListView listView;
    IlanlarimAdapter ilanlarimAdapter;
    List<IlanlarimModel> ilanlarimModels;

    ApiInterface apiInterface;
    SharedPreferences sharedPreferences;
    String memberId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilanlarim);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);


        sharedPreferences = getSharedPreferences("login", 0);
        AdvertisementModel.setMemberId(sharedPreferences.getString("memberId", null));

        listView = findViewById(R.id.ilanlarimListView);
        ilanlarimGoruntule();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ilanlarimAlertDialog(IlanlarimActivity.this,(String) ilanlarimModels.get(position).getAdvertisementId());
            }
        });

    }

    public void ilanlarimGoruntule(){
        ilanlarimModels = new ArrayList<>();
        Call<List<IlanlarimModel>> request = apiInterface.ilanlarim(AdvertisementModel.getMemberId());
        request.enqueue(new Callback<List<IlanlarimModel>>() {
            @Override
            public void onResponse(Call<List<IlanlarimModel>> call, Response<List<IlanlarimModel>> response) {
                if (response.isSuccessful()){
                    ilanlarimModels = response.body();
                    if (response.body().get(0).isTf()) {
                        ilanlarimAdapter = new IlanlarimAdapter(ilanlarimModels, getApplicationContext(), IlanlarimActivity.this);
                        listView.setAdapter(ilanlarimAdapter);
                        Toast.makeText(getApplicationContext(), response.body().get(0).getSayi() + " İlanınız bulunmaktadır.", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), " İlanınız Bulunmamaktadır.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<IlanlarimModel>> call, Throwable t) {
                Log.i("test 1","hata : "+t);
            }
        });
    }

    public void ilanlarimAlertDialog(Activity activity, final String advertisementId){
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.alertlayout, null);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);


        Button btnSil = (Button)view.findViewById(R.id.btnSil);
        Button btnDuzenle = (Button)view.findViewById(R.id.btnDuzenle);
        Button btnIptalEt = (Button)view.findViewById(R.id.btnIptalEt);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();

        btnIptalEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        btnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sil(advertisementId);
                alertDialog.cancel();
            }
        });

        btnDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IlanlarimActivity.this, IlanBilgiUpdateActivity.class);
                intent.putExtra("advertisementId",advertisementId);
                startActivity(intent);
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void sil(String advertisementId){
        Call<IlanSilModel> request = apiInterface.ilansil(advertisementId);
        request.enqueue(new Callback<IlanSilModel>() {
            @Override
            public void onResponse(Call<IlanSilModel> call, Response<IlanSilModel> response) {
                if (response.body().isTf()){
                    ilanlarimGoruntule();
                }
            }

            @Override
            public void onFailure(Call<IlanSilModel> call, Throwable t) {

            }
        });
    }
}