package com.amyhill.simon;

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
    private ColorButton [] buttons = new ColorButton[4];
    private Random random;
    private Vector<Integer> pattern;
    private final int SIZE;
    private boolean type;

    //Constructor
    Game(ColorButton [] buttons, int size, boolean type){
        SIZE = size;
        for(int i = 0; i < SIZE; i++) {
            this.buttons[i] = buttons[i];
        }
        this.type = type;
        random = new Random();
        pattern = new Vector<>(100);
        pattern.add(random.nextInt(SIZE));
    }

    public void run(){
        //Disable onClick for the buttons to display the pattern
        for(int i = 0; i < SIZE; i++){
            buttons[i].setEnabled(false);
        }

        //Play the pattern
        if(type){
            runHelper(true);
        }else{
            runHelper(false);
        }

        //Used to disable buttons long
        //enough to play the pattern
        //Need to work on the timing
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Re-enable onClick for the buttons to take input for the pattern
                for(int i = 0; i < SIZE; i++){
                    buttons[i].setEnabled(true);
                }
            }
        }, 250*SIZE*2);
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
        if(flag) {
            for (int i = 0; i < pattern.size(); i++) {
                buttons[pattern.get(i)].flashButton(250);
                buttons[pattern.get(i)].playSound(250);
                Log.i("=================", i + "");
            }
        }else{
            for (int i = pattern.size()-1; i <=0 ; i--) {
                buttons[pattern.get(i)].flashButton(250);
                buttons[pattern.get(i)].playSound(250);
            }
        }
    }

    public void deletePattern(){
        pattern.clear();
        addToPattern();
    }

    public boolean check(int position, int value){
        if(pattern.get(position) == value){
            return true;
        }
        return false;
    }
}
