package com.example.sananelazimv2;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sananelazimv2.Adapters.FavoriIlanlarimListeAdapter;
import com.example.sananelazimv2.Model.FavoriListelemeModel;
import com.example.sananelazimv2.Retrofit.ApiClient;
import com.example.sananelazimv2.Retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriIlanlarFragment extends Fragment {

    ListView listView;
    ApiInterface apiInterface;
    List<FavoriListelemeModel> list;
    FavoriIlanlarimListeAdapter favoriIlanlarimListeAdapter;


    SharedPreferences sharedPreferences;
    String memberId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favori_ilanlar_fragment, container, false);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        sharedPreferences = getActivity().getSharedPreferences("login", 0); //daha önce kaydedilen bir veriyi kullanmak istediğimiz zaman kullanırız.
        memberId = sharedPreferences.getString("memberId", null);


        listView = view.findViewById(R.id.favoriIlanlarimListView);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), IlanDetayActivity.class);
                intent.putExtra("ilanid", String.valueOf(list.get(position).getAdvertisementId()));
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            favoriIlanlarGoruntule();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public void favoriIlanlarGoruntule() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Favori İlanlar");
        progressDialog.setMessage("Favori İlanlar Listeleniyor, Lütfen Bekleyin...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<List<FavoriListelemeModel>> request = apiInterface.setfavoriListe(memberId); //setfavori(ınterface) metoduna memberıd parametre olarak gönder
        request.enqueue(new Callback<List<FavoriListelemeModel>>() {
            @Override
            public void onResponse(Call<List<FavoriListelemeModel>> call, Response<List<FavoriListelemeModel>> response) {
                if (response.isSuccessful()) {

                    if (response.body().get(0).isTf()) {
                        list = response.body();
                        favoriIlanlarimListeAdapter = new FavoriIlanlarimListeAdapter(list, getActivity().getApplicationContext());//bu fragmenten içindeki listemizi ve application contextimizi ilanlarım liste adaptera atarız.
                        listView.setAdapter(favoriIlanlarimListeAdapter);
                        progressDialog.cancel();
                        favoriIlanlarimListeAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Favori İlan Bulunmamaktadır.", Toast.LENGTH_LONG).show();
                        progressDialog.cancel();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FavoriListelemeModel>> call, Throwable t) {
                Log.i("My Tag", "Hata : " + t);
            }
        });
    }

}