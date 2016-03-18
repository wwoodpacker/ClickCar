package com.taxi.clickcar;

import android.accounts.NetworkErrorException;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
public static final String ADRESS="http://176.38.246.10:7200";
public static final String AUTH_URL=ADRESS+"/api/account";
public static final String CONFIRM_CODE=ADRESS+"/api/account/register/sendConfirmCode";
public static final String REGISTER=ADRESS+"/api/account/register";
public Button btnReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // my task = new my();
       //  task.execute();
        btnReg =(Button)findViewById(R.id.btn_register);



        try {
            String sha512 = hashText("1");
            Log.e("sha512", sha512);
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
        }

    }

    public static String convertByteToHex(byte data[])
    {
        StringBuffer hexData = new StringBuffer();
        for (int byteIndex = 0; byteIndex < data.length; byteIndex++)
            hexData.append(Integer.toString((data[byteIndex] & 0xff) + 0x100, 16).substring(1));

        return hexData.toString();
    }

    public static String hashText(String textToHash) throws Exception
    {
        final MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
        sha512.update(textToHash.getBytes());

        return convertByteToHex(sha512.digest());
    }
 class my extends AsyncTask<Void,Void,Void>{

     @Override
     protected Void doInBackground(Void... params) {

         HttpClient client = new DefaultHttpClient();
         HttpResponse response=null;
         HttpPost post = new HttpPost(getString(R.string.server_url)+"/api/account/register/sendConfirmCode");
         List<NameValuePair> pairs = new ArrayList<NameValuePair>();
         pairs.add(new BasicNameValuePair("phone", "380637988976"));
         try {
             post.setEntity(new UrlEncodedFormEntity(pairs));
         } catch (UnsupportedEncodingException e) {
             e.printStackTrace();
         }
         try {
             response = client.execute(post);
         } catch (IOException e) {
             e.printStackTrace();
         }catch (NullPointerException ex){

         }
         String text="";
         HttpEntity entity = null;
         if (response != null) {
             entity = response.getEntity();
         }
         try {
             text=GetText(entity.getContent());
         } catch (IOException e) {
             e.printStackTrace();
         }
         Log.e("SMS",text);
         return null;
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
}
