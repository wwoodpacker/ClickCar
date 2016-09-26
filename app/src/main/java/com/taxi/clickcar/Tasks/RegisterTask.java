package com.taxi.clickcar.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
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
public class RegisterTask  extends AsyncTask<String,Void,String> {
    private SimpleArcDialog mDialog;
    private Context mContext;
    public RegisterTask(Context context){
        mContext=context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog = new SimpleArcDialog(mContext);
        ArcConfiguration configuration = new ArcConfiguration(mContext);
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.SIMPLE_ARC);
        configuration.setText(mContext.getString(R.string.check_confirm));
        mDialog.setConfiguration(configuration);
        mDialog.show();

    }

    @Override
    protected void onPostExecute(String strJson) {
        super.onPostExecute(strJson);

        mDialog.dismiss();
    }
    //0672325863
    @Override
    protected String doInBackground(String... params) {
        Log.e("Registration", "Start...");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        HttpPost post = new HttpPost(mContext.getString(R.string.server_url) + mContext.getString(R.string.register_url));
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json; charset=utf-8");

        JSONObject object =new JSONObject();
        try {
            object.put("phone", params[0]);
            object.put("confirm_code", params[1]);
            object.put("password", params[2]);
            object.put("confirm_password", params[3]);
            object.put("user_first_name", params[4]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post.setEntity(new StringEntity(object.toString(),"UTF-8"));
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
