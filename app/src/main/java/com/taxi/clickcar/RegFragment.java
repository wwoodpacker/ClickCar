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

import com.taxi.clickcar.Tasks.GetConfirmCodeTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by Назар on 24.03.2016.
 */
public class RegFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    public View.OnClickListener mOnClickListener;
    public EditText ed_name;
    public EditText ed_phone;
    public EditText ed_pass;
    public EditText ed_pass_again;

    static RegFragment newInstance(int page) {
        RegFragment regFragment = new RegFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        regFragment.setArguments(arguments);
        Log.d("reg", "newInstance");
        return regFragment;

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
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        Log.d("reg", "oncreateview");
        CheckConnection();

        View v = inflater.inflate(R.layout.fragment_reg, null);
        Button btn_reg = (Button) v.findViewById(R.id.btn_reg);
        ed_name = (EditText) v.findViewById(R.id.editText3);
        ed_phone = (EditText) v.findViewById(R.id.editText4);
        ed_pass = (EditText) v.findViewById(R.id.editText5);
        ed_pass_again = (EditText) v.findViewById(R.id.editText6);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              UserRegistratonProfile userRegProf = new UserRegistratonProfile(
                ed_name.getText().toString(),
                ed_phone.getText().toString(),
                ed_pass.getText().toString(),
                ed_pass_again.getText().toString()
        );
        if (userRegProf.name.length() == 0 || userRegProf.phone.length() == 0 || userRegProf.pass.length() == 0 || userRegProf.pass_again.length() == 0) {
            Toast.makeText(getContext(), "Write all fields", Toast.LENGTH_SHORT).show();
        } else if (!userRegProf.pass.contains(userRegProf.pass_again)) {
            Toast.makeText(getContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
        } else if (userRegProf.pass.length() < 6) {
            Toast.makeText(getContext(), "Min length of password 7!", Toast.LENGTH_SHORT).show();
        } else {
            GetConfirmCodeTask getConfirmCode = new GetConfirmCodeTask(getContext());
            getConfirmCode.execute(ed_phone.getText().toString());
            try {
                String jsonstr = getConfirmCode.get();
                JSONObject object = new JSONObject(jsonstr);
                Toast.makeText(getContext(), object.getString("Message"), Toast.LENGTH_SHORT).show();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
                MainActivity.setFl(true);
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra(UserRegistratonProfile.class.getCanonicalName(), userRegProf);
                startActivity(intent);
            }
        }

    }
     });
        return v;
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