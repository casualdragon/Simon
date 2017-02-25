package com.amyhill.simon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_game);

        ColorButton inner = (ColorButton) findViewById(R.id.blue_game_button);
        ColorButton midInner = (ColorButton) findViewById(R.id.green_game_button);
        ColorButton midOuter = (ColorButton) findViewById(R.id.yellow_game_button);
        ColorButton outer = (ColorButton) findViewById(R.id.red_game_button);

        inner.setBaseColor(R.drawable.blue_game_button);
        midInner.setBaseColor(R.drawable.green_game_button);
        midOuter.setBaseColor(R.drawable.yellow_game_button);
        outer.setBaseColor(R.drawable.red_game_button);

        inner.setFlashColor(R.drawable.blueflash_game_button);
        midInner.setFlashColor(R.drawable.greenflash_game_button);
        midOuter.setFlashColor(R.drawable.yellowflash_game_button);
        outer.setFlashColor(R.drawable.redflash_game_button);

        gameClickListener listener = new gameClickListener();

        inner.setOnClickListener(listener);
        midInner.setOnClickListener(listener);
        midOuter.setOnClickListener(listener);
        outer.setOnClickListener(listener);
    }

    class gameClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int duration = 250;
            ColorButton button = (ColorButton) v;
            button.flashButton(duration);

            int viewid = v.getId();
                    }

        private void launchActivity(Class<?> activity) {
            Intent intent = new Intent(GameActivity.this, activity);
            startActivity(intent);
        }
    }
}

