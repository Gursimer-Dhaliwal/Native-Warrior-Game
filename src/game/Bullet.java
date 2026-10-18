package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

// create bullets fired by the player
public class Bullet extends DynamicBody implements StepListener {

    private static final Shape bulletShape = new CircleShape(0.2f);
    private float timeAlive = 0f;

    // create a bullet at the current position and send it in the chosen direction
    public Bullet(World world, Vec2 startPos, float direction) {
        super(world, bulletShape);

        setPosition(startPos);
        addImage(new BodyImage("data/Bullet.png", 0.6f));
        setGravityScale(0);
        setLinearVelocity(new Vec2(15f * direction, 0));

        world.addStepListener(this);

        addCollisionListener(new CollisionListener() {
            @Override
            public void collide(CollisionEvent e) {

                // ignore collision with the player
                if (e.getOtherBody() instanceof Native) {
                    return;
                }

                // bullet hits enemy bullet
                if (e.getOtherBody() instanceof EnemyBullet) {
                    SoundPlayer.playSound(SoundEffects.PLAYER_HIT, 0.4f);
                    e.getOtherBody().destroy();
                    destroy();
                    return;
                }

                // bullet hits enemy
                if (e.getOtherBody() instanceof Soldier) {
                    Soldier soldier = (Soldier) e.getOtherBody();
                    SoundPlayer.playSound(SoundEffects.BULLET_HIT_ENEMY, 0.7f);
                    soldier.kill();
                    destroy();
                    return;
                }

                // bullet hits bomb
                if (e.getOtherBody() instanceof Bomb) {
                    Bomb bomb = (Bomb) e.getOtherBody();
                    bomb.explode();
                    destroy();
                    return;
                }

                // bullet hits environment
                destroy();
            }
        });
    }

    @Override
    public void preStep(StepEvent e) {
        // track how long the bullet has existed
        timeAlive += 1f / 60f;

        // remove bullet after 10 seconds
        if (timeAlive >= 10f) {
            destroy();
        }
    }

    @Override
    public void postStep(StepEvent e) {
    }
}