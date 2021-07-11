package com.example.sananelazimv2;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sananelazimv2.Model.AccountModel;
import com.example.sananelazimv2.Model.AccountUpdateModel;
import com.example.sananelazimv2.Retrofit.ApiClient;
import com.example.sananelazimv2.Retrofit.ApiInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity {

    EditText txtuserName,txtEmail,txtPhone,txtPassword;
    FloatingActionButton btnGuncelle;

    ApiInterface apiInterface;
    SharedPreferences sharedPreferences;
    String memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ButterKnife.bind(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        ImageView imageView = (ImageView) findViewById(R.id.manzaraImgKayit);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager windowManager = window.getWindowManager();

        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        int width = point.x;
        int height = point.y;

        imageView.getLayoutParams().width = (int) (height * 1.58);
        imageView.getLayoutParams().height = height;

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"x",0,-(height*1.58f-width),0,-(height*1.58f-width));
        objectAnimator.setDuration(210000);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();


        txtuserName = (EditText)findViewById(R.id.editTextUsername);
        txtEmail = (EditText)findViewById(R.id.editTextEmail);
        txtPhone = (EditText)findViewById(R.id.editTextPhone);
        txtPassword = (EditText)findViewById(R.id.editTextPassword);

        sharedPreferences = getSharedPreferences("login", 0);
        memberId = sharedPreferences.getString("memberId", null);

        getUser(memberId);

        btnGuncelle = (FloatingActionButton) findViewById(R.id.btnGuncelle);
        btnGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtuserName.getText().equals("")&&txtEmail.getText().equals("")&&txtPhone.getText().equals("")&&txtPassword.getText().equals("")){
                    Toast.makeText(AccountActivity.this,"Eksik Veya Yanlış Bilgi Girmediğinizden Emin Olun",Toast.LENGTH_LONG).show();
                }else{
                    getUpdate(memberId,txtuserName.getText().toString(),txtPassword.getText().toString(),txtEmail.getText().toString(),txtPhone.getText().toString());
                }
            }
        });
    }

    public void getUser(String memberId){
        Call<AccountModel> request = apiInterface.account(memberId);
        request.enqueue(new Callback<AccountModel>() {
            @Override
            public void onResponse(Call<AccountModel> call, Response<AccountModel> response) {
                if (response.isSuccessful()){
                    txtuserName.setText((String)response.body().getUsername());
                    txtEmail.setText((String)response.body().getEmail());
                    txtPhone.setText((String)response.body().getPhone());
                    txtPassword.setText((String)response.body().getPassword());
                }
            }

            @Override
            public void onFailure(Call<AccountModel> call, Throwable t) {
                Log.i("My Tag : ","Hata : "+t);
            }
        });
    }

    public void getUpdate(String memberId, String username, String password, String email, String phone){
        Call<AccountUpdateModel> request = apiInterface.accountUpdate(memberId,username,password,email,phone);
        request.enqueue(new Callback<AccountUpdateModel>() {
            @Override
            public void onResponse(Call<AccountUpdateModel> call, Response<AccountUpdateModel> response) {
                if (response.body().isTf()){
                    Toast.makeText(AccountActivity.this,"Bilgileriniz Başarıyla Güncellendi",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(AccountActivity.this,"Sunucuya Bağlanılamadı",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AccountUpdateModel> call, Throwable t) {

            }
        });
    }
}