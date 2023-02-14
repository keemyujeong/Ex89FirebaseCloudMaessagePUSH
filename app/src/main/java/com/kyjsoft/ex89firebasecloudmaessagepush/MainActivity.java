package com.kyjsoft.ex89firebasecloudmaessagepush;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kyjsoft.ex89firebasecloudmaessagepush.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btn.setOnClickListener(view -> clickBtn());
        binding.btn2.setOnClickListener(view -> clickBtn2());
        binding.btn3.setOnClickListener(view -> clickBtn3());
    }

    void clickBtn3(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic("FCM").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) Toast.makeText(MainActivity.this, "ㅜㅜ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void clickBtn2(){
        // 구독누른 사람한테만 알림이 오도록
        FirebaseMessaging.getInstance().subscribeToTopic("FCM").addOnCompleteListener(task -> {
            if(task.isSuccessful()) Toast.makeText(this, "ㅋㅋ", Toast.LENGTH_SHORT).show();
        });
    }

    void clickBtn(){
        // FCM 서버에 등록된 토큰값 받기(한번만 주는 데 잊어버릴 수도 있잖아)
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    String token = task.getResult();
                    Log.i("TOKEN", token);
                    Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}