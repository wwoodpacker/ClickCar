package com.taxi.clickcar.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.taxi.clickcar.Fragments.FragmentOrder;
import com.taxi.clickcar.Order.History;
import com.taxi.clickcar.R;

import java.util.ArrayList;

/**
 * Created by Назар on 03.06.2016.
 */
public class HistoryAdapter extends BaseAdapter {
private Context mContext;
private LayoutInflater mLayoutInflater;
public FragmentManager fragmentManager;
private ArrayList<ArrayList<History>> nodes = new ArrayList<ArrayList<History>>();
private ArrayList<History> historyList=new ArrayList<History>();
public HistoryAdapter(Context context,FragmentManager fm){

        mContext = context;
        fragmentManager=fm;

        mLayoutInflater = (LayoutInflater) mContext
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
@Override
public int getCount() {
        return historyList.size();
        }

@Override
public Object getItem(int position) {
        return historyList.get(position);
        }


@Override
public long getItemId(int position) {
        return position;
        }
public void upDateHistory(ArrayList<History> history){

        this.historyList=history;
        notifyDataSetChanged();
        }
public void add(History history){

        this.historyList.add(history);

        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {


        RelativeLayout itemView;
        if (convertView == null) {
        itemView = (RelativeLayout) mLayoutInflater.inflate(
        R.layout.history_element, parent, false);

        } else {
        itemView = (RelativeLayout) convertView;
        }

        EditText addres_from=(EditText)itemView.findViewById(R.id.editText4);
        EditText addres_to=(EditText)itemView.findViewById(R.id.editText5);
        TextView date=(TextView)itemView.findViewById(R.id.txt_date);

final History history = this.historyList.get(position);
        addres_from.setText(history.getRoute().get(0).getName()+" "+history.getRoute().get(0).getNumber());
        addres_to.setText(history.getRoute().get(history.getRoute().size()-1).getName()+" "+history.getRoute().get(history.getRoute().size()-1).getNumber());
        date.setText(history.getRequiredTime().substring(0,10).replaceAll("-","."));
        ImageView btn_retry= (ImageView)itemView.findViewById(R.id.btn_retryzakaz);
        ImageView btn_spis= (ImageView)itemView.findViewById(R.id.btn_spisokzakaz);
        btn_retry.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
final AlertDialog.Builder adb = new AlertDialog.Builder(mContext);

        adb.setTitle(mContext.getString(R.string.retry_order));
        adb.setIcon(android.R.drawable.ic_dialog_alert);
        adb.setCancelable(true);
        adb.setPositiveButton(mContext.getString(R.string.yes), new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
        FragmentOrder fragmentOrder=new FragmentOrder();

        Bundle bundle = new Bundle();
        bundle.putBoolean("HISTORY",true);
        Gson gson = new Gson();
        String jsonCost = gson.toJson(history);
        bundle.putString("COSTJSON",jsonCost);
        fragmentOrder.setArguments(bundle);


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.container,fragmentOrder);

        fragmentTransaction.commit();
        } });


        adb.setNegativeButton(mContext.getString(R.string.no),null);
        adb.show();

        }
        });
        btn_spis.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Toast.makeText(mContext,mContext.getString(R.string.info_order),Toast.LENGTH_SHORT).show();
        }
        });
        return itemView;
        }

        }
