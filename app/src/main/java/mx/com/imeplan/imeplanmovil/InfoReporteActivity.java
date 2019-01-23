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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Properties;

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
    String [] info = new String[8];
    Bundle infoRep;
    ConnectivityManager cm;
    NetworkInfo ni;
    SQLiteOpenHelper conn;
    Intent miIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_reporte);

        conn = new ConexionSQLiteHelper(this, "bd_imeplanMovil.db", null, 1);

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
        dir = info[3] + ", " + info[4];
        ph = info[5];
        fech = info[6];
        edo = Integer.parseInt(info[7]);

        insertarInfo();

        if(edo == 0)
            estado.setVisibility(View.VISIBLE);

        estado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                ni = cm.getActiveNetworkInfo();

                if(ni != null && ni.isConnected()){
                    sendEmail(info);
                    updateEstado();
                    miIntent = new Intent(InfoReporteActivity.this, ReporteCiudadano.class);
                    miIntent.putExtra("id", "11");
                    startActivity(miIntent);
                }else{
                    Toast.makeText(getApplicationContext(), "Sin conexión a Internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateEstado() {
        SQLiteDatabase db = conn.getWritableDatabase();

        String update = "update "+Utilidades.TABLA_REPORTE+" set "+Utilidades.R_CAMPO_ESTADO+" = 1 where "+Utilidades.R_CAMPO_ID+" = "+ident;
        db.execSQL(update);
        db.close();
    }

    protected void sendEmail(String [] datos) {

        String mun = datos[2].split(" ")[1];
        String user= "";
        switch(mun) {
            case "Tampico":
                switch (datos[1]) {
                    case "COMAPA":
                        user = "adolfo.cabal@tam.gob.mx";
                        break;
                    case "Servicios públicos":
                        user = "servpublicos@tampico.gob.mx";
                        break;
                    case "Cuadrilla ecológica":
                        user = "secretariaecologiatampico@hotmail.com";
                        break;
                    case "Obras públicas":
                        user = "";
                        break;
                    case "Vialidad":
                        user = "";
                        break;
                    case "Otros":
                        user = "";
                        break;
                }
                break;
            case "Madero":
                switch (datos[1]) {
                    case "COMAPA":
                        user = "adolfo.cabal@tam.gob.mx";
                        break;
                    case "Servicios públicos":
                        user = "any335@hotmail.com";
                        break;
                    case "Cuadrilla ecológica":
                        user = "direccionecologia@ciudadmadero.gob.mx";
                        break;
                    case "Obras públicas":
                        user = "";
                        break;
                    case "Vialidad":
                        user = "";
                        break;
                    case "Otros":
                        user = "";
                        break;
                }
                break;
            case "Altamira":
                switch (datos[1]) {
                    case "COMAPA":
                        user = "monje@comapaaltamira.gob.mx";
                        break;
                    case "Servicios públicos":
                        user = "secretariasp.altamira@gmail.com";
                        break;
                    case "Cuadrilla ecológica":
                        user = "illescasfrancisco@hotmail.com";
                        break;
                    case "Obras públicas":
                        user = "";
                        break;
                    case "Vialidad":
                        user = "";
                        break;
                    case "Otros":
                        user = "";
                        break;
                }
                break;
            case "Miramar":
                switch (datos[1]) {
                    case "COMAPA":
                        user = "monje@comapaaltamira.gob.mx";
                        break;
                    case "Servicios públicos":
                        user = "secretariasp.altamira@gmail.com";
                        break;
                    case "Cuadrilla ecológica":
                        user = "illescasfrancisco@hotmail.com";
                        break;
                    case "Obras públicas":
                        user = "";
                        break;
                    case "Vialidad":
                        user = "";
                        break;
                    case "Otros":
                        user = "enoc.9714@gmail.com";
                        break;
                }
                break;
            default:
                Toast.makeText(getApplicationContext(), "Fuera de la zona conurbada", Toast.LENGTH_LONG).show();
                return;
        }
        /*try {*/
        String asunto = "Reporte ciudadano";
        String mensaje = "Ing. Gildardo\nJefe de Medio Ambiente\n";
        mensaje += "Por medio de la presente se notifica sobre el siguiente reporte ciudadano\n";
        mensaje += datos[1] + " " + datos[2] + ". Con ubicacion en: " + datos[4] + "\n Fecha y hora: " + datos[6] + "\n";
        mensaje += "Sin mas por el momento, agradeceriamos la pronta resolucion\n";
        mensaje += "Atentamente:\nCiudadanos";
        GMailSender sender = new GMailSender(getApplicationContext());
        sender.enviarEmail(user, asunto, mensaje);
        sender.adjuntarArchivo(ph);
        Toast.makeText(getApplicationContext(), "Reporte enviado exitosamente", Toast.LENGTH_LONG).show();
        /*} catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }*/
        /*String host="smtp.gmail.com";
        final String from="bryan.gtz.317@gmail.com";//change accordingly
        final String password="br31y07an97";//change accordingly

        String to="bryan.gtz.317@gmail.com";//change accordingly

        String sub = "Reporte dirigido hacia "+datos[1];
        String msg = "Subcategoría: "+datos[0]+"\n" +
                "Latitud: "+datos[2]+"\n"+
                "Longitud: "+datos[3]+"\n"+
                "Fecha: "+datos[4];

        //Get the session object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from,password);
                    }
                });
        //compose message
        try {
            final MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject(sub);
            message.setText(msg);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Toast.makeText(getContext(), "Reporte enviado exitosamente", Toast.LENGTH_LONG).show();
        } catch (MessagingException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }*/

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

