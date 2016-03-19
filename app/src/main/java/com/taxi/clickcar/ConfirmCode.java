package com.taxi.clickcar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Назар on 19.03.2016.
 */
public class ConfirmCode extends Activity {

    private String confirm_code="";

    public Button btn_confirm_ok;
    public EditText ed_confirm_code;
    public UserRegistratonProfile userRegistratonProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_code);
        btn_confirm_ok =(Button)findViewById(R.id.btn_confirm_ok);
        ed_confirm_code =(EditText)findViewById(R.id.ed_confirm_code);
        userRegistratonProfile = (UserRegistratonProfile)getIntent().getParcelableExtra(
                UserRegistratonProfile.class.getCanonicalName());
    }

    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.btn_confirm_ok:
                confirm_code=ed_confirm_code.getText().toString();
                if (confirm_code.isEmpty()){
                    Toast.makeText(this,"Please enter confirm code",Toast.LENGTH_SHORT).show();
                }
                else {
                    RegisterTask registerTask=new RegisterTask();
                    registerTask.execute(userRegistratonProfile.phone,
                            confirm_code,
                            userRegistratonProfile.pass,
                            userRegistratonProfile.pass_again,
                            userRegistratonProfile.name);
                    try {
                        String jsonStr=registerTask.get();
                        Log.e("REGISTER JSON", jsonStr);
                        JSONObject object = new JSONObject(jsonStr);
                        Toast.makeText(this,object.getString("Message"),Toast.LENGTH_SHORT).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this,"Пользователь зарегистрирован",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(this,MainActivity.class);
                        startActivity(intent);
                    }
                    ;
        }
                break;
        }
    }

    class RegisterTask extends AsyncTask<String,Void,String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia=new ProgressDialog(ConfirmCode.this);
            pdia.setMessage("Проверка кода...");
            pdia.show();
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            pdia.dismiss();
        }
        //0672325863
        @Override
        protected String doInBackground(String... params) {
            Log.e("Registration", "Start...");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = null;
            HttpPost post = new HttpPost(getString(R.string.server_url) + getString(R.string.register_url));
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
