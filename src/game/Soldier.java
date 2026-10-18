package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

// creates the enemy soldier
public class Soldier extends DynamicBody {

    private boolean alive = true;
    private boolean facingRight = true;
    private final Game game;

    // creates a soldier at the chosen position
    public Soldier(World world, float x, float y, Game game) {
        super(world, new BoxShape(1, 2));
        this.game = game;
        setPosition(new Vec2(x, y));
        addImage(new BodyImage("data/soldier1.png", 4));
    }

    // returns whether the soldier is still alive
    public boolean isAlive() {
        return alive;
    }

    // kills the soldier and removes it from the world
    public void kill() {
        if (!alive) {
            return;
        }

        alive = false;
        game.addScore(100);
        destroy();
    }

    // sets the direction the soldier is facing
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    // returns true if the soldier is facing right
    public boolean isFacingRight() {
        return facingRight;
    }
}