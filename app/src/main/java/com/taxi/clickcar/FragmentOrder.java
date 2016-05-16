package com.taxi.clickcar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pickerclickcar.time.RadialPickerLayout;
import com.example.pickerclickcar.time.TimePickerDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.taxi.clickcar.Dialogs.CarClassDialog;
import com.taxi.clickcar.Dialogs.DoplnDialog;
import com.taxi.clickcar.Dialogs.OplataDialog;
import com.taxi.clickcar.Order.Cost;
import com.taxi.clickcar.Tasks.CostTask;
import com.taxi.clickcar.Tasks.FragmentMap2;
import com.taxi.clickcar.Tasks.OrderTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Назар on 22.04.2016.
 */
public class FragmentOrder extends Fragment {
    public final static String BROADCAST_ACTION = "com.taxi.clickcar.p0961servicebackbroadcast";
    public final static String PARAM_RESULT = "result";
    public final static String PARAM_STATUS = "status";
    private CostTask costTask;
    public ImageView btn_back,img_carClass,img_Baggage,img_Animal,img_Condition,img_Curier;
    public Button btn_date,btn_zakaz,btn_car,btn_dop,btn_oplata;
    public EditText ed_comment=null;
    private SimpleArcDialog mDialog;
    public TextView textCost,textDate,textOplata;
    private Cost cost_object=null;
    public TimePickerDialog.OnTimeSetListener onTimeSetListener=null;
    public CarClassDialog.OnCarDialogListener onCarDialogListener=null;
    public DoplnDialog.OnDoplnDialogListener onDoplnDialogListener=null;
    public OplataDialog.OnOplataDialogListener onOplataDialogListener=null;
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
        ed_comment=(EditText)view.findViewById(R.id.editcomment);
        btn_back= (ImageView)view.findViewById(R.id.imageView13);
        img_carClass=(ImageView)view.findViewById(R.id.imageClass);
        img_Baggage=(ImageView)view.findViewById(R.id.imgBaggage);
        img_Animal=(ImageView)view.findViewById(R.id.imgAnimal);
        img_Condition=(ImageView)view.findViewById(R.id.imgCondition);
        img_Curier=(ImageView)view.findViewById(R.id.imgCurier);

        btn_zakaz=(Button)view.findViewById(R.id.btn_zakaz);
        btn_car=(Button)view.findViewById(R.id.btn_class);
        btn_dop=(Button)view.findViewById(R.id.btn_dop);
        btn_oplata=(Button)view.findViewById(R.id.btn_toplat);
        textCost = (TextView)view.findViewById(R.id.text_cost);
        textDate=(TextView)view.findViewById(R.id.txt_time);
        textOplata=(TextView)view.findViewById(R.id.txt_oplata);
        cost_add =(SeekBar)view.findViewById(R.id.seekBar);
        btn_date=(Button)view.findViewById(R.id.btn_date);
        Bundle bundle= this.getArguments();

        Log.e("FragmentOrder", "onCreateView");
        //----------------------------------------------------------------------------------------------------
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
        //----------------------------------------------------------------------------------------------------
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
        //----------------------------------------------------------------------------------------------------
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
        //----------------------------------------------------------------------------------------------------
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
                //btn_date.setText("Время подачи\t\t\t\t\t\t\t\t\t\t\t"+hour+":"+min);
                textDate.setText(hour+":"+min);
                Log.e("Time", cost_object.getRequiredTime());

            }
        };
        //----------------------------------------------------------------------------------------------------
        btn_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("FragmentOrder","Click CarClass");
                CarClassDialog.newInstance(onCarDialogListener).show(getFragmentManager(),"carclass");
            }
        });
        onCarDialogListener=new CarClassDialog.OnCarDialogListener() {
            @Override
            public void onCarClassSet(boolean standart, boolean premium, boolean universal, boolean microbus) {
                Log.e("FragmentOrder","callback CarClass");
                if (standart){ img_carClass.setImageResource(R.mipmap.icon_standart); cost_object.setStandart();}
                else
                if (premium) { img_carClass.setImageResource(R.mipmap.icon_premium); cost_object.setPremium(true);}
                else
                if (universal){ img_carClass.setImageResource(R.mipmap.icon_universal);cost_object.setWagon(true);}
                else
                if (microbus) { img_carClass.setImageResource(R.mipmap.icon_microbus); cost_object.setMinibus(true);}
                else {img_carClass.setImageResource(R.color.transparent);cost_object.setStandart();}
                //5555
                Gson gson=new Gson();
                String jsonCost = gson.toJson(cost_object);
                CostTask costTask1 = new CostTask(new MyCallBack() {
                    @Override
                    public void OnTaskDone(String result) {
                        Log.e("CoastResponse:",result);
                        try {
                            dataJsonObj=new JSONObject(result);
                            cost=dataJsonObj.getString("order_cost");
                            textCost.setText(cost);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                costTask1.setContext(getContext());
                costTask1.execute(jsonCost);

            }
        };
        //----------------------------------------------------------------------------------------------------
        btn_dop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("FragmentOrder","Click DoplnDialog");
                DoplnDialog.newInstance(onDoplnDialogListener).show(getFragmentManager(),"dopln");
            }
        });
        onDoplnDialogListener=new DoplnDialog.OnDoplnDialogListener() {
            @Override
            public void onDoplnSet(boolean baggage, boolean animal, boolean condition, boolean curier) {
                if (baggage){ img_Baggage.setImageResource(R.mipmap.ic_baggage); cost_object.setBaggage(true);} else {
                    img_Baggage.setImageResource(R.color.transparent);
                    cost_object.setBaggage(false);
                }
                if (animal) { img_Animal.setImageResource(R.mipmap.ic_animal); cost_object.setAnimal(true);}else{
                    img_Animal.setImageResource(R.color.transparent);
                    cost_object.setAnimal(false);
                }
                if (condition){ img_Condition.setImageResource(R.mipmap.ic_condition);cost_object.setConditioner(true);}else {
                    img_Condition.setImageResource(R.color.transparent);
                    cost_object.setConditioner(false);
                }
                if (curier) { img_Curier.setImageResource(R.mipmap.ic_curier); cost_object.setCourierDelivery(true);}else{
                    img_Curier.setImageResource(R.color.transparent);
                    cost_object.setCourierDelivery(false);
                }
                if(!baggage&&!animal&&!condition&&!curier) {
                    img_Baggage.setImageResource(R.color.transparent);
                    img_Animal.setImageResource(R.color.transparent);
                    img_Condition.setImageResource(R.color.transparent);
                    img_Curier.setImageResource(R.color.transparent);
                    cost_object.dropDopln();
                }
                //5555
                Gson gson=new Gson();
                String jsonCost = gson.toJson(cost_object);
                CostTask costTask1 = new CostTask(new MyCallBack() {
                    @Override
                    public void OnTaskDone(String result) {
                        Log.e("CoastResponse:",result);
                        try {
                            dataJsonObj=new JSONObject(result);
                            cost=dataJsonObj.getString("order_cost");
                            textCost.setText(cost);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                costTask1.setContext(getContext());
                costTask1.execute(jsonCost);
            }
        };
        //----------------------------------------------------------------------------------------------------
        btn_oplata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OplataDialog.newInstance(onOplataDialogListener).show(getFragmentManager(),"oplata");
            }
        });
        onOplataDialogListener=new OplataDialog.OnOplataDialogListener() {
            @Override
            public void onOplataSet(boolean cash, boolean nocash) {
                if (cash) {textOplata.setText("Наличный расчет");cost_object.setPaymentType(0);}else
                if(nocash) {textOplata.setText("Безналичный расчет");cost_object.setPaymentType(1);}else
                {textOplata.setText("");cost_object.setPaymentType(0);}
            }
        };
        //----------------------------------------------------------------------------------------------------
        btn_zakaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cost_object.setComment(ed_comment.getText().toString());
                //add date exeption "Id":-54,
                Gson gson=new Gson();
                String jsonCost = gson.toJson(cost_object);
                Log.e("Zakaz",jsonCost);

                OrderTask orderTask = new OrderTask(new MyCallBack() {
                    @Override
                    public void OnTaskDone(String result) {
                        Log.e("zakazResponse:",result);
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            String UID=jsonObject.getString("dispatching_order_uid");
                            getContext().startService(new Intent(getContext(),FoneService.class).putExtra("Url",UID));
                            mDialog = new SimpleArcDialog(getContext());
                            ArcConfiguration configuration = new ArcConfiguration(getContext());
                            configuration.setLoaderStyle(SimpleArcLoader.STYLE.SIMPLE_ARC);
                            configuration.setText("Поиск машины..");

                            mDialog.setConfiguration(configuration);

                            mDialog.show();

                            BroadcastReceiver br= new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {

                                    int status=intent.getIntExtra(PARAM_STATUS,0);
                                    if (status==100){
                                        mDialog.dismiss();
                                        String text=intent.getStringExtra(PARAM_RESULT);
                                       Bundle bundle=new Bundle();
                                       bundle.putString("ORDERJSON",text);

                                        FragmentMap2 fragmentMap2=new FragmentMap2();
                                        fragmentMap2.setArguments(bundle);

                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                        fragmentTransaction.replace(R.id.container,fragmentMap2);

                                        fragmentTransaction.commit();
                                    }
                                    if(status==200){
                                        mDialog.dismiss();
                                        Toast.makeText(getContext(),"Машина не найдена",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            };
                            // создаем фильтр для BroadcastReceiver
                            IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
                            // регистрируем (включаем) BroadcastReceiver
                            getContext().registerReceiver(br, intFilt);

                          /*  Bundle bundle=new Bundle();
                            bundle.putString("ORDERJSON",UID);

                            FragmentMap2 fragmentMap2=new FragmentMap2();
                            fragmentMap2.setArguments(bundle);

                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            fragmentTransaction.replace(R.id.container,fragmentMap2);

                            fragmentTransaction.commit();*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"Неверно выбрана дата!",Toast.LENGTH_SHORT).show();
                            Log.e("Json","Exeption");
                        }
                    }
                });
                orderTask.setContext(getContext());
                orderTask.execute(jsonCost);

               /* Bundle bundle=new Bundle();
                bundle.putString("COSTJSON","params");

                FragmentMap2 fragmentMap2=new FragmentMap2();
                fragmentMap2.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.container,fragmentMap2);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
            }
        });
        return view;
    }


}
