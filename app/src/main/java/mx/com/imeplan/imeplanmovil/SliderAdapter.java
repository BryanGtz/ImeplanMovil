package mx.com.imeplan.imeplanmovil;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context=context;
    }

    //Arrays
    public int[] slide_images ={
            R.drawable.imeicono250,
            R.drawable.tap001250,
            R.drawable.tap002250,
            R.drawable.tap003250
    };
    public String[] slide_headings ={
            "¡Hola!",
            "Toca un icono",
            "Vuelve a tocar",
            "¡Listo!"
    };
    public String[] slide_descs ={
            "Bienvenido a la app oficial\n"+" del IMEPLAN.",
            "Para obtener más información.",
            "Sobre la cinta desplegada\n"+"Para acceder.",
            ""
    };


    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (LinearLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);
        ImageView sliderImageView= (ImageView) view.findViewById(R.id.imgv01);
        TextView sliderHeading= (TextView) view.findViewById(R.id.txtHead);
        TextView sliderDesc= (TextView) view.findViewById(R.id.txtDesc);
        sliderImageView.setImageResource(slide_images[position]);
        sliderHeading.setText(slide_headings[position]);
        sliderDesc.setText(slide_descs[position]);
        container.addView(view);
        return  view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
