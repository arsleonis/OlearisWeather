package com.zrz.android.olearisweather;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zrz.android.olearisweather.data.Contract;
import com.zrz.android.olearisweather.data.DBHelper;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, LocationListener, GoogleMap.OnMyLocationButtonClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private static double currentLatitude, currentLongitude;
    private Marker marker;
    public static final int MY_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button buttonOk = (Button) findViewById(R.id.button_map_ok);
        Button buttonCancel = (Button) findViewById(R.id.button_map_cancel);
        buttonOk.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        currentLatitude=0;
        currentLongitude=0;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(MapActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)==
                    PackageManager.PERMISSION_GRANTED){
                LocationManager locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
                Location firstLocation=null;
                if (locationManager != null) {
                    firstLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                if (firstLocation != null) {
                    currentLatitude=firstLocation.getLatitude();
                    currentLongitude=firstLocation.getLongitude();
                }
                else {
                    currentLatitude= 50.001840;
                    currentLongitude= 36.238040;
                }
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(this);
            }
            else {
                checkPermissions();
            }
        }
        else{
            LocationManager locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
            Location firstLocation=null;
            if (locationManager != null) {
                firstLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (firstLocation != null) {
                currentLatitude=firstLocation.getLatitude();
                currentLongitude=firstLocation.getLongitude();
            }
            else {
                currentLatitude= 50.001840;
                currentLongitude= 36.238040;
            }
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
        }
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        marker=mMap.addMarker(new MarkerOptions().position(latLng).title(getResources().getString(R.string.marker_start)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMapClickListener(this);
    }

    private void checkPermissions(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.permission_needed)
                    .setMessage(R.string.permission_needed_because)
                    .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MapActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton(R.string.button_chancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
        else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if(requestCode==MY_REQUEST_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.button_map_ok:
                if(checkForDoubling()){
                    dataEntry();
                    setResult(RESULT_OK);
                    finish();
                }
                else{
                    Toast.makeText(this, R.string.location_is_exist, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.button_map_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(marker!=null){
            marker.remove();
        }
        marker=mMap.addMarker(new MarkerOptions().position(latLng).title(getResources().getString(R.string.marker_selected)));
        currentLatitude=latLng.latitude;
        currentLongitude=latLng.longitude;
    }

    private boolean checkForDoubling(){
        boolean doubling=true;
        DBHelper dbHelper=new DBHelper(this);
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        Cursor cursor=database.query(Contract.PLACES_TABLE_NAME, null, null,null,
                null, null, null);
        if(cursor.moveToFirst()) {
            double lat, lng;
            int columnIndexLat = cursor.getColumnIndex(Contract.PLACES_TABLE_COLUMN_LATITUDE);
            int columnIndexLng = cursor.getColumnIndex(Contract.PLACES_TABLE_COLUMN_LONGITUDE);
            do {
                lat = cursor.getDouble(columnIndexLat);
                lng = cursor.getDouble(columnIndexLng);
                if(lat==currentLatitude&&lng==currentLongitude){
                    doubling=false;
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        dbHelper.close();
        return doubling;
    }

    private void dataEntry(){
        DBHelper dbHelper=new DBHelper(this);
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.PLACES_TABLE_COLUMN_LATITUDE, currentLatitude);
        contentValues.put(Contract.PLACES_TABLE_COLUMN_LONGITUDE, currentLongitude);
        database.insert(Contract.PLACES_TABLE_NAME, null, contentValues);
        dbHelper.close();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
