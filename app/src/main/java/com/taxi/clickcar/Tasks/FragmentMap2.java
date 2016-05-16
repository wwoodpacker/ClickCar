package com.taxi.clickcar.Tasks;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taxi.clickcar.MyCallBack;
import com.taxi.clickcar.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Назар on 15.05.2016.
 */
public class FragmentMap2 extends Fragment implements OnMapReadyCallback {
    private static final int PERMISSION_LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    public String UID;
    public TextView textDriver,btn_info,btn_cancel;
    public ImageView btn_telefon,btn_sms;
    public View view;
    private LocationManager locationManager;
    public MyCallBack myCallBack;
    private String provider,car_info,driver_phone;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getCurrentLocation();
        init();
    }
    public FragmentMap2(){};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map2,null);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        Bundle bundle= this.getArguments();
        String json=bundle.getString("ORDERJSON");

        Log.e("MAp2",json);
        textDriver=(TextView)view.findViewById(R.id.textDriver);
        btn_telefon=(ImageView)view.findViewById(R.id.btn_phone);
        btn_sms=(ImageView)view.findViewById(R.id.btn_sms);
        btn_info=(TextView) view.findViewById(R.id.btn_info);
        btn_cancel=(TextView)view.findViewById(R.id.btn_otmena);
        Log.e("FragmentMap2", "onCreateView");
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
        textDriver.setText(car_info.toString());
        //-----------------------------------------------------------------------------------------
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Cancel","Click"+UID);
                CancelTask cancelTask=new CancelTask(new MyCallBack() {
                    @Override
                    public void OnTaskDone(String result) {
                        Log.e("Cancel Response",result);
                    }
                });
                cancelTask.setContext(getContext());
                cancelTask.execute(UID);
            }
        });
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusTask statusTask=new StatusTask(new MyCallBack() {
                    @Override
                    public void OnTaskDone(String result) {
                        Log.e("Status Response",result);
                    }
                });
                statusTask.setContext(getContext());
                statusTask.execute(UID);
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
        locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        else {
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider,1000,
                    500.0f, locationListener);
            if(location!=null){
                double dLatitude = location.getLatitude();
                double dLongitude = location.getLongitude();
                Log.i("APPLICATION"," : "+dLatitude);
                Log.i("APPLICATION"," : "+dLongitude);
                mMap.addMarker(new MarkerOptions().position(
                        new LatLng(dLatitude, dLongitude)).alpha(0));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 18));

            }else {
                Toast.makeText(getContext(),"Please enable GPS",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "Please enable GPS", Toast.LENGTH_SHORT).show();
        }
    };

}

