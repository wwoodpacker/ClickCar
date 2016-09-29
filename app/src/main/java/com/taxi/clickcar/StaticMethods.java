package com.taxi.clickcar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import java.security.MessageDigest;

/**
 * Created by Назар on 26.09.2016.
 */
public class StaticMethods {


    public static SimpleArcDialog getArcDialog(Context mContext,String data){
        SimpleArcDialog mDialog=null;
        if (mDialog==null){
            mDialog = new SimpleArcDialog(mContext);
            ArcConfiguration configuration = new ArcConfiguration(mContext);
            configuration.setLoaderStyle(SimpleArcLoader.STYLE.SIMPLE_ARC);
            configuration.setText(data);
            mDialog.setConfiguration(configuration);
        }
        return mDialog;
    }

    public static boolean CheckConnection(Activity activity, View.OnClickListener mOnClickListener, String data){
        ConnectivityManager conMan = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING || wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            return true;
        } else {
            Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), data, Snackbar.LENGTH_LONG)
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
    public static String convertByteToHex(byte data[])
    {
        StringBuffer hexData = new StringBuffer();
        for (int byteIndex = 0; byteIndex < data.length; byteIndex++)
            hexData.append(Integer.toString((data[byteIndex] & 0xff) + 0x100, 16).substring(1));

        return hexData.toString();
    }

    public static String hashText(String textToHash) throws Exception
    {
        final MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
        sha512.update(textToHash.getBytes());

        return convertByteToHex(sha512.digest());
    }
}
