package com.taxi.clickcar;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Назар on 18.03.2016.
 */
public class Register extends Activity {

    private String login="";
    private String name="";
    private String phone="";
    private String pass="";
    private String pass_again="";

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
                TasckMy task = new TasckMy();
                task.execute(ed_phone.getText().toString());
                Intent intent = new Intent(this,ConfirmCode.class);
                intent.putExtra("LOGIN",ed_login.getText().toString());
                intent.putExtra("NAME",ed_name.getText().toString());
                intent.putExtra("PHONE",ed_phone.getText().toString());
                intent.putExtra("PASS", ed_pass.getText().toString());
                intent.putExtra("PASS_AGAIN", ed_pass_again.getText().toString());

                startActivity(intent);
                break;
        }
    }

    class TasckMy extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... params) {
            Log.e("SMS", "Ruun");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = null;
            HttpPost post = new HttpPost(getString(R.string.server_url) + "/api/account/register/sendConfirmCode");
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
            Log.e("SMS", text);
            return null;
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
