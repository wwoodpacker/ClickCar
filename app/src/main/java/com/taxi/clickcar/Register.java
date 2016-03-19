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
 * Created by Назар on 18.03.2016.
 */
public class Register extends Activity {



    public EditText ed_login;
    public EditText ed_name;
    public EditText ed_phone;
    public EditText ed_pass;
    public EditText ed_pass_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ed_login = (EditText)findViewById(R.id.ed_login);
        ed_name = (EditText)findViewById(R.id.ed_name);
        ed_phone = (EditText)findViewById(R.id.ed_phone);
        ed_pass = (EditText)findViewById(R.id.ed_pass);
        ed_pass_again = (EditText)findViewById(R.id.ed_pass_again);

    }

    public void OnClick(View v){
        switch (v.getId()){
            case R.id.btn_acsses_reg:
                UserRegistratonProfile userRegProf = new UserRegistratonProfile(ed_login.getText().toString(),
                        ed_name.getText().toString(),
                        ed_phone.getText().toString(),
                        ed_pass.getText().toString(),
                        ed_pass_again.getText().toString()
                );
                if (userRegProf.name.length()==0||userRegProf.login.length()==0||userRegProf.phone.length()==0||userRegProf.pass.length()==0||userRegProf.pass_again.length()==0) {
                    Toast.makeText(this,"Write all fields",Toast.LENGTH_SHORT).show();
                }
                else
                if (!userRegProf.pass.contains(userRegProf.pass_again))
                {
                     Toast.makeText(this,"Passwords do not match!",Toast.LENGTH_SHORT).show();
                }
                else
                if(userRegProf.pass.length()<6){
                    Toast.makeText(this,"Min length of password 7!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    GetConfirmCode getConfirmCode=new GetConfirmCode();
                    getConfirmCode.execute(ed_phone.getText().toString());
                    try {
                        String jsonstr=getConfirmCode.get();
                        JSONObject object = new JSONObject(jsonstr);
                        Toast.makeText(this,object.getString("Message"),Toast.LENGTH_SHORT).show();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Intent intent=new Intent(this,ConfirmCode.class);
                        intent.putExtra(UserRegistratonProfile.class.getCanonicalName(),userRegProf);
                        startActivity(intent);
                    }
                }
             break;
        }
    }

    class GetConfirmCode extends AsyncTask<String,Void,String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia=new ProgressDialog(Register.this);
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
            HttpPost post = new HttpPost(getString(R.string.server_url) + getString(R.string.send_confirm_code_url));
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
