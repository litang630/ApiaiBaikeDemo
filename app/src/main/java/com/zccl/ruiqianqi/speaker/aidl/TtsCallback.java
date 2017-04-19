package com.zccl.ruiqianqi.speaker.aidl;

import android.os.RemoteException;

/**
 * Created by ruiqianqi on 2016/11/24 0024.
 */

public class TtsCallback extends ISpeakerCallback.Stub {

    private static String TAG = TtsCallback.class.getSimpleName();

    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }

    @Override
    public void OnBegin() throws RemoteException {
    }

    @Override
    public void OnPause() throws RemoteException {
    }

    @Override
    public void OnResume() throws RemoteException {
    }

    @Override
    public void OnComplete(String tag) throws RemoteException {
    }

    @Override
    public void OnError(String error, String tag) throws RemoteException {
    }

}
