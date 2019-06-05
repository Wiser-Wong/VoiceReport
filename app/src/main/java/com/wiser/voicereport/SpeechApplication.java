package com.wiser.voicereport;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import java.util.List;

public class SpeechApplication extends Application {

    private static SpeechApplication instance;

    @Override
    public void onCreate() {
        instance = this;
        String processName = getProcessName(SpeechApplication.getInstance(), android.os.Process.myPid());
        if (processName != null) {
            boolean defaultProcess = processName.equals("com.wiser.voicereport");
            if (defaultProcess) {
                TtsTool.getInstance().initSpeechTts(this);
            }
        }
        super.onCreate();
    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static SpeechApplication getInstance() {
        return instance;
    }

}
