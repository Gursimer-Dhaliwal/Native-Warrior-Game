package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

// create the player character
public class Native extends DynamicBody {

    private Game game;
    private float direction = 1;
    private boolean damageCooldown = false;
    private long damageCooldownEnd = 0;
    private static final int DAMAGE_COOLDOWN_MS = 1000;

    public Native(World world, Game game) {
        super(world, new BoxShape(1, 1.75f));
        this.game = game;

        setPosition(new Vec2(-15.9f, -7.7f));
        addImage(new BodyImage("data/NATIVE1.png", 4));

        addCollisionListener(new CollisionListener() {
            @Override
            public void collide(CollisionEvent e) {

                // if the player hits a bomb, trigger the explosion and deal damage
                if (e.getOtherBody() instanceof Bomb) {
                    Bomb bomb = (Bomb) e.getOtherBody();
                    bomb.explode();
                    takeDamage();
                }

                // if the player collects ammo then add reserve ammo
                if (e.getOtherBody() instanceof AmmoPickup) {
                    SoundPlayer.playSound(SoundEffects.AMMO_PICKUP);
                    e.getOtherBody().destroy();
                    game.getMovement().addReserveAmmo(15);
                    game.startAmmoRespawn();
                }

                // if the player collects a heart then restore 1 life up to the max
                if (e.getOtherBody() instanceof HeartPickup) {
                    SoundPlayer.playSound(SoundEffects.HEART_PICKUP);
                    e.getOtherBody().destroy();
                    game.gainLife();
                }
            }
        });
    }

    // deals damage to the player but only if the cooldown has finished
    public void takeDamage() {
        long now = System.currentTimeMillis();

        if (damageCooldown) {
            if (now < damageCooldownEnd) {
                return;
            } else {
                damageCooldown = false;
            }
        }

        SoundPlayer.playSound(SoundEffects.PLAYER_HIT);
        game.loseLife();

        damageCooldown = true;
        damageCooldownEnd = now + DAMAGE_COOLDOWN_MS;
    }

    // sets the direction the player is facing
    public void setDirection(float direction) {
        this.direction = direction;
    }

    // returns the direction the player is facing
    public float getDirection() {
        return direction;
    }
}