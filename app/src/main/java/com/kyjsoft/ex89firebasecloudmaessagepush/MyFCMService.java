package com.kyjsoft.ex89firebasecloudmaessagepush;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFCMService extends FirebaseMessagingService {
    // FCM 서버에 디바이스 고유 등록 ID(토큰)이 발급되었을 때 자동으로 발동하는 콜백메소드
    // Manifest에 이 서비스가 등록되어 있다면 앱을 실행만 하면 자동으로 발동

    @Override
    public void onNewToken(@NonNull String token) { // 내 토큰이 뭔지 알라고 쓰는 콜백메소드 -> 처음 한번만 발동함.
        super.onNewToken(token);

        // 발급된 token값 확인하기
        Log.i("TOKEN", token);
        // 푸시서비스에 저장된 내 기기 token : e6qM2d3tS0CN9G9xuUTfhj:APA91bF2YYWPQWCFRf7TRRocKP3-ps597DjIW50lpE5-TEx2HpXf0IaFVGcw4q0QmFW5P6DfoPcmvbVyPOLpX_iikNSxu78vGNM7Dajx3V1ksOJzewzqbybL-qN-c-Qa7rZLoMWDKM_9
    }

    // FCM에서 푸쉬하는 것을 Message라고 표현 : 유형이 두 가지 -> 알림, 데이터
    // 1. FCM 알림 유형 수신( 연습기에서는 알림밖에 안됨. )
    // 1) foregraound일 때 : onmessageReceived가 호출됨. 알림이 자동으로 만들어지지 않음.(메소드 안에 notification직접 만드는 코드 필요)
    // 2) background일 때 : onmessageReceived가 호출되지 않음. default 알림이 공지됨. -> onReceive() 이 발동함. 별도 아이콘 설정이 없으면 둥근 흰색 아이콘이 보여짐.

    // 2. FCM 데이터 유형 수신( 실무에서는 이걸 씀. )
    // 3) foregraound일 때 : onmessageReceived가 호출됨. 알림이 자동으로 만들어지지 않음.
    // 4) background일 때 : onmessageReceived가 호출됨. 알림이 자동으로 만들어지지 않음.
    // -> 얘는 둘 다 notification이 안뜨니까 onmessageReceived() 안에 내가 직접 만드는 코드를 작성해야함.

    // 3. FCM 알림 + 데이터 유형 수신(알림 유형이랑 동일 -> 연습기(백엔드 작업 안해도되는, 웹서버에서 토큰번호/메세지 안보내도 푸시해주는 연습)에서 이게됨.)
    // 5) foregraound일 때 : onmessageReceived가 호출됨. 알림이 자동으로 만들어지지 않음. (메소드 안에 notification직접 만드는 코드 필요)
    // 6) background일 때 : onmessageReceived가 호출되지 않음. default 알림이 공지됨. -> Intent에 Extradata로 전달됨. -> 원래는 onmessageReceived로 받아야하는데 못받으니까


    @Override // FCM 메세지를 수신했을 때 자동 발동하는 콜백 메소드 1), 3), 4), 5) 만 발동
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        // 메세지 수신했음을 확인
        Log.i("TAG", "onMessageReceive"); // 애뮬레이터에서는 엄청 느리게 옴.

        // 알림메세지 유형으로 보낸 메세지의 title과 text받기
        RemoteMessage.Notification notification = message.getNotification();
        if(notification != null) {
            String title = notification.getTitle();
            String text = notification.getBody();

            Log.i("TAG", "알림메세지 : "+title + ", " +text);
        }

        // 데이터가 전달되었을 때.
        Map<String, String> data = message.getData();
        if( data.size() > 0 ){
            String name = data.get("name");
            String addr = data.get("address");

            Log.i("TAG" , "받은 데이터 : "+name + ", " +addr);

            // 사용자에게는 메세지 수신을 알림으로 통지해야함.
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            NotificationChannelCompat notificationChannelCompat = new NotificationChannelCompat.Builder("fcm_ch", NotificationManagerCompat.IMPORTANCE_HIGH).setName("EX89 FCM").build();
            notificationManagerCompat.createNotificationChannel(notificationChannelCompat);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "fcm_ch");
            // 알림에 필요한 설정들
            builder.setSmallIcon(R.drawable.ic_stat_name);
            builder.setContentTitle(name);
            builder.setContentText(addr);

            // 알림 공지하기
            notificationManagerCompat.notify(20, builder.build());


        }


    }

    //
}
