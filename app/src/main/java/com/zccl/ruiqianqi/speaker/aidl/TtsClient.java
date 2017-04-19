package com.zccl.ruiqianqi.speaker.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;


/**
 * Created by ruiqianqi on 2016/11/24 0024.
 */

public class TtsClient {
    /** 类标志 */
    private static String TAG = TtsClient.class.getSimpleName();
    /** 单例引用 */
    private static TtsClient instance;
    /** 应用上下文 */
    private Context mContext;
    /** 封装成对应接口 */
    private ISpeakerService mRemoteService;

    /**
     * 通信接口是我连接服务时，由系统的回调传递给我的
     */
    private ServiceConnection mRemoteConnection = new ServiceConnection() {
        /**
         * 这个回调是由ActivityManagerService所在进程，通过Binder间通迅机制（Binder引用为ApplicationThread）
         * 通知当前进程（ActivityThread），当前进程再通过自己的消息分发处理机制，来处理这个调用，其实也就是发送到
         * 主线程循环中进行调用。所以我们无法确认在调用方法之前是否已经绑定好了（如果绑定和调用是一起执行的话）
         */
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.e(TAG, "onServiceConnected");
            mRemoteService = ISpeakerService.Stub.asInterface(service);
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "onServiceDisconnected");
            mRemoteService = null;
        }
    };

    private TtsClient(Context context) {
        this.mContext = context.getApplicationContext();
        init();
    }

    /**
     * 用这个用话，instance不需要用volatile修饰
     * @return
     */
    public static TtsClient getInstance(Context context) {
        if(instance == null) {
            synchronized(TtsClient.class) {
                TtsClient temp = instance;
                if(temp == null) {
                    temp = new TtsClient(context);
                    instance = temp;
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     */
    private void init(){
        bindMyService();
    }

    /**
     * 绑定服务
     */
    private void bindMyService(){
        Intent intent = new Intent(ISpeakerService.class.getName());
        intent.setPackage("com.zccl.ruiqianqi.speaker");
        mContext.bindService(intent, mRemoteConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 解除绑定
     */
    private void unbindMyService(){
        mContext.unbindService(mRemoteConnection);
    }

    /**
     * 设置合成语言
     * en ------------ 英文
     * zh ------------ 中文
     * @param language
     */
    public void switchLanguage(String language){
        if(mRemoteService!=null){
            try {
                mRemoteService.switchLanguage(language);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else {
            init();
        }
    }

    /**
     * 开始语音合成
     * @param words
     */
    public void startTTS(String words){
        if(mRemoteService!=null){
            try {
                mRemoteService.startTTS(words, null, null);
                Log.e(TAG, "startTTS");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else {
            init();
        }
    }

    /**
     * 开始语音合成
     * @param words
     * @param tag      携带的文字
     * @param ttsCallback
     */
    public void startTTS(String words, String tag, TtsCallback ttsCallback){
        if(mRemoteService!=null){
            try {
                mRemoteService.startTTS(words, tag, ttsCallback);
                Log.e(TAG, "startTTS");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else {
            init();
        }
    }

    /**
     * 暂停播放
     */
    public void pauseTTS(){
        if(mRemoteService!=null){
            try {
                mRemoteService.pauseTTS();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else {
            init();
        }
    }

    /**
     * 恢复播放
     */
    public void resumeTTS(){
        if(mRemoteService!=null){
            try {
                mRemoteService.resumeTTS();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else {
            init();
        }
    }

    /**
     * 停止播放
     */
    public void stopTTS(){
        if(mRemoteService!=null){
            try {
                mRemoteService.stopTTS();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else {
            init();
        }
    }

    /**
     * 销毁单例
     */
    public void destroy(){
        unbindMyService();
        instance = null;
    }

}
