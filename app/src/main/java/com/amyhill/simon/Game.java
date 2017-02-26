package com.amyhill.simon;

import java.util.Random;
import java.util.Vector;

/**
 * Created by Amy on 2/21/2017.
 */

public class Game {
    //Fields
    private ColorButton [] buttons = new ColorButton[4];
    private Random random;
    private Vector<Integer> pattern;
    private final int SIZE;

    /*
        true = normal
        false = extreme
    */
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

        //Re-enable onClick for the buttons to take input for the pattern
        for(int i = 0; i < SIZE; i++){
            buttons[i].setEnabled(true);
        }
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
            }
        }else{
            for (int i = pattern.size()-1; i <=0 ; i--) {
                buttons[pattern.get(i)].flashButton(250);
                buttons[pattern.get(i)].playSound(250);
            }
        }
    }

}
