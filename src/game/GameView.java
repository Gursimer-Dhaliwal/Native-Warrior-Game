package game;

import city.cs.engine.UserView;
import city.cs.engine.World;

import javax.swing.*;
import java.awt.*;

//game view class that draws the player's hearts on screen
public class GameView extends UserView {

    private final Game game;
    private final Image heart;

    public GameView(World world, int width, int height, Game game) {
        super(world, width, height);
        this.game = game;
        this.heart = new ImageIcon("data/heart.png").getImage();
    }

    @Override
    protected void paintForeground(Graphics2D g) {
        int lives = game.getLives();

        // show one heart image for each life remaining
        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, 20 + (i * 40), 20, 30, 30, this);
        }
    }
}