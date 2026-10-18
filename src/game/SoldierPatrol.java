package game;

import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import org.jbox2d.common.Vec2;

// this class controls the back-and-forth movement of the soldier
public class SoldierPatrol implements StepListener {

    private Soldier soldier;
    private float speed;
    private float leftLimit;
    private float rightLimit;
    private boolean movingRight = true;
    private Soldier_Animation animation;
    private boolean paused = false;

    // used to detect if the soldier is stuck against something
    private int stuckCounter = 0;
    private static final int STUCK_THRESHOLD = 12;

    // stores previous x position to detect if movement is blocked
    private float lastX;

    // sets the patrol limits, speed and linked animation
    public SoldierPatrol(Soldier soldier, float speed,
                         float leftLimit, float rightLimit,
                         Soldier_Animation animation) {
        this.soldier = soldier;
        this.speed = speed;
        this.leftLimit = leftLimit;
        this.rightLimit = rightLimit;
        this.animation = animation;
        this.lastX = soldier.getPosition().x;
    }

    @Override
    public void preStep(StepEvent e) {

        // stop if the soldier no longer exists in the world
        if (soldier == null || soldier.getWorld() == null || !soldier.isAlive()) {
            return;
        }

        // keep the soldier straight while moving
        soldier.setAngularVelocity(0);
        soldier.setAngle(0);

        // if paused, stand still but keep facing the chosen direction
        if (paused || speed == 0) {
            soldier.setLinearVelocity(new Vec2(0, soldier.getLinearVelocity().y));
            animation.setMoving(false);
            animation.setFacingRight(movingRight);
            lastX = soldier.getPosition().x;
            return;
        }

        float x = soldier.getPosition().x;

        // reverse direction when the soldier reaches a patrol limit
        if (x >= rightLimit) {
            movingRight = false;
        } else if (x <= leftLimit) {
            movingRight = true;
        }

        // detect if soldier is stuck pushing into a platform or wall
        float xMoved = Math.abs(x - lastX);

        if (xMoved < 0.02f) {
            stuckCounter++;
        } else {
            stuckCounter = 0;
        }

        if (stuckCounter >= STUCK_THRESHOLD) {
            movingRight = !movingRight;
            stuckCounter = 0;
        }

        // move in the current direction
        float moveSpeed = movingRight ? speed : -speed;
        soldier.setLinearVelocity(new Vec2(moveSpeed, soldier.getLinearVelocity().y));

        // update the animation so the soldier faces the right way
        animation.setMoving(true);
        animation.setFacingRight(movingRight);

        lastX = x;
    }

    @Override
    public void postStep(StepEvent e) {
    }

    // returns the current movement direction
    public boolean isMovingRight() {
        return movingRight;
    }

    // pauses or resumes patrol movement
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    // makes the soldier face towards a target x position
    public void faceTowards(float targetX) {
        if (soldier == null || soldier.getWorld() == null) {
            return;
        }

        movingRight = targetX >= soldier.getPosition().x;
        animation.setFacingRight(movingRight);
        animation.setMoving(false);
    }
}