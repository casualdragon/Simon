package com.amyhill.simon;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ColorButton play = (ColorButton) findViewById(R.id.play_button);
        ColorButton ins = (ColorButton) findViewById(R.id.ins_button);
        ColorButton about = (ColorButton) findViewById(R.id.about_button);
        ColorButton exit = (ColorButton) findViewById(R.id.exit_button);

        play.setBaseColor(R.drawable.blue_button);
        ins.setBaseColor(R.drawable.green_button);
        about.setBaseColor(R.drawable.yellow_button);
        exit.setBaseColor(R.drawable.red_button);

        play.setFlashColor(R.drawable.blueflash_button);
        ins.setFlashColor(R.drawable.greenflash_button);
        about.setFlashColor(R.drawable.yellowflash_button);
        exit.setFlashColor(R.drawable.redflash_button);

        clickListener listener = new clickListener();

        play.setOnClickListener(listener);
        ins.setOnClickListener(listener);
        about.setOnClickListener(listener);
        exit.setOnClickListener(listener);
    }

    class clickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int duration = 250;
            ColorButton button = (ColorButton) v;
            button.flashButton(duration);

            int viewid = v.getId();

            if (viewid == R.id.play_button) {
                launchActivity(GameSelectionActvity.class);
            } else if (viewid == R.id.exit_button){
                finish();
            }
        }

        private void launchActivity(Class<?> activity){
            Intent intent = new Intent(MainActivity.this, activity);
            startActivity(intent);
        }
    }

}
