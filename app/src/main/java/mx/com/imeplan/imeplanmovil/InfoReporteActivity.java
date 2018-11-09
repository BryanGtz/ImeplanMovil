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
    String cat, sc,fech, dir;
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

                if(ni != null && ni.isConnected() && sendEmail(info)){
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

    protected boolean sendEmail(String [] datos) {
        String host="smtp.gmail.com";
        final String from="bryan.gtz.317@gmail.com";//change accordingly
        final String password="br31y07an97";//change accordingly

        String to="shanock.charras@gmail.com";//change accordingly

        String sub = "Reporte dirigido hacia "+datos[1];
        String msg = "Subcategoría: "+datos[2]+"\n" +
                "Latitud: "+datos[3]+"\n"+
                "Longitud: "+datos[4]+"\n"+
                "Fecha: "+datos[6];

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
            return true;
        } catch (MessagingException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
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
    }
}

