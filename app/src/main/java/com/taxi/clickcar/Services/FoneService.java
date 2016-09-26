package com.taxi.clickcar.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.taxi.clickcar.Fragments.FragmentOrder;
import com.taxi.clickcar.GlobalVariables;
import com.taxi.clickcar.MainActivity;
import com.taxi.clickcar.R;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Назар on 16.05.2016.
 */
public class FoneService extends Service{
    String UID;
    ExecutorService es;
    int kil=0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.e("Start","service");

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
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(
                        BitmapFactory.decodeResource(getApplicationContext()
                                .getResources(), R.drawable.ic_stat_name))
                .setAutoCancel(true)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(getString(R.string.search_car));




        Notification notification = bI.build();
        startForeground(101, notification);

       // startLoop(UID);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UID=intent.getStringExtra("Url");
        MyRun mr = new MyRun(startId,UID);
        es.execute(mr);
        return  super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("FoneService","destroyService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        es = Executors.newFixedThreadPool(2);
    }

    @Override
    public boolean stopService(Intent name) {
        Log.e("FoneService","stopService");
        return super.stopService(name);

    }


    public void stop3(String _uid){

        Log.e("Cancel", "Start...");
        String base_aouth = GlobalVariables.getInstance().getBase64EncodedCredentials();
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

    class MyRun implements Runnable{
        int startId;
        String UID;
        boolean flag=false,flag2=false;
        public MyRun(int _startId,String _UID){
            startId=_startId;
            UID=_UID;
        }
        @Override
        public void run() {
            Intent intent = new Intent(FragmentOrder.BROADCAST_ACTION);
            try {

                while (kil != 60&&flag!=true) {
                    //if(!FragmentOrder.flag_zakaz){
                    String base_aouth = GlobalVariables.getInstance().getBase64EncodedCredentials();
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = null;
                    HttpGet post = new HttpGet(getApplicationContext().getString(R.string.server_url) + getApplicationContext().getString(R.string.order_url) + "/" + UID.toString());
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
                    Log.e("ServiceJson", text);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(text);
                        String stat = jsonObject.getString("order_car_info");
                        if (!stat.contains("null")) {
                            intent.putExtra(FragmentOrder.PARAM_STATUS, 100);
                            intent.putExtra(FragmentOrder.PARAM_RESULT, text);
                            flag=true;
                        }
                    } catch (JSONException e) {
                        Log.e("Json", "Service Exeption");
                        e.printStackTrace();
                    }

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (Exception e) {
                        Log.e("chat",
                                "+ FoneService - ошибка процесса: "
                                        + e.getMessage());
                    }
                    kil++;
                    if(FragmentOrder.flag_zakaz){kil=60;}
                }
               if (!flag) {
                   stop3(UID);
                   intent.putExtra(FragmentOrder.PARAM_STATUS,200);
               }

                 sendBroadcast(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }
            stop();
        }
        void stop() {
            stopSelfResult(startId);
        }
        }
    }
