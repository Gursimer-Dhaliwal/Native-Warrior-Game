package game;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.Shape;
import city.cs.engine.StaticBody;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;

// builds platforms, pickups, enemies and level objects
public class LevelBuilder {

    private static final float PLATFORM_HALF_WIDTH = 3f;
    private static final float PLATFORM_HALF_HEIGHT = 0.5f;
    private static final float PLATFORM_STEP = 5.8f;

    // set to 4 so the new 1536x1024 platform images fill the 6-unit-wide hitbox properly
    private static final float PLATFORM_IMAGE_SCALE = 4f;

    private final Game game;
    private final World world;
    private final Native player;
    private final float soldierSpeed;

    private String currentPlatformImage = "data/platform.png";

    public LevelBuilder(Game game, World world, Native player, float soldierSpeed) {
        this.game = game;
        this.world = world;
        this.player = player;
        this.soldierSpeed = soldierSpeed;
    }

    // sets the platform image for the current level
    public void setPlatformImage(String platformImage) {
        this.currentPlatformImage = platformImage;
    }

    // creates one platform
    public void createPlatform(float x, float y) {
        Shape platformShape = new BoxShape(PLATFORM_HALF_WIDTH, PLATFORM_HALF_HEIGHT);
        StaticBody platform = new StaticBody(world, platformShape);
        platform.setPosition(new Vec2(x, y));
        platform.addImage(new BodyImage(currentPlatformImage, PLATFORM_IMAGE_SCALE));
    }

    // creates a row of platforms
    public void createPlatformRow(float startX, int tiles, float y) {
        for (int i = 0; i < tiles; i++) {
            createPlatform(startX + (i * PLATFORM_STEP), y);
        }
    }

    // creates a wider platform section
    public void createWidePlatform(float centreX, float y, int tiles) {
        float startX = centreX - ((tiles - 1) * PLATFORM_STEP / 2f);
        createPlatformRow(startX, tiles, y);
    }

    // creates a tiled floor
    public void createTiledFloor(float startX, float endX, float y) {
        for (float x = startX; x <= endX; x += PLATFORM_STEP) {
            createPlatform(x, y);
        }
    }

    // creates a bomb
    public Bomb spawnBomb(float x, float y) {
        Bomb bomb = new Bomb(world);
        bomb.setPosition(new Vec2(x, y));
        return bomb;
    }

    // creates an ammo pickup
    public void spawnAmmo(float x, float y) {
        game.setAmmoRespawnPosition(new Vec2(x, y));

        AmmoPickup ammoPickup = new AmmoPickup(world);
        ammoPickup.setPosition(new Vec2(x, y));
    }

    // creates a heart pickup
    public void spawnHeartPickup(float x, float y) {
        HeartPickup heartPickup = new HeartPickup(world, new Vec2(x, y));
    }

    // creates a moving platform that moves horizontally
    public void spawnHorizontalMovingPlatform(float x, float y, float leftLimit, float rightLimit, float speed) {
        new MovingPlatform(world, x, y, leftLimit, rightLimit, speed, true, currentPlatformImage);
    }

    // creates a moving platform that moves vertically
    public void spawnVerticalMovingPlatform(float x, float y, float lowerLimit, float upperLimit, float speed) {
        new MovingPlatform(world, x, y, lowerLimit, upperLimit, speed, false, currentPlatformImage);
    }

    // creates the end flag
    public void spawnFlag(float x, float y) {
        Flag flag = new Flag(world, x, y);
        flag.addCollisionListener(new FlagListener(game));
    }

    // creates the animation controller for a soldier
    public Soldier_Animation createSoldierAnimation(Soldier soldier) {
        BodyImage idleRight = new BodyImage("data/soldier1.png", 4);
        BodyImage walk1Right = new BodyImage("data/soldier1.png", 4);
        BodyImage walk2Right = new BodyImage("data/soldier2.png", 4);

        BodyImage idleLeft = new BodyImage("data/soldier1_left.png", 4);
        BodyImage walk1Left = new BodyImage("data/soldier1_left.png", 4);
        BodyImage walk2Left = new BodyImage("data/soldier2_left.png", 4);

        Soldier_Animation soldierAnimation = new Soldier_Animation(
                soldier,
                idleRight, walk1Right, walk2Right,
                idleLeft, walk1Left, walk2Left
        );

        world.addStepListener(soldierAnimation);
        return soldierAnimation;
    }

    // creates a soldier that patrols between two points
    public Soldier spawnWalkingSoldier(float x, float y, float leftLimit, float rightLimit) {
        Soldier soldier = new Soldier(world, x, y, game);
        Soldier_Animation soldierAnimation = createSoldierAnimation(soldier);

        SoldierPatrol patrol = new SoldierPatrol(
                soldier, soldierSpeed, leftLimit, rightLimit, soldierAnimation
        );

        world.addStepListener(patrol);
        world.addStepListener(new SoldierShoot(soldier, player, patrol));

        game.registerSoldier(soldier);
        return soldier;
    }

    // creates a soldier that stands still
    public Soldier spawnGuardSoldier(float x, float y) {
        Soldier soldier = new Soldier(world, x, y, game);
        Soldier_Animation soldierAnimation = createSoldierAnimation(soldier);

        SoldierPatrol guardPatrol = new SoldierPatrol(
                soldier, 0f, x, x, soldierAnimation
        );

        world.addStepListener(guardPatrol);
        world.addStepListener(new SoldierShoot(soldier, player, guardPatrol));

        game.registerSoldier(soldier);
        return soldier;
    }
}