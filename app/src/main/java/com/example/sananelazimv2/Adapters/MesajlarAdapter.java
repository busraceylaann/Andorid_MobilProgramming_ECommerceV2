package com.example.sananelazimv2.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sananelazimv2.ActivityChat;
import com.example.sananelazimv2.Model.AccountModel;
import com.example.sananelazimv2.OtherId;
import com.example.sananelazimv2.R;
import com.example.sananelazimv2.Retrofit.ApiClient;
import com.example.sananelazimv2.Retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MesajlarAdapter extends BaseAdapter {

    List<String> otherIdList;
    String userId;
    Context context;
    ApiInterface apiInterface;
    Activity activity;

    public MesajlarAdapter(List<String> otherIdList, String userId, Context context, Activity activity) {
        this.otherIdList = otherIdList;
        this.userId = userId;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return otherIdList.size();
    }

    @Override
    public Object getItem(int position) {
        return otherIdList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.gelen_mesaj_liste, parent, false); //layoutumuzu tanımladık.
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        TextView textView = (TextView) convertView.findViewById(R.id.txtUserLeft);

        getUser(otherIdList.get(position),textView);

        LinearLayout linearOtherLayout = (LinearLayout)convertView.findViewById(R.id.linearOtherLayout);
        linearOtherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ActivityChat.class);  //İlgili Satırın Mesaj İçeriğini Açmasını Sağlar
                OtherId.setOtherId(otherIdList.get(position));  //Listemizin İlgili Satırından Dönen Id yi set eder.
                activity.startActivity(intent);
            }
        });
        return convertView;
    }

    public void getUser(String memberId, TextView textView){
        Call<AccountModel> request = apiInterface.account(memberId);
        request.enqueue(new Callback<AccountModel>() {
            @Override
            public void onResponse(Call<AccountModel> call, Response<AccountModel> response) {
                if (response.isSuccessful()){
                    textView.setText(response.body().getUsername().toString().toUpperCase());
                }
            }

            @Override
            public void onFailure(Call<AccountModel> call, Throwable t) {
                Log.i("My Tag : ","Hata : "+t);
            }
        });
    }
}
