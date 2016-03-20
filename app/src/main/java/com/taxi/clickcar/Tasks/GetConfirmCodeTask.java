package com.taxi.clickcar.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.taxi.clickcar.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Назар on 20.03.2016.
 */
public class GetConfirmCodeTask  extends AsyncTask<String,Void,String> {
    private ProgressDialog pdia;
    private Context mContext;

    public GetConfirmCodeTask(Context context){
        mContext=context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdia=new ProgressDialog(mContext);
        pdia.setMessage("Загрузка...");
        pdia.show();
    }

    @Override
    protected void onPostExecute(String strJson) {
        super.onPostExecute(strJson);

        pdia.dismiss();
    }

    @Override
    protected String doInBackground(String... params) {
        Log.e("SMS", "sending...");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        HttpPost post = new HttpPost(mContext.getString(R.string.server_url) + mContext.getString(R.string.send_confirm_code_url));
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("phone", params[0]));
        try {
            post.setEntity(new UrlEncodedFormEntity(pairs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
