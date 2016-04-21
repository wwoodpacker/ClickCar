package com.taxi.clickcar;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pickerclickcar.time.RadialPickerLayout;
import com.example.pickerclickcar.time.TimePickerDialog;
import com.example.searchviewclickcar.FloatingSearchView;
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
    private static final int PERMISSION_LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private GetGeoObjectTask task;
    private Button btn_zakaz;
    private ImageView btn_add1, btn_add2,btn_add3,btn_add4;
    private Calendar calendar;
    private boolean flag_add1 = false,flag_add2=false,flag_add3=false,flag_add4=false;
    private String provider;
    private LocationManager locationManager;
    TimePickerDialog.OnTimeSetListener callback;
    FloatingSearchView searchView1, searchView2,searchView3,searchView4;

    public FragmentMap() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Log.e("FragmentMap", "onCreateView");
        flag_add1 = false;flag_add2=false;flag_add3=false;flag_add4=false;
        searchView1 = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        searchView2 = (FloatingSearchView) view.findViewById(R.id.floating_search_view2);
        searchView3 = (FloatingSearchView) view.findViewById(R.id.floating_search_view3);
        searchView4 = (FloatingSearchView) view.findViewById(R.id.floating_search_view4);
        btn_add1 = (ImageView) view.findViewById(R.id.imageView9);
        btn_add2 = (ImageView) view.findViewById(R.id.imageView10);
        btn_add3 = (ImageView) view.findViewById(R.id.imageView11);
        btn_add4 = (ImageView) view.findViewById(R.id.imageView12);
        calendar = Calendar.getInstance();
        //search.setDrawerLogo();
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_zakaz = (Button) view.findViewById(R.id.btn_zakaz);
        btn_zakaz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callback = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

                    }
                };
                TimePickerDialog.newInstance(callback, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
            }
        });
        searchView1.bringToFront();
        btn_add1.bringToFront();
        // searchView.br
        searchView1.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {

                searchView1.showProgress();
            }

        });
        searchView2.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                searchView2.showProgress();
            }
        });
        searchView3.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                searchView3.showProgress();
            }
        });
        searchView4.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                searchView4.showProgress();
            }
        });
        searchView1.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                flag_add1=false;
                flag_add2=false;
                flag_add3=false;
                Log.d("search", "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                Log.d("search", "onFocusCleared()");
            }
        });
        searchView2.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                flag_add1=true;
                flag_add2=false;
                flag_add3=false;
                Log.d("search", "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                Log.d("search", "onFocusCleared()");
            }
        });
        searchView3.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                flag_add1=false;
                flag_add2=true;
                flag_add3=false;
                Log.d("search", "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                Log.d("search", "onFocusCleared()");
            }
        });
        searchView4.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                flag_add1=false;
                flag_add2=false;
                flag_add3=true;
                Log.d("search", "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                Log.d("search", "onFocusCleared()");
            }
        });
        btn_add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_add1.setVisibility(View.GONE);
                btn_add2.bringToFront();
                btn_add2.setVisibility(View.VISIBLE);
                searchView2.setVisibility(View.VISIBLE);
                searchView2.bringToFront();
                flag_add1 = true;
            }
        });
        btn_add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_add4){
                    searchView2.setVisibility(View.GONE);
                    searchView2.clearSearchFocus();
                    btn_add2.setVisibility(View.GONE);
                    btn_add1.setVisibility(View.VISIBLE);
                    btn_add1.bringToFront();
                    flag_add4=false;
                    flag_add1=false;
                    flag_add2=false;
                    flag_add3=false;
                }else {
                btn_add2.setVisibility(View.GONE);
                btn_add3.bringToFront();
                btn_add3.setVisibility(View.VISIBLE);
                searchView3.setVisibility(View.VISIBLE);
                searchView3.bringToFront();
                flag_add2=true;
                flag_add3=false;
                flag_add1=false;
                }
            }
        });
        btn_add3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_add4){
                    searchView3.setVisibility(View.GONE);
                    searchView3.clearSearchFocus();
                    btn_add3.setVisibility(View.GONE);
                    btn_add2.setVisibility(View.VISIBLE);
                    flag_add1=true;
                    flag_add2=false;
                    flag_add3=false;

                }else {
                btn_add3.setVisibility(View.GONE);
                btn_add3.bringToFront();
                btn_add4.setVisibility(View.VISIBLE);
                searchView4.setVisibility(View.VISIBLE);
                searchView4.bringToFront();
                flag_add3 = true;
                flag_add1 = false;
                flag_add2 = false;
                }
            }
        });
        btn_add4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_add3.setVisibility(View.VISIBLE);
                btn_add3.bringToFront();
                searchView3.setVisibility(View.VISIBLE);
                searchView3.bringToFront();
                searchView4.clearSearchFocus();
                btn_add4.setVisibility(View.GONE);
                searchView4.setVisibility(View.GONE);
                flag_add2=true;
                flag_add1 = false;
                flag_add3 = false;
                flag_add4= true;
            }
        });
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getCurrentLocation();
        init();
    }

    private void init() {
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition camera) {
                Log.d("FragmentMap", "onCameraChange: " + camera.target.latitude + "," + camera.target.longitude);
                String otvet = "";

                task = new GetGeoObjectTask(new MyCallBack() {
                    @Override
                    public void OnTaskDone(String result) {
                        if (flag_add1) {
                            searchView2.hideProgress();
                            searchView2.setSearchText(result);

                        } else
                        if(flag_add2){
                            searchView3.hideProgress();
                            searchView3.setSearchText(result);
                            Log.e("Json Camera", result);
                        }else
                            if(flag_add3){
                                searchView4.hideProgress();
                                searchView4.setSearchText(result);
                                Log.e("Json Camera", result);
                            }
                        else
                            {
                                searchView1.hideProgress();
                                searchView1.setSearchText(result);
                                Log.e("Json Camera", result);
                            }
                    }
                });
                task.setContext(getContext());
                if (flag_add1) {
                    searchView2.showProgress();
                } else
                if(flag_add2) {
                    searchView3.showProgress();
                }else
                if(flag_add3){
                    searchView4.showProgress();
                }else{
                    searchView1.showProgress();
                }
                task.execute(String.valueOf(camera.target.latitude), String.valueOf(camera.target.longitude));


            }
        });
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


}
