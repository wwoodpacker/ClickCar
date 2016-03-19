package com.taxi.clickcar;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    public Button btnReg;
    public Button btnOrder;
    public Button btnEnter;
    public EditText sign_login;
    public EditText sign_pass;

    public String login="";
    public String password="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReg =(Button)findViewById(R.id.btn_register);
        btnEnter=(Button)findViewById(R.id.btn_sing_in);
        btnOrder =(Button)findViewById(R.id.btn_order);
        sign_login=(EditText)findViewById(R.id.login_field);
        sign_pass=(EditText)findViewById(R.id.password_field);
        try {
            String sha512 = hashText("1");
          //  Log.e("sha512", sha512);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.btn_order:

                break;
            case R.id.btn_register:
                Intent intent= new Intent(this,Register.class);
                startActivity(intent);
                break;
            case R.id.btn_sing_in:
                login=sign_login.getText().toString();
                password=sign_pass.getText().toString();

                if (login.length()<=0){
                    Toast.makeText(this,"Please enter login",Toast.LENGTH_SHORT).show();
                }else
                if(password.length()<=0){
                    Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
                }else{
                    SignIn task= new SignIn();
                    String str="";
                    try {
                        task.execute(login,hashText(password));
                        str=task.get();
                        JSONObject obj = new JSONObject(str);
                        String ph=obj.getString("user_phone");
                        Log.e("Auth JSON:",str);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

        }

    }

    public  String convertByteToHex(byte data[])
    {
        StringBuffer hexData = new StringBuffer();
        for (int byteIndex = 0; byteIndex < data.length; byteIndex++)
            hexData.append(Integer.toString((data[byteIndex] & 0xff) + 0x100, 16).substring(1));

        return hexData.toString();
    }

    public String hashText(String textToHash) throws Exception
    {
        final MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
        sha512.update(textToHash.getBytes());

        return convertByteToHex(sha512.digest());
    }
    class SignIn extends AsyncTask<String,Void,String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia=new ProgressDialog(MainActivity.this);
            pdia.setMessage("Авторизация...");
            pdia.show();
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            pdia.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("Sign in", "in process...");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = null;
            HttpPost post = new HttpPost(getString(R.string.server_url)+getString(R.string.sign_in_url));
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
