package com.taxi.clickcar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.taxi.clickcar.Tasks.CostTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

/**
 * Created by Назар on 20.03.2016.
 */
public class Order extends Activity {
    private String login="";
    private String hpass="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_layout);
        login=getIntent().getStringExtra("LOGIN");
        hpass=getIntent().getStringExtra("PASSWORD");
    }
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.btn_check_price:
                CostTask task=new CostTask(this);

                JSONObject object =new JSONObject();
                JSONObject jsonObjectRoute1= new JSONObject();
                JSONObject jsonObjectRoute2= new JSONObject();
                JSONArray jsonArrayRoute= new JSONArray();

                try {
                    jsonObjectRoute1.put("name","вход м.Шевченко.");
                    jsonObjectRoute1.put("lat",50.474613);
                    jsonObjectRoute1.put("lng",30.506389);

                    jsonObjectRoute2.put("name","м.Иподром.");
                    jsonObjectRoute2.put("lat",50.377615);
                    jsonObjectRoute2.put("lng",30.468195);

                    object.put("user_full_name", "nazar");
                    object.put("user_phone",login);
                    object.put("client_sub_card",null);
                    object.put("required_time",null);
                    object.put("reservation",false);
                    object.put("route_address_entrance_from",null);
                    object.put("comment","");
                    object.put("add_cost",12.0);
                    object.put("wagon",true);
                    object.put("minibus",false);
                    object.put("premium",false);
                   // object.put("flexible_tariff_name","гибкий тариф");
                    object.put("baggage",false);
                    object.put("animal",false);
                    object.put("conditioner",true);
                    object.put("courier_delivery",false);
                    object.put("route_undefined",false);
                    object.put("terminal",false);
                    object.put("receipt",false);
                    object.put("mylogin",login);
                    object.put("myhpass",hpass);
                    jsonArrayRoute.put(jsonObjectRoute1);
                    jsonArrayRoute.put(jsonObjectRoute2);

                    object.put("route",jsonArrayRoute);
                    object.put("taxiColumnId",0);
                    Log.e("ORDER REQUEST JSON",object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                task.execute(object);
                try {
                    String jsonStr=task.get();
                    JSONObject obj=new JSONObject(jsonStr);
                    Log.e("ORGER RESPONSE JSON", obj.toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Цена")
                            .setMessage(Double.toString(obj.getDouble("order_cost"))+" "+obj.getString("currency"))
                            .setIcon(R.drawable.ic_done_white_24px)
                            .setCancelable(false)
                            .setNegativeButton("ОК",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    Log.e("ORGER RESPONSE JSON", jsonStr);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_search_taxi:
                break;
        }
    }


}
