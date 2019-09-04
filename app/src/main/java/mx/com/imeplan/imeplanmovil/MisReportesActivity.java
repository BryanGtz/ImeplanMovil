package mx.com.imeplan.imeplanmovil;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mx.com.imeplan.imeplanmovil.entidades.Reporte;
import mx.com.imeplan.imeplanmovil.utilidades.Utilidades;


/**
 * A simple {@link Fragment} subclass.
 */
public class MisReportesActivity extends Fragment {

    public MisReportesActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    TextView sin_reporte;
    ListView listView_reportes;
    ArrayList<String> listaInfo;
    ArrayList<Reporte> listaReportes;
    Intent miIntent;
    Bundle infoReportes;
    SQLiteOpenHelper conn;
    String [] infoR = new String [9];
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View frag = inflater.inflate(R.layout.fragment_mis_reportes, container, false);
        context = frag.getContext();

        conn = new ConexionSQLiteHelper(getContext());
        sin_reporte = (TextView) frag.findViewById(R.id.s_reporte);
        listView_reportes = (ListView) frag.findViewById(R.id.lista_reportes);
        consultarListaReportes();

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1, listaInfo);

        if(adaptador.isEmpty()){
            sin_reporte.setVisibility(View.VISIBLE);
        }
        else {
            sin_reporte.setVisibility(View.INVISIBLE);
            listView_reportes.setVisibility(View.VISIBLE);
            listView_reportes.setAdapter(adaptador);
            listView_reportes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    miIntent = new Intent(context, InfoReporteActivity.class);
                    infoReportes = new Bundle();
                    infoR [0] = Integer.toString(listaReportes.get(i).getId());
                    infoR [1] = listaReportes.get(i).getCategoria();
                    infoR [2] = listaReportes.get(i).getSubcategoria();
                    infoR [3] = listaReportes.get(i).getLatitud();
                    infoR [4] = listaReportes.get(i).getLongitud();
                    infoR [5] = listaReportes.get(i).getFoto();
                    infoR [6] = listaReportes.get(i).getFecha();
                    Date d;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        d = sdf.parse(infoR[6]);
                        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        infoR[6] = sdf.format(d);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    infoR [7] = Integer.toString(listaReportes.get(i).getEstado());
                    infoR [8] = listaReportes.get(i).getDireccion();
                    infoReportes.putStringArray("array", infoR);
                    miIntent.putExtras(infoReportes);
                    startActivity(miIntent);
                }
            });
        }
        return frag;
    }

    private void consultarListaReportes() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Reporte reporte = null;
        listaReportes = new ArrayList<Reporte>();

        Cursor cursor = db.rawQuery(Utilidades.VER_REPORTE,null);
        while (cursor.moveToNext()){
            reporte = new Reporte();
            reporte.setId(cursor.getInt(0));
            reporte.setSubcategoria(cursor.getString(1));
            reporte.setCategoria(cursor.getString(2));
            reporte.setLatitud(cursor.getString(3));
            reporte.setLongitud(cursor.getString(4));
            reporte.setFoto(cursor.getString(5));
            reporte.setFecha(cursor.getString(6));
            reporte.setEstado(cursor.getInt(7));
            reporte.setDireccion(cursor.getString(8));
            listaReportes.add(reporte);
        }
        obtenerLista();
        cursor.close();
    }

    private void obtenerLista() {
        listaInfo = new ArrayList<>();

        for(int i=0; i<listaReportes.size(); i++){
            listaInfo.add(listaReportes.get(i).getId()+" - "+listaReportes.get(i).getSubcategoria());
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
