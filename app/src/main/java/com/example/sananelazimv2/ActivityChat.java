package com.example.sananelazimv2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sananelazimv2.Adapters.MessageAdapter;
import com.example.sananelazimv2.Model.AccountModel;
import com.example.sananelazimv2.Model.MessageModel;
import com.example.sananelazimv2.Retrofit.ApiClient;
import com.example.sananelazimv2.Retrofit.ApiInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityChat extends AppCompatActivity {


    String userId, otherId, userTable, otherTable, key, zaman;
    SharedPreferences sharedPreferences;
    DatabaseReference reference;

    FloatingActionButton btnGonder;
    EditText txtMesaj;

    List<MessageModel> list;
    MessageAdapter messageAdapter;

    RecyclerView recyclerView;

    TextView userName;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tanimla();
        action();
        load();
    }

    public void tanimla() {
        txtMesaj = (EditText) findViewById(R.id.txtMesaj);
        btnGonder = (FloatingActionButton) findViewById(R.id.btnGonder);

        userName = (TextView) findViewById(R.id.txtUserName);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        otherId = OtherId.getOtherId();
        sharedPreferences = getApplicationContext().getSharedPreferences("login", 0);
        userId = sharedPreferences.getString("memberId", null);
        reference = FirebaseDatabase.getInstance().getReference();

        long msTime = System.currentTimeMillis();
        Date curDateTime = new Date(msTime);
        SimpleDateFormat formatter = new SimpleDateFormat("dd'/'MM'/'y HH:mm:ss");
        zaman = formatter.format(curDateTime);


        list = new ArrayList<>();
        messageAdapter = new MessageAdapter(list, getApplicationContext(), userId);
        recyclerView = (RecyclerView) findViewById(R.id.mesajListe);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(messageAdapter);

        getUser(otherId, userName);

    }

    public void action() {
        btnGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtMesaj.getText().toString().equals("")) {
                    Toast.makeText(ActivityChat.this, "Boş Mesaj Atamazsınız", Toast.LENGTH_LONG).show();
                } else {
                    sendMessage(txtMesaj.getText().toString(), userId, otherId, zaman);
                }
            }
        });
    }


    public void sendMessage(String messageBody, String usrId, String othId, String zaman) {
        userTable = "messages/" + userId + "/" + otherId;
        otherTable = "messages/" + otherId + "/" + userId;
        key = reference.child("messages").child(userTable).child(otherTable).push().getKey();

        Log.i("Keyim", key);
        Map map = send(messageBody, usrId, othId, zaman);
        Map map2 = new HashMap();

        map2.put(userTable + "/" + key, map);
        map2.put(otherTable + "/" + key, map);

        txtMesaj.setText("");
        reference.updateChildren(map2, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

            }
        });
    }

    public Map send(String messageBody, String userId, String otherId, String zaman) {
        Map message = new HashMap();
        message.put("mesaj", messageBody);
        message.put("from", userId);
        message.put("to", otherId);
        message.put("zaman", zaman);
        return message;
    }

    public void load() {

        reference.child("messages").child(userId).child(otherId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessageModel messageModel = snapshot.getValue(MessageModel.class);
                list.add(messageModel);
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(list.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessageModel messageModel = snapshot.getValue(MessageModel.class);
                list.add(messageModel);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                MessageModel messageModel = snapshot.getValue(MessageModel.class);
                list.add(messageModel);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getUser(String memberId, TextView textView) {
        Call<AccountModel> request = apiInterface.account(memberId);
        request.enqueue(new Callback<AccountModel>() {
            @Override
            public void onResponse(Call<AccountModel> call, Response<AccountModel> response) {
                if (response.isSuccessful()) {
                    textView.setText(response.body().getUsername().toString().toUpperCase());
                }
            }

            @Override
            public void onFailure(Call<AccountModel> call, Throwable t) {
                Log.i("My Tag : ", "Hata : " + t);
            }
        });
    }

}