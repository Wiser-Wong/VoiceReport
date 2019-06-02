package com.wiser.voicereport;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;

    private Button btnVoiceReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVoiceReport = findViewById(R.id.btn_voice_report);

        btnVoiceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAuto("语音播报 今天天气晴朗，风和日丽");
            }
        });

        textToSpeech = new TextToSpeech(this, this);
    }

    private void startAuto(String data) {
        if (data == null || "".equals(data)) return;
        // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
        textToSpeech.setPitch(1.0f);
        // 设置语速
        textToSpeech.setSpeechRate(0.3f);
        textToSpeech.speak(data,//输入中文，若不支持的设备则不会读出来
                TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        textToSpeech.stop(); // 不管是否正在朗读TTS都被打断
        textToSpeech.shutdown(); // 关闭，释放资源
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setPitch(1.0f);//方法用来控制音调
            textToSpeech.setSpeechRate(1.0f);//用来控制语速

            //判断是否支持下面两种语言
            int result1 = textToSpeech.setLanguage(Locale.US);
            int result2 = textToSpeech.setLanguage(Locale.
                    SIMPLIFIED_CHINESE);
            boolean a = (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED);
            boolean b = (result2 == TextToSpeech.LANG_MISSING_DATA || result2 == TextToSpeech.LANG_NOT_SUPPORTED);

            Log.i("zhh_tts", "US支持否？--》" + a +
                    "\nzh-CN支持否》--》" + b);
        } else {
            Toast.makeText(this, "数据丢失或不支持", Toast.LENGTH_LONG).show();
        }
    }
}
