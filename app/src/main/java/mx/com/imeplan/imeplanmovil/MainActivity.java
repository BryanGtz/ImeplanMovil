package mx.com.imeplan.imeplanmovil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import br.com.bloder.magic.view.MagicButton;

public class MainActivity extends AppCompatActivity{
    MagicButton mbtn01, mbtn02, mbtn03, mbtn04, mbtn05;
    Intent miIntent = null;
    int permissionCheckGPS;
    //String latitud, longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Solicitar Permisos GPS
        permissionCheckGPS = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        solicitarGPS();

        mbtn01=(MagicButton)findViewById(R.id.magic_button01);
        mbtn02=(MagicButton)findViewById(R.id.magic_button02);
        mbtn03=(MagicButton)findViewById(R.id.magic_button03);
        mbtn04=(MagicButton)findViewById(R.id.magic_button04);
        mbtn05=(MagicButton)findViewById(R.id.magic_button05);

        //Boton Plan de terreno
        mbtn01.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miIntent = new Intent(MainActivity.this, WebView_Imeplan.class);
                miIntent.putExtra("id",1);
                startActivity(miIntent);
            }
        });
        //Boton Nuestros Proyectos
        mbtn02.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miIntent = new Intent(MainActivity.this, WebView_Imeplan.class);
                miIntent.putExtra("id",2);
                startActivity(miIntent);
            }
        });
        //Boton Reporte ciudadano
        mbtn03.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miIntent = new Intent(MainActivity.this, ReporteCiudadano.class);
                /*
                // Obtener la Ubicación
                LocationManager locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);

                // Define a listener that responds to location updates
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        latitud = String.valueOf(location.getLatitude());
                        longitud = String.valueOf(location.getLongitude());
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    public void onProviderEnabled(String provider) {}

                    public void onProviderDisabled(String provider) {}
                };

                // Register the listener with the Location Manager to receive location updates
                permissionCheckGPS = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                miIntent.putExtra("latitud", latitud);
                miIntent.putExtra("longitud", longitud);
                */
                startActivity(miIntent);
            }
        });
        //Boton Movilidad
        mbtn04.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miIntent = new Intent(MainActivity.this, WebView_Imeplan.class);
                miIntent.putExtra("id",3);
                startActivity(miIntent);
            }
        });
        //Boton Calculadora Ambiental
        mbtn05.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miIntent = new Intent(MainActivity.this, WebView_Imeplan.class);
                miIntent.putExtra("id",4);
                startActivity(miIntent);
            }
        });
    }

    private void solicitarGPS() {
        if (permissionCheckGPS != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Decir porque estamos solicitando permisos

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        }
    }
}

