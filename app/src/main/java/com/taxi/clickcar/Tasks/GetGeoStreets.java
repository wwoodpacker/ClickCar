package com.taxi.clickcar.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.taxi.clickcar.ActivityDrawer;
import com.taxi.clickcar.GlobalVariables;
import com.taxi.clickcar.MyCallBack;
import com.taxi.clickcar.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Назар on 24.05.2016.
 */
public class GetGeoStreets  extends AsyncTask<String,Void,String> {

    private Context mContext;
    MyCallBack myCallBack;
    private String base_aouth="";
    public void setContext(Context context){mContext=context;}
    public GetGeoStreets(MyCallBack callBack){
        myCallBack=callBack;
    }
    @Override
    protected String doInBackground(String... params) {
        Log.e("GetGeoStreet by words", "in process...");
        base_aouth = GlobalVariables.getInstance().getBase64EncodedCredentials();
        String word = params[0].toString();
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        HttpGet post = new HttpGet(mContext.getString(R.string.server_url) + mContext.getString(R.string.search_geo_street) + "q=" + word + "&offset=0&limit=10&transliteration=true&qwertySwitcher=true");
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
            Log.e("geo2text",text);
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

    @Override
    protected void onPostExecute(String s) {
        myCallBack.OnTaskDone(s);

    }
}

