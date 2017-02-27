package com.amyhill.simon;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Vector;

public class GameActivity extends AppCompatActivity {
    public enum GameType {NORMAL, COLOR, POSITION, EXTREME}

    private Game game;
    private int [] highscores = new int []{0,0,0};
    private final String HIGHSCORE = "HIGHSCORE";
    private SoundPool soundPool;
    private ColorButton [] color;
    private Vector patternAI;
    private Vector <Integer> patternUser = new Vector<Integer>(100);
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_game);

        counter = 1;

        ColorButton inner = (ColorButton) findViewById(R.id.blue_game_button);
        ColorButton midInner = (ColorButton) findViewById(R.id.green_game_button);
        ColorButton midOuter = (ColorButton) findViewById(R.id.yellow_game_button);
        ColorButton outer = (ColorButton) findViewById(R.id.red_game_button);
        ColorButton fail =  (ColorButton)findViewById(R.id.fail_button);
        ColorButton success = (ColorButton) findViewById(R.id.success_button);

        playGameClickListener listener = new playGameClickListener();

        int radius = 10;

        inner.setUpButton(R.color.colorBlue, R.color.colorBlueFlash, listener, radius);
        midInner.setUpButton(R.color.colorGreen, R.color.colorGreenFlash, listener, radius);
        midOuter.setUpButton(R.color.colorYellow, R.color.colorYellowFlash, listener, radius);
        outer.setUpButton(R.color.colorRed, R.color.colorRedFLash, listener, radius);

        color = new ColorButton[] {inner, midInner, midOuter, outer};

        for(int i = 0; i < 4; i++){
            color[i].setEnabled(false);
            color[i].setId(i);
        }
        game = new Game(color, 4, true, soundPool);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                game.run();
            }
        }, 1250);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle persistableBundle) {
        persistableBundle.putIntArray(HIGHSCORE,highscores);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        if(persistentState != null){
            highscores = persistentState.getIntArray(HIGHSCORE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AudioAttributes.Builder builder = new AudioAttributes.Builder();
        builder.setUsage(AudioAttributes.USAGE_GAME);

        SoundPool.Builder spbuilder = new SoundPool.Builder();
        spbuilder.setAudioAttributes(builder.build());
        spbuilder.setMaxStreams(1);
        soundPool = spbuilder.build();
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if(status == 0){
                }
            }
        });
        ColorButton inner = (ColorButton) findViewById(R.id.blue_game_button);
        ColorButton midInner = (ColorButton) findViewById(R.id.green_game_button);
        ColorButton midOuter = (ColorButton) findViewById(R.id.yellow_game_button);
        ColorButton outer = (ColorButton) findViewById(R.id.red_game_button);

        inner.setSound(soundPool.load(this, R.raw.a_piano, 1));
        midInner.setSound(soundPool.load(this, R.raw.c_piano, 1));
        midOuter.setSound(soundPool.load(this, R.raw.f_sharp_piano, 1));
        outer.setSound(soundPool.load(this, R.raw.g_piano, 1));

    }
    public void playSound(ColorButton button){
        if(soundPool != null) {
            soundPool.play(button.getSound(), 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    class playGameClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            ColorButton button = (ColorButton) v;

            flashAndNoise(button);
            if(patternUser.size() == 0) {
                game.addToPattern();
                patternAI = game.getPattern();
            }
            for(int i =0; i < patternAI.size(); i++){
                Log.i("==================", i+": "+patternAI.get(i));
            }
            for(int i = 0; i < highscores.length; i++){
                Log.i("==============", "highscore"+(i+1)+":"+highscores[i]);
            }
            patternUser.add(button.getID());
            for(int i = 0; i < patternUser.size(); i++){
                if(patternUser.get(i) != patternAI.get(i)){
                    checkHighScore(patternAI.size()-1);
                    patternUser.clear();
                    game.deletePattern();
                    game.addToPattern();
                    run();
                }
            }
            if(patternUser.size() == patternAI.size()-1 && patternUser.size() != 0){
                run();
                patternUser.clear();
            }
        }
    }

    private void flashAndNoise(ColorButton button){
        int duration = 250;
        button.flashButton(duration);
        playSound(button);
    }

    private void checkHighScore(int value){
        if(value > highscores[0]){
            highscores[2] = highscores[1];
            highscores[1] = highscores [0];
            highscores[0] = value;
        }else if(value > highscores[1]) {
            highscores[2] = highscores[1];
            highscores[1] = value;
        }else if(value > highscores[2]){
            highscores[2] = value;
        }
    }

    private void run(){
        //Disable onClick for the buttons to display the pattern
        for(int i = 0; i < color.length; i++){
            color[i].setEnabled(false);
        }

        Handler handler = new Handler();
        //Play the pattern
        runHelper(true);

        //Used to disable buttons long
        //enough to play the pattern
        //Need to work on the timing

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Re-enable onClick for the buttons to take input for the pattern
                for(int i = 0; i < color.length; i++){
                    color[i].setEnabled(true);
                }
            }
        }, 250*color.length*2);
    }
    private void runHelper(boolean flag){

        if(flag) {
            for (int i = 0; i < patternAI.size(); i++) {
                flashAndNoise(color[(int)patternAI.get(i)]);
            }
        }else{
            for (int i = patternAI.size()-1; i <=0 ; i--) {
                flashAndNoise(color[(int)patternAI.get(i)]);
            }
        }
    }
}

