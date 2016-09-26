package com.taxi.clickcar.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.taxi.clickcar.Fragments.FragmentHistory;
import com.taxi.clickcar.GlobalVariables;
import com.taxi.clickcar.Adapters.HistoryAdapter;
import com.taxi.clickcar.MyCallBack;
import com.taxi.clickcar.Order.History;
import com.taxi.clickcar.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Назар on 04.06.2016.
 */
public class HistoryTask extends AsyncTask<String,Void,String> {

    private Context mContext;
    private SimpleArcDialog mDialog;
    MyCallBack myCallBack;
    public HistoryAdapter mAdapter;
    private String base_aouth="";
    public ArrayList<History> historyarray;
    public void setContext(Context context){mContext=context;}
    public HistoryTask(MyCallBack callBack){
        myCallBack=callBack;
    }
    public HistoryTask(HistoryAdapter historyAdapter){mAdapter=historyAdapter;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog = new SimpleArcDialog(mContext);
        ArcConfiguration configuration = new ArcConfiguration(mContext);
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.SIMPLE_ARC);
        configuration.setText(mContext.getString(R.string.download_history));
        mDialog.setConfiguration(configuration);
        mDialog.show();

    }
    @Override
    protected String doInBackground(String... params) {
        Log.e("GetHistory", "in process...");
        FragmentHistory.setLoading(true);
        historyarray = new ArrayList<History>();
        base_aouth = GlobalVariables.getInstance().getBase64EncodedCredentials();
        //String word = params[0].toString();
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        HttpGet post = new HttpGet(mContext.getString(R.string.server_url) + mContext.getString(R.string.history_url) + params[0]+"&limit=10&executionStatus=*");
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
            Log.e("History text",text);
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
    protected void onPostExecute(String res) {

        mDialog.dismiss();

        super.onPostExecute(res);
        display(res);
    }
public void display(String text){
    JSONArray responseObj = null;
    if (text.length()>10) {
        try {

            Gson gson = new Gson();
            responseObj = new JSONArray(text);
            Log.e("Historytask", "display");


            for (int i = 0; i < responseObj.length(); i++) {

                JSONObject routeListObj = responseObj.getJSONObject(i);

                String historyInfo = responseObj.getJSONObject(i).toString();

                History history = gson.fromJson(historyInfo, History.class);

                mAdapter.add(history);
            }

            mAdapter.notifyDataSetChanged();
            FragmentHistory.setLoading(false);

        } catch (JSONException e) {
            Log.e("Historytask", "Json Exeption");
            e.printStackTrace();

        }
    }
}

}
