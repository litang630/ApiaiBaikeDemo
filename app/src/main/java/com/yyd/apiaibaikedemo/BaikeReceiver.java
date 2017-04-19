package com.yyd.apiaibaikedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


public class BaikeReceiver extends BroadcastReceiver {
    private static String TAG = "WikiBaike";
    String actionIntent = null;
    String baikeLiteral = null;
    public BaikeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive onReceive****************************");
        if(intent!=null){
            String str = intent.getStringExtra("result");
            ApiAiResult apiAiResult = new Gson().fromJson(str, ApiAiResult.class);

            if(apiAiResult!=null){
                actionIntent = apiAiResult.getIntent();
                if (actionIntent != null && actionIntent.equals("WikiBaike")) {
                    Log.i(TAG, "****************************");
                    final HashMap<String, String> params = apiAiResult.getParameters();
                    if (params != null && !params.isEmpty()) {
                        Log.i(TAG, "Parameters: ");
                        for (final Map.Entry<String, String> entry : params.entrySet()) {
                            Log.i(TAG, String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
                            if (entry.getKey().equals("search_original")) {
                                baikeLiteral = entry.getValue().toString();
                                Log.i(TAG, "baikeLiteral = " + baikeLiteral);

                                Intent intentBaike = new Intent(context, MainActivity.class);
                                intentBaike.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intentBaike.putExtra("baike", baikeLiteral);
                                context.startActivity(intentBaike);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
