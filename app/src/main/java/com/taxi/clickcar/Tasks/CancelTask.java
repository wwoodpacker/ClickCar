package com.taxi.clickcar.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.taxi.clickcar.ActivityDrawer;
import com.taxi.clickcar.GlobalVariables;
import com.taxi.clickcar.MyCallBack;
import com.taxi.clickcar.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Назар on 15.05.2016.
 */
public class CancelTask extends AsyncTask<String,Void,String> {
    private SimpleArcDialog mDialog;
    private Context mContext;
    MyCallBack myCallBack;
    private String base_aouth="";
    public void setContext(Context context){mContext=context;}
    public CancelTask(MyCallBack callBack){
        myCallBack=callBack;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog = new SimpleArcDialog(mContext);
        ArcConfiguration configuration = new ArcConfiguration(mContext);
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.SIMPLE_ARC);
        configuration.setText(mContext.getString(R.string.cancel_order));
        mDialog.setConfiguration(configuration);
        mDialog.show();

    }

    @Override
    protected void onPostExecute(String strJson) {
        myCallBack.OnTaskDone(strJson);

        mDialog.dismiss();
    }

    @Override
    protected String doInBackground(String... params) {
        Log.e("Cancel", "Start...");

        base_aouth = GlobalVariables.getInstance().getBase64EncodedCredentials();
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        HttpPut post = new HttpPut(mContext.getString(R.string.server_url) + mContext.getString(R.string.cancel_url)+"/"+params[0].toString());
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
