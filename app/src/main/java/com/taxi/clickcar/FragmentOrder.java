package com.taxi.clickcar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.pickerclickcar.time.RadialPickerLayout;
import com.example.pickerclickcar.time.TimePickerDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taxi.clickcar.Order.Cost;
import com.taxi.clickcar.Tasks.CostTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Назар on 22.04.2016.
 */
public class FragmentOrder extends Fragment {
    private CostTask costTask;
    public ImageView btn_back;
    public Button btn_date,btn_zakaz;
    public TextView textCost;
    private Cost cost_object=null;
    public TimePickerDialog.OnTimeSetListener onTimeSetListener=null;
    public SeekBar cost_add;
    public String cost="";
    private Calendar calendar;
    public JSONObject dataJsonObj = null;
    public FragmentOrder(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_order,null);
        calendar = Calendar.getInstance();
        cost_object = new Cost();
        btn_back= (ImageView)view.findViewById(R.id.imageView13);
        btn_zakaz=(Button)view.findViewById(R.id.btn_zakaz);
        textCost = (TextView)view.findViewById(R.id.text_cost);
        cost_add =(SeekBar)view.findViewById(R.id.seekBar);
        btn_date=(Button)view.findViewById(R.id.btn_date);
        Bundle bundle= this.getArguments();

        Log.e("FragmentOrder", "onCreateView");
        costTask=new CostTask(new MyCallBack() {
            @Override
            public void OnTaskDone(String result) {

                try {
                    dataJsonObj=new JSONObject(result);
                    cost=dataJsonObj.getString("order_cost");
                    textCost.setText(cost);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("CoastResponse:",result);
            }
        });
        costTask.setContext(getContext());
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        cost_object= gson.fromJson(bundle.getString("COSTJSON"), Cost.class);
        Log.e("CoastRequest:",bundle.getString("COSTJSON"));
        costTask.execute(bundle.getString("COSTJSON"));

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMap fragmentMap=new FragmentMap();


                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.container,fragmentMap);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        cost_add.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cost_object.setAddCost((double)progress);
                Log.e("AddCost",String.valueOf(cost_object.getAddCost()));
                 textCost.setText(Integer.toString(Integer.parseInt(cost)+progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.newInstance(onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
            }
        });
        onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                cost_object.setReservation(true);

                String month=String.valueOf(calendar.get(Calendar.MONTH)+1);//
                if(Integer.parseInt(month)<10) month="0"+month; else month=String.valueOf(month);
                String day=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));//
                if(Integer.parseInt(day)<10) day="0"+calendar.get(Calendar.DAY_OF_MONTH); else day=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                String hour,min;
                if(hourOfDay<10) hour="0"+String.valueOf(hourOfDay); else hour=String.valueOf(hourOfDay);
                if(minute<10) min="0"+String.valueOf(minute); else min=String.valueOf(minute);


                String date=calendar.get(Calendar.YEAR)+"-"+month+"-"+day+"T"+hour+":"+min+":00";
                cost_object.setRequiredTime(date);
                btn_date.setText("Время подачи\t\t\t\t\t\t\t\t\t\t\t\t\t"+hour+":"+min);
                Log.e("Time", cost_object.getRequiredTime());

            }
        };
        btn_zakaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson=new Gson();
                String jsonCost = gson.toJson(cost_object);
                CostTask costTask1 = new CostTask(new MyCallBack() {
                    @Override
                    public void OnTaskDone(String result) {
                        Log.e("CoastResponse:",result);
                    }
                });
                costTask1.setContext(getContext());
                costTask1.execute(jsonCost);
            }
        });
        return view;
    }


}
