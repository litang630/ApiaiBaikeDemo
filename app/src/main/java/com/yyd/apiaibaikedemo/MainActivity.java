package com.yyd.apiaibaikedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zccl.ruiqianqi.speaker.aidl.TtsClient;

public class MainActivity extends Activity implements Handler.Callback {
    public static final String TAG = "WikiBaike";
    private WebView webWikiCon;
    private Handler handler;
    String baikeLiteral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webWikiCon = (WebView) findViewById(R.id.wikicontent);
        handler = new Handler(this);
        if(getIntent()!=null){
            baikeLiteral = getIntent().getStringExtra("baike");
            Log.i(TAG, "baikeLiteral ==1111 " + baikeLiteral);
            Message msg = handler.obtainMessage(0, baikeLiteral);
            msg.sendToTarget();
        }
        TtsClient.getInstance(getBaseContext());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if(intent!=null){
            baikeLiteral = intent.getStringExtra("baike");
            Log.i(TAG, "baikeLiteral ==aaaa " + baikeLiteral);
            Message msg = handler.obtainMessage(0, baikeLiteral);
            msg.sendToTarget();
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        if (message.what == 0) {
            String baikeLiteral = message.obj+"";
            baikeLiteral = baikeLiteral.replace("\"","");
            if (baikeLiteral != null) {
                webWikiCon.loadUrl("https://en.wikipedia.org/wiki/" + baikeLiteral);
                webWikiCon.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        // TODO Auto-generated method stub
                        view.loadUrl(url);
                        return true;
                    }
                });
                WikiContent mWikiContent = new WikiContent(baikeLiteral, getBaseContext());
                mWikiContent.sendRequestWithHttpURLConnection();
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TtsClient.getInstance(getBaseContext()).destroy();
    }
}
