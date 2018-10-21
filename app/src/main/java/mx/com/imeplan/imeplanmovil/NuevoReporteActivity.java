package mx.com.imeplan.imeplanmovil;

import android.Manifest;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import javax.xml.transform.sax.SAXResult;

import mx.com.imeplan.imeplanmovil.utilidades.Utilidades;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NuevoReporteActivity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NuevoReporteActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NuevoReporteActivity extends Fragment {

    TextView tv;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NuevoReporteActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NuevoReporteActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static NuevoReporteActivity newInstance(String param1, String param2) {
        NuevoReporteActivity fragment = new NuevoReporteActivity();
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

    TextView campoLatitud, campoLongitud;
    EditText campoID, campoFoto;
    SQLiteOpenHelper conn;
    Button btn;
    String latitud, longitud;
    int valor, valorSC;
    int permissionCheckGPS;
    Spinner spinnerC, subCategoria;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View frag = inflater.inflate(R.layout.fragment_nuevo_reporte, container, false);
        conn = new ConexionSQLiteHelper(getContext(), "bd_imeplanMovil.db", null, 1);

        campoID = (EditText) frag.findViewById(R.id.campo_ID);
        campoLatitud = (TextView) frag.findViewById(R.id.campo_Latitud);
        campoLongitud = (TextView) frag.findViewById(R.id.campo_Longitud);
        campoFoto = (EditText) frag.findViewById(R.id.campo_Foto);
        btn = (Button) frag.findViewById(R.id.nvo_reporte);
        spinnerC = (Spinner) frag.findViewById(R.id.categoria);
        subCategoria = (Spinner) frag.findViewById(R.id.campo_SubCategoria);

        String[] categorias = {"Seleccione","Comapa","CFE","Recoleccion de basura","Cuadrilla ecologica","Pavimentacion","Vialidad","Otros"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categorias);
        spinnerC.setAdapter(adapter);

        // Establecer Spinner
        spinnerC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String[] subcategorias = {""};
                valor = position;
                switch (position) {
                    case 0:
                        subcategorias = new String[]{""};
                        break;
                    case 1:
                        subcategorias = new String[]{"Fugas", "Corte de agua"};
                        break;
                    case 2:
                        subcategorias = new String[]{"Cableado", "Corte de luz", "Poste caído"};
                        break;
                    case 3:
                        subcategorias = new String[]{"Recoleccion de basura"};
                        break;
                    case 4:
                        subcategorias = new String[]{"Limpieza","Árbol caído"};
                        break;
                    case 5:
                        subcategorias = new String[] {"Bache", "Pavimentación"};
                        break;
                    case 6:
                        subcategorias = new String[]{"Vialidad peligrosa", "Semáforo", "Tope", "Accidente"};
                        break;
                    case 7:
                        subcategorias = new String[]{"Anuncio", "Banqueta"};
                        break;
                    default:
                        subcategorias = new String[]{""};
                        break;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(adapterView.getContext(), android.R.layout.simple_spinner_item, subcategorias);
                subCategoria.setAdapter(adapter);
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        subCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (valor){
                    case 1:
                        valorSC = i;
                        break;
                    case 2:
                        valorSC = 2 + i;
                        break;
                    case 3:
                        valorSC = 5 + i;
                        break;
                    case 4:
                        valorSC = 6 + i;
                        break;
                    case 5:
                        valorSC = 8 + i;
                        break;
                    case 6:
                        valorSC = 10 + i;
                        break;
                    case 7:
                        valorSC = 14 + i;
                        break;
                }
                valorSC+= 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Obtener la Ubicación
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                latitud = String.valueOf(location.getLatitude());
                longitud = String.valueOf(location.getLongitude());
                campoLatitud.setText(latitud);
                campoLongitud.setText(longitud);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        permissionCheckGPS = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        //latitud = getArguments().getString("latitud");
        //longitud = getArguments().getString("longitud");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarReporteSQL();
                limpiar();
            }
        });

        return frag;
    }

    private void limpiar() {
        campoID.setText("");
        campoLatitud.setText("");
        campoLongitud.setText("");
        campoFoto.setText("");
        valor =0 ;
        valorSC = 0;
        spinnerC.setSelection(0);

    }

    private void registrarReporteSQL() {
        SQLiteDatabase db = conn.getWritableDatabase();

        String insert = "insert into "+Utilidades.TABLA_REPORTE+
                " values("+campoID.getText().toString()+","+String.valueOf(valorSC)+"," +
                "'"+campoLatitud.getText().toString()+"','"+campoLongitud.getText().toString()+"','"+campoFoto.getText().toString()+"'," +
                "datetime(),0)";

        db.execSQL(insert);
        db.close();
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
