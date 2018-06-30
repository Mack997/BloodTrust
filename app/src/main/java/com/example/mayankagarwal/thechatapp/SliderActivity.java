package com.example.mayankagarwal.thechatapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SliderActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Button next, skip;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private IntroManager introManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        introManager = new IntroManager(this);

        if (!IntroManager.Check()){
            introManager.setFirst(false);
            Intent i = new Intent(SliderActivity.this,StartActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(SliderActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }

        if (Build.VERSION.SDK_INT>=21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.view_pager);
        mViewPager = (ViewPager)findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.dots_layout);
        skip = (Button) findViewById(R.id.skip);
        next = (Button) findViewById(R.id.next);

        layouts = new int[]{R.layout.screen1, R.layout.screen2, R.layout.screen3, R.layout.screen4};

        addBottomDots(0);
       changeStatusBarColor();

        viewPagerAdapter = new ViewPagerAdapter();

        mViewPager.setAdapter(viewPagerAdapter);

        mViewPager.addOnPageChangeListener(viewListener);
        skip.setOnClickListener(new View.OnClickListener() {
          @Override
            public void onClick(View view) {
                Intent i = new Intent(SliderActivity.this, StartActivity.class);
                startActivity(i);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(+1);
                if (current < layouts.length) {
                   mViewPager.setCurrentItem(current);
                } else {
                    Intent i = new Intent(SliderActivity.this, StartActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }


    private int getItem(int i) {
        return mViewPager.getCurrentItem() + 1;
    }



    private void addBottomDots(int position){

        dots = new TextView[layouts.length];
        dotsLayout.removeAllViews();

        for (int i=0; i<dots.length; i++)
        {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(36);
            dots[i].setTextColor(getResources().getColor(R.color.darkGrey));
            dotsLayout.addView(dots[i]);
        }
        if (dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.lightGrey));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addBottomDots(position);
            if (position == layouts.length - 1) {
                next.setText("Proceed");
                skip.setVisibility(View.GONE);
            } else {
                next.setText("Next");
                skip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
          window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

           layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(layouts[position], container, false);
            container.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }
    }


    private void sendToHome() {
        Intent homeIntent = new Intent(SliderActivity.this ,MainActivity.class);
        startActivity(homeIntent);
        finish();
    }

}