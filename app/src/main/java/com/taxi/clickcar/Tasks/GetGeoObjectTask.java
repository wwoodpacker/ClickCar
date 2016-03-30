package com.taxi.clickcar.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.leo.simplearcloader.SimpleArcDialog;
import com.taxi.clickcar.ActivityDrawer;
import com.taxi.clickcar.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Назар on 29.03.2016.
 */
public class GetGeoObjectTask extends AsyncTask<String,Void,String> {

    private Context mContext;
    private String base_aouth="";
    public GetGeoObjectTask(Context context){
        mContext=context;
    }
    @Override
    protected String doInBackground(String... params) {
        Log.e("GetGeoLatLong", "in process...");
        base_aouth = ActivityDrawer.base64EncodedCredentials;
        String lat = params[0].toString();
        String lng = params[1].toString();
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        HttpGet post = new HttpGet(mContext.getString(R.string.server_url) + mContext.getString(R.string.search_geo_coord) + "lat=" + lat + "&lng=" + lng + "&r=500");
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json; charset=utf-8");
        post.setHeader("Authorization", "Basic " + base_aouth);

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
        return text;
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
