package mx.com.imeplan.imeplanmovil;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Address;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import mx.com.imeplan.imeplanmovil.utilidades.Utilidades;

public class InfoReporteActivity extends AppCompatActivity {
    TextView identificador,categoria,subcategoria,fecha,direccion;
    Button estado;
    ImageView foto;
    int ident, edo;
    String cat, sc,fech, dir, ph;
    String [] info = new String[9];
    Bundle infoRep;
    ConnectivityManager cm;
    NetworkInfo ni;
    SQLiteOpenHelper conn;
    Intent miIntent;
    boolean isInternet;
    LocationTask lt;
    String municipio;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_reporte);

        conn = new ConexionSQLiteHelper(this);
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        ni = cm.getActiveNetworkInfo();
        isInternet = ni != null && ni.isConnected();
        identificador = (TextView) findViewById(R.id.info_id);
        categoria = (TextView) findViewById(R.id.info_cat);
        subcategoria = (TextView) findViewById(R.id.info_sc);
        foto = (ImageView) findViewById(R.id.info_foto);
        direccion = (TextView) findViewById(R.id.info_dir);
        fecha = (TextView) findViewById(R.id.info_fecha);
        estado = (Button) findViewById(R.id.info_edo);

        infoRep = getIntent().getExtras();
        for (int i = 0; i<infoRep.getStringArray("array").length; i++){
            info[i] = infoRep.getStringArray("array")[i];
        }

        ident = Integer.parseInt(info[0]);
        cat = info[1];
        sc = info[2];
        //dir = info[3] + ", " + info[4];
        if(info[8].equals("")){
            Location l = new Location("");
            Log.e("coordinates",info[3]+", "+info[4]);
            l.setLatitude(Double.parseDouble(info[3]));
            l.setLongitude(Double.parseDouble(info[4]));
            lt = new LocationTask(getBaseContext(),l,direccion,2);
            lt.execute();

            if(!isInternet){
                dir = "";
                direccion.setText("");
            }
            else{
                List<Address> address = lt.getAddress();
                if(address!=null&&address.size()>0){
                    dir = address.get(0).getAddressLine(0);
                }
                else{
                    dir = "";
                }
            }
            Log.e("dir",dir);
        }
        else{
            dir = info[8];
        }
        ph = info[5];
        fech = info[6];
        edo = Integer.parseInt(info[7]);

        insertarInfo();

        if(edo == 0)
            estado.setVisibility(View.VISIBLE);

        estado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ni != null && ni.isConnected()){
                    try {
                        municipio = lt.get()[0];
                        municipio = lt.getMunicipio();
                        Log.w("mun",municipio);
                    } catch (InterruptedException e) {
                        Log.e("ie",e.toString());
                    } catch (ExecutionException e) {
                        Log.e("ee",e.toString());
                    }
                    if(isInternet){
                        if(municipio == null){
                            Toast.makeText(getApplicationContext(), "Fuera de la zona conurbada", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!municipio.equals("Tampico") && !municipio.equals("Ciudad Madero") && !municipio.equals("Altamira") && !municipio.equals("Miramar")) {
                            Toast.makeText(getApplicationContext(), "Fuera de la zona conurbada", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //Toast.makeText(getContext(), "Reporte Enviado", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getContext(), ""+municipio+"", Toast.LENGTH_SHORT).show();
                    }
                    sendEmail(info);
                    updateEstado();
                    miIntent = new Intent(InfoReporteActivity.this, ReporteCiudadano.class);
                    miIntent.putExtra("id", "11");
                    startActivity(miIntent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Sin conexión a Internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateEstado() {
        SQLiteDatabase db = conn.getWritableDatabase();

        String update = "update "+Utilidades.TABLA_REPORTE+" set "+Utilidades.R_CAMPO_ESTADO+" = 1, " +
                Utilidades.R_CAMPO_DIRECCION+" = '"+((dir.equals("")) ? lt.getAddress().get(0).getAddressLine(0): dir)+
                "' where "+Utilidades.R_CAMPO_ID+" = "+ident;
        db.execSQL(update);
        db.close();
    }

    protected void sendEmail(String [] datos) {

        String mail="", dependencia = "IMEPLAN", cargo = "Dirección de Medio Ambiente";
        switch(municipio) {
            case "Tampico":
                switch (datos[1]) {
                    case "COMAPA":
                        mail = "gildardo.ponce@tam.gob.mx";//adolfo.cabal@tam.gob.mx
                        dependencia = "COMAPA";
                        cargo = "Atención Ciudadana";
                        break;
                    case "Servicios públicos":
                        mail = "gildardo.ponce@tam.gob.mx";//servpublicos@tampico.gob.mx
                        break;
                    case "Cuadrilla ecológica":
                        mail = "gildardo.ponce@tam.gob.mx";//secretariaecologiatampico@hotmail.com
                        dependencia = "IMEPLAN";
                        cargo = "Dirección de Medio Ambiente";
                        break;
                    case "Obras públicas":
                        mail = "gildardo.ponce@tam.gob.mx";//Vacio
                        break;
                    case "Vialidad":
                        mail = "gildardo.ponce@tam.gob.mx";//Vacio
                        break;
                    case "Otros":
                        mail = "gildardo.ponce@tam.gob.mx";//Vacio
                        dependencia = "IMEPLAN";
                        cargo = "Jefatura de Informática";
                        break;
                }
                break;
            case "Ciudad Madero":
                switch (datos[1]) {
                    case "COMAPA":
                        mail = "gildardo.ponce@tam.gob.mx";//adolfo.cabal@tam.gob.mx
                        break;
                    case "Servicios públicos":
                        mail = "gildardo.ponce@tam.gob.mx";//any335@hotmail.com
                        break;
                    case "Cuadrilla ecológica":
                        mail = "gildardo.ponce@tam.gob.mx";//direccionecologia@ciudadmadero.gob.mx
                        break;
                    case "Obras públicas":
                        mail = "gildardo.ponce@tam.gob.mx";//Vacio
                        break;
                    case "Vialidad":
                        mail = "gildardo.ponce@tam.gob.mx";
                        break;
                    case "Otros":
                        mail = "gildardo.ponce@tam.gob.mx";//bryan.gtz.317@gmail.com
                        break;
                }
                break;
            case "Miramar":
            case "Altamira":
                switch (datos[1]) {
                    case "COMAPA":
                        mail = "gildardo.ponce@tam.gob.mx";//monje@comapaaltamira.gob.mx
                        break;
                    case "Servicios públicos":
                        mail = "gildardo.ponce@tam.gob.mx";//secretariasp.altamira@gmail.com
                        break;
                    case "Cuadrilla ecológica":
                        mail = "gildardo.ponce@tam.gob.mx";//illescasfrancisco@hotmail.com
                        break;
                    case "Obras públicas":
                        mail = "gildardo.ponce@tam.gob.mx";//Vacio
                        break;
                    case "Vialidad":
                        mail = "gildardo.ponce@tam.gob.mx";//Vacio
                        break;
                    case "Otros":
                        mail = "gildardo.ponce@tam.gob.mx";//Vacio
                        break;
                }
                break;
            default:
                Toast.makeText(getApplicationContext(), "Fuera de la zona conurbada", Toast.LENGTH_LONG).show();
                return;
        }

        //mail = "enoc.9714@gmail.com";

        String asunto = "Reporte ciudadano";
        String mensaje = cargo + "\n" + dependencia + "\n";
        mensaje += "Por medio de la presente se notifica sobre el siguiente reporte ciudadano:\n";
        mensaje += "Categoría: " + cat + "\nSubcategoría: " + sc +
                "\nCon ubicacion en: " + ((dir.equals("")) ? lt.getAddress().get(0).getAddressLine(0): dir) + "\n" + datos[2] + "," + datos[3] +
                "\nFecha y hora: " + datos[4] + "\n";
        mensaje += "Sin mas por el momento, agradeceríamos la pronta resolución\n";
        mensaje += "Atentamente:\nCiudadanos";
        GMailSender sender = new GMailSender(getApplicationContext());
        sender.enviarEmail(mail, asunto, mensaje);
        sender.adjuntarArchivo(ph);
        Toast.makeText(getApplicationContext(), "Reporte enviado exitosamente", Toast.LENGTH_LONG).show();

    }

    private void insertarInfo() {
        String folio = "<b>Folio: </b>"+String.valueOf(ident);
        String category = "<b>Categoría: </b>"+cat;
        String subcategory = "<b>Subcategoría:</b> "+sc;
        String address = "<b>Dirección: </b>"+dir;
        String date = "<b>Fecha: </b>"+fech;
        identificador.setText(Html.fromHtml(folio));
        categoria.setText(Html.fromHtml(category));
        subcategoria.setText(Html.fromHtml(subcategory));
        direccion.setText(Html.fromHtml(address));
        fecha.setText(Html.fromHtml(date));
        insertarFoto();
    }

    private void insertarFoto() {
        File imgFile = new File(ph);
        if(imgFile.exists()){
            Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            foto.setImageBitmap(bmp);
        } else
            Toast.makeText(getApplicationContext(), "La imagen no existe", Toast.LENGTH_LONG).show();
    }
}

