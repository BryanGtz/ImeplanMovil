package mx.com.imeplan.imeplanmovil;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LocationTask extends AsyncTask<Void,Void,String[]> {

    Context context;
    Location l;
    public final static int GEOCODER_MAX_RESULTS = 2;
    private TextView tvLocation;
    int sourceCode;
    String municipio = "";

    public LocationTask(Context c, Location location, TextView txtview, int sourceCode){
        Log.e("lt constructor","Construyendose location task "+sourceCode);
        if(l!=null){
            Log.e("coordinates",l.getLatitude()+", "+l.getLatitude());
        }
        context = c;
        l = location;
        tvLocation = txtview;
        this.sourceCode = sourceCode;
    }

    public List<Address> getAddress(){
        List<Address> addresses = null;
        if(Geocoder.isPresent()){
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try{
                addresses = geocoder.getFromLocation(l.getLatitude(),l.getLongitude(),GEOCODER_MAX_RESULTS);
                Set<Address> uniqueAddress = new HashSet<>(addresses);
                addresses = new ArrayList<>(uniqueAddress);

            }
            catch (Exception e){
                Log.e("error",e.getMessage());
            }
        }
        else{
            Log.e("no entra","geocoder is not present");
        }
        return addresses;
    }

    public String getMunicipio(){
        return municipio;
    }

    @Override
    protected String[] doInBackground(Void... voids) {
        Log.e("entra","doing in background task");
        List<Address> addresses = getAddress();
        String[] resultados = {};
        if(addresses!=null&&!addresses.isEmpty()){
            resultados = new String[addresses.size()];
            Log.e("numAddress",addresses.size()+"");
            for (int i = 0; i < addresses.size(); i++) {
                resultados[i] = addresses.get(i).getAddressLine(0);
                Log.e("numIndex",addresses.get(i).getMaxAddressLineIndex()+"");
                for (int j = 0; j <= addresses.get(i).getMaxAddressLineIndex(); j++) {
                    Log.e("AddressesLine",addresses.get(i).getAddressLine(j));
                }
            }
            municipio = addresses.get(0).getLocality();
        }
        else{
            Log.e("no entra","addresses null or addresses empty");
        }
        return resultados;
    }

    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);
        if(result.length>0&&tvLocation!=null){
            Log.e("sourceCode",sourceCode+"");
            switch (sourceCode){
                case 1:
                    tvLocation.setText(result[0]);
                    break;
                case 2:
                    String address = "<b>Dirección: </b>"+result[0];
                    tvLocation.setText(Html.fromHtml(address));
                    break;
                default:
                    tvLocation.setText(result[0]);
                    break;
            }
        }
    }
}