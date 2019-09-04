package mx.com.imeplan.imeplanmovil;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PlanDeOrdenamientoActivity extends AppCompatActivity {
    DownloadManager dManager;
    Uri uri;
    ListView estados;
    String [] edos = {"Tampico [2018]","Madero","Altamira"};
    String [] rutas = {"http://po.tamaulipas.gob.mx/wp-content/uploads/2018/05/cxliii-61-220518F-ANEXO.pdf",
            "http://po.tamaulipas.gob.mx/wp-content/uploads/2016/09/cxli-Ext.No_.4-260916F-ANEXO-1.pdf",
            "http://po.tamaulipas.gob.mx/wp-content/uploads/2016/09/cxli-117-290916F-ANEXO.pdf"};

    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_pot);

        estados = (ListView) findViewById(R.id.estados);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,edos);
        estados.setAdapter(adapter);

        estados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialogoDatos(view, i);
            }
        });
    }

    public void dialogoDatos(View v, int pos){
        String mensaje = "";
        int mb = 0;
        final int id = pos;
        switch (pos){
            case 0:
                mb = 18;
                break;
            case 1:
                mb = 22;
                break;
            case 2:
                mb = 30;
                break;
        }
        mensaje = "El archivo que está a punto de descargar tiene un peso aproximado de <b>" + mb +
                " MB</b>, le sugerimos conectarse a una red WiFi para que sus datos no se" +
                " consuman."+"<br><br>"+ "¿Desea continuar?";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ALERTA DE CONSUMO DE DATOS")
                .setMessage(Html.fromHtml(mensaje))
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Comienza descarga", Toast.LENGTH_LONG).show();
                        downloadFile(id);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Descarga cancelada", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    public void downloadFile(int pos){
        dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        uri = Uri.parse(rutas[pos]);
        DownloadManager.Request rq = new DownloadManager.Request(uri);
        rq.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = dManager.enqueue(rq);
    }
}