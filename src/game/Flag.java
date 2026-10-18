package game;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.StaticBody;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;

// creates the level end flag
public class Flag extends StaticBody {

    // creates the flag at the chosen position
    public Flag(World world, float x, float y) {
        super(world, new BoxShape(0.8f, 2.2f));

        setPosition(new Vec2(x, y));
        addImage(new BodyImage("data/flag.png", 6f));
    }
}