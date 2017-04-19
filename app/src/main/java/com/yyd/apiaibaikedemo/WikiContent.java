package com.yyd.apiaibaikedemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;

import com.zccl.ruiqianqi.speaker.aidl.TtsClient;


public class WikiContent {
	private Context mContext;
	private String mWikiTitle;
	private String linkText;
	private static String TAG = "WikiBaike";
	
	public static final int SHOW_RESPONSE = 0;
	private Document doc = null;
	
	public WikiContent(String wikiTitle, Context context){
		this.mWikiTitle = wikiTitle;
		this.mContext = context;
	}

		// 新建Handler的对象，在这里接收Message，然后更新TextView控件的内容
		private Handler handler1 = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case SHOW_RESPONSE:
					Elements links = doc.getElementsByTag("p");
					for(Element link:links){
						String subText = null;
						linkText = link.text();
						if (linkText.indexOf(",") >= 0) {
							subText = linkText.substring(0, linkText.indexOf(",")).toLowerCase();
							Log.i(TAG, "subText=====>>>>>>>>>>>>>>>>" + subText);
						}else{
							if(linkText.length()>mWikiTitle.length()+4){
								subText = linkText.substring(0,mWikiTitle.length()+4).toLowerCase();
								Log.i(TAG, "subText=====>>>>>>>>>>>22222" + subText);
							}
						}

						if (subText != null && subText.contains(mWikiTitle.toLowerCase())) {
							Log.i(TAG, "$$$$$$$$$$$$$$$>>>>>>>>>>>>>>>>");
							break;
						}
					}

					Time time = new Time("GMT+8");
					time.setToNow();

					if (linkText != null) {
						String reg = "\\[\\d+\\]";
						linkText = linkText.replaceAll(reg, "");
						reg = "\\(.*\\)";
						linkText = linkText.replaceAll(reg, "");
						Log.i(TAG, "wikiMessage" + "   " + linkText + "    "
								+ time.minute + ":" + time.second);
					}else{
						linkText = "sorry, I found no information about " + mWikiTitle;
					}
					TtsClient.getInstance(mContext).startTTS(linkText);
					break;

				default:
					break;
				}
			}

		};

		public void sendRequestWithHttpURLConnection() {
			// TODO Auto-generated method stub
			Thread getThread = new Thread() {
				public void run() {
					String result = null;
					HttpURLConnection urlConnection = null;
					try {
						URL url = new URL("https://en.wikipedia.org/wiki/"
								+ mWikiTitle);
						//URL url = new URL("https://en.wikipedia.org/wiki/cat");
						Log.i(TAG, "url=====>>>>>>>>>>>>>" + url);
						urlConnection = (HttpURLConnection) url.openConnection();
						// 设置请求方法，默认是GET
						urlConnection.setRequestMethod("GET");
						// 设置字符集
						urlConnection.setRequestProperty("Charset", "UTF-8");
						Log.i(TAG, "getResponseCode=====>>>>>>>>"+urlConnection.getResponseCode());
						if (urlConnection.getResponseCode() == 200) {
							InputStream is = urlConnection.getInputStream();
							BufferedReader reader = new BufferedReader(
									new InputStreamReader(is));
							StringBuilder sb = new StringBuilder();
							String line = null;
							try {
								while ((line = reader.readLine()) != null) {
									sb.append(line + "/n");
								}
							} catch (IOException e) {
								e.printStackTrace();
							} finally {
								try {
									is.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							result = sb.toString();

						}
						if (result != null) {
							doc = Jsoup.parse(result);
							// 在子线程中将Message对象发出去
							Message message = new Message();
							message.what = SHOW_RESPONSE;
							message.obj = result.toString();
							handler1.sendMessage(message);
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						urlConnection.disconnect();
					}
				}
			};
			getThread.start();
		}

}
