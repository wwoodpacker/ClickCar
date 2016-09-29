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
import com.taxi.clickcar.Requests.RegisterRequest;
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
 * Created by Назар on 25.03.2016.
 */
public class ConfirmFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    public SimpleArcDialog mDialog;
    public View.OnClickListener mOnClickListener;
    public String confirm_code;
    public static ConfirmFragment newInstance(int page) {
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
        mDialog= StaticMethods.getArcDialog(getContext(),getString(R.string.check_confirm));
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
                        mDialog.show();
                        RegisterRequest registerRequest = new RegisterRequest(GlobalVariables.getInstance().getRegisterPhone(),
                                confirm_code,
                                GlobalVariables.getInstance().getRegisterPassword(),
                                GlobalVariables.getInstance().getRegisterPasswordAgain(),
                                GlobalVariables.getInstance().getRegisterName());
                        WebOrdersApiInterface apiService= ApiClient.getClient().create(WebOrdersApiInterface.class);
                        Call<StatusResponse> call= apiService.registerUser(registerRequest);
                        call.enqueue(new Callback<StatusResponse>() {
                            @Override
                            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                                if(response.code()==200){
                                    MainActivity.setFl(false);
                                    mDialog.dismiss();
                                    Toast.makeText(getContext(), getString(R.string.succses_add_user), Toast.LENGTH_LONG).show();
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
                                Log.e("ConfirmFrag onFailure",t.toString());
                            }
                        });
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ConfirmFragment","OnDestroy");
    }

}