package com.codingame.game;
import java.io.*;
import java.util.*;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;
import com.google.inject.Inject;

/*
* ideas to work with:
* 1. duplicate letter in word
* 2. if a letter is unlocked for a position remove the position and continue the turn with the new word
*
*/

public class Referee extends AbstractReferee {
    // Uncomment the line below and comment the line under it to create a Solo Game
    // @Inject private SoloGameManager<Player> gameManager;
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;
    @Inject private EndScreenModule endScreenModule;




    private String magicalWord; //the word needs to guess

    private int calculateScore(Player player, String output){

        if(magicalWord.equals(output)) return 50;

        int score = player.getAlphabetStates()
                .values()
                .stream()
                .mapToInt(v -> Constant.STATE_WEIGHT.get(v.getState()))
                .sum();

        return  score;
    }

    //process output and update player's states
    private void process_output(Player player, String output){
        if (output.length() != Constant.WORD_LEN){
            player.deactivate(String.format("%s provides invalid output", player.getNicknameToken()));

            return;
        }

        String[] report = new String[Constant.WORD_LEN];
        LetterState[] letterStates = new LetterState[Constant.WORD_LEN];
        //preparing player's next turninput state and generating report for current turn
        for(int i=0; i<Constant.WORD_LEN; i++){
            char ch = output.charAt(i);
            int state, pos;

            if(magicalWord.charAt(i) == ch){
                state = Constant.ALL_KNOWN_STATE;
                pos = i+1;
            }
            else if(magicalWord.indexOf(ch) != -1){
                state = Constant.POSITION_UNKNOWN_STATE;
                pos = -(i+1);
            }
            else{
                state = Constant.ABSENT_STATE;
                pos = Constant.ABSENT_POSITION;
            }

            player.updateTurnInput(i, new LetterState(ch, state, pos));
            report[i] = String.format("(%c, %d)", ch, state);

            letterStates[i] = new LetterState(ch, state, pos);

        }
        gameManager.addToGameSummary(
                String.format("%s guesses %s: %s", player.getNicknameToken(), output, String.join(" ", report))
        );

        player.outputHistory.add(letterStates);
        if(player.outputHistory.size() > GraphicHandler.BOARD_HEIGHT){
            player.outputHistory.remove(0);
        }

        //updating player alphabet state
        //aren't updating a letters state if previously reviled it's better state
        for(int i=0; i<Constant.WORD_LEN; i++){
            char ch = output.charAt(i);
            int ch_prev_state = player.getAlphabetStates().get(ch).getState();

            if (magicalWord.charAt(i) == ch){
                player.updateAlphabetStates(ch, Constant.ALL_KNOWN_STATE, i+1);
            }
            else if(magicalWord.indexOf(ch)!=-1 && ch_prev_state<=Constant.POSITION_UNKNOWN_STATE){
                player.updateAlphabetStates(ch, Constant.POSITION_UNKNOWN_STATE, -(i+1));
            }
            else if(ch_prev_state==Constant.UNKNOWN_STATE) {
                player.updateAlphabetStates(ch, Constant.ABSENT_STATE, Constant.ABSENT_POSITION);
            }
        }

    }

    private void setMagicalWord(){
        List<String> wordSet = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(Constant.WORDS_FILE_PATH)));
            for(int i=0; i<Constant.WORD_COUNT; i++){
                wordSet.add(reader.readLine());
            }

            Random rand = new Random();
            int rIndex = rand.nextInt(Constant.WORD_COUNT);
            this.magicalWord = wordSet.get(rIndex).toUpperCase();
//            this.magicalWord = "ABCDE";

            for(Player player: gameManager.getActivePlayers()){
                player.sendInputLine(String.format("%d", Constant.WORD_COUNT));

                Collections.shuffle(wordSet);

                player.sendInputLine(String.join(" ", wordSet));

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(String.format("%s %d", magicalWord, magicalWord.length()));

    }

    @Override
    public void init() {
        // Initialize your game here.
        gameManager.setFrameDuration(1300);
        setMagicalWord();

        GraphicHandler.drawBackgroundImage(graphicEntityModule);
        GraphicHandler.drawGrids(gameManager, graphicEntityModule);
        GraphicHandler.drawHud(gameManager, graphicEntityModule);

    }

    @Override
    public void gameTurn(int turn) {
        //sending input and execute
        for (Player player : gameManager.getActivePlayers()) {
            player.sendInputLine(player.generateInputLine());

            player.execute();
        }


        //getting outputs and process
        for(Player player: gameManager.getActivePlayers()){
            try {
                List<String> outputs = player.getOutputs();
                String output = outputs.get(0).toUpperCase();

                process_output(player, output);
                GraphicHandler.drawLiveBoard(player, graphicEntityModule);

                int score = calculateScore(player, output);
                player.setScore(score);
                gameManager.addToGameSummary(String.format("%s score: %d", player.getNicknameToken(), score));

                if(magicalWord.equals(output)){
                    player.setWInner(true);
                }
            } catch (TimeoutException e) {
                player.deactivate(String.format("$%d timeout!", player.getIndex()));
            }
        }

        //checking winning and end game
        Player p0 = gameManager.getPlayer(0);
        Player p1 = gameManager.getPlayer(1);


        if(p0.isWInner() || p1.isWInner()){
            gameManager.endGame();
        }
    }


    @Override
    public void onEnd() {
        endScreenModule.setScores(gameManager.getPlayers().stream().mapToInt(p -> p.getScore()).toArray());
    }



}
