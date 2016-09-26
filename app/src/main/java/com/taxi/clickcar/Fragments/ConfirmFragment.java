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
                    Toast.makeText(getContext(),getString(R.string.error_confirm),Toast.LENGTH_SHORT).show();
                }
                else {
                    if (StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))) {
                        RegisterTask registerTask = new RegisterTask(getContext());
                        registerTask.execute(MainActivity.phone,
                                confirm_code,
                                MainActivity.password,
                                MainActivity.password_again,
                                MainActivity.name);
                        try {
                            String jsonStr = registerTask.get();
                            Log.e("REGISTER JSON", jsonStr);
                            JSONObject object = new JSONObject(jsonStr);
                            Toast.makeText(getContext(), object.getString("Message"), Toast.LENGTH_SHORT).show();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), getString(R.string.succses_add_user), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            MainActivity.setFl(false);
                            startActivity(intent);
                        }
                    }
                }
            }
        });

        return view;


    }
}