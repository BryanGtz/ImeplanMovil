package mx.com.imeplan.imeplanmovil;

import android.Manifest;
import android.support.v7.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
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
    public final int MY_GPS_ENABLEMENT_REQUEST = 2;
    private  String PREFS_KEY = "mispreferencias";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Mostrar OnBoarding activity 1 vez
        boolean muestra= getValuePreference(getApplicationContext());
        if(muestra){
            Intent intent = new Intent(MainActivity.this, OnBoarding.class);
            startActivity(intent);
            saveValuePreference(getApplicationContext(), false);
        }
        // Solicitar Permisos GPS
        permissionCheckGPS = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = false;
        if (lm != null) {
            isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        if(isGPSEnabled){
            solicitarGPS();
        }
        else{
            String mensaje = "GPS apagado. ¿Desea habilitarlo?";
            String titulo = "Configuración del GPS";
            String intentName = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
            getSettingsDialog(mensaje,titulo,intentName).show();
        }

        init();

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
                miIntent = new Intent(MainActivity.this, SitiosInteres.class);
                startActivity(miIntent);
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
                    miIntent.putExtra("id", 10);
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
                    //miIntent = new Intent(MainActivity.this, WebView_Imeplan.class);
                    //miIntent.putExtra("id", 2);
                    miIntent = new Intent(MainActivity.this, RutasActivity.class);
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

    private void init() {
        mbtn01 = findViewById(R.id.magic_button01);
        mbtn02 = findViewById(R.id.magic_button02);
        mbtn03 = findViewById(R.id.magic_button03);
        mbtn04 = findViewById(R.id.magic_button04);
        mbtn05 = findViewById(R.id.magic_button05);
    }

    public AlertDialog getSettingsDialog(String message, String title, final String intentName){
        //Dialogo para activacion del GPS
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Title
        alertDialog.setTitle(title)
        // Setting Dialog Message
        .setMessage(message)
        // Accion de Aceptacion button
        .setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(intentName);
                startActivityForResult(intent,MY_GPS_ENABLEMENT_REQUEST);
            }
        });
        // Acción de cancelacion
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return alertDialog.create();
    }

    /**
     * Método para solicitar permisos del gps al usuario
     */
    private void solicitarGPS() {
        if (permissionCheckGPS != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Han sido negados los permisos anteriormente
                // (Aparece la opcion de no volver a mostrar)
                // TODO: Decir porqué estamos solicitando permisos
                // TODO: Tomar accion si se vuelven a negar los permisos
                DialogoExplicacion();

            } else {
                //Es la primer vez que se están solicitando los permisos
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_REQUEST_GPS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_GPS) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permiso Denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionCheckGPS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheckGPS != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

        }
        else{
            Toast.makeText(getApplicationContext(), "Necesario activar GPS", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void DialogoExplicacion(){
        String msj = "Para enviar los reportes necesitamos conocer su ubicación para ";
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

    public void saveValuePreference(Context context, Boolean mostrar){
        SharedPreferences settings= context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor= settings.edit();
        editor.putBoolean("license", mostrar);
        editor.apply();
    }
    public boolean getValuePreference(Context context){
        SharedPreferences sp= context.getSharedPreferences(PREFS_KEY,MODE_PRIVATE);
        return sp.getBoolean("license",true);
    }
}

