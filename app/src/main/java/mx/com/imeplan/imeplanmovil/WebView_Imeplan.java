package mx.com.imeplan.imeplanmovil;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        id = getIntent().getExtras().getInt("id");

        ejecutarWebView(id);
        setTitle(title);
    }

    private void ejecutarWebView(int id) {
        switch (id){
            case 1:
                title = "Sitios de interés";
                wb.loadUrl("http://www.google.com");
                break;
            case 2:
                title = "Movilidad";
                wb.loadUrl("http://www.imeplansurdetamaulipas.gob.mx");
                break;
            case 3:
                title = "Calculadora ambiental";
                wb.loadUrl("http://www.bing.com");
                break;
        }
    }
}
