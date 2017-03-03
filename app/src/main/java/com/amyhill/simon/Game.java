package com.amyhill.simon;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import static java.util.Collections.shuffle;

/**
 * Public Methods
 * run() - executes the pattern for the user but does not take input
 * addTopattern() - adds one number to the current pattern
 * deletePattern() - erases the vector
 *
 *
 */

public class Game {
    //Fields

    private final int SIZE = 4;
    private final int duration = 350;
    private final int radius = 10;
    private final int extremeDuration = 50;
    private final Random random = new Random();

    private SoundPool soundPool;
    private GameActivity.GameType gameType;
    private ColorButton fail;
    private ColorButton success;
    private Context context;
    private PatternPlayer playerThread;


    private ColorButton [] buttons = new ColorButton[4];
    public final int [] baseColorIDs = {R.color.colorBlue, R.color.colorGreen, R.color.colorYellow, R.color.colorRed};
    public final int [] flashColorIDs = {R.color.colorBlueFlash, R.color.colorGreenFlash, R.color.colorYellowFlash, R.color.colorRedFLash};
    private Vector<Integer> pattern;
    private Vector <Integer> patternUser = new Vector<Integer>(100);

    private Highscore highscores = new Highscore(context);

    //Constructor
    Game(Context context, ColorButton [] buttons, GameActivity.GameType gameType, ColorButton success, ColorButton fail, Highscore highscores){
        this.buttons = buttons;
        pattern = new Vector<>(100);

        this.gameType = gameType;
        this.success = success;
        this.fail = fail;
        this.context = context;
        this.highscores = highscores;

        startSoundPool();
        setupButtons();
    }

    private void setupButtons()
    {
        playGameClickListener listener = new playGameClickListener();

        //Sets buttons for extreme or normal mode.

        setDefaultButtons(listener);


        if (gameType == GameActivity.GameType.COLOR || gameType == GameActivity.GameType.POSITION) {
            shuffleButtons(listener);
        }
    }

    private void setDefaultButtons(playGameClickListener listener) {
        if(gameType == GameActivity.GameType.EXTREME){
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setUpButton(baseColorIDs[3], flashColorIDs[3], listener, radius);
            }
            fail.setUpButton(R.color.colorRed, R.color.colorRedFLash, null, radius);
            success.setUpButton(R.color.colorRed, R.color.colorGreenFlash, null, radius);
        }
        else{
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setUpButton(baseColorIDs[i], flashColorIDs[i], listener, radius);
            }
            fail.setUpButton(R.color.colorRed, R.color.colorRedFLash, null, radius);
            success.setUpButton(R.color.colorGreen, R.color.colorGreenFlash, null, radius);
        }

        for (int i = 0; i < 4; i++) {
            buttons[i].setEnabled(false);
            buttons[i].setId(i);
            buttons[i].setSoundPool(soundPool);
        }
    }

    private void shuffleButtons(View.OnClickListener listener){
        ArrayList<Integer> numbers = new ArrayList<Integer>();

        for(int i = 0; i < SIZE; i++){
            numbers.add(i);
        }
        shuffle(numbers);

        if(listener == null) {
            for(int i = 0; i < SIZE; i++) {
                buttons[i].setColorID(baseColorIDs[numbers.get(i)]);
                buttons[i].setFlashColorID(flashColorIDs[numbers.get(i)]);
            }
        } else {
            if(gameType == GameActivity.GameType.COLOR) {
                for (int i = 0; i < SIZE; i++) {
                    buttons[i].setUpButton(baseColorIDs[numbers.get(i)], flashColorIDs[numbers.get(i)], listener, radius);
                }
            }else if (gameType == GameActivity.GameType.POSITION){
                for (int i = 0; i < SIZE; i++) {
                    buttons[i].setUpButton(baseColorIDs[numbers.get(i)], flashColorIDs[numbers.get(i)], listener, radius);
                }
            }
        }
    }


    public void run(){
        //Starts Async Task that plays pattern
        addToPattern();
        readFile();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playerThread = new PatternPlayer();
                playerThread.execute();
            }
        }, 550);

    }

    public void stop(){
        if(playerThread != null){
            playerThread.cancel(true);
        }
    }

    // Adds a random int to the pattern
    public void addToPattern() {
        if(gameType == GameActivity.GameType.COLOR) {
            pattern.add(buttons[random.nextInt(SIZE)].getColorID());
        } else{
            pattern.add(random.nextInt(SIZE));
        }
    }

    public void deletePattern(){
        if(!pattern.isEmpty()) {
            pattern.clear();
        }
    }

    public void toggleButtons(boolean enabled) {
        for (int i = 0; i < SIZE; i++) {
            buttons[i].setEnabled(enabled);
        }
    }




    //Sets up sound pool
    private void startSoundPool() {

        AudioAttributes.Builder builder = new AudioAttributes.Builder();
        builder.setUsage(AudioAttributes.USAGE_GAME);

        SoundPool.Builder spbuilder = new SoundPool.Builder();
        spbuilder.setAudioAttributes(builder.build());
        spbuilder.setMaxStreams(4);
        soundPool = spbuilder.build();
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if(status == 0){
                }
            }
        });

        buttons[0].setSound(soundPool.load(context, R.raw.a_piano, 1));
        buttons[1].setSound(soundPool.load(context, R.raw.c_piano, 1));
        buttons[2].setSound(soundPool.load(context, R.raw.f_sharp_piano, 1));
        buttons[3].setSound(soundPool.load(context, R.raw.g_piano, 1));
    }

    private void loseDialog(){
        stop();
        pattern.clear();
        patternUser.clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("Play", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                run();
            }
        });
        readFile();
        builder.setMessage("Top Highscores\n1st: " +  highscores.getHighscore(0) + " "
                + highscores.getName(0)+ " " + highscores.getGameType(0) + "\n2nd: "
                + highscores.getHighscore(1)+ " " + highscores.getName(1) + " " + highscores.getGameType(1)
                + "\n3rd: " + highscores.getHighscore(2) + " " + highscores.getName(2)+ " "
                + highscores.getGameType(2));
        builder.setTitle("You Lost");

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private class PatternPlayer extends AsyncTask<Void, Integer, Void>{
        @Override
        protected Void doInBackground(Void... params) {
                /*
                Determines if the pattern should
                be done in reverse.
               */

            if(gameType == GameActivity.GameType.EXTREME) {
                for (int i = pattern.size()-1; i >= 0 ; i--) {
                    if (sleepPPThread(extremeDuration)) return null;
                    publishProgress(pattern.get(i));
                    if (sleepPPThread(100)) return null;

                }
            }else{
                for (int i = 0; i < pattern.size(); i++) {
                    if (sleepPPThread(duration)) return null;
                    publishProgress(pattern.get(i));
                    if (sleepPPThread(100)) return null;
                }
            }
            return null;
        }

        private boolean sleepPPThread(int duration) {
            try{
                Thread.sleep(duration);
            } catch (InterruptedException e){
                return true;
            }
            return false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            toggleButtons(false);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(gameType == GameActivity.GameType.COLOR){
                for(ColorButton button: buttons){
                    if(button.getColorID() == values[0]){
                        button.pokeButton(duration);
                    }
                }
            }else {
                if(gameType == GameActivity.GameType.EXTREME){
                    buttons[values[0]].pokeButton(extremeDuration);
                }else {
                    buttons[values[0]].pokeButton(duration);
                }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            toggleButtons(true);
        }
    }

    //Onclick lister for game buttons.
    class playGameClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            boolean doesSuccessFlash = true;
            ColorButton button = (ColorButton) v;

            button.pokeButton(duration);

            if(gameType == GameActivity.GameType.COLOR || gameType == GameActivity.GameType.POSITION) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shuffleButtons(null);

                    }
                }, duration);
            }

//            if(patternUser.size() == 0) {
//                addToPattern();
//            }
//            //code for testing bugs
//            for(int i =0; i < pattern.size(); i++){
//                Log.i("==================", i+": "+pattern.get(i));
//            }
//            for(int i = 0; i < highscores.length; i++){
//                Log.i("==============", "highscore"+(i+1)+":"+ highscores[i]);
//            }
            if(gameType == GameActivity.GameType.COLOR){
                patternUser.add(button.getColorID());
            } else {
                patternUser.add(button.getID());
            }
            for(int i = 0; i < patternUser.size(); i++){
                if(!patternUser.get(i).equals(pattern.get(i))){
                    readFile();
                    highscores.checkHighScore(pattern.size()-1);
                    writeFile();
                    patternUser.clear();
                    deletePattern();
                    fail.pokeButton(duration);
                    for(ColorButton item : buttons){
                        item.playSound();
                    }
                    run();
                    toggleButtons(false);
                    loseDialog();
                    return;
                }
            }
            if(patternUser.size() == pattern.size() && patternUser.size() != 0){
                if(doesSuccessFlash) {
                    success.pokeButton(duration);
                }
                run();
                patternUser.clear();
                toggleButtons(false);
                return;
            }

        }
    }

    public void writeFile(){
        try{
            FileOutputStream fos = context.openFileOutput("highscores.txt", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(highscores);

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

            highscores = (Highscore) ois.readObject();

            fis.close();
            ois.close();
            Log.i("==============", "Game readed the file");

        }catch (Exception e){ return false; }
        return true;
    }


}
