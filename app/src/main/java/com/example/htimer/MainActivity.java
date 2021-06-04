package com.example.htimer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude", 0);

        TextView location2 = findViewById(R.id.location);
        location2.setText("위도 = " + latitude + ", 경도 = " + longitude);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //36.668233, 127.482371 우리집
        //37.485235, 127.064041 수도공고
        //36.628915, 127.456340 충북대학교
        double startLatitude = 36.628915; // 여기엔 DB에 저장된 값을 불러와야 함
        double startLongitude = 127.456340; // 여기엔 DB에 저장된 값을 불러와야 함
        Intent intent = getIntent();
        double endLatitude = intent.getDoubleExtra("latitude", 0); // 실시간으로 가져오기
        double endLongitude = intent.getDoubleExtra("longitude", 0); // 실시간으로 가져오기
        LatLng latLng = new LatLng(startLatitude, startLongitude);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startLatitude, startLongitude), 15));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("충북대학교");
        googleMap.addMarker(markerOptions);

        ////////////////cal distance

        float[] results = new float[1];
        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);
        float distance = results[0]; // 이 결과를 이용해서 바운더리 내에 들어오면 알람을 줘야 함!!!!!!
        int kilometer = (int) (distance/1000);
        Toast.makeText(this, String.valueOf(kilometer)+"km", Toast.LENGTH_SHORT).show();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}

