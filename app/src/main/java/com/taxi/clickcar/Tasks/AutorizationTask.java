package com.taxi.clickcar.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.taxi.clickcar.ActivityDrawer;
import com.taxi.clickcar.EnterFragment;
import com.taxi.clickcar.MyCallBack;
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
 * Created by Назар on 12.03.2016.
 */
public class AutorizationTask extends AsyncTask<String,String,String> {

    private SimpleArcDialog mDialog;
    MyCallBack myCallBack;
    private Context mContext;

    public void setcon(Context con){mContext=con;};
    public AutorizationTask(MyCallBack callBack){
        myCallBack=callBack;
            }
    @Override
    protected void onPreExecute() {
       // super.onPreExecute();
        mDialog = new SimpleArcDialog(mContext);
        ArcConfiguration configuration = new ArcConfiguration(mContext);
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.SIMPLE_ARC);
        configuration.setText("Авторизация");

        mDialog.setConfiguration(configuration);

        mDialog.show();


    }

    @Override
    protected void onPostExecute(String strJson) {
        Log.e("post",strJson);
        myCallBack.OnTaskDone(strJson);
        mDialog.dismiss();


    }

    @Override
    protected String doInBackground(String... params) {
        Log.e("Sign in", "in process...");

        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        HttpPost post = new HttpPost(mContext.getString(R.string.server_url)+mContext.getString(R.string.sign_in_url));
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json; charset=utf-8");

        JSONObject object =new JSONObject();
        try {
            object.put("Login", params[0]);
            object.put("Password", params[1]);

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
        catch (NullPointerException e){
            e.printStackTrace();
            return "Wrong phone or password";
        }

        JSONObject dataJsonObj = null;
        try {
            dataJsonObj = new JSONObject(text);
            dataJsonObj.getString("user_full_name");
            ActivityDrawer.Name=dataJsonObj.getString("user_full_name").toString();
        } catch (JSONException e) {
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
