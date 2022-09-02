import com.codingame.gameengine.runner.MultiplayerGameRunner;
import com.codingame.gameengine.runner.simulate.GameResult;

public class SkeletonMain {
    public static void main(String[] args) {
        /* Multiplayer Game */
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        // Adds as many player as you need to test your game
//        gameRunner.addAgent("python3 /home/tanvir/IdeaProjects/agents/wordle_multiplayer/input_test.py");
        gameRunner.addAgent("python3 /home/tanvir/IdeaProjects/agents/wordle_multiplayer/arena.py");
        gameRunner.addAgent("python3 /home/tanvir/IdeaProjects/agents/wordle_multiplayer/random_word.py");

        gameRunner.start();
//        GameResult result = gameRunner.simulate();

//        System.out.println(result.summaries.toString());

    }
}
