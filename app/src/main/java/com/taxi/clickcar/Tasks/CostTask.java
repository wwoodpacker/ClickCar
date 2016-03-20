package com.taxi.clickcar.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.taxi.clickcar.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Назар on 20.03.2016.
 */
public class CostTask  extends AsyncTask<JSONObject,Void,String> {
    private ProgressDialog pdia;
    private String credentials;
    private String base64EncodedCredentials ;
    private Context mContext;
    public CostTask(Context context){
        mContext=context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdia=new ProgressDialog(mContext);
        pdia.setMessage("Расчёт заказа...");
        pdia.show();
    }

    @Override
    protected void onPostExecute(String strJson) {
        super.onPostExecute(strJson);

        pdia.dismiss();
    }

    @Override
    protected String doInBackground(JSONObject... object) {
        Log.e("Calculation COST", "Start...");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        HttpPost post = new HttpPost(mContext.getString(R.string.server_url) + mContext.getString(R.string.cost_url));

        try {
            credentials = object[0].getString("mylogin") + ":" +object[0].getString("myhpass");
            object[0].remove("mylogin");
            object[0].remove("myhpass");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json; charset=utf-8");
        post.setHeader("Authorization", "Basic " + base64EncodedCredentials);

        post.setEntity(new StringEntity(object[0].toString(),"UTF-8"));
        try {
            response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {

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
