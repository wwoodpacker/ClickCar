package com.taxi.clickcar;


import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;


import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.arlib.floatingsearchview.FloatingSearchView;
import com.example.pickerclickcar.date.DatePickerDialog;
import com.example.pickerclickcar.time.RadialPickerLayout;
import com.example.pickerclickcar.time.TimePickerDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taxi.clickcar.R;
import com.taxi.clickcar.Tasks.GetGeoObjectTask;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import android.support.v4.app.DialogFragment;
import static android.support.v4.content.PermissionChecker.checkPermission;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Created by Назар on 26.03.2016.
 */
public class FragmentMap extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private GetGeoObjectTask task;
    private Button btn_zakaz;
    private Calendar calendar;
    TimePickerDialog.OnTimeSetListener callback;
    FloatingSearchView searchView;

    public FragmentMap() {
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Log.e("FragmentMap", "onCreateView");
        searchView=(FloatingSearchView )view.findViewById(R.id.floating_search_view);
        calendar = Calendar.getInstance();
        //search.setDrawerLogo();
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_zakaz=(Button)view.findViewById(R.id.btn_zakaz);
        btn_zakaz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callback=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

                    }
                };
                TimePickerDialog.newInstance(callback, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
            }
        });
        searchView.bringToFront();
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {

                searchView.showProgress();
            }

        });

        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                //show suggestions when search bar gains focus (typically history suggestions)


                Log.d("search", "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                Log.d("search", "onFocusCleared()");
            }
        });

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        // mMap.setMyLocationEnabled(true);
        getCurrentLocation();
        init();
    }

    private void init() {
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition camera) {
                Log.d("FragmentMap", "onCameraChange: " + camera.target.latitude + "," + camera.target.longitude);
                String otvet="";

                task=new GetGeoObjectTask(new MyCallBack() {
                    @Override
                    public void OnTaskDone(String result) {
                        searchView.hideProgress();
                                                searchView.setSearchText(result);
                        Log.e("Json Camera",result);
                    }
                });
                task.setContext(getContext());
                searchView.showProgress();
                task.execute(String.valueOf(camera.target.latitude),String.valueOf(camera.target.longitude));


            }
        });
    }

    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location!=null){
            double dLatitude = location.getLatitude();
            double dLongitude = location.getLongitude();
            Log.i("APPLICATION"," : "+dLatitude);
            Log.i("APPLICATION"," : "+dLongitude);
            mMap.addMarker(new MarkerOptions().position(
                    new LatLng(dLatitude, dLongitude)).alpha(0));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 18));

        }
        else
        {
            Toast.makeText(getContext(),"Please enable GPS",Toast.LENGTH_SHORT).show();
        }
    }


}
