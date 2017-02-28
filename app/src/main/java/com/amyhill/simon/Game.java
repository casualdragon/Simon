package com.amyhill.simon;

import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import java.util.Random;
import java.util.Vector;

/**
 * Public Methods
 * run() - executes the pattern for the user but does not take input
 * addTopattern() - adds one number to the current pattern
 * deletePattern() - erases the vector
 * check() - used to check if the button pressed is corrected
 *          *This relies on the buttons position in the array
 *
 */

public class Game {
    //Fields
    private final int SIZE;
    private int duration;
    private boolean isReverse;
    private Random random;
    private Vector<Integer> pattern;
    private ColorButton [] buttons = new ColorButton[4];


    //Constructor
    Game(ColorButton [] buttons, int size, boolean type, SoundPool soundPool, int duration){
        SIZE = size;
        for(int i = 0; i < SIZE; i++) {
            this.buttons[i] = buttons[i];
        }
        random = new Random();
        pattern = new Vector<>(100);
        pattern.add(random.nextInt(SIZE));
        isReverse = false;
        this.duration = duration;
    }

    public void run(){
        //Starts Async Task that plays pattern

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new PatternPlayer().execute();
            }
        }, 500);

    }

    // Adds a random int to the pattern
    public void addToPattern() {
        pattern.add(random.nextInt(SIZE));
    }

    public void deletePattern(){
        if(!pattern.isEmpty()) {
            pattern.clear();
        }
    }

    public Vector<Integer> getPattern() {
        return pattern;
    }

    private class PatternPlayer extends AsyncTask<Void, Integer, Void>{
        @Override
        protected Void doInBackground(Void... params) {
                /*
                Determines if the pattern should
                be done in reverse.
               */
            if(!isReverse) {
                for (int i = 0; i < pattern.size(); i++) {
                    try{
                        Thread.sleep(250);
                    } catch (InterruptedException e){
                        return null;

                    }
                    publishProgress(pattern.get(i));

                }

            }else{
                for (int i = pattern.size()-1; i >= 0 ; i--) {
                    try{
                        Thread.sleep(250);
                    } catch (InterruptedException e){
                        return null;

                    }
                    publishProgress(pattern.get(i));

                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            toggleButtons(false);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            buttons[values[0]].pokeButton(250);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            toggleButtons(true);
        }

        private void toggleButtons(boolean enabled) {
            for (int i = 0; i < SIZE; i++) {
                buttons[i].setEnabled(enabled);
            }
        }
    }

}
