package mx.com.imeplan.imeplanmovil;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class MisBorradoresActivity extends Fragment {


    public MisBorradoresActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Context context;
    SQLiteOpenHelper conn;
    TextView sin_borrador;
    ListView listView_borradores;
    ArrayList<Reporte> listaBorradores;
    ArrayList<String> listaInfo;
    Intent miIntent;
    Bundle infoReporte;
    String [] infoR = new String [9];
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.fragment_mis_borradores, container, false);
        context = frag.getContext();
        conn = new ConexionSQLiteHelper(getContext());

        sin_borrador = (TextView) frag.findViewById(R.id.s_borrador);
        listView_borradores = (ListView) frag.findViewById(R.id.lista_borradores);
        consultarListaBorradores();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listaInfo);

        if(adapter.isEmpty())
            sin_borrador.setVisibility(View.VISIBLE);
        else{
            sin_borrador.setVisibility(View.INVISIBLE);
            listView_borradores.setVisibility(View.VISIBLE);
            listView_borradores.setAdapter(adapter);
            listView_borradores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    miIntent = new Intent(context, InfoReporteActivity.class);
                    infoReporte = new Bundle();
                    infoR [0] = Integer.toString(listaBorradores.get(i).getId());
                    infoR [1] = listaBorradores.get(i).getCategoria();
                    infoR [2] = listaBorradores.get(i).getSubcategoria();
                    infoR [3] = listaBorradores.get(i).getLatitud();
                    infoR [4] = listaBorradores.get(i).getLongitud();
                    infoR [5] = listaBorradores.get(i).getFoto();
                    infoR [6] = listaBorradores.get(i).getFecha();
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date d = sdf.parse(infoR[6]);
                        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        infoR[6] = sdf.format(d);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    infoR [7] = Integer.toString(listaBorradores.get(i).getEstado());
                    infoR [8] = listaBorradores.get(i).getDireccion();
                    infoReporte.putStringArray("array", infoR);
                    miIntent.putExtras(infoReporte);
                    startActivity(miIntent);
                }
            });
        }

        return frag;
    }

    private void consultarListaBorradores() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Reporte reporte = null;
        listaBorradores = new ArrayList<>();

        Cursor cursor = db.rawQuery(Utilidades.VER_BORRADOR,null);
        while(cursor.moveToNext()){
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
            listaBorradores.add(reporte);
        }
        getLista();
    }

    private void getLista() {
        listaInfo = new ArrayList<>();

        for(int i=0; i<listaBorradores.size(); i++){
            listaInfo.add(listaBorradores.get(i).getId()+" - "+listaBorradores.get(i).getSubcategoria());
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
