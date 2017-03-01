package com.amyhill.simon;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(10, ContextCompat.getColor(getApplicationContext(), R.color.colorBlue));
        gradientDrawable.setCornerRadius(100);

        gradientDrawable.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlue));

        findViewById(R.id.about_textView).setBackground(gradientDrawable);
    }
}
