package com.amyhill.simon;

import java.util.Random;
import java.util.Vector;

/**
 * Created by nonam on 3/6/2017.
 */

public class SimonGame {
    public enum Mode {PREVIOUS, RANDOM}

    private Vector<Integer> pattern;
    private int nextElementChecked;
    private Random random;
    private Mode mode;

    public SimonGame(Mode mode) {
        this.mode = mode;

        nextElementChecked = 0;
        this.pattern = new Vector<>();
        this.random = new Random();
    }

    public void generatePattern(){
        int length = pattern.size();
        if(mode == Mode.PREVIOUS) {
            length = 1;
        } else{
            pattern.clear();
        }

        for (int i = 0; i < length; i++){
            pattern.add(getRandomNumber());
        }

        nextElementChecked = 0;
    }

    public void clearPattern(){
        pattern.clear();
        nextElementChecked = 0;
    }

    public boolean checkPattern(int number){
        if(pattern.elementAt(nextElementChecked).equals((number))){
            nextElementChecked++;
            return true;
        }

        return false;
    }

    private int getRandomNumber(){
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return random.nextInt(4);
    }

    //Getters and Setters
    public int getPatternLength() {
        return pattern.size();
    }

    public int getPatternItem(int item){
        return pattern.elementAt(item);
    }

}
