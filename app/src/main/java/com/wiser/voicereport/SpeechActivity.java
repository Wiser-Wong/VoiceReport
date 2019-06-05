package com.wiser.voicereport;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class SpeechActivity extends Activity {
    private Button btnVoiceReport;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVoiceReport = findViewById(R.id.btn_voice_report);

        btnVoiceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TtsTool.getInstance().speak("voice report 今天天气晴朗，风和日丽。");
            }
        });
    }
}
