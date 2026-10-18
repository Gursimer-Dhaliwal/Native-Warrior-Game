package game;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;

// checks if the player has touched the flag
public class FlagListener implements CollisionListener {

    private final Game game;

    // links the flag collision to the game
    public FlagListener(Game game) {
        this.game = game;
    }

    @Override
    public void collide(CollisionEvent e) {
        if (e.getOtherBody() instanceof Native) {
            game.tryFinishLevel();
        }
    }
}