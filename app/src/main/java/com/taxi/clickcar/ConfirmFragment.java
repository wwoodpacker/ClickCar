package com.taxi.clickcar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.taxi.clickcar.Tasks.RegisterTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by Назар on 25.03.2016.
 */
public class ConfirmFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    public View.OnClickListener mOnClickListener;
    public String confirm_code;
    static ConfirmFragment newInstance(int page) {
        ConfirmFragment confirmFragment = new ConfirmFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        confirmFragment.setArguments(arguments);
        Log.d("enter", "newInstance");
        return confirmFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("confirm", "oncreateview");
        View view = inflater.inflate(R.layout.fragment_confirm, null);
        Button btn1 = (Button) view.findViewById(R.id.btn_confirm);
        final EditText confirm = (EditText) view.findViewById(R.id.edit_confirm);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm_code=confirm.getText().toString();
                if (confirm_code.isEmpty()){
                    Toast.makeText(getContext(),"Please enter confirm code",Toast.LENGTH_SHORT).show();
                }
                else {
                    RegisterTask registerTask=new RegisterTask(getContext());
                    registerTask.execute(MainActivity.phone,
                            confirm_code,
                            MainActivity.password,
                            MainActivity.password_again,
                            MainActivity.name);
                    try {
                        String jsonStr=registerTask.get();
                        Log.e("REGISTER JSON", jsonStr);
                        JSONObject object = new JSONObject(jsonStr);
                        Toast.makeText(getContext(),object.getString("Message"),Toast.LENGTH_SHORT).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(),"Пользователь зарегистрирован",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getContext(),MainActivity.class);
                        MainActivity.setFl(false);
                        startActivity(intent);
                    }
                    ;
                }
            }
        });

        return view;


    }

    public boolean CheckConnection() {

        ConnectivityManager conMan = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING || wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            return true;
        } else {
            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "No connection", Snackbar.LENGTH_LONG)
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
}