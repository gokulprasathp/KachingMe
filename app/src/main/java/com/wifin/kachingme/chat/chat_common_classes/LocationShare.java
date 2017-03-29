package com.wifin.kachingme.chat.chat_common_classes;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat.chat_adaptors.LocationAdapter;
import com.wifin.kachingme.pojo.Location_getset;

import com.wifin.kachingme.util.DelayAutoCompleteTextView;
import com.wifin.kachingme.adaptors.GeoAutoCompleteAdapter;
import com.wifin.kachingme.util.GeoSearchResult;

import com.wifin.kachingme.util.GPSTracking;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.PlaceJSONParser;
import com.wifin.kachingme.util.Constant;

public class LocationShare extends ActionBarActivity implements
        LocationListener, ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public GoogleMap map;

    public static FragmentManager fragmentManager;

    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    ArrayList<Location_getset> list_places = new ArrayList<Location_getset>();
    LocationAdapter adapter;
    boolean isGPSEnabled = false;
    // if Network is enabled
    boolean isNetworkEnabled = false;
    ListView listview;
    View frag;
    ArrayList<MarkerOptions> marker_list = new ArrayList<MarkerOptions>();
    Intent intentMessage = new Intent();
    Button btn_current_location, btn_select;
    static String TAG = LocationShare.class.getSimpleName();
    ProgressDialog progressDialog;
    GoogleApiClient mGoogleApiClient;
    SharedPreferences sharedPrefs;
    private Integer THRESHOLD = 2;
    private DelayAutoCompleteTextView geo_autocomplete;
    private ImageView geo_autocomplete_clear;
    LatLng selected_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.location_share);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        listview = (ListView) findViewById(R.id.list_places);
        btn_current_location = (Button) findViewById(R.id.btn_mylocation);
        fragmentManager = getSupportFragmentManager();
        btn_select = (Button) findViewById(R.id.btn_find);

        frag = (View) findViewById(R.id.map);
        map = ((SupportMapFragment) fragmentManager.findFragmentById(R.id.map))
                .getMap();

        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0, this);
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        buildGoogleApiClient();
        // Getting Current Location
        Location location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location != null) {
            Constant.printMsg("location   " + location.getLatitude() + "   "
                    + location.getLongitude());
            onLocationChanged(location);
        }

        locationManager.requestLocationUpdates(provider, 20000, 0, this);

		/* View localView = ((Activity)this).getWindow().getDecorView(); */
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Log.d("Screen", "Screen Height::" + height);
        int i = 40 * (height) / 100;
        int j = 50 * (height) / 100;

        Log.d("Screen", "Map Height::" + i);
        Log.d("Screen", "List Height::" + j);

        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, i);
        LayoutParams lp_list = new LayoutParams(LayoutParams.FILL_PARENT, j);
        frag.setLayoutParams(lp);

		/*
		 * locationManager = (LocationManager)
		 * getSystemService(Context.LOCATION_SERVICE);
		 * locationManager.requestLocationUpdates
		 * (LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
		 */

        Constant.printMsg("list size ::::>>> 2222" + list_places.size()
                + "   " + list_places);
        adapter = new LocationAdapter(this, R.layout.item_places, list_places);
        listview.setAdapter(adapter);
        listview.setLayoutParams(lp_list);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        btn_select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                // latLongTV.setText(result.getLatLng());
                if (selected_location != null) {
                    double lat = selected_location.latitude;
                    double lng = selected_location.longitude;

                    intentMessage.putExtra("lat", "" + lat);
                    intentMessage.putExtra("lon", "" + lng);
                    intentMessage.putExtra("place",
                            KachingMeApplication.getNifty_name());

                    // map.snapshot(callback);
                    new MyAsync_screenshot().execute();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please search location first", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
        btn_current_location.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                map.clear();

                // Creating a criteria object to retrieve provider
                Criteria criteria = new Criteria();

                // Getting the name of the best provider
                String provider = locationManager.getBestProvider(criteria,
                        true);

                // Getting Current Location
                Location location = locationManager
                        .getLastKnownLocation(provider);

                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                GPSTracking mGPSService = new GPSTracking(LocationShare.this);

                Constant.printMsg("loaction::" + location);
                if (isGPSEnabled || isNetworkEnabled) {

                    mGPSService.getLocation();

                    Constant.printMsg("loaction::1"
                            + mGPSService.getLatitude() + "  "
                            + mGPSService.getLongitude());

                    if (mGPSService.getLatitude() != 0
                            && mGPSService.getLongitude() != 0) {

                        double latitude = mGPSService.getLatitude();

                        double longitude = mGPSService.getLongitude();

                        // Creating a LatLng object for the current location
                        LatLng latLng = new LatLng(latitude, longitude);

                        LatLng myPosition = new LatLng(latitude, longitude);

                        MarkerOptions marker = new MarkerOptions();
                        marker.position(myPosition);

                        map.addMarker(marker);

                        intentMessage.putExtra("lat", ""
                                + marker.getPosition().latitude);
                        intentMessage.putExtra("lon", ""
                                + marker.getPosition().longitude);
                        intentMessage.putExtra("place",
                                KachingMeApplication.getNifty_name());

                        // map.snapshot(callback);
                        new MyAsync_screenshot().execute();

                    }

                    mGPSService.stopUsingGPS();

                } else {

                    Toast.makeText(getApplicationContext(),
                            "Location Service is not active.",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
        // map = ((SupportMapFragment)findViewById(R.id.map)).getMap();
        // map = ((SupportMapFragment) getSupportFragmentManager()
        // .findFragmentById(R.id.map)).getMap();
        //
        // Constant.printMsg("latlng   " + location.getLatitude() + "  "
        // + location.getLongitude());
        // LatLng myPosition = new LatLng(location.getLatitude(),
        // location.getLongitude());
        //
        // MarkerOptions marker = new MarkerOptions();
        // marker.position(myPosition);
        //
        // map.addMarker(marker);

        // if (map != null) {
        // GPSTracker mGPSService = new GPSTracker(location_share.this);
        //
        // if (isGPSEnabled || isNetworkEnabled) {
        //
        // mGPSService.getLocation();
        //
        // Constant.printMsg("loaction::1" + mGPSService.getLatitude()
        // + "  " + mGPSService.getLongitude());
        //
        // if (mGPSService.getLatitude() != 0
        // && mGPSService.getLongitude() != 0) {
        //
        // double latitude = mGPSService.getLatitude();
        //
        // double longitude = mGPSService.getLongitude();
        //
        // // Creating a LatLng object for the current location
        // LatLng latLng = new LatLng(latitude, longitude);
        //
        // LatLng myPosition = new LatLng(latitude, longitude);
        //
        // MarkerOptions marker = new MarkerOptions();
        // marker.position(myPosition);
        //
        // map.addMarker(marker);
        //
        // intentMessage.putExtra("lat", ""
        // + marker.getPosition().latitude);
        // intentMessage.putExtra("lon", ""
        // + marker.getPosition().longitude);
        // intentMessage.putExtra("place",
        // KachingMeApplication.getNifty_name());
        //
        // // map.snapshot(callback);
        // new MyAsync_screenshot().execute();
        //
        // }
        //
        // mGPSService.stopUsingGPS();
        //
        // }
        // }

        map.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {

                ArrayList<Location_getset> list_new = new ArrayList<Location_getset>();
                LatLng loc = marker.getPosition();
                map.clear();
                for (int i = 0; i < list_places.size(); i++) {
                    Location_getset loc_getset = list_places.get(i);
                    MarkerOptions mo = new MarkerOptions();
                    LatLng lo;
                    if (marker.getTitle().equals(loc_getset.getName())) {
                        loc_getset.setIs_selected(true);
                        mo.icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.location_red));
                    } else {
                        loc_getset.setIs_selected(false);
                        mo.icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.location_green));
                    }

                    lo = new LatLng(loc_getset.getLat(), loc_getset.getLon());

                    mo.title(loc_getset.getName());
                    mo.position(lo);
                    list_new.add(loc_getset);
                    map.addMarker(mo);
                }

                list_places.clear();
                list_places = list_new;

                Constant.printMsg("list size ::::>>>344 " + list_new.size()
                        + "   " + list_new);

                adapter = new LocationAdapter(LocationShare.this,
                        R.layout.item_places, list_new);
                listview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                map.clear();
                LatLng latLng = new LatLng(list_places.get(position).getLat(),
                        list_places.get(position).getLon());

                MarkerOptions marker = new MarkerOptions();
                marker.position(latLng);

                map.addMarker(marker);

                intentMessage.putExtra("lat", ""
                        + marker.getPosition().latitude);
                intentMessage.putExtra("lon", ""
                        + marker.getPosition().longitude);
                intentMessage.putExtra("place", list_places.get(position)
                        .getName());

                new MyAsync_screenshot().execute();

                Constant.bux = sharedPrefs.getLong("buxvalue", 0);

                Long buxval = Constant.bux + Constant.locationpoints;
                Constant.bux = buxval;

                Editor e = sharedPrefs.edit();
                e.putLong("buxvalue", buxval);
                e.commit();

            }
        });

        // check if GPS enabled
        GPSTracking gpsTracker = new GPSTracking(this);

        if (!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
        }
        geo_autocomplete_clear = (ImageView) findViewById(R.id.geo_autocomplete_clear);
        geo_autocomplete = (DelayAutoCompleteTextView) findViewById(R.id.geo_autocomplete);
        geo_autocomplete.setThreshold(THRESHOLD);
        geo_autocomplete.setAdapter(new GeoAutoCompleteAdapter(this)); // 'this'
        // is
        // Activity
        // instance

        geo_autocomplete
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, int position, long id) {
                        GeoSearchResult result = (GeoSearchResult) adapterView
                                .getItemAtPosition(position);

                        geo_autocomplete.setText(result.getAddress());

                        // latLongTV.setText(result.getLatLng());

                        map.clear();

                        LatLng latLng = result.getLatLng();
                        selected_location = latLng;
                        Constant.printMsg("latlong:" + latLng);

                        final CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng) // Sets the center of the map
                                        // to Mountain View
                                .zoom(13) // Sets the zoom
                                .bearing(90) // Sets the orientation of the
                                        // camera to east
                                .tilt(30) // Sets the tilt of the camera to 30
                                        // degrees
                                .build();

                        MarkerOptions marker = new MarkerOptions();
                        marker.position(latLng);

                        map.addMarker(marker);

                        map.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));

                    }
                });
        geo_autocomplete.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        geo_autocomplete_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                geo_autocomplete.setText("");

            }
        });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                        // .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    class MyAsync_screenshot extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // //ACRA.getErrorReporter().handleException(e);
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            map.snapshot(callback);

            return null;
        }

    }

    SnapshotReadyCallback callback = new SnapshotReadyCallback() {
        Bitmap bitmap;

        @Override
        public void onSnapshotReady(Bitmap snapshot) {
            // TODO Auto-generated method stub
            bitmap = snapshot;

            ByteArrayOutputStream outstream_thumb = new ByteArrayOutputStream();
            try {

                int quality = 100;
                bitmap.compress(CompressFormat.JPEG, quality, outstream_thumb);

                while ((outstream_thumb.size() / 1024) > 10) {
                    outstream_thumb = new ByteArrayOutputStream();
                    bitmap.compress(CompressFormat.JPEG, quality,
                            outstream_thumb);
                    quality = quality - 5;
                    // Log.d(TAG,"Map Image Size::"+(outstream_thumb.size())/1024);

                    // Log.d(TAG,"Map Image Size::"+(outstream_thumb.size())/1024);

                    Log.d(TAG, "Map Image Size::" + (outstream_thumb.size())
                            / 1024);

                    Constant.printMsg("thumb value..........."
                            + outstream_thumb.size());

                    intentMessage.putExtra("map_thumb",
                            outstream_thumb.toByteArray());

                    // Constant.printMsg("thumb value to byte array" +
                    // outstream_thumb.toByteArray());

                    // Set The Result in Intents

                    setResult(RESULT_OK, intentMessage);
                    // finish The activity



                    Log.d("Screenshoot", "Screenshot has been taken");

                }

            } catch (Exception e) {// //ACRA.getErrorReporter().handleException(e);
                Log.w("TAG", "Error saving image file: " + e.getMessage());

            }finally {

                try {
                    outstream_thumb.close();

                }catch (Exception e){

                }

                progressDialog.dismiss();

                finish();
            }

            // Log.d(TAG, "Map Image Size::" + (outstream_thumb.size()) / 1024);
            //
            // intentMessage.putExtra("map_thumb",
            // outstream_thumb.toByteArray());
            //
            // // Set The Result in Intents
            // Constant.printMsg("thum     " +
            // outstream_thumb.toByteArray());
            // setResult(RESULT_OK, intentMessage);
            // // finish The activity
            // finish();
            //
            // Log.d("Screenshoot", "Screenshot has been taken");
        }
    };

    @Override
    public void onLocationChanged(Location location) {
        Constant.printMsg("called location");
        LatLng latLng = new LatLng(location.getLatitude(),
                location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,
                15);
        map.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);

        String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/"
                + "json?location="
                + location.getLatitude()
                + ","
                + location.getLongitude()
                + "&radius=500&sensor=true"
                + "&types=food|bar|shopping_mall|hospital|restaurant|atm|bank|gas_station"
                + "&key=AIzaSyA2CR5o6W_D9eZM3140a3_BL9E1WL_vH6g";
		/* "&key=AIzaSyAze44pM-kMBviP4jRwAf2uiKOPDHSZslc"; */
        Log.d("result Share Location", "URL::" + placesSearchStr);
        new MyAsync().execute(placesSearchStr);
    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    public class MyAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(params[0]);
                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();
                // Connecting to url
                urlConnection.connect();
                // Reading data from url
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        iStream));

                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();

                br.close();

            } catch (Exception e) {// //ACRA.getErrorReporter().handleException(e);
                Log.d("Exception while downloading url", e.toString());
            } finally {
                try {
                    iStream.close();
                } catch (IOException e) {// //ACRA.getErrorReporter().handleException(e);
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                urlConnection.disconnect();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask();
            Constant.printMsg("result  " + result);
            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }

    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends
            AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(
                String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {// //ACRA.getErrorReporter().handleException(e);
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            // Clears all the existing markers
            map.clear();
            marker_list.clear();
            list_places.clone();

            Constant.printMsg("mutiple " + list.size());
            for (int i = 0; i < list.size(); i++) {
                Location_getset loc = new Location_getset();
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);

                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));
                String icon = hmPlace.get("icon");
                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("place_name");

                // Getting vicinity
                String vicinity = hmPlace.get("vicinity");

                Constant.printMsg("vicinity::" + vicinity);

                LatLng latLng = new LatLng(lat, lng);

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(name);
                markerOptions.icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.location_green));
                // Placing a marker on the touched position
                map.addMarker(markerOptions);

                loc.setIs_selected(false);
                loc.setLat(lat);
                loc.setLon(lng);
                loc.setName(name);
                loc.setVicinity(vicinity);
                loc.setIcon_path(icon);
                list_places.add(loc);
                marker_list.add(markerOptions);

            }

            Constant.printMsg("list size ::::>>> 1111" + list_places.size()
                    + "   " + list_places);

            adapter = new LocationAdapter(LocationShare.this,
                    R.layout.item_places, list_places);
            listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub

        Location location = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if (location != null) {
            onLocationChanged(location);
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

}
