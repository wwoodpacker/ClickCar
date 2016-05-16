package com.taxi.clickcar;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Назар on 16.05.2016.
 */
public class FoneService extends Service{
    Thread thr;
    HttpURLConnection conn;
    String UID;
    Intent intent2;
    Long last_time;
    int kil=0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.e("Start","service");
        kil=0;
        intent2= new Intent(FragmentOrder.BROADCAST_ACTION);
        UID=intent.getStringExtra("Url");
        // создадим и покажем notification
        // это позволит стать сервису "бессмертным"
        // и будет визуально видно в трее
        Intent iN = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pI = PendingIntent.getActivity(getApplicationContext(),
                0, iN, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder bI = new Notification.Builder(
                getApplicationContext());

        bI.setContentIntent(pI)
                .setSmallIcon(R.drawable.ic_search_black_24dp)
                .setLargeIcon(
                        BitmapFactory.decodeResource(getApplicationContext()
                                .getResources(), R.drawable.ic_search_black_24dp))
                .setAutoCancel(true)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Поиск машины...");

        Notification notification = bI.build();
        startForeground(101, notification);

        startLoop(UID);
    }
    private void startLoop(final String _uid) {

        thr = new Thread(new Runnable() {

            // ansver = ответ на запрос
            // lnk = линк с параметрами
            String ansver, lnk;

            public void run() {

                while (kil!=120) {

                    String base_aouth = ActivityDrawer.base64EncodedCredentials;
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = null;
                    HttpGet post = new HttpGet(getApplicationContext().getString(R.string.server_url) + getApplicationContext().getString(R.string.order_url)+"/"+_uid.toString());
                    post.setHeader("Accept", "application/json");
                    // post.setHeader("Content-type", "application/json; charset=utf-8");
                    post.setHeader("Authorization", "Basic " + base_aouth);
                    // post.setEntity(new StringEntity(params[0].toString(),"UTF-8"));
                    try {
                        response = client.execute(post);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String text = "";
                    HttpEntity entity = null;
                    if (response != null) {
                        entity = response.getEntity();
                    }
                    try {
                        text = GetText(entity.getContent());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                  Log.e("ServiceJson",text);
                    JSONObject jsonObject= null;
                    try {
                        jsonObject = new JSONObject(text);
                        String stat=jsonObject.getString("order_car_info");
                        if(!stat.contains("null")) {
                            intent2.putExtra(FragmentOrder.PARAM_RESULT,text);
                            intent2.putExtra(FragmentOrder.PARAM_STATUS,100);
                            sendBroadcast(intent2);
                            stopSelf();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        Log.e("chat",
                                "+ FoneService - ошибка процесса: "
                                        + e.getMessage());
                    }
                    kil++;
                }

                stop(_uid);
            }
        });

        thr.setDaemon(true);
        thr.start();

    }
    public void stop(String _uid){

        Log.e("Cancel", "Start...");
        intent2.putExtra(FragmentOrder.PARAM_STATUS,200);
        sendBroadcast(intent2);
        String base_aouth = ActivityDrawer.base64EncodedCredentials;
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        HttpPut post = new HttpPut(getApplicationContext().getString(R.string.server_url) + getApplicationContext().getString(R.string.cancel_url)+"/"+_uid);
        post.setHeader("Accept", "application/json");
        //post.setHeader("Content-type", "application/json; charset=utf-8");
        post.setHeader("Authorization", "Basic " + base_aouth);
        //post.setEntity(new StringEntity(params[0].toString(),"UTF-8"));
        try {
            response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String text = "";
        HttpEntity entity = null;
        if (response != null) {
            entity = response.getEntity();
        }
        try {
            text = GetText(entity.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("Cancel",text.toString());
        stopSelf();

    }
    public String GetText(InputStream in) {
        String text = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            text = sb.toString();
        } catch (Exception ex) {

        } finally {
            try {

                in.close();
            } catch (Exception ex) {
            }
        }
        return text;
    }
}
