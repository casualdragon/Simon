package com.amyhill.simon;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Amy on 3/2/2017.
 */

public class Highscore  implements Serializable{
    public int getHighscore(int i) {
        return highscore[i];
    }

    public String getName(int i) {
        return name[i];
    }

    public GameActivity.GameType getGameType(int i) {
        return gameType[i];
    }

    private int [] highscore = new int [3];
    private String [] name = new String [3];
    private GameActivity.GameType [] gameType = new GameActivity.GameType[3];
    Context context;
    private String filename = "highscores.txt";

    public Highscore(Context context) {
        for (int i = 0; i < highscore.length; i++) {
            highscore[i] = 0;
            name[i] = "";
            gameType[i] = GameActivity.GameType.NORMAL;
        }
    }



    public void checkHighScore(int value){
        if(value > highscore[0]){
            Log.i("===============", value + " " + highscore[0]);
            highscore[2] = highscore[1];
            highscore[1] = highscore [0];
            highscore[0] = value;

        }else if(value > highscore[1]) {
            Log.i("===============", value + " " + highscore[1]);
            highscore[2] = highscore[1];
            highscore[1] = value;

        }else if(value > highscore[2]){
            Log.i("===============", value + " " + highscore[2]);
            highscore[2] = value;
        }
    }

}
