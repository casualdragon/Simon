package com.amyhill.simon;

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

        normal.setBaseColor(R.drawable.blue_button);
        match.setBaseColor(R.drawable.green_button);
        swap.setBaseColor(R.drawable.yellow_button);
        extreme.setBaseColor(R.drawable.red_button);

        normal.setFlashColor(R.drawable.blueflash_button);
        match.setFlashColor(R.drawable.greenflash_button);
        swap.setFlashColor(R.drawable.yellowflash_button);
        extreme.setFlashColor(R.drawable.redflash_button);

        clickListener listener = new clickListener();

        normal.setOnClickListener(listener);
        match.setOnClickListener(listener);
        swap.setOnClickListener(listener);
        extreme.setOnClickListener(listener);
    }

    class clickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int duration = 250;
            ColorButton button = (ColorButton) v;
            button.flashButton(duration);
        }
    }

}
