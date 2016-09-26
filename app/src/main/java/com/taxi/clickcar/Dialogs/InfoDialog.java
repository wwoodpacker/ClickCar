package com.taxi.clickcar.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taxi.clickcar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Назар on 26.05.2016.
 */
public class InfoDialog extends DialogFragment {


    private OnInfoDialogListener mCallback;
    private Button mDoneButton;

    public ImageView img_carClass,img_Baggage,img_Animal,img_Condition,img_Curier;
    public EditText adress_from,adress_to,comment;
    public TextView text_summa,text_time,text_oplata;
    public String suma,req_time, adress_from2, adress_to2;
    public static String comment1,json1;
    public static int carClass1,payment1;

    public static boolean baggage1;
    public static boolean animal1;
    public static boolean condition1;
    public static boolean curier1;

    public InfoDialog(){};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("infoDialog","onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getDialog()==null) {return;}
        getDialog().getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("infoDialog","onCreateView");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.info_dialog, null);

        img_carClass=(ImageView)view.findViewById(R.id.imageClass);
        img_Baggage=(ImageView)view.findViewById(R.id.imgBaggage);
        img_Animal=(ImageView)view.findViewById(R.id.imgAnimal);
        img_Condition=(ImageView)view.findViewById(R.id.imgCondition);
        img_Curier=(ImageView)view.findViewById(R.id.imgCurier);
        adress_from=(EditText)view.findViewById(R.id.editText4);
        adress_to=(EditText)view.findViewById(R.id.editText5);
        comment=(EditText)view.findViewById(R.id.edit_comment);
        text_time=(TextView)view.findViewById(R.id.txt_time);
        text_summa=(TextView)view.findViewById(R.id.txt_summa);
        text_oplata=(TextView)view.findViewById(R.id.txt_oplata);
        //******************************************************************************************
        mDoneButton=(Button)view.findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //******************************************************************************************
        switch (carClass1){
            case 1:
                img_carClass.setImageResource(R.mipmap.icon_standart);
                break;
            case 2:
                img_carClass.setImageResource(R.mipmap.icon_premium);
                break;
            case 3:
                img_carClass.setImageResource(R.mipmap.icon_universal);
                break;
            case 4:
                img_carClass.setImageResource(R.mipmap.icon_microbus);
                break;
        }
        if (baggage1){ img_Baggage.setImageResource(R.mipmap.ic_baggage); } else {
            img_Baggage.setImageResource(R.color.transparent);
        }
        if (animal1) { img_Animal.setImageResource(R.mipmap.ic_animal);}else{
            img_Animal.setImageResource(R.color.transparent);
        }
        if (condition1){ img_Condition.setImageResource(R.mipmap.ic_condition);}else {
            img_Condition.setImageResource(R.color.transparent);
            }
        if (curier1) { img_Curier.setImageResource(R.mipmap.ic_curier);}else{
            img_Curier.setImageResource(R.color.transparent);
        }
        if(!baggage1&&!animal1&&!condition1&&!curier1) {
            img_Baggage.setImageResource(R.color.transparent);
            img_Animal.setImageResource(R.color.transparent);
            img_Condition.setImageResource(R.color.transparent);
            img_Curier.setImageResource(R.color.transparent);}
        if (payment1==1) text_oplata.setText(getString(R.string.payment_noncash));
        comment.setText(comment1);
        //******************************************************************************************

        try {
            JSONObject jsonObject = new JSONObject(json1);
            req_time = jsonObject.getString("required_time");
            suma=jsonObject.getString("order_cost");
            JSONObject route1 = jsonObject.getJSONObject("route_address_from");
            adress_from2=route1.getString("name")+" "+route1.getString("number");
            JSONObject route2 = jsonObject.getJSONObject("route_address_to");
            adress_to2=route2.getString("name")+" "+route2.getString("number");
            adress_to.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            adress_to.setVisibility(View.GONE);
            e.printStackTrace();
        }
        adress_from.setText(adress_from2);
        adress_to.setText(adress_to2);
        text_time.setText(req_time.substring(req_time.indexOf("T")+1,req_time.indexOf("T")+6));

        text_summa.setText(suma+getString(R.string.uah));
        return view;
    }
    public static InfoDialog newInstance(String json,int carClass,int payment,String comment,boolean baggage,boolean animal,boolean condition,boolean curier) {
        InfoDialog ret = new InfoDialog();
        carClass1=carClass;
        payment1=payment;
        json1=json;
        comment1=comment;
        baggage1=baggage;
        animal1=animal;
        condition1=condition;
        curier1=curier;

       // ret.initialize(callback);
        return ret;
    }
    public void initialize(OnInfoDialogListener callback) {
        mCallback = callback;

    }
    public interface OnInfoDialogListener {
        void onCarClassSet();
    }
    public void setOnCarDialogListener(OnInfoDialogListener callback) {
        mCallback = callback;
    }


}

