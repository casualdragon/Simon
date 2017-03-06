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

    public GameRunner.GameType getGameType(int i) {
        return gameType[i];
    }

    private int [] highscore = new int [3];
    private String [] name = new String [3];
    private GameRunner.GameType [] gameType = new GameRunner.GameType[3];
    Context context;
    private String filename = "highscores.txt";

    public Highscore() {
        for (int i = 0; i < highscore.length; i++) {
            highscore[i] = 0;
            name[i] = "";
            gameType[i] = GameRunner.GameType.NORMAL;
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

    public int [] getHighscores(){return highscore;}


    public void writeFile(){
        try{
            FileOutputStream fos = context.openFileOutput("highscores.txt", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);

            oos.close();
            fos.close();
            Log.i("==============", "Game wrote the file");

        }catch (Exception e){
            Log.i("==============", e.getMessage());
        }
    }
    public boolean readFile(){
        try{
            FileInputStream fis = context.openFileInput("highscores.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);

            highscore = ((Highscore) ois.readObject()).getHighscores();

            fis.close();
            ois.close();
            Log.i("==============", "Game readed the file");

        }catch (Exception e){ return false; }
        return true;
    }
}
