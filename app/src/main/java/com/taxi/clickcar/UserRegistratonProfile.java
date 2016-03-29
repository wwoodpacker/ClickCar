package com.taxi.clickcar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Назар on 19.03.2016.
 */
public class UserRegistratonProfile implements Parcelable {

    public String name;
    public String phone;
    public String confirm_code;
    public String pass;
    public String pass_again;
    public UserRegistratonProfile(){};
    public UserRegistratonProfile(String _name,String _phone,String _pass,String _pass_again){

        name=_name;
        phone=_phone;
        pass=_pass;
        pass_again=_pass_again;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(pass);
        dest.writeString(pass_again);
    }




    public static final Creator<UserRegistratonProfile> CREATOR = new Creator<UserRegistratonProfile>() {
        @Override
        public UserRegistratonProfile createFromParcel(Parcel in) {
            return new UserRegistratonProfile(in);
        }

        @Override
        public UserRegistratonProfile[] newArray(int size) {
            return new UserRegistratonProfile[size];
        }
    };

     private UserRegistratonProfile(Parcel parcel){

         name=parcel.readString();
         phone=parcel.readString();
         pass=parcel.readString();
         pass_again=parcel.readString();
    }
}