package mx.com.imeplan.imeplanmovil;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class ReporteCiudadano extends AppCompatActivity implements NuevoReporteActivity.OnFragmentInteractionListener, MisReportesActivity.OnFragmentInteractionListener{
    private TextView mTextMessage;
    FragmentTransaction ft;
    final NuevoReporteActivity nuevo_reporte = new NuevoReporteActivity();
    final MisReportesActivity mis_reportes = new MisReportesActivity();
    //Bundle args = new Bundle();
    //String latitud, longitud;
    int permissionCheckCAMERA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_ciudadano);

        // Solicitar permisos Camara
        permissionCheckCAMERA = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        solicitarCAMARA();

        // Recibir y enviar latitud y longitud
        /*latitud = getIntent().getStringExtra("latitud");
        longitud = getIntent().getStringExtra("longitud");
        args.putString("latitud", latitud);
        args.putString("longitud", longitud);
        nuevo_reporte.setArguments(args);*/

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contenedor, nuevo_reporte);
        ft.commit();
    }

    private void solicitarCAMARA() {
        if (permissionCheckCAMERA != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Decir porque estamos solicitando permisos

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        1);
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //nuevo_reporte.setArguments(args);
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.contenedor, nuevo_reporte);
                    ft.commit();
                    return true;
                case R.id.navigation_reports:
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.contenedor, mis_reportes);
                    ft.commit();
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
