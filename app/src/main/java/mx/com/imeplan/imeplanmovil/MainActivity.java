package mx.com.imeplan.imeplanmovil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import br.com.bloder.magic.view.MagicButton;

public class MainActivity extends AppCompatActivity{
    MagicButton mbtn01, mbtn02, mbtn03, mbtn04, mbtn05;
    Intent miIntent = null;
    int permissionCheckGPS;
    ConnectivityManager cm;
    NetworkInfo ni;
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
                miIntent = new Intent(MainActivity.this, PlanDeOrdenamientoActivity.class);
                startActivity(miIntent);
            }
        });
        //Boton Nuestros Proyectos
        mbtn02.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Revisar conexion a Internet
                cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                ni = cm.getActiveNetworkInfo();

                if (ni != null && ni.isConnected()) {
                    miIntent = new Intent(MainActivity.this, WebView_Imeplan.class);
                    miIntent.putExtra("id", 1);
                    startActivity(miIntent);
                }
                else
                    Toast.makeText(getApplicationContext(), "Sin conexión a Internet", Toast.LENGTH_LONG).show();
            }
        });
        //Boton Reporte ciudadano
        mbtn03.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miIntent = new Intent(MainActivity.this, ReporteCiudadano.class);
                startActivity(miIntent);
            }
        });
        //Boton Movilidad
        mbtn04.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Revisar conexion a Internet
                cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                ni = cm.getActiveNetworkInfo();

                if (ni != null && ni.isConnected()) {
                    miIntent = new Intent(MainActivity.this, WebView_Imeplan.class);
                    miIntent.putExtra("id", 2);
                    startActivity(miIntent);
                }
                else
                    Toast.makeText(getApplicationContext(), "Sin conexión a Internet", Toast.LENGTH_LONG).show();
            }
        });
        //Boton Calculadora Ambiental
        mbtn05.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Revisar conexion a Internet
                cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                ni = cm.getActiveNetworkInfo();

                if (ni != null && ni.isConnected()) {
                    miIntent = new Intent(MainActivity.this, WebView_Imeplan.class);
                    miIntent.putExtra("id", 3);
                    startActivity(miIntent);
                }
                else
                    Toast.makeText(getApplicationContext(), "Sin conexión a Internet", Toast.LENGTH_LONG).show();
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

