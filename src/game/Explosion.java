package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

// visual 3-frame explosion effect
public class Explosion extends StaticBody implements StepListener {

    private int stepCount = 0;
    private final int frameNumber;

    public Explosion(World world, Vec2 position, int frameNumber) {
        super(world, new CircleShape(0.1f));

        this.frameNumber = frameNumber;
        setPosition(position);

        if (frameNumber == 1) {
            addImage(new BodyImage("data/explosion1.png", 3.5f));
        } else if (frameNumber == 2) {
            addImage(new BodyImage("data/explosion2.png", 4.2f));
        } else {
            addImage(new BodyImage("data/explosion3.png", 4f));
        }

        world.addStepListener(this);
    }

    @Override
    public void preStep(StepEvent e) {
        stepCount++;

        if (stepCount >= 4) {
            Vec2 position = getPosition();
            World world = getWorld();

            // spawn next frame if needed
            if (frameNumber < 3 && world != null) {
                new Explosion(world, position, frameNumber + 1);
            }

            destroy();
        }
    }

    @Override
    public void postStep(StepEvent e) {
    }
}