package com.codingame.game;

import java.util.HashMap;

public class Constant {

    public static final String WORDS_FILE_PATH = "no_duplicate_letters.txt";
    public static final int WORD_COUNT = 4995;
    public static final int WORD_LEN = 5;

    public static final char UNKNOWN_LETTER = '-';
    public static final int UNKNOWN_STATE = 0;
    public static final int ABSENT_STATE = 1;
    public static final int POSITION_UNKNOWN_STATE = 2;
    public static final int ALL_KNOWN_STATE = 3;
    public static final int UNKNOWN_POSITION = 0;
    public static final int ABSENT_POSITION = 999;

    public static final HashMap<Integer, Integer> STATE_WEIGHT = new HashMap<Integer, Integer>(){{
        put(UNKNOWN_STATE, 0);
        put(ABSENT_STATE, 0);
        put(POSITION_UNKNOWN_STATE, 2);
        put(ALL_KNOWN_STATE, 5);
    }};
}
