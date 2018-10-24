package mx.com.imeplan.imeplanmovil;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
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
    public final int MY_PERMISSION_REQUEST_GPS = 1;

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
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Decir porque estamos solicitando permisos
                    DialogoExplicacion();

                }
                else {
                    miIntent = new Intent(MainActivity.this, ReporteCiudadano.class);
                    startActivity(miIntent);
                }
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
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Decir porque estamos solicitando permisos
                DialogoExplicacion();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_REQUEST_GPS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case MY_PERMISSION_REQUEST_GPS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getApplicationContext(), "Permiso Denegado", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void DialogoExplicacion(){
        String msj = "Descripcion here..";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Por qué necesitamos acceder a tu Ubicación?")
               .setMessage(msj)
               .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_REQUEST_GPS);
                    }
                })
        .show();
    }
}

