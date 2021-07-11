package com.example.sananelazimv2;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sananelazimv2.Model.RegisterModel;
import com.example.sananelazimv2.Retrofit.ApiClient;
import com.example.sananelazimv2.Retrofit.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


    @BindView(R.id.editTextEmail) EditText editTextEmail;
    @BindView(R.id.editTextUsername) EditText editTextUsername;
    @BindView(R.id.editTextPhone) EditText editTextPhone;
    @BindView(R.id.editTextPassword) EditText editTextPassword;

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
    }

    public void registerUser(View v){

        Call<RegisterModel> callRegister = apiInterface.registerUser(editTextEmail.getText().toString(),
                editTextUsername.getText().toString(),
                editTextPhone.getText().toString(),
                editTextPassword.getText().toString());

        callRegister.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                if (response.isSuccessful() && response.body()!=null){
                    RegisterModel registerModel = response.body();

                    if (registerModel.isSuccess()){
                        Toast.makeText(RegisterActivity.this,"Kayıt Başarı İle Gerçekleşti",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"Kullanıcı Kayıt Edilemedi",Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                Log.w("MyTag", "requestFailed", t);
                Toast.makeText(RegisterActivity.this,"Hata : Veri Tabanına Bağlanamadı",Toast.LENGTH_LONG).show();
            }
        });
    }
}