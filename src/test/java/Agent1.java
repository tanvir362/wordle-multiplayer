import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Agent1 {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int wordCount = in.nextInt();
        String[] wordSet = new String[wordCount];

        for (int i = 0; i < wordCount; i++) {
            String word = in.next();
            wordSet[i] = word;
        }

        System.err.println(String.join(" ", wordSet));

        // game loop
        while (true) {

            int[] states = new int[5];

            for (int i = 0; i < 5; i++) {
                int state = in.nextInt();
                states[i] = state;
            }

            System.err.println(String.join(" ", Arrays.toString(states).split("[\\[\\]]")[1].split(", ")));

            // Write an answer using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("GUESS");
        }
    }
}
