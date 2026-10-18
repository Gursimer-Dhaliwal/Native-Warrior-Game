package game;

import city.cs.engine.BodyImage;
import city.cs.engine.DynamicBody;
import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;

// controls the player's walking and idle animation
public class Animation implements StepListener {

    private final DynamicBody body;

    // images for facing right
    private final BodyImage idleRight;
    private final BodyImage walk1Right;
    private final BodyImage walk2Right;

    // images for facing left
    private final BodyImage idleLeft;
    private final BodyImage walk1Left;
    private final BodyImage walk2Left;

    private int tick = 0;
    private boolean moving = false;
    private boolean toggle = false;

    // controls animation speed
    private static final int FRAME_DELAY = 8;

    // sets up the animation images for both directions
    public Animation(DynamicBody body,
                     BodyImage idleRight, BodyImage walk1Right, BodyImage walk2Right,
                     BodyImage idleLeft, BodyImage walk1Left, BodyImage walk2Left) {

        this.body = body;
        this.idleRight = idleRight;
        this.walk1Right = walk1Right;
        this.walk2Right = walk2Right;
        this.idleLeft = idleLeft;
        this.walk1Left = walk1Left;
        this.walk2Left = walk2Left;

        body.removeAllImages();
        body.addImage(idleRight);
    }

    // Used to tell the animation if the player is moving or not
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    @Override
    public void preStep(StepEvent stepEvent) {
        tick++;

        // the player faces right unless changed
        boolean facingRight = true;
        if (body instanceof Native) {
            facingRight = ((Native) body).getDirection() >= 0;
        }

        // show idle image if the player is not moving
        if (!moving) {
            body.removeAllImages();
            body.addImage(facingRight ? idleRight : idleLeft);
            return;
        }

        // Switch between walking frames to create the walking animation
        if (tick % FRAME_DELAY == 0) {
            toggle = !toggle;
            body.removeAllImages();

            if (facingRight) {
                body.addImage(toggle ? walk1Right : walk2Right);
            } else {
                body.addImage(toggle ? walk1Left : walk2Left);
            }
        }
    }

    @Override
    public void postStep(StepEvent stepEvent) {
    }
}