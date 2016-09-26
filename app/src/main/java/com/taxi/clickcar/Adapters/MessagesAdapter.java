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
import com.taxi.clickcar.Order.History;
import com.taxi.clickcar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Назар on 12.06.2016.
 */
public class MessagesAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public FragmentManager fragmentManager;
    private ArrayList<String> massagesList=new ArrayList<String>();
    private List<Boolean> checkList=new ArrayList<Boolean>();
    public MessagesAdapter(Context context,FragmentManager fm){

        mContext = context;
        fragmentManager=fm;

        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return massagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return massagesList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }
    public void upDateMassage(ArrayList<String> massages){

        this.massagesList=massages;
        notifyDataSetChanged();
    }
    public void add(String massage){

        this.massagesList.add(massage);
        checkList.add(false);

    }
public void add_check(boolean check){
        checkList.add(check);
    }
    public boolean get_check(int pos){
        return checkList.get(pos);
    }
    public void clearData() {
        // clear the data
        massagesList.clear();
        checkList.clear();
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        RelativeLayout itemView;
        if (convertView == null) {
            itemView = (RelativeLayout) mLayoutInflater.inflate(
                    R.layout.massages_item, parent, false);

        } else {
            itemView = (RelativeLayout) convertView;
        }


        final TextView massage=(TextView)itemView.findViewById(R.id.text_massage);
        final ImageView btn_star=(ImageView)itemView.findViewById(R.id.img);
        final String msg = this.massagesList.get(position);
        final boolean ch=checkList.get(position);
        massage.setText(msg);
        btn_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch) {btn_star.setImageResource(R.mipmap.ic_star1);checkList.add(position,false);}
                    else {btn_star.setImageResource(R.mipmap.ic_star2);checkList.add(position,true);}

            }
        });


        return itemView;
    }


}
