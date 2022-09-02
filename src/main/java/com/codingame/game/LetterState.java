package com.codingame.game;

public class LetterState {
    private char letter;
    private int position;
    private int state;

    public LetterState(char letter, int state, int position){
        this.letter = letter;
        this.state = state;
        this.position = position;
    }

    public char getLetter() {
        return letter;
    }

    public int getState() {
        return state;
    }

    public int getPosition() {
        return position;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
