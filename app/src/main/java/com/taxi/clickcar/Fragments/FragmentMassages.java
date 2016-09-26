package com.taxi.clickcar.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.taxi.clickcar.ActivityDrawer;
import com.taxi.clickcar.Adapters.MessagesAdapter;
import com.taxi.clickcar.Dialogs.SendDialog;
import com.taxi.clickcar.GlobalVariables;
import com.taxi.clickcar.Mail.ExtendedMail;
import com.taxi.clickcar.MyCallBack;
import com.taxi.clickcar.R;
import com.taxi.clickcar.StaticMethods;
import com.taxi.clickcar.Tasks.MassageTask;
import com.taxi.clickcar.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Назар on 26.03.2016.
 */
public class FragmentMassages extends Fragment {
    public static final String MESSAGE="message";
    public static final String PHONE="phone";
    public MessagesAdapter messagesAdapter=null;
    public MyCallBack myCallBack=null;
    public FragmentMassages(){};
    public SendDialog.OnSendDialogListener onSendDialogListener=null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_massages,null);
        FloatingActionButton fab=(FloatingActionButton)view.findViewById(R.id.fab);
        final ListView listView=(ListView)view.findViewById(R.id.list2);
        messagesAdapter=new MessagesAdapter(getContext(),getFragmentManager());
        listView.setAdapter(messagesAdapter);
        MassageTask task= new MassageTask(messagesAdapter);
        task.setContext(getContext());
        task.execute("task");

        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))) {
                    SendDialog.newInstance(onSendDialogListener).show(getFragmentManager(), "SendDialog");
                }
            }
        });
        onSendDialogListener=new SendDialog.OnSendDialogListener() {
            @Override
            public void onEnterSet(String text) {
                if (StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))){
                    messagesAdapter.clearData();
                    addEntryToJsonFile(getContext(), GlobalVariables.getInstance().getPhone(),text);
                ExtendedMail extendedMail= new ExtendedMail(getContext(), ActivityDrawer.LOGIN.toString(),text);
                extendedMail.Run();
                    MassageTask task= new MassageTask(messagesAdapter);
                    task.setContext(getContext());
                    task.execute("task");
                }
            }
        };
        Log.e("FragmentMassages","onCreateView");
        return view;
    }


    public void addEntryToJsonFile(Context ctx, String phone,String _message) {
        // parse existing/init new JSON
        File path = ctx.getExternalFilesDir(null);
        File root = new File(path,"Massages");
        if (!root.exists()) {
            root.mkdirs();
        }
        File jsonFile = new File(root, "massagesFile.json");
        String previousJson = null;
        if (jsonFile.exists()) {
                previousJson = Utils.readFromFile(jsonFile);
            }
        else {  previousJson = "{}";  }

        // create new "complex" object
        JSONObject mO = null;
        JSONObject jO = new JSONObject();

        try {
            mO = new JSONObject(previousJson);
            jO.put(MESSAGE,_message);
            jO.put(PHONE,phone);
            mO.put(String.valueOf(mO.length()+1), jO); //thanks "retired" answer
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // generate string from the object
        String jsonString = null;
        if (mO != null) {
            jsonString = mO.toString();
        }else jsonString=previousJson;
        // write back JSON file
        Utils.writeToFile(jsonFile, jsonString);
    }
    public View.OnClickListener mOnClickListener;

}
