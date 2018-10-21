package mx.com.imeplan.imeplanmovil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class InfoReporteActivity extends AppCompatActivity {
    TextView identificador,categoria,subcategoria,fecha,direccion;
    Button estado;
    ImageView foto;
    int ident, edo;
    String cat, sc,fech, dir;
    String [] info = new String[8];
    Bundle infoRep;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_reporte);

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

        estado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edo == 0)
                    Toast.makeText(getApplicationContext(), "Pendiente", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Atendido", Toast.LENGTH_LONG).show();
            }
        });
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

