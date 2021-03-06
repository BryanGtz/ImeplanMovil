package mx.com.imeplan.imeplanmovil;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebView_Imeplan extends AppCompatActivity {
    WebView wb;
    int id;
    String title;

    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_webview_imeplan);
        wb = (WebView) findViewById(R.id.web_imeplan);
        wb.setWebViewClient(new WebViewClient());
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setPluginState(WebSettings.PluginState.ON);
        //wb.getSettings().setPluginsEnabled(true);
        id = getIntent().getExtras().getInt("id");

        ejecutarWebView(id);
        setTitle(title);
    }

    private void ejecutarWebView(int id) {
        switch (id){
            case 1:
                title = "Rutas de Transporte";
                wb.loadUrl("http://www.imeplansurdetamaulipas.gob.mx/rutastransporte.html");
                break;
            case 2:
                title = "Calculadora ambiental";
                wb.loadUrl("http://calculadoraambiental.imeplansurdetamaulipas.gob.mx/");
                break;
            case 3:
                title = "IMEPLAN";
                wb.loadUrl("http://www.imeplansurdetamaulipas.gob.mx");
                break;
        }
    }
}
