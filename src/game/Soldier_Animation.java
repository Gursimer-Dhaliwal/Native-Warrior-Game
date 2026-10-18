package game;

import city.cs.engine.*;

// handles the animation of the soldier (idle + walking + direction)
public class Soldier_Animation implements StepListener {

    private Soldier soldier;

    private BodyImage idleRight;
    private BodyImage walk1Right;
    private BodyImage walk2Right;

    private BodyImage idleLeft;
    private BodyImage walk1Left;
    private BodyImage walk2Left;

    private boolean moving = false;
    private boolean facingRight = true; // NEW: controls direction

    private boolean toggle = false;
    private int frameCounter = 0;

    // constructor to initialise all animation frames
    public Soldier_Animation(Soldier soldier,
                             BodyImage idleRight,
                             BodyImage walk1Right,
                             BodyImage walk2Right,
                             BodyImage idleLeft,
                             BodyImage walk1Left,
                             BodyImage walk2Left) {

        this.soldier = soldier;

        this.idleRight = idleRight;
        this.walk1Right = walk1Right;
        this.walk2Right = walk2Right;

        this.idleLeft = idleLeft;
        this.walk1Left = walk1Left;
        this.walk2Left = walk2Left;

        // start with idle image facing right
        soldier.removeAllImages();
        soldier.addImage(idleRight);
    }

    // sets whether the soldier is currently moving
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    // NEW: sets which direction the soldier is facing
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    @Override
    public void preStep(StepEvent e) {

        // stop animation if soldier no longer exists
        if (soldier == null || soldier.getWorld() == null || !soldier.isAlive()) {
            return;
        }

        frameCounter++;

        // slows down animation speed so it looks natural
        if (frameCounter < 10) {
            return;
        }
        frameCounter = 0;

        soldier.removeAllImages();

        // use stored direction (NOT from Soldier class)
        boolean direction = this.facingRight;

        // if not moving → show idle frame
        if (!moving) {
            soldier.addImage(direction ? idleRight : idleLeft);
            return;
        }

        // walking animation (alternates between 2 frames)
        if (direction) {
            soldier.addImage(toggle ? walk1Right : walk2Right);
        } else {
            soldier.addImage(toggle ? walk1Left : walk2Left);
        }

        toggle = !toggle;
    }

    @Override
    public void postStep(StepEvent e) {
    }
}