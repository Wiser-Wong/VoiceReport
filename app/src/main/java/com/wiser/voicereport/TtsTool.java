package com.wiser.voicereport;

import android.content.Context;
import android.util.Log;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizeBag;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.wiser.voicereport.tool.OfflineResource;

import java.io.IOException;
import java.util.List;

/**
 * @author Wiser
 * <p>
 * 语音合成工具
 */
public class TtsTool {
    private final String TAG = TtsTool.class.getName();

    private SpeechSynthesizer mSpeechSynthesizer;

    private static class TtsToolHolder {
        private static TtsTool ttsTool = new TtsTool();
    }

    public static TtsTool getInstance() {
        return TtsToolHolder.ttsTool;
    }

    public SpeechSynthesizer getSpeechSynthesizer() {
        return mSpeechSynthesizer;
    }

    /**
     * 初始化语音合成包
     * 初始化引擎，需要的参数均在InitConfig类里
     * DEMO中提供了3个SpeechSynthesizerListener的实现
     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
     * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     *
     * @param context
     */
    public void initSpeechTts(Context context) {
        LoggerProxy.printable(true); // 日志打印在logcat中
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        // 设置context
        mSpeechSynthesizer.setContext(context);
        // 设置语音合成状态监听器
        mSpeechSynthesizer.setSpeechSynthesizerListener(speechSynthesizerListener);
        // 设置在线语音合成授权，需要填入从百度语音官网申请的api_key和secret_key
        mSpeechSynthesizer.setApiKey("yKDHoRG0Y9xdrk3yTeDEf7Fm", "Aa95zf6iuyAhrc7lu4NCrWFTLNA0owTg");
        // 设置离线语音合成授权，需要填入从百度语音官网申请的app_id
        mSpeechSynthesizer.setAppId("16434912");
        // 获取语音合成授权信息
        AuthInfo authInfo = mSpeechSynthesizer.auth(TtsMode.MIX);
        if (authInfo.isSuccess()) {
            Log.e(TAG, "Tts语音合成:---->>授权成功");
            mSpeechSynthesizer.initTts(TtsMode.MIX);
            // 设置合成的音量，0-9 ，默认 5
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
            // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
            // 设置合成的语速，0-9 ，默认 5
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
            // 设置合成的语调，0-9 ，默认 5
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
            // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
            // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
            // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
            // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
            // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_NETWORK);
            // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
            OfflineResource offlineResource = createOfflineResource(context, OfflineResource.VOICE_MALE);
            // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, offlineResource.getModelFilename());
        } else {
            // 授权失败
            Log.e(TAG, "Tts语音合成:---->>授权失败");
        }
    }

    /**
     * 创建离线资源
     *
     * @param context
     * @param voiceType
     * @return
     */
    protected OfflineResource createOfflineResource(Context context, String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(context, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
            Log.e(TAG, "Offline:---->>" + e.getMessage());
        }
        return offlineResource;
    }

    public SpeechSynthesizerListener speechSynthesizerListener = new SpeechSynthesizerListener() {
        @Override
        public void onSynthesizeStart(String s) {
            Log.e(TAG,"onSynthesizeStart:-->>"+s);
        }

        @Override
        public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
            Log.e(TAG,"onSynthesizeDataArrived:-->>"+s);
        }

        @Override
        public void onSynthesizeFinish(String s) {
            Log.e(TAG,"onSynthesizeFinish:-->>"+s);
        }

        @Override
        public void onSpeechStart(String s) {
            Log.e(TAG,"onSpeechStart:-->>"+s);
        }

        @Override
        public void onSpeechProgressChanged(String s, int i) {
            Log.e(TAG,"onSpeechProgressChanged:-->>"+i);
        }

        @Override
        public void onSpeechFinish(String s) {
            Log.e(TAG,"onSpeechFinish:-->>"+s);
        }

        @Override
        public void onError(String s, SpeechError speechError) {
            Log.e(TAG,"onError:-->>"+s);
        }
    };

    /**
     * 合成但是不播放，
     * 音频流保存为文件的方法可以参见SaveFileActivity及FileSaveListener
     */
    public void synthesize(String text) {
        int result = mSpeechSynthesizer.synthesize(text);
        Log.e(TAG, "语音合成但不播放:-->>" + result);
    }

    /**
     * speak 实际上是调用 synthesize后，获取音频流，然后播放。
     * 获取音频流的方式见SaveFileActivity及FileSaveListener
     * 需要合成的文本text的长度不能超过1024个GBK字节。
     */
    public void speak(String text) {
        if (text == null || "".equals(text)) return;
        // 合成前可以修改参数：
        // Map<String, String> params = getParams();
        // synthesizer.setParams(params);
        int result = mSpeechSynthesizer.speak(text);
        Log.e(TAG, "speakResult:---->>" + result);
    }


    /**
     * 批量播放
     *
     * @param speakTexts
     */
    public void batchSpeak(List<SpeechSynthesizeBag> speakTexts) {
        if (speakTexts == null || speakTexts.size() == 0 || mSpeechSynthesizer == null) return;
        mSpeechSynthesizer.batchSpeak(speakTexts);
    }

    /**
     * 暂停播放。仅调用speak后生效
     */
    public void pause() {
        if (mSpeechSynthesizer == null) return;
        int result = mSpeechSynthesizer.pause();
        Log.e(TAG, "pauseResult:---->>" + result);
    }

    /**
     * 继续播放。仅调用speak后生效，调用pause生效
     */
    public void resume() {
        if (mSpeechSynthesizer == null) return;
        int result = mSpeechSynthesizer.resume();
        Log.e(TAG, "resumeResult:---->>" + result);
    }

    /**
     * 停止合成引擎。即停止播放，合成，清空内部合成队列。
     */
    public void stop() {
        if (mSpeechSynthesizer == null) return;
        int result = mSpeechSynthesizer.stop();
        Log.e(TAG, "stopResult:---->>" + result);
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mSpeechSynthesizer == null) return;
        mSpeechSynthesizer.release();
    }

}
