package mx.com.imeplan.imeplanmovil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.core.content.ContextCompat;

import android.Manifest.permission;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import static android.content.Context.LOCATION_SERVICE;


public class LocationHelper {

    Context context;
    LocationManager lm;
    LocationListener ll;
    int permissionGPS;
    boolean isGPSEnabled;
    boolean isNetworkEnabled;
    String locationProvider;
    Location l;
    final int GEOCODER_MAX_RESULTS = 2;
    TextView tvLocation;
    String municipio;
    LocationTask lt;

    /**
     *
     *
     */

    public LocationHelper(Context c, int minTime, int minDist, final TextView tvLocation) {
        context = c;
        this.tvLocation = tvLocation;
        lm = (LocationManager)context.getSystemService(LOCATION_SERVICE);
        //Seleccionar el proveedor de la ubicación dando prioridad al gps
        if (lm != null) {
            isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        else{
            Log.e("LocationManager", "null");
        }

        if (isNetworkEnabled) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (isGPSEnabled) {
            locationProvider = LocationManager.GPS_PROVIDER;
        }
        //Revisar que se haya seleccionado un proveedor de ubicación
        if (locationProvider != null && !locationProvider.isEmpty()) {
            permissionGPS = ContextCompat.checkSelfPermission(context, permission.ACCESS_FINE_LOCATION);
            if (permissionGPS == PackageManager.PERMISSION_GRANTED) {
                ll = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        l = location;
                        lt = new LocationTask(context, l, tvLocation, 1);
                        lt.execute();
                        Log.e("locationChanged", location.getLatitude() + ", " + location.getLongitude());
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    //Si se desactiva el proveedor seleccionado, cambiar al otro si está diaponible
                    @Override
                    public void onProviderDisabled(String provider) {
                        if (provider.equals(LocationManager.GPS_PROVIDER)
                                && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                            locationProvider = LocationManager.NETWORK_PROVIDER;
                        } else if (provider.equals(LocationManager.NETWORK_PROVIDER)
                                && lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            locationProvider = LocationManager.GPS_PROVIDER;
                        } else {
                            String mensaje = "GPS apagado. ¿Desea habilitarlo?";
                            String titulo = "Configuración del GPS";
                            String intentName = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
                            getSettingsDialog(mensaje, titulo, intentName).show();
                        }
                    }
                };
                lm.requestLocationUpdates(locationProvider, minTime, minDist, ll);
            }
        }
        //Sino, volver a revisar qué proveedor está disponible dando prioridad al gps
        else {
            if (lm != null) {
                isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                locationProvider = LocationManager.GPS_PROVIDER;
            } else if (isNetworkEnabled) {
                locationProvider = LocationManager.NETWORK_PROVIDER;
            }
        }

    }



    public AlertDialog getSettingsDialog(String message, String title, final String intentName){
        //Dialogo para activacion del GPS
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // Setting Dialog Title
        alertDialog.setTitle(title)
                // Setting Dialog Message
                .setMessage(message)
                // Accion de Aceptacion button
                .setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(intentName);
                        context.startActivity(intent);
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

    public double getLatitude(){
        return (l!=null) ? l.getLatitude(): 0.0;
    }

    public double getLongitud(){
        return (l!=null) ? l.getLongitude(): 0.0;
    }

    public String getMunicipio(){
        if(lt!=null){
            Log.e("null", "locationTask es null");
            return lt.getMunicipio();
        }else{
            return "";
        }
    }

}
