package com.taxi.clickcar.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taxi.clickcar.MainActivity;
import com.taxi.clickcar.R;
import com.taxi.clickcar.StaticMethods;
import com.taxi.clickcar.Tasks.GetConfirmCodeTask;
import com.taxi.clickcar.UserRegistratonProfile;

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
        StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection));

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
            Toast.makeText(getContext(), getString(R.string.error_all_fields), Toast.LENGTH_SHORT).show();
        } else if (!userRegProf.pass.contains(userRegProf.pass_again)) {
            Toast.makeText(getContext(), getString(R.string.error_match_pass), Toast.LENGTH_SHORT).show();
        } else if (userRegProf.pass.length() < 6) {
            Toast.makeText(getContext(), getString(R.string.error_lenght_pass), Toast.LENGTH_SHORT).show();
        } else {
            if (StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))) {
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
    }
     });
        return v;
 }
}