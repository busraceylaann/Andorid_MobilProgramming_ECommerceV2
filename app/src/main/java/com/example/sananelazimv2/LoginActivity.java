package com.example.sananelazimv2;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
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

import com.example.sananelazimv2.Model.LoginModel;
import com.example.sananelazimv2.Retrofit.ApiClient;
import com.example.sananelazimv2.Retrofit.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    @BindView(R.id.editTextEmail) EditText editTextEmail;
    @BindView(R.id.editTextPassword) EditText editTextPassword;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView imageView = (ImageView) findViewById(R.id.manzaraImg);

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


        ButterKnife.bind(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        sharedPreferences = getApplicationContext().getSharedPreferences("login",0);
        if (sharedPreferences.getString("memberEmail",null)!=null&&sharedPreferences.getString("memberId",null)!=null){
            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void loginUser(View v){
        Call<LoginModel> memberModelCall = apiInterface.loginuser(editTextEmail.getText().toString(),
                editTextPassword.getText().toString());

        memberModelCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful()){

                    if (response.body().getEmail()!=null&&response.body().getId()!=null){
                        String memberEmail=response.body().getEmail().toString();
                        String memberId=response.body().getId().toString();
                        sharedPreferences = getApplicationContext().getSharedPreferences("login",0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("memberEmail",memberEmail);
                        editor.putString("memberId",memberId);
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(LoginActivity.this,"Giriş Başarılı",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Kullanıcı Bulunamadı",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Hata : Veri Tabanına Bağlanamadı",Toast.LENGTH_LONG).show();
                Log.i("My Tag ","Hata : "+t);
            }
        });
    }

    public void registerUser(View v){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    public void sifremiUnuttum(View v){
        /*Intent intent = new Intent(LoginActivity.this,SifremiUnuttumActivity.class);
        startActivity(intent);
        finish();*/
    }
}