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

import com.leo.simplearcloader.SimpleArcDialog;
import com.taxi.clickcar.GlobalVariables;
import com.taxi.clickcar.MainActivity;
import com.taxi.clickcar.R;
import com.taxi.clickcar.Requests.PhoneRequest;
import com.taxi.clickcar.Responses.StatusResponse;
import com.taxi.clickcar.StaticMethods;
import com.taxi.clickcar.WebOrdersAPI.ApiClient;
import com.taxi.clickcar.WebOrdersAPI.ErrorUtils;
import com.taxi.clickcar.WebOrdersAPI.WebOrdersApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Назар on 24.03.2016.
 */
public class RegFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    public View.OnClickListener mOnClickListener;
    public SimpleArcDialog mDialog;
    public EditText ed_name;
    public EditText ed_phone;
    public EditText ed_pass;
    public EditText ed_pass_again;

    public static RegFragment newInstance(int page) {
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
        mDialog= StaticMethods.getArcDialog(getContext(),getString(R.string.get_confirm));
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

                GlobalVariables.getInstance().setRegisterName(ed_name.getText().toString());
                GlobalVariables.getInstance().setRegisterPhone(ed_phone.getText().toString());
                GlobalVariables.getInstance().setRegisterPassword(ed_pass.getText().toString());
                GlobalVariables.getInstance().setRegisterPasswordAgain(ed_pass_again.getText().toString());

        if (GlobalVariables.getInstance().getRegisterName().length() == 0 || GlobalVariables.getInstance().getRegisterPhone().length() == 0 || GlobalVariables.getInstance().getRegisterPassword().length() == 0 || GlobalVariables.getInstance().getRegisterPasswordAgain().length() == 0) {
            Toast.makeText(getContext(), getString(R.string.error_all_fields), Toast.LENGTH_SHORT).show();
        } else if (!GlobalVariables.getInstance().getRegisterPassword().contains(GlobalVariables.getInstance().getRegisterPasswordAgain())) {
            Toast.makeText(getContext(), getString(R.string.error_match_pass), Toast.LENGTH_SHORT).show();
        } else if (GlobalVariables.getInstance().getRegisterPassword().length() < 6) {
            Toast.makeText(getContext(), getString(R.string.error_lenght_pass), Toast.LENGTH_SHORT).show();
        } else {
            if (StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))) {
                mDialog.show();
                PhoneRequest phoneRequest = new PhoneRequest(ed_phone.getText().toString());

                WebOrdersApiInterface apiService= ApiClient.getClient().create(WebOrdersApiInterface.class);
                Call<StatusResponse> call= apiService.getConfirmCode(phoneRequest);
                call.enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        if(response.code()==429){
                            MainActivity.setFl(true);
                            mDialog.dismiss();
                            Toast.makeText(getContext(),  getString(R.string.succses_add_user), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            mDialog.dismiss();
                            StatusResponse error = ErrorUtils.parseError(response);
                            Toast.makeText(getContext(),  error.getStatus(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        mDialog.dismiss();
                        Log.e("RegFragment onFailure",t.toString());
                    }

                });
            }
        }
    }
    });
        return v;
 }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("RegFragment","OnDestroy");
        }
}