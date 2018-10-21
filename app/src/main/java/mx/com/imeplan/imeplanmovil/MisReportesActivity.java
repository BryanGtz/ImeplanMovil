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
 * {@link MisReportesActivity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MisReportesActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MisReportesActivity extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MisReportesActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MisReportesActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static MisReportesActivity newInstance(String param1, String param2) {
        MisReportesActivity fragment = new MisReportesActivity();
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

    TextView sin_reporte;
    ListView listView_reportes;
    ArrayList<String> listaInfo;
    ArrayList<Reporte> listaReportes;
    Intent miIntent;
    Bundle infoReportes;
    SQLiteOpenHelper conn;
    String [] infoR = new String [8];
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View frag = inflater.inflate(R.layout.fragment_mis_reportes, container, false);
        context = frag.getContext();

        conn = new ConexionSQLiteHelper(getContext(), "bd_imeplanMovil.db",null,1);
        sin_reporte = (TextView) frag.findViewById(R.id.s_reporte);
        listView_reportes = (ListView) frag.findViewById(R.id.lista_reportes);
        consultarListaReportes();

        ArrayAdapter adaptador = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, listaInfo);

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
                    infoR [7] = Integer.toString(listaReportes.get(i).getEstado());
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
            listaReportes.add(reporte);
        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaInfo = new ArrayList<String>();

        for(int i=0; i<listaReportes.size(); i++){
            listaInfo.add(listaReportes.get(i).getId()+" - "+listaReportes.get(i).getSubcategoria());
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
