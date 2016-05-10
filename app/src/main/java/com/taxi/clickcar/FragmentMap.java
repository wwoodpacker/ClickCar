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
import android.view.KeyEvent;
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
import com.example.searchviewclickcar.suggestions.SearchSuggestionsAdapter;
import com.example.searchviewclickcar.suggestions.model.SearchSuggestion;
import com.example.searchviewclickcar.util.view.BodyTextView;
import com.example.searchviewclickcar.util.view.IconImageView;
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
import com.google.gson.Gson;
import com.taxi.clickcar.GeoData.GeoSuggestion;
import com.taxi.clickcar.GeoData.RootObject;
import com.taxi.clickcar.Order.Cost;
import com.taxi.clickcar.Order.Route;
import com.taxi.clickcar.R;
import com.taxi.clickcar.Tasks.GetGeoObjectTask;
import com.taxi.clickcar.Tasks.GetGeoObjectTask2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
    private GetGeoObjectTask2 task2;
    private Button btn_zakaz;
    private Gson gson;
    private ImageView btn_add1, btn_add2,btn_add3,btn_add4;
    private Calendar calendar;
    private boolean flag_add1 = false,flag_add2=false,flag_add3=false,flag_add4=false;
    private String provider;
    public View view;
    private LocationManager locationManager;
    TimePickerDialog.OnTimeSetListener callback;
    FloatingSearchView searchView1, searchView2,searchView3,searchView4;

    public FragmentMap() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_map, container, false);
        Log.e("FragmentMap", "onCreateView");
        flag_add1 = false;flag_add2=false;flag_add3=false;flag_add4=false;
        gson=new Gson();
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
                ArrayList<Route> routes = new ArrayList<Route>();
                String r1="",r2="",r3="",r4="";
                if(searchView1.getQuery().length()>0){
                    r1=searchView1.getQuery();
                    //route1
                    Route route = new Route();
                    route.setName(r1.substring(0,r1.lastIndexOf(" ")));
                    route.setNumber(r1.substring(r1.lastIndexOf(" "),r1.length()));
                    routes.add(route);

                }
                if(searchView2.getQuery().length()>0) {
                    r2 = searchView2.getQuery();
                    //route2
                    Route route2 = new Route();
                    route2.setName(r2.substring(0,r2.lastIndexOf(" ")));
                    route2.setNumber(r2.substring(r2.lastIndexOf(" "),r2.length()));
                    routes.add(route2);
                }
                if(searchView3.getQuery().length()>0){
                    r3=searchView3.getQuery();
                    //route3
                    Route route3 = new Route();
                    route3.setName(r3.substring(0,r3.lastIndexOf(" ")));
                    route3.setNumber(r3.substring(r3.lastIndexOf(" "),r3.length()));
                    routes.add(route3);
                }
                if(searchView4.getQuery().length()>0){
                    r4=searchView4.getQuery();
                    //route4
                    Route route4 = new Route();
                    route4.setName(r4.substring(0,r4.lastIndexOf(" ")));
                    route4.setNumber(r4.substring(r4.lastIndexOf(" "),r4.length()));
                    routes.add(route4);

                }

                //cost
                Cost cost=new Cost();
                cost.setUserFullName(ActivityDrawer.Name);
                cost.setUserPhone(ActivityDrawer.Phone);
                cost.setReservation(false);
                cost.setTaxiColumnId(0);
                cost.setRoute(routes);
               // container.removeAllViews();
                FragmentOrder fragmentOrder=new FragmentOrder();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack("back");
                fragmentTransaction.replace(R.id.container,fragmentOrder);
                fragmentTransaction.commit();

            }
        });
        searchView1.bringToFront();
        btn_add1.bringToFront();
        // searchView.br
        searchView1.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                GeoSuggestion geoSuggestion=(GeoSuggestion)searchSuggestion;
                searchView1.setSearchText(geoSuggestion.getBody());
            }

            @Override
            public void onSearchAction() {

            }
        });
        searchView2.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                GeoSuggestion geoSuggestion=(GeoSuggestion)searchSuggestion;
                searchView2.setSearchText(geoSuggestion.getBody());
            }

            @Override
            public void onSearchAction() {

            }
        });
        searchView3.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                GeoSuggestion geoSuggestion=(GeoSuggestion)searchSuggestion;
                searchView3.setSearchText(geoSuggestion.getBody());
            }

            @Override
            public void onSearchAction() {

            }
        });
        searchView4.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                GeoSuggestion geoSuggestion=(GeoSuggestion)searchSuggestion;
                searchView4.setSearchText(geoSuggestion.getBody());
            }

            @Override
            public void onSearchAction() {

            }
        });
        searchView1.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                Log.e("search1","querychange");
                searchView1.showProgress();
                task2=new GetGeoObjectTask2(new MyCallBack() {
                    @Override
                    public void OnTaskDone(String result) {

                        RootObject obj=gson.fromJson(result,RootObject.class);
                        List<GeoSuggestion> sug_list =new ArrayList<>();
                        for(int i=0;i<obj.getGeoStreets().getGeoStreet().size();i++){
                            for(int j=0;j<obj.getGeoStreets().getGeoStreet().get(i).getHouses().size();j++) {
                                sug_list.add(new GeoSuggestion(obj.getGeoStreets().getGeoStreet().get(i).getName()+"" +
                                        ""+obj.getGeoStreets().getGeoStreet().get(i).getHouses().get(j).getHouse()));
                            }
                        }
                        for(int i=0;i<obj.getGeoObjects().getGeoObject().size();i++){
                            sug_list.add(new GeoSuggestion(obj.getGeoObjects().getGeoObject().get(i).getName()));
                        }

                        searchView1.hideProgress();
                        searchView1.bringToFront();
                        searchView1.swapSuggestions(sug_list);

                        //searchView1.setSearchText(result);
                    }
                });
                task2.setContext(getContext());
                task2.execute(newQuery.replace(" ",""));
            }

        });
        searchView2.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                Log.e("search2","querychange");
                searchView2.showProgress();
                task2=new GetGeoObjectTask2(new MyCallBack() {
                    @Override
                    public void OnTaskDone(String result) {

                        RootObject obj=gson.fromJson(result,RootObject.class);
                        List<GeoSuggestion> sug_list =new ArrayList<>();
                        for(int i=0;i<obj.getGeoStreets().getGeoStreet().size();i++){
                            for(int j=0;j<obj.getGeoStreets().getGeoStreet().get(i).getHouses().size();j++) {
                                sug_list.add(new GeoSuggestion(obj.getGeoStreets().getGeoStreet().get(i).getName()+"" +
                                        ""+obj.getGeoStreets().getGeoStreet().get(i).getHouses().get(j).getHouse()));
                            }
                        }
                        for(int i=0;i<obj.getGeoObjects().getGeoObject().size();i++){
                            sug_list.add(new GeoSuggestion(obj.getGeoObjects().getGeoObject().get(i).getName()));
                        }

                        searchView2.hideProgress();
                        searchView2.bringToFront();
                        searchView2.swapSuggestions(sug_list);

                        //searchView1.setSearchText(result);
                    }
                });
                task2.setContext(getContext());
                task2.execute(newQuery.replace(" ",""));
            }
        });
        searchView3.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                Log.e("search3","querychange");
                searchView3.showProgress();
                task2=new GetGeoObjectTask2(new MyCallBack() {
                    @Override
                    public void OnTaskDone(String result) {

                        RootObject obj=gson.fromJson(result,RootObject.class);
                        List<GeoSuggestion> sug_list =new ArrayList<>();
                        for(int i=0;i<obj.getGeoStreets().getGeoStreet().size();i++){
                            for(int j=0;j<obj.getGeoStreets().getGeoStreet().get(i).getHouses().size();j++) {
                                sug_list.add(new GeoSuggestion(obj.getGeoStreets().getGeoStreet().get(i).getName()+"" +
                                        ""+obj.getGeoStreets().getGeoStreet().get(i).getHouses().get(j).getHouse()));
                            }
                        }
                        for(int i=0;i<obj.getGeoObjects().getGeoObject().size();i++){
                            sug_list.add(new GeoSuggestion(obj.getGeoObjects().getGeoObject().get(i).getName()));
                        }

                        searchView3.hideProgress();
                        searchView3.bringToFront();
                        searchView3.swapSuggestions(sug_list);

                        //searchView1.setSearchText(result);
                    }
                });
                task2.setContext(getContext());
                task2.execute(newQuery.replace(" ",""));
            }
        });
        searchView4.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                Log.e("search4","querychange");
                searchView4.showProgress();
                task2=new GetGeoObjectTask2(new MyCallBack() {
                    @Override
                    public void OnTaskDone(String result) {

                        RootObject obj=gson.fromJson(result,RootObject.class);
                        List<GeoSuggestion> sug_list =new ArrayList<>();
                        for(int i=0;i<obj.getGeoStreets().getGeoStreet().size();i++){
                            for(int j=0;j<obj.getGeoStreets().getGeoStreet().get(i).getHouses().size();j++) {
                                sug_list.add(new GeoSuggestion(obj.getGeoStreets().getGeoStreet().get(i).getName()+"" +
                                        ""+obj.getGeoStreets().getGeoStreet().get(i).getHouses().get(j).getHouse()));
                            }
                        }
                        for(int i=0;i<obj.getGeoObjects().getGeoObject().size();i++){
                            sug_list.add(new GeoSuggestion(obj.getGeoObjects().getGeoObject().get(i).getName()));
                        }

                        searchView4.hideProgress();
                        searchView4.bringToFront();
                        searchView4.swapSuggestions(sug_list);

                        //searchView1.setSearchText(result);
                    }
                });
                task2.setContext(getContext());
                task2.execute(newQuery.replace(" ",""));
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
