package mx.com.imeplan.imeplanmovil;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class RutasActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latitud;
    private double longitud;
    int permissionCheckGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Obtener la Ubicación
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        // Register the listener with the Location Manager to receive location updates
        permissionCheckGPS = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 5, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Thread t = new Thread(new MyRunnable(location));
                t.run();
                t.interrupt();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}

            class MyRunnable implements Runnable {
                Location l;
                public MyRunnable(Location location){
                    l = location;
                }
                @Override
                public void run() {
                    latitud =l.getLatitude();
                    longitud =l.getLongitude();
                }
            }
        });
        /*CameraPosition camPos = new CameraPosition.Builder()
                .target(new LatLng(latitud,longitud))//Centramos el mapa en Madrid
                .zoom(18) //Establecemos el zoom en 19
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(camUpd3);*/
        Log.e("Lat: ",String.valueOf(latitud));
        Log.e("Long: ",String.valueOf(longitud));
        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(22.29085, -97.865193),13);
        mMap.moveCamera(camUpd1);
        mMap.addMarker(new MarkerOptions().position(new LatLng(22.29085, -97.865193)));



/*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
*/
        /*
        try {

            KmlLayer kmlLayer = new KmlLayer(mMap, R.raw.r10, getApplicationContext());
            kmlLayer.addLayerToMap();
            KmlLayer kmlLayer2 = new KmlLayer(mMap, R.raw.r2, getApplicationContext());
            kmlLayer2.addLayerToMap();
            KmlLayer kmlLayer3 = new KmlLayer(mMap, R.raw.r3, getApplicationContext());
            kmlLayer3.addLayerToMap();
            KmlLayer kmlLayer4 = new KmlLayer(mMap, R.raw.r4, getApplicationContext());
            kmlLayer4.addLayerToMap();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        */
    }
}