package mx.com.imeplan.imeplanmovil;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

import java.util.ArrayList;

import mx.com.imeplan.imeplanmovil.entidades.Reporte;
import mx.com.imeplan.imeplanmovil.utilidades.Utilidades;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MisBorradoresActivity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MisBorradoresActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MisBorradoresActivity extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MisBorradoresActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MisBorradoresActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static MisBorradoresActivity newInstance(String param1, String param2) {
        MisBorradoresActivity fragment = new MisBorradoresActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    Context context;
    SQLiteOpenHelper conn;
    TextView sin_borrador;
    ListView listView_borradores;
    ArrayList<Reporte> listaBorradores;
    ArrayList<String> listaInfo;
    Intent miIntent;
    Bundle infoReporte;
    String [] infoR = new String [8];
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.fragment_mis_borradores, container, false);
        context = frag.getContext();
        conn = new ConexionSQLiteHelper(getContext(), "bd_imeplanMovil.db",null,1);

        sin_borrador = (TextView) frag.findViewById(R.id.s_borrador);
        listView_borradores = (ListView) frag.findViewById(R.id.lista_borradores);
        consultarListaBorradores();

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, listaInfo);

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
                    infoR [7] = Integer.toString(listaBorradores.get(i).getEstado());
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
        listaBorradores = new ArrayList<Reporte>();

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
            listaBorradores.add(reporte);
        }
        getLista();
    }

    private void getLista() {
        listaInfo = new ArrayList<String>();

        for(int i=0; i<listaBorradores.size(); i++){
            listaInfo.add(listaBorradores.get(i).getId()+" - "+listaBorradores.get(i).getSubcategoria());
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
