package com.taxi.clickcar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Назар on 19.03.2016.
 */
public class ConfirmCode extends Activity {
    private String login="";
    private String name="";
    private String phone="";
    private String pass="";
    private String pass_again="";
    private String confirm_code="";

    public Button btn_confirm_ok;
    public EditText ed_confirm_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_code);
        btn_confirm_ok =(Button)findViewById(R.id.btn_confirm_ok);
        ed_confirm_code =(EditText)findViewById(R.id.ed_confirm_code);
        Intent intent=getIntent();
        Bundle extras =intent.getExtras();
        if (extras!=null){
            login=extras.getString("LOGIN");
            name=extras.getString("NAME");
            phone=extras.getString("PHONE");
            pass=extras.getString("PASS");
            pass_again=extras.getString("PASS_AGAIN");
        }

    }

    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.btn_confirm_ok:
                ed_confirm_code.getText().toString();

                break;
        }
    }
}
