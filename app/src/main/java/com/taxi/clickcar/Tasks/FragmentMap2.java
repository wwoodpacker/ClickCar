package com.taxi.clickcar.Tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taxi.clickcar.Dialogs.InfoDialog;
import com.taxi.clickcar.Fragments.FragmentMap;
import com.taxi.clickcar.MyCallBack;
import com.taxi.clickcar.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Назар on 15.05.2016.
 */
public class FragmentMap2 extends Fragment implements OnMapReadyCallback {
    private static final int PERMISSION_LOCATION_REQUEST_CODE = 1;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOCATION="location";
    public static final String APP_PREFERENCES_PUSH="PUSH";
    public static final String APP_PREFERENCES_SMS="SHOWSMS";
    public boolean loc=false,push=false,shsms=false;
    public SharedPreferences mSettings;
    private GoogleMap mMap;
    public String UID;
    public TextView textDriver,btn_info,btn_cancel;
    public ImageView btn_telefon,btn_sms;
    public View view;
    private LocationManager locationManager;
    public MyCallBack myCallBack;
    private String provider,car_info,driver_phone;
    public boolean standart, premium,  universal,  microbus,
            baggage,  animal,  condition,  curier,
            cash,  nocash;
    public String json,comment;
    public  int carClass,payment;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getCurrentLocation();
        init();
    }
    public void readSettings(){
        if(mSettings.contains(APP_PREFERENCES_LOCATION)) {
            loc=mSettings.getBoolean(APP_PREFERENCES_LOCATION, false);
        }
        if(mSettings.contains(APP_PREFERENCES_PUSH)) {
            push=mSettings.getBoolean(APP_PREFERENCES_PUSH, false);
        }
        if(mSettings.contains(APP_PREFERENCES_SMS)) {
            shsms=mSettings.getBoolean(APP_PREFERENCES_SMS, false);
        }
    }
    public FragmentMap2(){};
    public View.OnClickListener mOnClickListener;
    public boolean CheckConnection(){

        ConnectivityManager conMan = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING||wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            return true;
        } else  {
            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),getString(R.string.error_connection),Snackbar.LENGTH_LONG)
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map2,null);
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, getActivity().MODE_PRIVATE);

        readSettings();
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        Bundle bundle= this.getArguments();
        json=bundle.getString("ORDERJSON");
        carClass=bundle.getInt("CARCLASS");
        payment=bundle.getInt("PAYMENT");
        comment=bundle.getString("COMMENT");
        baggage= bundle.getBoolean("BAG");
        animal=bundle.getBoolean("ANIMAL");
        condition=bundle.getBoolean("CONDITION");
        curier =bundle.getBoolean("CURIER");
        textDriver=(TextView)view.findViewById(R.id.textDriver);
        btn_telefon=(ImageView)view.findViewById(R.id.btn_phone);
        btn_sms=(ImageView)view.findViewById(R.id.btn_sms);
        btn_info=(TextView) view.findViewById(R.id.btn_info);
        btn_cancel=(TextView)view.findViewById(R.id.btn_otmena);
        Log.e("FragmentMap2", "onCreateView");
        if (!shsms) btn_sms.setVisibility(View.GONE);
                else {
            btn_sms.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)btn_telefon.getLayoutParams();
            params.addRule(RelativeLayout.LEFT_OF, R.id.imageView6);
            params.removeRule(RelativeLayout.ALIGN_RIGHT);
        }
        Log.e("MAp2",json);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            UID=jsonObject.getString("dispatching_order_uid");
            car_info=jsonObject.getString("order_car_info");
            driver_phone=jsonObject.getString("driver_phone");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Json","exeption");
        }
        textDriver.setText(car_info.toString().substring(0,car_info.length()-12));
        //-----------------------------------------------------------------------------------------
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Cancel", "Click" + UID);
                if (CheckConnection()){
                    CancelTask cancelTask = new CancelTask(new MyCallBack() {
                        @Override
                        public void OnTaskDone(String result) {
                            Log.e("Cancel Response", result);
                            Toast.makeText(getContext(), getString(R.string.cancel_order2), Toast.LENGTH_SHORT).show();
                            FragmentMap fragmentMap = new FragmentMap();


                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            fragmentTransaction.replace(R.id.container, fragmentMap);
                            //fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });
                cancelTask.setContext(getContext());
                cancelTask.execute(UID);
            }

            }
        });
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoDialog.newInstance(json,carClass,payment,comment,baggage,animal,condition,curier).show(getFragmentManager(), "Infodialog");
            }
        });
        btn_telefon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+driver_phone));
                startActivity(in);
            }
        });
        btn_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",driver_phone, null)));
            }
        });
        return view;
    }




    private void init() {
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition camera) {
                Log.d("FragmentMap", "onCameraChange: " + camera.target.latitude + "," + camera.target.longitude);
                String otvet = "";
            }
        });
    }
    private void getCurrentLocation() {
        if (CheckConnection()) {
            locationManager = (LocationManager)
                    getActivity().getSystemService(Context.LOCATION_SERVICE);

            boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!enabled) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            } else {
                Criteria criteria = new Criteria();
                provider = locationManager.getBestProvider(criteria, false);
                Location location = locationManager.getLastKnownLocation(provider);
                locationManager.requestLocationUpdates(provider, 1000,
                        500.0f, locationListener);
                if (location != null) {
                    double dLatitude = location.getLatitude();
                    double dLongitude = location.getLongitude();
                    Log.i("APPLICATION", " : " + dLatitude);
                    Log.i("APPLICATION", " : " + dLongitude);
                    mMap.addMarker(new MarkerOptions().position(
                            new LatLng(dLatitude, dLongitude)).alpha(0));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 18));

                } else {
                    Toast.makeText(getContext(), getString(R.string.enable_gps), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double dLatitude = location.getLatitude();
            double dLongitude = location.getLongitude();
            Log.i("APPLICATION", " : " + dLatitude);
            Log.i("APPLICATION", " : " + dLongitude);
            mMap.addMarker(new MarkerOptions().position(
                    new LatLng(dLatitude, dLongitude)).alpha(0));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 18));

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getContext(), getString(R.string.enable_gps), Toast.LENGTH_SHORT).show();
        }
    };

}

