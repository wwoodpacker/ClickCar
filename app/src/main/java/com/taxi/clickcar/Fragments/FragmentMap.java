package com.taxi.clickcar.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.pickerclickcar.time.TimePickerDialog;
import com.example.searchviewclickcar.FloatingSearchView;
import com.example.searchviewclickcar.suggestions.model.SearchSuggestion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.taxi.clickcar.GeoData.GeoSuggestion;
import com.taxi.clickcar.GeoData.RootObject;
import com.taxi.clickcar.GlobalVariables;
import com.taxi.clickcar.MyCallBack;
import com.taxi.clickcar.Order.Cost;
import com.taxi.clickcar.Order.Route;
import com.taxi.clickcar.R;
import com.taxi.clickcar.StaticMethods;
import com.taxi.clickcar.Tasks.GetGeoObjectTask;
import com.taxi.clickcar.Tasks.GetGeoObjectTask2;
import com.taxi.clickcar.Tasks.GetGeoStreets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Назар on 26.03.2016.
 */
public class FragmentMap extends Fragment implements OnMapReadyCallback {
    private static final int PERMISSION_LOCATION_REQUEST_CODE = 1;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOCATION = "location";
    public static final String APP_PREFERENCES_PUSH = "PUSH";
    public static final String APP_PREFERENCES_SMS = "SHOWSMS";
    public SharedPreferences mSettings;
    private static final int REQUEST_LOCATION = 2;
    private GoogleMap mMap;
    private GetGeoObjectTask task;
    private GetGeoObjectTask2 task2;
    private GetGeoStreets getGeoStreets;
    private Button btn_zakaz;
    private Gson gson;
    private ImageView btn_add;
    private Calendar calendar;
    private boolean flag_add1 = false, flag_add2 = false, flag_add3 = false, flag_add4 = false, flag_add = false;
    private String provider;
    private int count_click = 1;
    public ArrayList<Route> routes;
    public View view;
    private LocationManager locationManager;
    TimePickerDialog.OnTimeSetListener callback;
    private EditText house1, house2, house3, house4;
    FloatingSearchView searchView1, searchView2, searchView3, searchView4;
    public boolean loc = false, push = false, shsms = false;

    public FragmentMap() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onResume() {
        Log.e("FragmentMap", "onresume");
        super.onResume();

    }

    @Override
    public void onStop() {
        if(locationManager!=null) locationManager.removeUpdates(locationListener);
        super.onStop();
    }

    @Override
    public void onPause() {
        //nullpointer
        if(locationManager!=null) locationManager.removeUpdates(locationListener);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.e("FragmentMap", "onDestroy");

        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("FragmentMap", "onCreate");
        super.onCreate(savedInstanceState);
    }
    public View.OnClickListener mOnClickListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, null);
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, getActivity().MODE_PRIVATE);

        Log.e("FragmentMap", "onCreateView");
        flag_add=false;
        count_click=1;
        flag_add1 = false;
        flag_add2 = false;
        flag_add3 = false;
        flag_add4 = false;
        gson = new Gson();
        searchView1 = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        searchView2 = (FloatingSearchView) view.findViewById(R.id.floating_search_view2);
        searchView3 = (FloatingSearchView) view.findViewById(R.id.floating_search_view3);
        searchView4 = (FloatingSearchView) view.findViewById(R.id.floating_search_view4);
        routes = new ArrayList<Route>();
        house1=(EditText)view.findViewById(R.id.edit_house1);
        house2=(EditText)view.findViewById(R.id.edit_house2);
        house3=(EditText)view.findViewById(R.id.edit_house3);
        house4=(EditText)view.findViewById(R.id.edit_house4);
        btn_add =(ImageView)view.findViewById(R.id.btn_add_adress);

        calendar = Calendar.getInstance();

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_zakaz = (Button) view.findViewById(R.id.btn_zakaz);
        btn_zakaz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))){
                routes.clear();
                String r1 = "", r2 = "", r3 = "", r4 = "";

                if (searchView1.getQuery().length() > 1) {
                    r1 = searchView1.getQuery();
                    //route1
                    Route route = new Route();
                    route.setName(r1.trim());
                    route.setNumber(house1.getText().toString().trim());
                    routes.add(route);

                }
                if (searchView2.getQuery().length() > 1) {
                    r2 = searchView2.getQuery();
                    //route2
                    Route route2 = new Route();
                    route2.setName(r2.trim());
                    route2.setNumber(house2.getText().toString().trim());
                    routes.add(route2);
                }
                if (searchView3.getQuery().length() > 1) {
                    r3 = searchView3.getQuery();
                    //route3
                    Route route3 = new Route();
                    route3.setName(r3.trim());
                    route3.setNumber(house3.getText().toString().trim());
                    routes.add(route3);
                }
                if (searchView4.getQuery().length() > 1) {
                    r4 = searchView4.getQuery();
                    //route4
                    Route route4 = new Route();
                    route4.setName(r4.trim());
                    route4.setNumber(house4.getText().toString().trim());
                    routes.add(route4);

                }

                //cost
                Cost cost = new Cost();
                cost.setUserFullName(GlobalVariables.getInstance().getName());
                cost.setUserPhone(GlobalVariables.getInstance().getPhone());
                cost.setReservation(false);
                cost.setTaxiColumnId(0);
                if (routes.size() <= 1) cost.setRouteUndefined(true);
                cost.setRoute(routes);
                    if (routes.size() != 0) {
                        Gson gson = new Gson();
                        String jsonCost = gson.toJson(cost);

                        Bundle bundle = new Bundle();
                        bundle.putString("COSTJSON", jsonCost);
                        bundle.putBoolean("HISTORY", false);
                        Log.e("From map to order", jsonCost);
                        FragmentOrder fragmentOrder = new FragmentOrder();
                        fragmentOrder.setArguments(bundle);

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        fragmentTransaction.replace(R.id.container, fragmentOrder);

                        fragmentTransaction.commit();
                    }else Toast.makeText(getContext(),"Введите маршрут",Toast.LENGTH_SHORT).show();
            }
            }
        });
        searchView1.bringToFront();

        searchView1.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                if (StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))){
                GeoSuggestion geoSuggestion = (GeoSuggestion) searchSuggestion;
                searchView1.setSearchText(geoSuggestion.getBody());}
            }

            @Override
            public void onSearchAction() {

            }
        });
        searchView2.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                if (StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))){
                GeoSuggestion geoSuggestion = (GeoSuggestion) searchSuggestion;
                searchView2.setSearchText(geoSuggestion.getBody());}
            }

            @Override
            public void onSearchAction() {

            }
        });
        searchView3.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                if (StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))){
                GeoSuggestion geoSuggestion = (GeoSuggestion) searchSuggestion;
                searchView3.setSearchText(geoSuggestion.getBody());}
            }

            @Override
            public void onSearchAction() {

            }
        });
        searchView4.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                if (StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))){
                GeoSuggestion geoSuggestion = (GeoSuggestion) searchSuggestion;
                searchView4.setSearchText(geoSuggestion.getBody());}
            }

            @Override
            public void onSearchAction() {

            }
        });
        searchView1.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                Log.e("search1", "querychange");
                searchView1.showProgress();
                if (StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))) {
                    task2 = new GetGeoObjectTask2(new MyCallBack() {
                        @Override
                        public void OnTaskDone(String result) {

                            RootObject obj = gson.fromJson(result, RootObject.class);
                            List<GeoSuggestion> sug_list = new ArrayList<>();
                            for (int i = 0; i < obj.getGeoStreets().getGeoStreet().size(); i++) {
                                sug_list.add(new GeoSuggestion(obj.getGeoStreets().getGeoStreet().get(i).getName()));

                            }
                            for (int i = 0; i < obj.getGeoObjects().getGeoObject().size(); i++) {
                                sug_list.add(new GeoSuggestion(obj.getGeoObjects().getGeoObject().get(i).getName()));
                            }

                            searchView1.hideProgress();
                            searchView1.bringToFront();
                            searchView1.swapSuggestions(sug_list);


                        }
                    });
                    task2.setContext(getContext());
                    task2.execute(newQuery.replace(" ", ""));
                }else  searchView1.hideProgress();
            }
        });
        searchView2.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                Log.e("search2", "querychange");
                searchView2.showProgress();
                if (StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))){
                task2 = new GetGeoObjectTask2(new MyCallBack() {
                    @Override
                    public void OnTaskDone(String result) {

                        RootObject obj = gson.fromJson(result, RootObject.class);
                        List<GeoSuggestion> sug_list = new ArrayList<>();
                        for (int i = 0; i < obj.getGeoStreets().getGeoStreet().size(); i++) {
                            sug_list.add(new GeoSuggestion(obj.getGeoStreets().getGeoStreet().get(i).getName()));

                        }
                        for (int i = 0; i < obj.getGeoObjects().getGeoObject().size(); i++) {
                            sug_list.add(new GeoSuggestion(obj.getGeoObjects().getGeoObject().get(i).getName()));
                        }

                        searchView2.hideProgress();
                        searchView2.bringToFront();
                        searchView2.swapSuggestions(sug_list);


                    }
                });
                task2.setContext(getContext());
                task2.execute(newQuery.replace(" ", ""));
            }

            else searchView2.hideProgress();
        }
        });
        searchView3.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                Log.e("search3", "querychange");
                searchView3.showProgress();
                if(StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))){
                task2 = new GetGeoObjectTask2(new MyCallBack() {
                    @Override
                    public void OnTaskDone(String result) {

                        RootObject obj = gson.fromJson(result, RootObject.class);
                        List<GeoSuggestion> sug_list = new ArrayList<>();
                        for (int i = 0; i < obj.getGeoStreets().getGeoStreet().size(); i++) {
                            sug_list.add(new GeoSuggestion(obj.getGeoStreets().getGeoStreet().get(i).getName()));

                        }
                        for (int i = 0; i < obj.getGeoObjects().getGeoObject().size(); i++) {
                            sug_list.add(new GeoSuggestion(obj.getGeoObjects().getGeoObject().get(i).getName()));
                        }

                        searchView3.hideProgress();
                        searchView3.bringToFront();
                        searchView3.swapSuggestions(sug_list);


                    }
                });
                task2.setContext(getContext());
                task2.execute(newQuery.replace(" ", ""));
                }else  searchView3.hideProgress();
            }
        });
        searchView4.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                Log.e("search4", "querychange");
                searchView4.showProgress();
                if (StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))){
                task2 = new GetGeoObjectTask2(new MyCallBack() {
                    @Override
                    public void OnTaskDone(String result) {

                        RootObject obj = gson.fromJson(result, RootObject.class);
                        List<GeoSuggestion> sug_list = new ArrayList<>();
                        for (int i = 0; i < obj.getGeoStreets().getGeoStreet().size(); i++) {
                            sug_list.add(new GeoSuggestion(obj.getGeoStreets().getGeoStreet().get(i).getName()));

                        }
                        for (int i = 0; i < obj.getGeoObjects().getGeoObject().size(); i++) {
                            sug_list.add(new GeoSuggestion(obj.getGeoObjects().getGeoObject().get(i).getName()));
                        }

                        searchView4.hideProgress();
                        searchView4.bringToFront();
                        searchView4.swapSuggestions(sug_list);


                    }
                });
                task2.setContext(getContext());
                task2.execute(newQuery.replace(" ", ""));
                }else  searchView4.hideProgress();
            }
        });
        searchView1.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                flag_add1 = false;
                flag_add2 = false;
                flag_add3 = false;
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
                flag_add1 = true;
                flag_add2 = false;
                flag_add3 = false;
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
                flag_add1 = false;
                flag_add2 = true;
                flag_add3 = false;
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
                flag_add1 = false;
                flag_add2 = false;
                flag_add3 = true;
                Log.d("search", "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                Log.d("search", "onFocusCleared()");
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flag_add) count_click++; else count_click--;
                if (count_click==1){
                    flag_add=false;
                    flag_add1=false;
                    searchView2.setVisibility(View.GONE);
                    house2.setVisibility(View.GONE);
                    house2.setText("");
                    searchView2.clearSearchFocus();
                }else
                if (count_click==2){
                    flag_add1=true;
                    flag_add2=false;
                    searchView2.setVisibility(View.VISIBLE);
                    searchView2.bringToFront();
                    house2.setVisibility(View.VISIBLE);
                    house2.bringToFront();
                    searchView3.setVisibility(View.GONE);
                    searchView3.clearSearchFocus();
                    house3.setVisibility(View.GONE);
                    house3.setText("");
                }else
                if(count_click==3){
                    flag_add2=true;
                    flag_add3=false;
                    flag_add1=false;
                    searchView3.setVisibility(View.VISIBLE);
                    searchView3.bringToFront();
                    house3.setVisibility(View.VISIBLE);
                    house3.bringToFront();
                    searchView4.setVisibility(View.GONE);
                    searchView4.clearSearchFocus();
                    house4.setVisibility(View.GONE);
                    house4.setText("");
                }else
                if(count_click==4){
                    flag_add=true;
                    flag_add3=true;
                    flag_add2=false;
                    searchView4.setVisibility(View.VISIBLE);
                    searchView4.bringToFront();
                    house4.setVisibility(View.VISIBLE);
                    house4.bringToFront();
                }
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
                if (StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))) {
                    task = new GetGeoObjectTask(new MyCallBack() {
                        @Override
                        public void OnTaskDone(String result) {
                            int pos;

                            if (flag_add1) {
                                searchView2.hideProgress();
                                pos = 0;
                                pos = findFirstDigit(result);
                                searchView2.setSearchText(result.substring(0, pos).trim());
                                house2.setText(result.substring(pos, result.length()).trim());
                                Log.e("Json Camera1", result);
                            } else if (flag_add2) {
                                searchView3.hideProgress();
                                pos = 0;
                                pos = findFirstDigit(result);
                                searchView3.setSearchText(result.substring(0, pos).trim());
                                house3.setText(result.substring(pos, result.length()).trim());
                                Log.e("Json Camera2", result);
                            } else if (flag_add3) {
                                searchView4.hideProgress();
                                pos = 0;
                                pos = findFirstDigit(result);
                                searchView4.setSearchText(result.substring(0, pos).trim());
                                house4.setText(result.substring(pos, result.length()).trim());
                                Log.e("Json Camera3", result);
                            } else {
                                searchView1.hideProgress();
                                pos = 0;
                                pos = findFirstDigit(result);
                                searchView1.setSearchText(result.substring(0, pos).trim());
                                house1.setText(result.substring(pos, result.length()).trim());
                                Log.e("Json Camera4", result);
                            }
                        }
                    });
                    task.setContext(getContext());
                    if (flag_add1) {
                        searchView2.showProgress();
                    } else if (flag_add2) {
                        searchView3.showProgress();
                    } else if (flag_add3) {
                        searchView4.showProgress();
                    } else {
                        searchView1.showProgress();
                    }
                    task.execute(String.valueOf(camera.target.latitude), String.valueOf(camera.target.longitude));

                }
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
            Toast.makeText(getContext(),getString(R.string.enable_gps), Toast.LENGTH_SHORT).show();
        }
    };
    public void saveSettings(boolean loc,boolean push,boolean shsms){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_LOCATION, loc);
        editor.putBoolean(APP_PREFERENCES_PUSH, push);
        editor.putBoolean(APP_PREFERENCES_SMS, shsms);
        editor.apply();
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
    private void getCurrentLocation() {
        if (StaticMethods.CheckConnection(getActivity(),mOnClickListener,getString(R.string.error_connection))) {
            locationManager = (LocationManager)
                    getActivity().getSystemService(Context.LOCATION_SERVICE);

            boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!enabled) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            } else {
                Criteria criteria = new Criteria();
                provider = locationManager.getBestProvider(criteria, false);
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION);
                } else {
                    saveSettings(true, push, shsms);
                    Location location = locationManager.getLastKnownLocation(provider);
                    locationManager.requestLocationUpdates(provider, 0,
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
                        saveSettings(false, push, shsms);
                        Toast.makeText(getContext(), getString(R.string.enable_gps), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }
    public int findFirstDigit(String s){
        char[] crs = s.toCharArray();
        int pos=0;
        for (int i = 0; i < crs.length; i++) {
            if (Character.isDigit(crs[i])) {
                pos=i;
                break;
            }
        }
        return pos;
    }
}
