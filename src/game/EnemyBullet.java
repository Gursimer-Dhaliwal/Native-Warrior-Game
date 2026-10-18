package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

// create bullets fired by the enemy soldier
public class EnemyBullet extends DynamicBody implements StepListener {

    private static final Shape bulletShape = new CircleShape(0.2f);

    private float timeAlive = 0f;

    private Native target;
    private Soldier owner;

    // create an enemy bullet and stores who fired it and who it is aimed at
    public EnemyBullet(World world, Vec2 startPos, float direction, Native target, Soldier owner) {
        super(world, bulletShape);

        this.target = target;
        this.owner = owner;

        setPosition(startPos);
        addImage(new BodyImage("data/Bullet.png", 0.6f));

        setGravityScale(0);
        setLinearVelocity(new Vec2(15f * direction, 0));

        world.addStepListener(this);

        addCollisionListener(new CollisionListener() {
            @Override
            public void collide(CollisionEvent e) {

                // ignore collision with the soldier that fired the bullet
                if (e.getOtherBody() == owner) {
                    return;
                }

                // enemy bullet hits player bullet
                if (e.getOtherBody() instanceof Bullet) {
                    destroy();
                    return;
                }

                // damage the player if the bullet hits
                if (e.getOtherBody() instanceof Native) {
                    target.takeDamage();
                    destroy();
                    return;
                }

                // destroy the bullet after any other collision
                destroy();
            }
        });
    }

    @Override
    public void preStep(StepEvent e) {

        // if the soldier that fired this bullet is dead then remove the bullet
        if (owner == null || !owner.isAlive()) {
            destroy();
            return;
        }

        timeAlive += 1f / 60f;

        // remove the bullet after 10 seconds
        if (timeAlive >= 10f) {
            destroy();
        }
    }

    @Override
    public void postStep(StepEvent e) {
    }
}