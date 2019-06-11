package mx.com.imeplan.imeplanmovil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
 */
public class NuevoReporteActivity extends Fragment {

    public NuevoReporteActivity() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    TextView campoLatitud, campoLongitud; //direccion
    String municipio;
    LocationHelper lh;
    ConexionSQLiteHelper conn;
    Button enviar, camara;
    double latitud, longitud;
    int valorSC = -1;
    int permissionCheckGPS;
    Spinner spinnerC, subCategoria;
    ConnectivityManager cm;
    NetworkInfo ni;
    int isInternet;

    View frag;
    Bitmap bmp;
    ImageView img;
    String mCurrentPhotoPath = "";
    private static final int TAKE_PHOTO = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frag = inflater.inflate(R.layout.fragment_nuevo_reporte, container, false);
        conn = new ConexionSQLiteHelper(getContext());

        init();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, conn.getCategorias());
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerC.setAdapter(adapter);

        // Establecer Spinner
        spinnerC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String [] subcategorias = (position==0)
                        ? new String[]{"--Seleccione una categoria--"}
                        : conn.getNombreSubcategorias(position);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(adapterView.getContext(), android.R.layout.simple_spinner_item, subcategorias);
                adapter.setDropDownViewResource(R.layout.spinner_item);
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
                    valorSC = conn.getIdSubcategoria(subnombre);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Obtener la ubicacion
        lh = new LocationHelper(getContext(),250,1,campoLongitud);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Revisar conexion a Internet
                cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                ni = cm.getActiveNetworkInfo();
                isInternet = (ni != null && ni.isConnected()) ? 1 : 0;

                if (!mCurrentPhotoPath.isEmpty() && valorSC != -1){
                    Toast.makeText(getContext(), "Reporte Enviado", Toast.LENGTH_SHORT).show();
                    municipio = lh.getMunicipio();
                    registrarReporteSQL();
                    limpiar();
                }
                else
                    Toast.makeText(getContext(), "Los datos deben estar llenos", Toast.LENGTH_SHORT).show();


            }
        });

        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Abrir la camara
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                }catch (Exception e){

                }
                if(photoFile != null){
                    Uri photoURI = FileProvider.getUriForFile(
                            NuevoReporteActivity.this.getActivity().getBaseContext(),
                            "mx.com.imeplan.imeplanmovil.android.fileprovider",photoFile
                    );
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                    startActivityForResult(takePictureIntent, TAKE_PHOTO);
                }
                //startActivityForResult(takePictureIntent, TAKE_PHOTO);

            }

        });
        return frag;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            //File file = new File(mCurrentPhotoPath);
            /*Uri uri = data.getData();
            try {
                bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            } catch (IOException e){

            }*/
            /*Bundle ext = data.getExtras();
            bmp = (Bitmap)ext.get("data");*/
            //img.setImageBitmap(bmp);
            //img.setImageURI(Uri.fromFile(file));

            //String ruta = guardarFoto(bmp);
            //Log.d("Ruta: ",ruta);

            ExifInterface exif = null;
            int orientacion = 0;
            try {
                exif = new ExifInterface(mCurrentPhotoPath);
                orientacion = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                                    ExifInterface.ORIENTATION_UNDEFINED);
                switch (orientacion){
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        orientacion = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        orientacion = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        orientacion = 270;
                        break;
                    default:
                        orientacion = 0;
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Matrix m = new Matrix();
            m.postRotate(orientacion);
            Bitmap thumb = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mCurrentPhotoPath),300,200);
            bmp = Bitmap.createBitmap(thumb,0,0,thumb.getWidth(),thumb.getHeight(),m,true);
            img.setImageBitmap(bmp);

        }
    }

    //Método para inicializar
    private void init(){
        campoLatitud = (TextView) frag.findViewById(R.id.campo_Latitud);
        campoLongitud = (TextView) frag.findViewById(R.id.campo_Longitud);
        enviar = (Button) frag.findViewById(R.id.nvo_reporte);
        camara = (Button) frag.findViewById(R.id.camera);
        img = (ImageView)frag.findViewById(R.id.imageView);

        spinnerC = (Spinner) frag.findViewById(R.id.categoria);
        subCategoria = (Spinner) frag.findViewById(R.id.campo_SubCategoria);
    }

    // Método para limpiar la pantalla cuando se guarde el reporte
    private void limpiar() {
        campoLatitud.setText("");
        campoLongitud.setText("");
        valorSC = 0;
        spinnerC.setSelection(0);
        mCurrentPhotoPath = "";
        img.setImageBitmap(null);
    }

    // Método para guardar el reporte en la base de datos
    private void registrarReporteSQL() {
        latitud = lh.getLatitude();
        longitud = lh.getLongitud();
        String direccion;
        if(campoLongitud.getText().equals(getString(R.string.obteniendo_ubicacion))){
            direccion = "";
        }
        else{
            direccion = campoLongitud.getText().toString();
        }
        //String address = lh.getAddress().get(0).getAddressLine(0);
        SQLiteDatabase db = conn.getWritableDatabase();
        String insert = "insert into "+Utilidades.TABLA_REPORTE+
                "("+Utilidades.R_CAMPO_SUBCATEGORIA+","+
                Utilidades.R_CAMPO_LATITUD+","+
                Utilidades.R_CAMPO_LONGITUD+","+
                Utilidades.R_CAMPO_DIRECCION+","+
                Utilidades.R_CAMPO_FOTO+","+
                Utilidades.R_CAMPO_FECHA+","+
                Utilidades.R_CAMPO_ESTADO+")"+
                " values("+String.valueOf(valorSC)+"," +
                "'"+String.valueOf(latitud)+"','"+String.valueOf(longitud)+"',"+
                "'"+direccion+"',"+
                "'"+mCurrentPhotoPath+"'," +
                "datetime(current_timestamp, 'localtime'),"+isInternet+")";

        db.execSQL(insert);
        db.close();

        if (isInternet == 1) {
            sendEmail(getReporte());
        }
        else{
            Toast.makeText(getContext(), "Reporte enviado a mis borradores", Toast.LENGTH_LONG).show();
        }
    }

    private String[] getReporte() {
        String [] data = new String[6];
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor c = db.rawQuery(Utilidades.VER_ULTIMO_REPORTE, null);
        while(c.moveToNext()){
            data[0] = c.getString(1);   // Subcategoria
            data[1] = c.getString(2);   // Categoria
            data[2] = c.getString(3);   // Latitud
            data[3] = c.getString(4);   // Longitud
            data[4] = c.getString(6);   // Fecha
            data[5] = c.getString(8);  //Direccion
        }
        c.close();
        return data;
    }

    // Método para enviar el reporte al correo correcto
    protected void sendEmail(String [] datos) {

        String mun = municipio.split(" ")[1];
        Toast.makeText(getContext(), "Mun: " + mun, Toast.LENGTH_SHORT).show();
        String mail= "enoc.9714@gmail.com", dependencia = "IMEPLAN", cargo = "Dirección de Medio Ambiente";
        switch(mun) {
            case "Tampico":
                switch (datos[1]) {
                    case "COMAPA":
                        mail = "gildardo.ponce@tam.gob.mx";//adolfo.cabal@tam.gob.mx
                        dependencia = "COMAPA";
                        cargo = "Atención Ciudadana";
                        break;
                    case "Servicios públicos":
                        mail = "gildardo.ponce@tam.gob.mx";//servpublicos@tampico.gob.mx
                        break;
                    case "Cuadrilla ecológica":
                        mail = "gildardo.ponce@tam.gob.mx";//secretariaecologiatampico@hotmail.com
                        dependencia = "IMEPLAN";
                        cargo = "Dirección de Medio Ambiente";
                        break;
                    case "Obras públicas":
                        mail = "gildardo.ponce@tam.gob.mx";//Vacio
                        break;
                    case "Vialidad":
                        mail = "gildardo.ponce@tam.gob.mx";//Vacio
                        break;
                    case "Otros":
                        mail = "gildardo.ponce@tam.gob.mx";//Vacio
                        dependencia = "IMEPLAN";
                        cargo = "Jefatura de Informática";
                        break;
                }
                break;
            case "Madero":
                switch (datos[1]) {
                    case "COMAPA":
                        mail = "gildardo.ponce@tam.gob.mx";//adolfo.cabal@tam.gob.mx
                        break;
                    case "Servicios públicos":
                        mail = "gildardo.ponce@tam.gob.mx";//any335@hotmail.com
                        break;
                    case "Cuadrilla ecológica":
                        mail = "gildardo.ponce@tam.gob.mx";//direccionecologia@ciudadmadero.gob.mx
                        break;
                    case "Obras públicas":
                        mail = "gildardo.ponce@tam.gob.mx";//Vacio
                        break;
                    case "Vialidad":
                        mail = "gildardo.ponce@tam.gob.mx";
                        break;
                    case "Otros":
                        mail = "gildardo.ponce@tam.gob.mx";//bryan.gtz.317@gmail.com
                        break;
                }
                break;
            case "Miramar":
            case "Altamira":
                switch (datos[1]) {
                    case "COMAPA":
                        mail = "gildardo.ponce@tam.gob.mx";//monje@comapaaltamira.gob.mx
                        break;
                    case "Servicios públicos":
                        mail = "gildardo.ponce@tam.gob.mx";//secretariasp.altamira@gmail.com
                        break;
                    case "Cuadrilla ecológica":
                        mail = "gildardo.ponce@tam.gob.mx";//illescasfrancisco@hotmail.com
                        break;
                    case "Obras públicas":
                        mail = "gildardo.ponce@tam.gob.mx";//Vacio
                        break;
                    case "Vialidad":
                        mail = "gildardo.ponce@tam.gob.mx";//Vacio
                        break;
                    case "Otros":
                        mail = "gildardo.ponce@tam.gob.mx";//Vacio
                        break;
                }
                break;
            default:
                Toast.makeText(getContext(), "Fuera de la zona conurbada", Toast.LENGTH_LONG).show();
                return;
        }

        mail = "enoc.9714@gmail.com";

        String asunto = "Reporte ciudadano";
        String mensaje = cargo + "\n" + dependencia + "\n";
        mensaje += "Por medio de la presente se notifica sobre el siguiente reporte ciudadano:\n";
        mensaje += "Categoría: " + datos[1] + "\nSubcategoría: " + datos[0] +
                "\nCon ubicacion en: " + datos[5] + "\n" + datos[2] + "," + datos[3] +
                "\nFecha y hora: " + datos[4] + "\n";
        mensaje += "Sin mas por el momento, agradeceríamos la pronta resolución\n";
        mensaje += "Atentamente:\nCiudadanos";
        GMailSender sender = new GMailSender(getContext());
        sender.enviarEmail(mail, asunto, mensaje);
        sender.adjuntarArchivo(mCurrentPhotoPath);
        Toast.makeText(getContext(), "Reporte enviado exitosamente", Toast.LENGTH_LONG).show();
    }

    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Imeplan_" + timeStamp;
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(imageFileName,".jpg", storageDir);
        } catch (IOException e) {

        }
        // Save a file: path for use with ACTION_VIEW intents
        if (image != null) {
            mCurrentPhotoPath = image.getAbsolutePath();
        }
        return image;
    }

    // Método para guardar la foto en el teléfono
    private String guardarFoto(Bitmap b){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Imeplan " + timeStamp;
        return MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),b,imageFileName,"");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
