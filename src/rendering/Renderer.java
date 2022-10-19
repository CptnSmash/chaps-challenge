package rendering;


import domain.actors.Actor;
import domain.actors.BugEnemy;
import domain.actors.NonPlayerActor;
import domain.game.Game;
import domain.game.Player;
import domain.game.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;

/**
 * The renderer module is responsible for drawing the maze onto the JPane
 * @author Sam Lockhart
 */

public class Renderer extends JPanel {

    private final Game game;
    private final Player player;
    private HashSet<BugEnemy> enemies;

    private final int boardWidth, boardHeight;

    /**
     * Constructor for the renderer.
     * @param game the game currently being played.
     * @param player the object representing the player.
     * @param g the graphics object representing the JPane
     */
    public Renderer(Game game, Player player) {
        this.game = game;
        this.player = player;
        int[] dimensions = game.getBoardDimension();
        this.boardWidth = dimensions[0];
        this.boardHeight = dimensions[1];
        this.enemies = game.getAllEnemies();
        System.out.println(enemies.size());
    }

    /**
     * This function draws the maze around the player, as well as the player themselves.
     * @param g the graphics object representing the JPane.
     */

    public void draw(Graphics g) {
        //find the player's position
        Position chapPos = player.getPosition();
        int xPos = chapPos.getX();
        int yPos = chapPos.getY();

        //calculate the minimum and maximum for tiles to be drawn (positions in the board)
        int xMin = boardWidth - xPos < 5 ? boardWidth - 9 : Math.max(xPos - 4, 0);
        int yMin = boardHeight - yPos < 5 ? boardHeight - 9 : Math.max(yPos - 4, 0);
        int xMax = Math.min(xMin + 9, boardWidth);
        int yMax = Math.min(yMin + 9, boardHeight);

        //initialise x and y values on the pane
        int x = 36;
        int y = 82;

        //draw each tile in a 9x9 area around the player
        BufferedImage image;
        for (int i = xMin; i < xMax; i++) {
            for (int j = yMin; j < yMax; j++) {
                Position tilePos = new Position(i, j);
                try {
                    image = game.getTileAt(tilePos).getImage(); //loads the image for the current tile
                    g.drawImage(image, x, y, this);     //and draws it
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                for (BugEnemy enemy : enemies){
                    if (enemy.getPosition().equals(tilePos)){
                        //System.out.println(enemy.getPosition().getX() + " " + enemy.getPosition().getY());
                        try {
                            image = enemy.getImage();
                            g.drawImage(image, x, y, this);
                        }
                        catch (IllegalArgumentException e){
                            e.printStackTrace();
                        }
                    }
                }
                y += 50;
            }
            y = 82;
            x += 50;
        }

        //find the limit of the centre of view, and initialise the player's drawing position (centre of the board)
        int xDrawMax = boardWidth - 5;
        int yDrawMax = boardHeight - 5;
        int drawX = 236;
        int drawY = 282;

        //adjust the player's drawing position if necessary (ie, drawing them centred would require shaving off parts of the board)
        if (xPos < 4) {
            for (int i = xPos; i < 4; i++) {
                drawX -= 50;
            }
        }
        else if (xPos > xDrawMax){
            for (int i = xDrawMax; i < xPos; i++){
                drawX += 50;
            }
        }

        if (yPos < 4) {
            for (int i = yPos; i < 4; i++) {
                drawY -= 50;
            }
        }
        else if (yPos > yDrawMax){
            for (int i = yDrawMax; i < yPos; i++){
                drawY += 50;
            }
        }

        //draws the player
        try {
            image = player.getPlayerImage();
            g.drawImage(image, drawX, drawY, this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        setOpaque(false);
    }


    @Override
	public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    }
}

