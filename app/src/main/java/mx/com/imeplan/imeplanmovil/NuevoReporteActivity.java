package mx.com.imeplan.imeplanmovil;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Spinner;
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
    SQLiteOpenHelper conn;
    Button btn;
    String latitud, longitud;
    int valor, valorSC;
    int permissionCheckGPS;
    Spinner spinnerC, subCategoria;
    ConnectivityManager cm;
    NetworkInfo ni;
    int isInternet;
    View frag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frag = inflater.inflate(R.layout.fragment_nuevo_reporte, container, false);
        conn = new ConexionSQLiteHelper(getContext(), "bd_imeplanMovil.db", null, 1);

        campoLatitud = (TextView) frag.findViewById(R.id.campo_Latitud);
        campoLongitud = (TextView) frag.findViewById(R.id.campo_Longitud);
        btn = (Button) frag.findViewById(R.id.nvo_reporte);
        spinnerC = (Spinner) frag.findViewById(R.id.categoria);
        subCategoria = (Spinner) frag.findViewById(R.id.campo_SubCategoria);

        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor;
        String[]categorias;
        try {
            cursor = db.rawQuery("select " + Utilidades.C_CAMPO_CATEGORIA +
                    " from " + Utilidades.TABLA_CATEGORIA, null);
            categorias = new String[cursor.getCount()+1];
            categorias[0]="--Seleccione una categoria---";
            int i = 0;
            while(cursor.moveToNext()&&i<cursor.getCount()){
                categorias[i+1] = cursor.getString(0);
                i++;
            }
            db.close();
            cursor.close();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categorias);
            adapter.setDropDownViewResource(R.layout.spinner_item);
            spinnerC.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT);
        }

        // Establecer Spinner
        spinnerC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String[] subcategorias;
                if(position==0){
                    subcategorias = new String[]{"--Seleccione una categoria--"};
                }
                else{
                    SQLiteDatabase db = conn.getReadableDatabase();
                    Cursor cursor = db.rawQuery("select "+Utilidades.SC_CAMPO_SUBCATEGORIA+
                            " from "+Utilidades.TABLA_SUBCATEGORIA +
                            " where "+Utilidades.SC_CAMPO_CATEGORIA+" = "+ position,null);
                    subcategorias = new String[cursor.getCount()];
                    int i = 0;
                    if(cursor.moveToFirst()){
                        do{
                            subcategorias[i]=cursor.getString(0);
                            i++;
                        }
                        while(cursor.moveToNext());
                    }
                    db.close();
                    cursor.close();
                }
                //subCategoria = (Spinner) frag.findViewById(R.id.campo_SubCategoria);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(adapterView.getContext(), android.R.layout.simple_spinner_item, subcategorias);
                subCategoria.setAdapter(adapter);
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        subCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String subnombre = subCategoria.getSelectedItem().toString();
                if(!subnombre.equals("--Seleccione una categoria--")){
                    SQLiteDatabase db = conn.getReadableDatabase();
                    int[] subcategorias;
                    Cursor cursor = db.rawQuery(
                            "select "+Utilidades.SC_CAMPO_ID+
                            " from "+Utilidades.TABLA_SUBCATEGORIA +
                            " where "+Utilidades.SC_CAMPO_SUBCATEGORIA+" like '"+subnombre+"'",null);
                    int num = cursor.getCount();
                    subcategorias = new int[num];
                    int i = 0;
                    if(cursor.moveToFirst()){
                        do{
                            subcategorias[i]=cursor.getInt(0);
                            i++;
                        }
                        while(cursor.moveToNext());
                    }
                    db.close();
                    cursor.close();
                    valorSC = subcategorias[0];
                    //Toast.makeText(adapterView.getContext(),String.valueOf(valorSC),Toast.LENGTH_LONG).show();
                }

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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Revisar conexion a Internet
                cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                ni = cm.getActiveNetworkInfo();
                isInternet = (ni != null && ni.isConnected()) ? 1 : 0;
                registrarReporteSQL();
                limpiar();
            }
        });

        return frag;
    }

    private void limpiar() {
        campoLatitud.setText("");
        campoLongitud.setText("");
        valor =0 ;
        valorSC = 0;
        spinnerC.setSelection(0);

    }

    private void registrarReporteSQL() {
        SQLiteDatabase db = conn.getWritableDatabase();
        String insert = "insert into "+Utilidades.TABLA_REPORTE+
                "("+Utilidades.R_CAMPO_SUBCATEGORIA+","+Utilidades.R_CAMPO_LATITUD+","+Utilidades.R_CAMPO_LONGITUD+","+Utilidades.R_CAMPO_FOTO+","+Utilidades.R_CAMPO_FECHA+","+Utilidades.R_CAMPO_ESTADO+")"+
                " values("+String.valueOf(valorSC)+"," +
                "'"+campoLatitud.getText().toString()+"','"+campoLongitud.getText().toString()+"','"+"algo.jpg"+"'," +
                "datetime(current_timestamp, 'localtime'),"+isInternet+")";

        db.execSQL(insert);
        db.close();

        if (isInternet == 1)
            sendEmail(getReporte());
        else
            Toast.makeText(getContext(), "Reporte enviado a mis borradores", Toast.LENGTH_LONG).show();
    }

    private String[] getReporte() {
        String [] data = new String[5];
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor c = db.rawQuery(Utilidades.VER_ULTIMO_REPORTE, null);
        while(c.moveToNext()){
            data[0] = c.getString(1);   // Subcategoria
            data[1] = c.getString(2);   // Categoria
            data[2] = c.getString(3);   // Latitud
            data[3] = c.getString(4);   // Longitud
            data[4] = c.getString(6);   // Fecha
        }

        return data;
    }

    protected void sendEmail(String [] datos) {
        /*try {
            String user = "bryangtz317@gmail.com";
            Mail sender = new Mail(user);
            sender.sendMail("This is Subject",
                    "This is Body",
                    user,
                    user);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }*/
        String host="smtp.gmail.com";
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
