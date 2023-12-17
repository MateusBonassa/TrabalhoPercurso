package com.example.appnavigationdrawer;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public class MinhaLocalizacaoListener implements LocationListener{
    public static double latitude;
    public static double longitude;
    public static double altitude;

    public static Marker pontoAtual;

    private static TextView tvAltitude,tvLatitude,tvLongitude;



    public static void vincularCords(View view)
    {

        tvAltitude = view.findViewById(R.id.tvAltitude);
        tvLatitude = view.findViewById(R.id.tvLatitude);
        tvLongitude = view.findViewById(R.id.tvLongitude);
    }
    @Override
    public void onLocationChanged(Location location) {
        this.latitude  = location.getLatitude();
        this.longitude = location.getLongitude();
        this.altitude = location.getAltitude();
        if(pontoAtual!=null){
            pontoAtual.setPosition(new GeoPoint(latitude,longitude));
            MapaFragment.mapController.setCenter(pontoAtual.getPosition());
            tvAltitude.setText(this.altitude+"");
            tvLatitude.setText(this.latitude+"");
            tvLongitude.setText(this.longitude+"");
            }
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
