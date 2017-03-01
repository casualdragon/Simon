package com.amyhill.simon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ColorButton play = (ColorButton) findViewById(R.id.play_button);
        ColorButton ins = (ColorButton) findViewById(R.id.ins_button);
        ColorButton about = (ColorButton) findViewById(R.id.about_button);
        ColorButton exit = (ColorButton) findViewById(R.id.exit_button);

        clickListener listener = new clickListener();

        int radius = 100;

        play.setUpButton(R.color.colorBlue, R.color.colorBlueFlash, listener, radius);
        ins.setUpButton(R.color.colorGreen, R.color.colorGreenFlash, listener, radius);
        about.setUpButton(R.color.colorYellow, R.color.colorYellowFlash, listener, radius);
        exit.setUpButton(R.color.colorRed, R.color.colorRedFLash, listener, radius);
    }

    class clickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int duration = 50;
            ColorButton button = (ColorButton) v;
            button.pokeButton(duration);

            int viewid = v.getId();

            if (viewid == R.id.play_button) {
                launchActivity(GameSelectionActvity.class);
            } else if (viewid == R.id.about_button) {
                launchActivity(AboutActivity.class);
            } else if(viewid == R.id.ins_button){
                launchActivity(HowToActivity.class);
            }else if (viewid == R.id.exit_button){
                finish();
            }
        }

        private void launchActivity(Class<?> activity){
            Intent intent = new Intent(MainActivity.this, activity);
            startActivity(intent);
        }
    }

}
