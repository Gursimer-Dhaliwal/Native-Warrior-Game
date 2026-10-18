package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

// create the bomb object that damages the player
public class Bomb extends StaticBody {

    // creates a bomb with a circle shape
    public Bomb(World world) {
        super(world, new CircleShape(0.7f));
        addImage(new BodyImage("data/bomb.png", 3f));
    }

    // creates the explosion effect, plays the sound and removes the bomb
    public void explode() {
        Vec2 position = getPosition();
        World world = getWorld();

        if (world != null) {
            new Explosion(world, position, 1);
        }

        SoundPlayer.playSound(SoundEffects.BOMB_EXPLOSION, 0.9f);
        destroy();
    }
}