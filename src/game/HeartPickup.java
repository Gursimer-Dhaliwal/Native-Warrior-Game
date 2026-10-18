package game;

import city.cs.engine.BodyImage;
import city.cs.engine.CircleShape;
import city.cs.engine.Shape;
import city.cs.engine.StaticBody;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;

// creates a heart pickup that restores a life
public class HeartPickup extends StaticBody {

    private static final Shape heartShape = new CircleShape(0.7f);

    public HeartPickup(World world, Vec2 position) {
        super(world, heartShape);

        setPosition(position);
        addImage(new BodyImage("data/heart.png", 2.5f));
    }
}