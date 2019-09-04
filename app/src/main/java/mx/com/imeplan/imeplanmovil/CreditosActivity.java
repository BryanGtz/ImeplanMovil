package mx.com.imeplan.imeplanmovil;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
        String texto =
                "IMEPLAN móvil es un sistema de información y gestión metropolitana, el cual está diseñado para integrar información de los tres municipios que conforman la zona metropolitana del sur del estado de Tamaulipas. \n" +
                        "Planea así ser una herramienta en las labores relacionadas al ordenamiento del territorio y a la prestación de servicios para que sean gestionadas a través de ella y atendidas de una manera rápida y expedita por cada una de las dependencias municipales correspondientes, fomentado el desarrollo y la integración como la zona metropolitana más grande del estado de Tamaulipas. \n" +
                        "Podrás realizar de forma ágil consultas a los distintos sitios de interés dentro de la zona metropolitana, reportes ciudadanos en tiempo real, herramientas como POTs y rutas de trasporte estarán disponibles entre muchas cosas más.  \n" +
                        "IMEPLAN móvil fomentara entonces una fuerte colaboración, participación y transparencia entre ciudadanos, instituciones y administraciones municipales.\n" +
                        "Coadyuvando juntos  en una mejor integración como zona metropolitana.\n";
        creditos.setText(texto);
        creditos.startAnimation(animacion);
    }
}
