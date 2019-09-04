package mx.com.imeplan.imeplanmovil;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class SitiosInteres extends AppCompatActivity {

    private ViewPager viewPagerSitiosInteres;
    private LinearLayout linearLayoutSitiosInteres;
    private SliderAdapterSitiosInteres sliderAdapterSitiosInteres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitios_interes);

        viewPagerSitiosInteres = (ViewPager) findViewById(R.id.viewPager01);


        sliderAdapterSitiosInteres = new SliderAdapterSitiosInteres(this);
        viewPagerSitiosInteres.setAdapter(sliderAdapterSitiosInteres);
    }
}
