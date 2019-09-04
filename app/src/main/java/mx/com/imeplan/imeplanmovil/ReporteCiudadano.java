package mx.com.imeplan.imeplanmovil;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class ReporteCiudadano extends AppCompatActivity {

    FragmentTransaction ft;
    final NuevoReporteActivity nuevo_reporte = new NuevoReporteActivity();
    final MisReportesActivity mis_reportes = new MisReportesActivity();
    final MisBorradoresActivity mis_borradores = new MisBorradoresActivity();
    int permissionCheckCAMERA;
    public final int MY_PERMISSION_REQUEST_CAMERA = 1;
    int type;
    Bitmap bmp;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_ciudadano);

        // Solicitar permisos Camara
        solicitarCAMARA();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        type = getIntent().getExtras().getInt("id");
        img = (ImageView)findViewById(R.id.imageView);

        ft = getSupportFragmentManager().beginTransaction();
        if(type == 10) {
            ft.replace(R.id.contenedor, nuevo_reporte);
            ft.commit();
        } else{
            ft.replace(R.id.contenedor, mis_reportes);
            ft.commit();
            navigation.getMenu().getItem(1).setChecked(true);
            Toast.makeText(getApplicationContext(), "Correo enviado exitosamente", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSION_REQUEST_CAMERA){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(getApplicationContext(), "Permiso Asignado", Toast.LENGTH_SHORT).show();
                //else
                //    Toast.makeText(getApplicationContext(), "Permiso Denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void DialogoExplicacion(){
        String msj = "Porque necesitamos estar seguros que el reporte que mandas existe; la forma en la que lo" +
                "corroboramos es observando la foto que tomas.";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Por qué necesitamos acceder a tu Cámara?")
                .setMessage(msj)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(ReporteCiudadano.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSION_REQUEST_CAMERA);
                    }
                })
                .show();
    }

    private void solicitarCAMARA() {
        permissionCheckCAMERA = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (permissionCheckCAMERA != PackageManager.PERMISSION_GRANTED) {
            /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
            }*/

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                // Decir porque estamos solicitando permisos
                DialogoExplicacion();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSION_REQUEST_CAMERA);
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
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
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.contenedor, mis_borradores);
                    ft.commit();
                    return true;
            }
            return false;
        }
    };

}
