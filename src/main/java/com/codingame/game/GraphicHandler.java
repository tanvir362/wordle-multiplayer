package com.codingame.game;

import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;

import java.util.ArrayList;

public class GraphicHandler {

    public static final int BOARD_HEIGHT = 6;

    private static int getBaseX(Player player){
        return (int)(1920/4) * (player.getIndex()==0 ? 1: 3) - 283;
    }

    private static int getBaseY(Player player){
        return (int)1080/2 - 280;
    }

    public static void drawLetter(Player player, char letter, int state, int row, int column, GraphicEntityModule graphicEntityModule){
        if(row == player.outputHistory.size()-1){
            player.board.get(row)[column].setImage(String.format("letters/%c0.png", Character.toLowerCase(letter)));
            graphicEntityModule.commitEntityState(0.025*(column+1), player.board.get(row)[column]);

            player.board.get(row)[column].setImage(String.format("letters/%c%d.png", Character.toLowerCase(letter), state));
            graphicEntityModule.commitEntityState(0.125+0.175*(column+1), player.board.get(row)[column]);
        }
        else{
            player.board.get(row)[column].setImage(String.format("letters/%c%d.png", Character.toLowerCase(letter), state));
        }

    }

    public static void drawBackgroundImage(GraphicEntityModule graphicEntityModule){
        graphicEntityModule.createSprite()
                .setImage("Background.jpg")
                .setAnchor(0)
                .setX(0)
                .setY(0);
    }

    public static void drawLiveBoard(Player player, GraphicEntityModule graphicEntityModule){
        for(int i=0; i<player.outputHistory.size(); i++){
            LetterState[] output = player.outputHistory.get(i);
            for(int j=0; j<Constant.WORD_LEN; j++){
                LetterState letterState = output[j];
                drawLetter(player, letterState.getLetter(), letterState.getState(), i, j, graphicEntityModule);
            }
        }
    }

    public static void drawGrids(MultiplayerGameManager<Player> gameManager, GraphicEntityModule graphicEntityModule) {
        for (Player player : gameManager.getPlayers()) {
            int base_x = getBaseX(player);
            int base_y = getBaseY(player);

            player.board = new ArrayList<Sprite[]>();

            for(int i=0; i<BOARD_HEIGHT; i++){
                Sprite[] rw = new Sprite[5];

                for(int j=0; j<Constant.WORD_LEN; j++){
                    rw[j] = graphicEntityModule.createSprite()
                            .setImage("empty_cell.png")
                            .setScale(0.5)
                            .setX((int) (base_x + j*(1.25+100+1.25+13.5)))
                            .setY((int) (base_y + i*(1.25+100+1.25+13.5)));
                }

                player.board.add(rw);

            }

//            graphicEntityModule.createSprite()
//                    .setImage("empty_board1.png")
//                    .setScale(0.5)
//                    .setAnchor(0.5)
//                    .setX(x)
//                    .setY(y);

        }

    }


    public static void drawHud(MultiplayerGameManager<Player> gameManager, GraphicEntityModule graphicEntityModule) {
        for (Player player : gameManager.getPlayers()) {
            int x = player.getIndex() == 0 ? 100 : 1920 - 100;
            int y = 75;

            graphicEntityModule
                    .createRectangle()
                    .setWidth(140)
                    .setHeight(140)
                    .setX(x - 70)
                    .setY(y - 70)
                    .setLineWidth(0)
                    .setFillColor(player.getColorToken());

            graphicEntityModule
                    .createRectangle()
                    .setWidth(120)
                    .setHeight(120)
                    .setX(x - 60)
                    .setY(y - 60)
                    .setLineWidth(0)
                    .setFillColor(0xffffff);

            Text text = graphicEntityModule.createText(player.getNicknameToken())
                    .setX(x)
                    .setY(y + 120)
                    .setZIndex(20)
                    .setFontSize(40)
                    .setFillColor(0xffffff)
                    .setAnchor(0.5);

            Sprite avatar = graphicEntityModule.createSprite()
                    .setX(x)
                    .setY(y)
                    .setZIndex(20)
                    .setImage(player.getAvatarToken())
                    .setAnchor(0.5)
                    .setBaseHeight(116)
                    .setBaseWidth(116);

            player.hud = graphicEntityModule.createGroup(text, avatar);
        }
    }
}
