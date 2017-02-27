package com.amyhill.simon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GameSelectionActvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        ColorButton normal = (ColorButton) findViewById(R.id.normal_button);
        ColorButton match = (ColorButton) findViewById(R.id.match_button);
        ColorButton swap = (ColorButton) findViewById(R.id.swap_button);
        ColorButton extreme = (ColorButton) findViewById(R.id.extreme_button);

        int radius = 100;

        clickListener listener = new clickListener();
        normal.setUpButton(R.color.colorBlue, R.color.colorBlueFlash, listener, radius);
        match.setUpButton(R.color.colorGreen, R.color.colorGreenFlash, listener, radius);
        swap.setUpButton(R.color.colorYellow, R.color.colorYellowFlash, listener, radius);
        extreme.setUpButton(R.color.colorRed, R.color.colorRedFLash, listener, radius);
    }

    class clickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int duration = 50;
            ColorButton button = (ColorButton) v;
            button.flashButton(duration);

            int viewid = v.getId();

            if (viewid == R.id.normal_button) {
                launchActivity(GameActivity.class);
            }
        }

        private void launchActivity(Class<?> activity){
            Intent intent = new Intent(GameSelectionActvity.this, activity);
            startActivity(intent);
        }
    }

}
