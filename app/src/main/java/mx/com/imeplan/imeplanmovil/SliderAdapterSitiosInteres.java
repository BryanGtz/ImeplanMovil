package mx.com.imeplan.imeplanmovil;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SliderAdapterSitiosInteres extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapterSitiosInteres(Context context){
        this.context = context;
    }

    //Arreglo de Imagenes a Mostrar
    public int[] sliderImages={
            R.mipmap.si001_centro_historico,
            R.mipmap.si002_dunas_doradas,
            R.mipmap.si003_laguna_carpintero,
            R.mipmap.si004_laguna_champayan,
            R.mipmap.si005_laguna_vega_escondida,
            R.mipmap.si006_parque_bicentenario,
            R.mipmap.si007_parque_fray_andres,
            R.mipmap.si008_playa_miramar,
            R.mipmap.si009_playa_tesoro,
            R.mipmap.si010_playa_tesoro
            /*
            R.drawable.si03_laguna_carpintero,
            R.drawable.si04_laguna_champayan,
            R.drawable.si05_laguna_vega_escondida,
            R.drawable.si06_parque_bicentenario,
            R.drawable.si07_parque_fray_andres,
            R.drawable.si08_playa_miramar,
            R.drawable.si09_playa_tesoro,
            R.drawable.si10_playa_tesoro
            */
    };
    @Override
    public int getCount() {
        return sliderImages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (LinearLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_sitios_interes_layout, container,false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.imageViewSlideSitiosInteres);
        slideImageView.setImageResource(sliderImages[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((LinearLayout) object);
    }
}
