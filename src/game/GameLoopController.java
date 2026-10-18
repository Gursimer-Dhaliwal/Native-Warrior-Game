package game;

import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;

// handles the core step logic for the running game
public class GameLoopController {

    private final Game game;
    private final World world;
    private final Native player;
    private final WASDMovement movement;
    private final Animation animator;
    private final LevelBuilder builder;

    public GameLoopController(Game game, World world, Native player,
                              WASDMovement movement, Animation animator,
                              LevelBuilder builder) {
        this.game = game;
        this.world = world;
        this.player = player;
        this.movement = movement;
        this.animator = animator;
        this.builder = builder;
    }

    // adds all core step listeners to the world
    public void addCoreStepListeners() {
        addPlayerUprightListener();
        addAmmoRespawnListener();
        addGameUpdateListener();
    }

    // keeps the player upright every step
    private void addPlayerUprightListener() {
        world.addStepListener(new StepListener() {
            @Override
            public void preStep(StepEvent e) {
                if (player != null && player.getWorld() != null) {
                    player.setAngularVelocity(0);
                    player.setAngle(0);
                }
            }

            @Override
            public void postStep(StepEvent e) {
            }
        });
    }

    // handles ammo respawning
    private void addAmmoRespawnListener() {
        world.addStepListener(new StepListener() {
            @Override
            public void preStep(StepEvent e) {
                if (!game.isAmmoRespawning()) {
                    return;
                }

                game.addAmmoRespawnTime(1f / 60f);

                if (game.getAmmoRespawnTimer() >= game.getAmmoRespawnTimeLimit()) {
                    Vec2 respawnPosition = game.getAmmoRespawnPosition();

                    if (builder != null) {
                        builder.spawnAmmo(respawnPosition.x, respawnPosition.y);
                    }

                    game.stopAmmoRespawn();
                }
            }

            @Override
            public void postStep(StepEvent e) {
            }
        });
    }

    // handles movement, animation, camera, timer and win/lose checks
    private void addGameUpdateListener() {
        world.addStepListener(new StepListener() {
            @Override
            public void preStep(StepEvent e) {
                if (movement != null) {
                    movement.updateMovement();
                    animator.setMoving(movement.isMoving());
                }

                game.addElapsedTime(1f / 60f);
                game.updateStatusMessage();
                game.updateCamera();
                game.checkWinLoseConditions();
            }

            @Override
            public void postStep(StepEvent e) {
            }
        });
    }
}