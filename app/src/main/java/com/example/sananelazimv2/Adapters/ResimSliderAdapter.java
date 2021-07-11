package com.example.sananelazimv2.Adapters;

import android.app.Activity;
import android.content.Context;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;


import com.example.sananelazimv2.IlanResimDuzenlemeActivity;
import com.example.sananelazimv2.Model.ResimDuzenleSliderModel;
import com.example.sananelazimv2.Model.ResimSilModel;
import com.example.sananelazimv2.R;
import com.example.sananelazimv2.Retrofit.ApiClient;
import com.example.sananelazimv2.Retrofit.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResimSliderAdapter extends PagerAdapter {

    List<ResimDuzenleSliderModel> list;
    Context context;
    Activity activity;
    LayoutInflater layoutInflater;
    ApiInterface apiInterface;

    public ResimSliderAdapter(List<ResimDuzenleSliderModel> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (RelativeLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.resimduzenleslider_layout, container, false);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        ImageView imageView = (ImageView) view.findViewById(R.id.sliderResim); //İlan Resimlerimizi Üst Üste Basacağımız İmageviewu Tanımladık.

        Picasso.with(context).load("http://192.168.1.6:80/snldb_files/" + list.get(position).getImage()).resize(1050, 600).into(imageView); //Veri Tabanımızdaki Kayıtlı Resimleri İmageVİewa Basıyoruz.

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Uyarı");
                builder.setMessage("Silmek İstediğinize Emin Misiniz?");
                builder.setNegativeButton("İptal Et", null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<ResimSilModel> request = apiInterface.resimsil(String.valueOf(list.get(position).getImageId()));
                        request.enqueue(new Callback<ResimSilModel>() {
                            @Override
                            public void onResponse(Call<ResimSilModel> call, Response<ResimSilModel> response) {
                                if (response.body().isTf()) {
                                    Toast.makeText(activity, response.body().getSonuc(), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResimSilModel> call, Throwable t) {
                                Log.i("test 1 ","hata : "+t);
                            }
                        });
                    }
                });
                builder.show();

            }
        });

        container.addView(view); //viewı containera ekledilk kalıp bu.
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //Bunu yazmamaızın amacı pageAdapter kullandığımız için view mızı destroy etmesin diye içindeki super metodunu silerek slideradepter sayfamızda Override ettik.
    }

}
