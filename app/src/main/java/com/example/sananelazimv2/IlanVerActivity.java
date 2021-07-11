package com.example.sananelazimv2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sananelazimv2.Model.AdvertisementModel;
import com.example.sananelazimv2.Model.AdvertisementResultModel;
import com.example.sananelazimv2.Retrofit.ApiClient;
import com.example.sananelazimv2.Retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IlanVerActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ApiInterface apiInterface;

    AdvertisementResultModel advertisementResultModel;

    EditText editTextİlanBaslik,editTextİlanAciklama, editTextİlanFiyat;
    String[] category = {"Kategori Seçin", "Elektronik", "Giyim", "Ev/Bahçe", "Kişisel Bakım", "Araç Parçaları", "Diğer"};

    String[] state = {"Ürün Durumu Seçin", "Sıfır Ürün", "İkinci El Ürün"};

    Spinner spinnerCategory, spinnerState;
    ArrayAdapter arrayAdapterCategory, arrayAdapterState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilan_ver);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        editTextİlanBaslik = findViewById(R.id.editTextİlanBaslik);
        editTextİlanAciklama = findViewById(R.id.editTextİlanAciklama);
        editTextİlanFiyat = findViewById(R.id.editTextİlanFiyat);

        spinnerCategory = findViewById(R.id.spinnerKategori);
        spinnerState = findViewById(R.id.spinnerUrunDurumu);

        arrayAdapterCategory = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, category);
        spinnerCategory.setAdapter(arrayAdapterCategory);

        arrayAdapterState = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, state);
        spinnerState.setAdapter(arrayAdapterState);

        // editTextİlanBaslik.setText(AdvertisementModel.getTitle());
        // editTextİlanAciklama.setText(AdvertisementModel.getDescription());
        // editTextİlanFiyat.setText(AdvertisementModel.getPrice());

        sharedPreferences = this.getSharedPreferences("login", 0);
        AdvertisementModel.setMemberId(sharedPreferences.getString("memberId", null));

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Button buttonİleri = findViewById(R.id.btnİleri);
        buttonİleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextİlanBaslik.getText().toString().equals("") || editTextİlanAciklama.getText().toString().equals("") || editTextİlanFiyat.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Lütfen İlan İle İlgili Boş Olan Yerleri Doldurunuz!", Toast.LENGTH_LONG).show();
                } else if (spinnerCategory.getSelectedItem().equals("Kategori Seçin") || spinnerState.getSelectedItem().equals("Ürün Durumu Seçin")) {
                    Toast.makeText(getApplicationContext(), "Lütfen Kategori Ve Ürün Durumu Seçiniz", Toast.LENGTH_LONG).show();
                } else {
                    AdvertisementModel.setTitle(editTextİlanBaslik.getText().toString());
                    AdvertisementModel.setDescription(editTextİlanAciklama.getText().toString());
                    AdvertisementModel.setPrice(editTextİlanFiyat.getText().toString());
                    AdvertisementModel.setCategory(spinnerCategory.getSelectedItem().toString());
                    AdvertisementModel.setState((spinnerState.getSelectedItem().toString()));

                    Call<AdvertisementResultModel> advertisementModelCall = apiInterface.advertisement(AdvertisementModel.getMemberId(), AdvertisementModel.getTitle(), AdvertisementModel.getDescription(), AdvertisementModel.getPrice(), AdvertisementModel.getCategory(), AdvertisementModel.getState());
                    advertisementModelCall.enqueue(new Callback<AdvertisementResultModel>() {
                        @Override
                        public void onResponse(Call<AdvertisementResultModel> call, Response<AdvertisementResultModel> response) {
                            if (response.body().isTf()) {
                                Toast.makeText(getApplicationContext(), "İlanınız Oluşturuldu Lütfen İlan Resmini Yayınlayın", Toast.LENGTH_SHORT).show();
                                String memberId = String.valueOf(response.body().getMemberId());
                                String advertisementId = response.body().getAdvertisementId();

                                Intent intent = new Intent(IlanVerActivity.this,IlanResimVerActivity.class);
                                intent.putExtra("uyeid",memberId);
                                intent.putExtra("ilanid",advertisementId);
                                startActivity(intent);


                            }
                        }

                        @Override
                        public void onFailure(Call<AdvertisementResultModel> call, Throwable t) {

                        }
                    });
                }
            }
        });
        return;
    }
}