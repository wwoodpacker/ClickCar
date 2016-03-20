package com.taxi.clickcar;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.TextView;
import android.widget.Toast;

import com.taxi.clickcar.Tasks.AutorizationTask;

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
    public View.OnClickListener mOnClickListener;

    public String login="";
    public String password="";
    public String hashPass="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReg =(Button)findViewById(R.id.btn_register);
        btnEnter=(Button)findViewById(R.id.btn_sing_in);
        btnOrder =(Button)findViewById(R.id.btn_order);
        sign_login=(EditText)findViewById(R.id.login_field);
        sign_pass=(EditText)findViewById(R.id.password_field);



    }


    public void setmOnClickListener(View.OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public boolean CheckConnection(){

        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING||wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            return true;
        } else  {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"No connection",Snackbar.LENGTH_LONG)
                    .setAction("Hide", mOnClickListener);
            snackbar.setActionTextColor(Color.RED);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.DKGRAY);
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
            return false;
        }

    }

    public void OnClick(View v){
        switch (v.getId()){
            case R.id.btn_order:
                if (CheckConnection()){

                }
                break;
            case R.id.btn_register:
                if (CheckConnection()) {
                    Intent intent = new Intent(this, Register.class);
                    startActivity(intent);
                }
                break;
            case R.id.btn_sing_in:
                if (CheckConnection()) {
                    login = sign_login.getText().toString();
                    password = sign_pass.getText().toString();

                    if (login.length() <= 0) {
                        Toast.makeText(this, "Please enter login", Toast.LENGTH_SHORT).show();
                    } else if (password.length() <= 0) {
                        Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                    } else {
                       AutorizationTask task = new AutorizationTask(this);
                        String str = "";
                        try {
                            hashPass=hashText(password);
                            task.execute(login, hashPass);
                            str = task.get();
                            JSONObject obj = new JSONObject(str);
                            Log.e("Auth JSON:", str);
                            Intent intent= new Intent(this,Order.class);
                            intent.putExtra("LOGIN",login);
                            intent.putExtra("PASSWORD",hashPass);
                            startActivity(intent);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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

 }
