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
    private final String FILE_NAME = "highscores1.txt";
    private int [] highscore = new int [3];
    private String [] name = new String [3];
    private GameRunner.GameType [] gameType = new GameRunner.GameType[3];


    public Highscore() {
        for (int i = 0; i < highscore.length; i++) {
            highscore[i] = 0;
            name[i] = "";
            gameType[i] = GameRunner.GameType.NORMAL;
        }
    }

    public void checkHighScore(int value, GameRunner.GameType gameType){
        if(value >= highscore[0]){
            Log.i("===============", value + " " + highscore[0]);
            highscore[2] = highscore[1];
            this.gameType[2] = this.gameType[1];
            highscore[1] = highscore [0];
            this.gameType[1] = this.gameType[0];
            highscore[0] = value;
            this.gameType[0] = gameType;

        }else if(value > highscore[1]) {
            Log.i("===============", value + " " + highscore[1]);
            highscore[2] = highscore[1];
            this.gameType[2] = this.gameType[1];
            highscore[1] = value;
            this.gameType[1] = gameType;

        }else if(value > highscore[2]){
            Log.i("===============", value + " " + highscore[2]);
            highscore[2] = value;
            this.gameType[2] = gameType;
        }
    }

    public int [] getHighscores(){return highscore;}


    public void writeFile(Context context){
        try{
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);

            oos.close();
            fos.close();
            Log.i("==============", "Game wrote the file");

        }catch (Exception e){
            Log.i("==============", e.getMessage());
        }
    }

    public boolean readFile(Context context){
        try{
            FileInputStream fis = context.openFileInput(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Highscore high = (Highscore) ois.readObject();

            //highscore = ((Highscore) ois.readObject()).getHighscores();
            highscore = high.getHighscores();
            gameType = high.getGameType();

            fis.close();
            ois.close();
            Log.i("==============", "Game read the file");

        }catch (Exception e){
            return false;
        }
        return true;
    }

    public int getHighscore(int i) {
        return highscore[i];
    }

    public String getName(int i) {
        return name[i];
    }

    public GameRunner.GameType getGameType(int i) {
        Log.i("=====================", "GameType: " + gameType[i]);
        return gameType[i];
    }
    public GameRunner.GameType [] getGameType() {
        Log.i("=====================", "GameType: " + gameType);
        return gameType;
    }
}
