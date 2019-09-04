package mx.com.imeplan.imeplanmovil;

import android.content.Intent;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class OnBoarding extends AppCompatActivity {
    private ViewPager nSlideViewPager;
    Intent intent;
    private LinearLayout nDotLayout;

    private TextView[]nDots;

    private SliderAdapter sliderAdapter;
    private Button nNextbtn;
    private Button nBackbtn;
    private Button nAceptar;
    private int nCurrentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        nSlideViewPager =(ViewPager) findViewById(R.id.vp01);
        nDotLayout = (LinearLayout) findViewById(R.id.linearly01);
        nBackbtn=(Button) findViewById(R.id.btnL);
        nNextbtn=(Button) findViewById(R.id.btnR);
        nAceptar=(Button) findViewById(R.id.btnAceptar);
        sliderAdapter= new SliderAdapter(this);
        nSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        nSlideViewPager.addOnPageChangeListener(viewListener);

        //OnClickListeners
        nNextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nSlideViewPager.setCurrentItem(nCurrentPage+1);
            }
        });
        nBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nSlideViewPager.setCurrentItem(nCurrentPage-1);
            }
        });
        nAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent= new Intent(OnBoarding.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void addDotsIndicator(int position){
        nDots= new TextView[4];
        nDotLayout.removeAllViews();
        for (int i = 0; i < nDots.length; i++) {
            nDots[i]= new TextView(this);
            nDots[i].setText(Html.fromHtml("&#8226;"));
            nDots[i].setTextSize(35);

            nDotLayout.addView(nDots[i]);
        }
        if (nDots.length > 0) {

            nDots[position].setTextColor(getResources().getColor(R.color.blanco));
        }

    }

    ViewPager.OnPageChangeListener viewListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            nCurrentPage=i;
            if (i==0){
                nNextbtn.setEnabled(true);
                nBackbtn.setEnabled(false);
                nAceptar.setEnabled(false);
                nBackbtn.setVisibility(View.INVISIBLE);
                nAceptar.setVisibility(View.INVISIBLE);
                nNextbtn.setText("Siguiente");
                nBackbtn.setText("");
                nAceptar.setText("");
            }
            else if(i==nDots.length-1){
                nNextbtn.setEnabled(false);
                nBackbtn.setEnabled(true);
                nAceptar.setEnabled(true);
                nBackbtn.setVisibility(View.VISIBLE);
                nNextbtn.setVisibility(View.INVISIBLE);
                nAceptar.setVisibility(View.VISIBLE);
                nNextbtn.setText("");
                nBackbtn.setText("Atrás");
                nAceptar.setText("Aceptar");
            }
            else{
                nNextbtn.setEnabled(true);
                nBackbtn.setEnabled(true);
                nAceptar.setEnabled(false);
                nBackbtn.setVisibility(View.VISIBLE);
                nAceptar.setVisibility(View.INVISIBLE);
                nNextbtn.setVisibility(View.VISIBLE);
                nNextbtn.setText("Siguiente");
                nBackbtn.setText("Atrás");
                nAceptar.setText("");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}