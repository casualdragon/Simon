package com.amyhill.simon;


import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;

import static java.util.Collections.shuffle;

public class GameActivity extends AppCompatActivity {
    public static final String MODE_NAME = "MODE_NAME";
    public static final String HIGHSCORE = "HIGHSCORE";

    public enum GameType {NORMAL, COLOR, POSITION, EXTREME}




    private Game game;
    private GameType gameType;
    private ColorButton [] buttons;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_game);


        //Gets the gamemode from the intent.
        if (getIntent().hasExtra(MODE_NAME)) {
            gameType = (GameType) getIntent().getSerializableExtra(MODE_NAME);
        } else {
            gameType = GameType.NORMAL;
        }

        //Gets buttons
        ColorButton inner = (ColorButton) findViewById(R.id.blue_game_button);
        ColorButton midInner = (ColorButton) findViewById(R.id.green_game_button);
        ColorButton midOuter = (ColorButton) findViewById(R.id.yellow_game_button);
        ColorButton outer = (ColorButton) findViewById(R.id.red_game_button);
        ColorButton fail = (ColorButton) findViewById(R.id.fail_button);
        ColorButton success = (ColorButton) findViewById(R.id.success_button);

        buttons = new ColorButton[]{inner, midInner, midOuter, outer};
        game = new Game(this, buttons, gameType, success, fail);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                game.run();
            }
        }, 750);


    }


    //Saves high score data to bundle
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle persistableBundle) {
        persistableBundle.putIntArray(HIGHSCORE,game.getHighscores());
        super.onSaveInstanceState(outState);
    }

    //Sets high score data from bundle.
    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        if(persistentState != null){
            game.setHighscores(persistentState.getIntArray(HIGHSCORE));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //startSoundPool();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopGameThreads();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopGameThreads();
    }

    private void stopGameThreads() {
        if(game != null){
            game.stop();
        }
    }

    //Checks the highscores and updates if necessary.


    private void loseDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("Play", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                game.addToPattern();
                game.run();
            }
        });
        builder.setNegativeButton("Level Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setTitle("You Lost");

    }

}

