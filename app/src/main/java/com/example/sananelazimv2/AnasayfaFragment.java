package com.example.sananelazimv2;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sananelazimv2.Adapters.TumIlanlarAdapter;
import com.example.sananelazimv2.Model.TumIlanlarModel;
import com.example.sananelazimv2.Retrofit.ApiClient;
import com.example.sananelazimv2.Retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnasayfaFragment extends Fragment {
    ListView listView;
    ApiInterface apiInterface;
    List<TumIlanlarModel> tumIlanlarModels;
    TumIlanlarAdapter tumIlanlarAdapter;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.anasayfa_fragment, container, false);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        listView = view.findViewById(R.id.ilanlarimListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =  new Intent(getActivity(),IlanDetayActivity.class);
                intent.putExtra("ilanid", String.valueOf(tumIlanlarModels.get(position).getAdvertisementId()));
                startActivity(intent);

            }
        });
        tumIlanlarGoruntule();

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.ansayfaRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tumIlanlarGoruntule();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void tumIlanlarGoruntule() {
        /*final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Tüm İlanlar");
        progressDialog.setMessage("Tüm İlanlar Listeleniyor, Lütfen Bekleyin...");
        progressDialog.setCancelable(false);
        progressDialog.show();*/

        Call<List<TumIlanlarModel>> request = apiInterface.tumIlanlar();
        request.enqueue(new Callback<List<TumIlanlarModel>>() {
            @Override
            public void onResponse(Call<List<TumIlanlarModel>> call, Response<List<TumIlanlarModel>> response) {
                if (response.isSuccessful()){
                    if (response.body().get(0).isTf()){
                        tumIlanlarModels = response.body();
                        tumIlanlarAdapter = new TumIlanlarAdapter(tumIlanlarModels,getActivity().getApplicationContext());
                        listView.setAdapter(tumIlanlarAdapter);
                        //progressDialog.cancel();
                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(),  " İlan Bulunamamıştır.", Toast.LENGTH_LONG).show();
                        //progressDialog.cancel();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TumIlanlarModel>> call, Throwable t) {

            }
        });
    }


}