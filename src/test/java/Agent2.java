import java.util.Random;
import java.util.Scanner;

public class Agent2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int start = scanner.nextInt();
        int end = scanner.nextInt();

        int guess = (start+end)/2;

        while (true) {
            int input = scanner.nextInt();
            if (input == 1) end = guess - 1;
            else if (input == -1) start = guess + 1;

            guess = (start+end)/2;

            System.out.println(guess);
        }
    }
}
