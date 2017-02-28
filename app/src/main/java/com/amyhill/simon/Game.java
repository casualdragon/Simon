package com.amyhill.simon;

import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Handler;
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

    private Random random;
    private Vector<Integer> pattern;
    private final int SIZE;
    private ColorButton [] buttons = new ColorButton[4];
    private boolean type;
    private SoundPool soundPool;
    private boolean isReverse;

    //Constructor
    Game(ColorButton [] buttons, int size, boolean type, SoundPool soundPool){
        SIZE = size;
        for(int i = 0; i < SIZE; i++) {
            this.buttons[i] = buttons[i];
        }
        this.type = type;
        random = new Random();
        pattern = new Vector<>(100);
        pattern.add(random.nextInt(SIZE));
        this.soundPool = soundPool;
        isReverse = false;
    }

    public Vector getPattern(){return pattern;}

    public void run(){
        //Disable onClick for the buttons to display the pattern
        for(int i = 0; i < SIZE; i++){
            buttons[i].setEnabled(false);
        }

        new testAsync().execute();

//        Handler handler = new Handler();
        //Play the pattern
//        runHelper(type);

        //Used to disable buttons long
        //enough to play the pattern
        //Need to work on the timing

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //Re-enable onClick for the buttons to take input for the pattern
//                for(int i = 0; i < SIZE; i++){
//                    buttons[i].setEnabled(true);
//                }
//            }
//        }, 250*SIZE*2);
    }

    // Adds a random int to the pattern
    public void addToPattern(){
        pattern.add(random.nextInt(SIZE));
    }

    /*
        Parameter is to determine if the pattern should
        be done in reverse. Used in run()
     */
    private void runHelper(boolean flag){
        GameActivity gameActivity = new GameActivity();

        if(flag) {
            for (int i = 0; i < pattern.size(); i++) {
                buttons[pattern.get(i)].flashButton(250);
                //gameActivity.playSound(buttons[i]);

            }
        }else{
            for (int i = pattern.size()-1; i <=0 ; i--) {
                buttons[pattern.get(i)].flashButton(250);
                gameActivity.playSound(buttons[i]);
            }
        }
    }


    public void deletePattern(){
        if(!pattern.isEmpty()) {
            pattern.clear();
        }
    }

    public boolean check(int value, int counter){
        if(pattern.size()-1 == counter) {
            addToPattern();
        }
        if (pattern.get(counter%4) == value) {
            counter++;
            return true;
        }
        deletePattern();
        return false;
    }

    class testAsync extends AsyncTask<Void, Integer, Void>{
        @Override
        protected Void doInBackground(Void... params) {

            if(!isReverse) {
                for (int i = 0; i < pattern.size(); i++) {
                    publishProgress(pattern.get(i));
                    try {
                        Thread.sleep(250);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }else{
                for (int i = pattern.size()-1; i >= 0 ; i--) {
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
            buttons[values[0]].flashButton(250);
//            soundPool.play(buttons[values[0]].getSound(), 1.0f, 1.0f, 0, 0, 1.0f);
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
