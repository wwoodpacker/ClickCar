package com.taxi.clickcar.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.leo.simplearcloader.SimpleArcDialog;
import com.taxi.clickcar.Fragments.FragmentMassages;
import com.taxi.clickcar.GlobalVariables;
import com.taxi.clickcar.Adapters.MessagesAdapter;
import com.taxi.clickcar.MyCallBack;
import com.taxi.clickcar.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Назар on 12.06.2016.
 */
public class MassageTask  extends AsyncTask<String,Void,String> {

    private Context mContext;
    private SimpleArcDialog mDialog;
    MyCallBack myCallBack;
    public MessagesAdapter mAdapter;
    private String base_aouth="";
    public ArrayList<String> Massagearray;
    public void setContext(Context context){mContext=context;}
    public MassageTask(MyCallBack callBack){
        myCallBack=callBack;
    }
    public MassageTask(MessagesAdapter MassageAdapter){mAdapter=MassageAdapter;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }
    @Override
    protected String doInBackground(String... params) {
        Log.e("GetMassage", "in process...");
        // parse existing/init new JSON
        File path = mContext.getExternalFilesDir(null);
        File root = new File(path,"Massages");
        if (!root.exists()) {
            root.mkdirs();
        }
        File jsonFile = new File(root, "massagesFile.json");
        String previousJson = null;
        if (jsonFile.exists()) {
            previousJson = Utils.readFromFile(jsonFile);
        }
        else {  previousJson = "{}";  }
        return previousJson;

    }


    @Override
    protected void onPostExecute(String res) {



        super.onPostExecute(res);
        display(res);
    }
    public void display(String text) {
        Log.e("Massagetask", text);
        JSONObject responseObj = null;

        try {

            Gson gson = new Gson();
            responseObj = new JSONObject(text);
            Log.e("Massagetask", "display");
            for (int i = responseObj.length(); i >0; i--) {
                JSONObject object=responseObj.getJSONObject(String.valueOf(i));
                if(GlobalVariables.getInstance().getPhone().contains(object.getString(FragmentMassages.PHONE))){
                    mAdapter.add(object.getString(FragmentMassages.MESSAGE));
                }

            }
            mAdapter.notifyDataSetChanged();


        } catch (JSONException e) {
            Log.e("Masasgetask", "Json Exeption");
            e.printStackTrace();

        }
    }

}
