package mx.com.imeplan.imeplanmovil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class CreditosActivity extends AppCompatActivity {
    TextView creditos;
    Animation animacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);

        animacion = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.creditos);
        creditos = (TextView) findViewById(R.id.creditos);
        String texto = "Aplicación creada por alumnos del Instituto Teconógico de Ciudad Madero" +
                " en conjunto con el IMEPLAN.\n\n" +
                "- Bryan Steven Gutiérrez Castro\n" +
                "- Pablo Eduardo Hernández Vicente\n" +
                "- Enoc Alberto Martínez Olivares\n\n\n" +
                "Dirreción del IMEPLAN:\n\n" +
                "Av. Miguel Hidalgo 3509, Guadalupe, 89210 Tampico, Tamps.\n\n\n" +
                "Copyright \u00a9 2019.";
        creditos.setText(texto);
        creditos.startAnimation(animacion);
    }
}
