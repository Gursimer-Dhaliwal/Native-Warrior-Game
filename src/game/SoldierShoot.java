package game;

import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import org.jbox2d.common.Vec2;

// this class allows the soldier to shoot at the player
public class SoldierShoot implements StepListener {

    private Soldier soldier;
    private Native target;
    private SoldierPatrol patrol;

    private long lastShotTime = 0;
    private static final long SHOOT_DELAY = 2000;

    private static final float DETECTION_RANGE = 12f;
    private static final float VERTICAL_TOLERANCE = 3f;

    // links the shooting system to the soldier, player and patrol logic
    public SoldierShoot(Soldier soldier, Native target, SoldierPatrol patrol) {
        this.soldier = soldier;
        this.target = target;
        this.patrol = patrol;
    }

    @Override
    public void preStep(StepEvent e) {

        // stop shooting completely if the soldier is dead or removed
        if (soldier == null || !soldier.isAlive() || soldier.getWorld() == null) {
            return;
        }

        if (target == null || target.getWorld() == null) {
            return;
        }

        boolean playerVisible = canSeePlayer();

        // stop the soldier and make them face the player while aiming
        if (playerVisible) {
            patrol.setPaused(true);
            patrol.faceTowards(target.getPosition().x);
        } else {
            patrol.setPaused(false);
            return;
        }

        long now = System.currentTimeMillis();

        // only shoot after the delay has passed
        if (now - lastShotTime >= SHOOT_DELAY) {
            shootAtPlayer();
            lastShotTime = now;
        }
    }

    @Override
    public void postStep(StepEvent e) {
    }

    // checks if the player is close enough and roughly on the same height
    private boolean canSeePlayer() {
        Vec2 soldierPos = soldier.getPosition();
        Vec2 targetPos = target.getPosition();

        float dx = Math.abs(targetPos.x - soldierPos.x);
        float dy = Math.abs(targetPos.y - soldierPos.y);

        return dx <= DETECTION_RANGE && dy <= VERTICAL_TOLERANCE;
    }

    // creates an enemy bullet and fires it towards the player
    private void shootAtPlayer() {
        Vec2 soldierPos = soldier.getPosition();
        Vec2 targetPos = target.getPosition();

        float direction = (targetPos.x >= soldierPos.x) ? 1f : -1f;
        Vec2 bulletStart = new Vec2(soldierPos.x + direction * 1.5f, soldierPos.y);

        new EnemyBullet(soldier.getWorld(), bulletStart, direction, target, soldier);
    }
}