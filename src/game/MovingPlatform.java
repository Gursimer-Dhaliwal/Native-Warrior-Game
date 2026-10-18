package game;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.StaticBody;
import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;

// reusable moving platform class for horizontal or vertical movement
public class MovingPlatform extends StaticBody implements StepListener {

    private static final float PLATFORM_IMAGE_SCALE = 4f;

    private float minPosition;
    private float maxPosition;
    private float speed;
    private boolean horizontal;
    private boolean movingForward = true;

    public MovingPlatform(World world, float x, float y,
                          float minPosition, float maxPosition,
                          float speed, boolean horizontal,
                          String imagePath) {
        super(world, new BoxShape(3f, 0.5f));

        this.minPosition = minPosition;
        this.maxPosition = maxPosition;
        this.speed = speed;
        this.horizontal = horizontal;

        setPosition(new Vec2(x, y));
        addImage(new BodyImage(imagePath, PLATFORM_IMAGE_SCALE));

        world.addStepListener(this);
    }

    @Override
    public void preStep(StepEvent e) {
        Vec2 pos = getPosition();

        if (horizontal) {
            float x = pos.x;

            if (movingForward) {
                x += speed;
                if (x >= maxPosition) {
                    x = maxPosition;
                    movingForward = false;
                }
            } else {
                x -= speed;
                if (x <= minPosition) {
                    x = minPosition;
                    movingForward = true;
                }
            }

            setPosition(new Vec2(x, pos.y));
        } else {
            float y = pos.y;

            if (movingForward) {
                y += speed;
                if (y >= maxPosition) {
                    y = maxPosition;
                    movingForward = false;
                }
            } else {
                y -= speed;
                if (y <= minPosition) {
                    y = minPosition;
                    movingForward = true;
                }
            }

            setPosition(new Vec2(pos.x, y));
        }
    }

    @Override
    public void postStep(StepEvent e) {
    }
}