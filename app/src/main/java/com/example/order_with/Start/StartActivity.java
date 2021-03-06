package com.example.order_with.Start;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.speech.tts.TextToSpeech.ERROR;

import com.example.order_with.R;
import com.example.order_with.Start.NonVoiceVer.NVoiceMenu;
import com.example.order_with.Start.VoiceVer.HeadsetReceiver;

import java.util.Locale;

public class StartActivity extends AppCompatActivity {
    private TextToSpeech tts;
    String startVoice = "음성이 필요하시면 기계 하단에 이어폰을 꽂아주세요.";
    final int PERMISSION = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }

        VoiceStarting();

        Button btnNonVoiceStart = (Button) findViewById(R.id.btnNonVoiceStart_start);
        btnNonVoiceStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNonVoiceVer();
            }
        });
        startVoiceVer();
    }

    private void VoiceStarting() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                    tts.speak(startVoice, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        VoiceStarting();
        Toast.makeText(this, "다시 돌아옴", Toast.LENGTH_SHORT).show();
    }

    public void startNonVoiceVer() {
        Intent intent = new Intent(this, NVoiceMenu.class);
        startActivity(intent);
    }

    private void startVoiceVer() {
        HeadsetReceiver headsetReceiver = new HeadsetReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(headsetReceiver, filter);
    }

}
