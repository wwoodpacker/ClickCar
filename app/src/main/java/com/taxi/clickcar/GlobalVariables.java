package com.taxi.clickcar;

import android.app.Application;

/**
 * Created by Назар on 26.09.2016.
 */
public class GlobalVariables {
    private static GlobalVariables instance=null;
    private String name;
    private String phone;
    private String base64EncodedCredentials ;
    //Registration variebles
    public String registerName;
    public String registerPhone;
    public String registerConfirmCode;
    public String registerPassword;
    public String registerPasswordAgain;

    public static synchronized GlobalVariables getInstance(){
        if(instance==null){
            instance=new GlobalVariables();

        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBase64EncodedCredentials() {
        return base64EncodedCredentials;
    }

    public void setBase64EncodedCredentials(String base64EncodedCredentials) {
        this.base64EncodedCredentials = base64EncodedCredentials;
    }

    public String getRegisterConfirmCode() {
        return registerConfirmCode;
    }

    public String getRegisterName() {
        return registerName;
    }

    public String getRegisterPhone() {
        return registerPhone;
    }

    public String getRegisterPassword() {
        return registerPassword;
    }

    public String getRegisterPasswordAgain() {
        return registerPasswordAgain;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }

    public void setRegisterPhone(String registerPhone) {
        this.registerPhone = registerPhone;
    }

    public void setRegisterConfirmCode(String registerConfirmCode) {
        this.registerConfirmCode = registerConfirmCode;
    }

    public void setRegisterPassword(String registerPassword) {
        this.registerPassword = registerPassword;
    }

    public void setRegisterPasswordAgain(String registerPasswordAgain) {
        this.registerPasswordAgain = registerPasswordAgain;
    }
}
