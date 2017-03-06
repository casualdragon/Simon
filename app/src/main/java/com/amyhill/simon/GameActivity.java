package com.amyhill.simon;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class GameActivity extends AppCompatActivity {
    public static final String MODE_NAME = "MODE_NAME";

    private GameRunner.GameType gameType;
    private ColorButton [] buttons;
    private GameRunner gameRunner;
    private SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_game);

        //Gets the gamemode from the intent.
        if (getIntent().hasExtra(MODE_NAME)) {
            gameType = (GameRunner.GameType) getIntent().getSerializableExtra(MODE_NAME);
        } else {
            gameType = GameRunner.GameType.NORMAL;
        }

        //Gets buttons
        ColorButton inner = (ColorButton) findViewById(R.id.blue_game_button);
        ColorButton midInner = (ColorButton) findViewById(R.id.green_game_button);
        ColorButton midOuter = (ColorButton) findViewById(R.id.yellow_game_button);
        ColorButton outer = (ColorButton) findViewById(R.id.red_game_button);
        ColorButton fail = (ColorButton) findViewById(R.id.fail_button);
        ColorButton success = (ColorButton) findViewById(R.id.success_button);

        buttons = new ColorButton[]{inner, midInner, midOuter, outer, fail, success};

        startButtons();
        startSoundPool();

        gameRunner = new GameRunner(buttons, gameType);
        gameRunner.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //startSoundPool();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gameRunner.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameRunner.stop();
    }

    private void startButtons() {
        GameClick listener = new GameClick();
        if(gameType == GameRunner.GameType.EXTREME){
            buttons[0].setUpButton(ColorButton.Color.RED, listener, 8);
            buttons[1].setUpButton(ColorButton.Color.RED, listener, 8);
            buttons[2].setUpButton(ColorButton.Color.RED, listener, 8);
            buttons[3].setUpButton(ColorButton.Color.RED, listener, 8);
            buttons[4].setUpButton(ColorButton.Color.RED, null, 8);
            buttons[5].setUpButton(ColorButton.Color.GREEN, null, 8);
            buttons[5].redTheButton();
        } else {
            buttons[0].setUpButton(ColorButton.Color.BLUE, listener, 8);
            buttons[1].setUpButton(ColorButton.Color.GREEN, listener, 8);
            buttons[2].setUpButton(ColorButton.Color.YELLOW, listener, 8);
            buttons[3].setUpButton(ColorButton.Color.RED, listener, 8);
            buttons[4].setUpButton(ColorButton.Color.RED, null, 8);
            buttons[5].setUpButton(ColorButton.Color.GREEN, null, 8);
        }

        if(gameType == GameRunner.GameType.EXTREME){

        }
    }

    //Sets up sound pool
    private void startSoundPool() {

        AudioAttributes.Builder builder = new AudioAttributes.Builder();
        builder.setUsage(AudioAttributes.USAGE_GAME);

        SoundPool.Builder spbuilder = new SoundPool.Builder();
        spbuilder.setAudioAttributes(builder.build());
        spbuilder.setMaxStreams(10);
        soundPool = spbuilder.build();
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if(status == 0){
                }
            }
        });

        for(ColorButton button: buttons){
            button.setSoundPool(soundPool);
        }

        buttons[0].setSoundID(soundPool.load(this, R.raw.a_piano, 1));
        buttons[1].setSoundID(soundPool.load(this, R.raw.c_piano, 1));
        buttons[2].setSoundID(soundPool.load(this, R.raw.f_sharp_piano, 1));
        buttons[3].setSoundID(soundPool.load(this, R.raw.g_piano, 1));
    }

    class GameClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            gameRunner.play((ColorButton) v);
        }
    }

}

