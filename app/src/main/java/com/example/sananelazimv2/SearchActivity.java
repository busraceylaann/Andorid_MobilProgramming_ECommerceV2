package com.example.sananelazimv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sananelazimv2.Adapters.SearchAdapter;
import com.example.sananelazimv2.Model.TumIlanlarModel;
import com.example.sananelazimv2.Retrofit.ApiClient;
import com.example.sananelazimv2.Retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchActivity extends AppCompatActivity {
    ListView listView;
    ApiInterface apiInterface;
    List<TumIlanlarModel> tumIlanlarModels;
    SearchAdapter searchAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);


        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listView = (ListView) findViewById(R.id.searchListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =  new Intent(SearchActivity.this,IlanDetayActivity.class);
                intent.putExtra("ilanid", String.valueOf(tumIlanlarModels.get(position).getAdvertisementId()));
                startActivity(intent);

            }
        });
        tumIlanlarGoruntule();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search,menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)menuItem.getActionView();

        searchView.setQueryHint("İlan Ara");
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
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
                        searchAdapter = new SearchAdapter(tumIlanlarModels,getApplicationContext());
                        listView.setAdapter(searchAdapter);
                        //progressDialog.cancel();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),  " İlan Bulunamamıştır.", Toast.LENGTH_LONG).show();
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