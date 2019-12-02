package com.example.shoppy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Mashup extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    double latitude = 45;
    double longitude = 45;
    boolean tester = false;
    ProgressBar progressBar;
    Map<Marker, Bitmap> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mashup);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        markers = new HashMap<Marker, Bitmap>();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new Yourcustominfowindowadpater());
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    tester = true;
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            tester = true;
        }
    };

    public void locate(View view)
    {
        getLastLocation();
        LatLng pos = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromResource(R.drawable.buttonmap)).title("Your Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 16), 3000, null);
        new RetrieveFeedTask().execute();
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... urls) {


            try {
                URL url = new URL("https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=034bdf1edc4d8019c13486b8e7571495&tags=food&lat=" + latitude + "&lon=" + longitude + "&radius=5&extras=geo%2Curl_s&format=json&nojsoncallback=1"
                );
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", response);

            try {
                JSONObject object = new JSONObject(response);
                JSONObject outerPhotos = object.getJSONObject("photos");
                JSONArray photos = outerPhotos.getJSONArray("photo");

                for (int i = 0; i < 50; i ++)
                {
                    JSONObject first = photos.getJSONObject(i);
                    new RetrievePhoto(first.getString("url_s"), first.getDouble("latitude"), first.getDouble("longitude")).execute();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    class RetrievePhoto extends AsyncTask<String, Void, Bitmap> {

        private Exception exception;
        private String URLY;
        private double lat;
        private double lon;
        Bitmap picture = null;

        public RetrievePhoto(String url, double lati, double longi) {
            this.URLY = url;
            this.lat = lati;
            this.lon = longi;
        }

        protected Bitmap doInBackground(String... urls) {

            try {
                //Picasso.get().load(URLY).into(img);
                InputStream in = new java.net.URL(URLY).openStream();
                picture = BitmapFactory.decodeStream(in);
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
            }
            return picture;
        }

        protected void onPostExecute(Bitmap response) {
            progressBar.setVisibility(View.GONE);
            LatLng actualPos = new LatLng(lat, lon);
            Marker m = mMap.addMarker(new MarkerOptions().position(actualPos).title("Second Marker!"));
            markers.put(m, response);

            // Do this later? Use a hashmap with a marker? Create a new marker here, then add it?
            // So, create marker, add to map, put in hashmap <maker, bitmap>, then access bitmap in adapter?
        }
    }

    class Yourcustominfowindowadpater implements GoogleMap.InfoWindowAdapter {
        private final View mymarkerview;

        Yourcustominfowindowadpater() {
            mymarkerview = getLayoutInflater()
                    .inflate(R.layout.custominfowindow, null);
        }

        public View getInfoWindow(Marker marker) {
            render(marker, mymarkerview);
            return mymarkerview;
        }

        public View getInfoContents(Marker marker) {
            return null;
        }

        private void render(Marker marker, View view) {

            Bitmap img = markers.get(marker);
            ((ImageView) view.findViewById(R.id.foodimg)).setImageBitmap(img);
        }
    }

}
