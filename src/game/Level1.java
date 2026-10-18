package game;

// level 1
public class Level1 extends Level {

    public Level1(Game game, city.cs.engine.World world, LevelBuilder builder) {
        super(game, world, builder);
    }

    @Override
    public int getLevelNumber() {
        return 1;
    }

    @Override
    public float getWinXPosition() {
        return 126f;
    }

    @Override
    public String getBackgroundImage() {
        return "data/background_level1.png";
    }

    @Override
    public String getPlatformImage() {
        return "data/platform_level1.png";
    }

    @Override
    public void populate() {
        playBackgroundMusic("data/level1backgroundsound.wav");

        createTiledFloor(-20f, 124f, Game.LEVEL_FLOOR_Y);

        createWidePlatform(-10f, -11f, 2);
        createWidePlatform(2f, -6.5f, 2);
        createWidePlatform(15f, -1.5f, 1);
        createWidePlatform(8f, -12.5f, 1);

        createWidePlatform(28f, -1.5f, 2);
        createWidePlatform(40f, 3.5f, 1);
        createWidePlatform(50f, 8.5f, 2);

        createWidePlatform(36f, -9.5f, 1);
        createWidePlatform(47f, -4.5f, 1);

        createWidePlatform(62f, 1.0f, 2);
        createWidePlatform(73f, 6.0f, 1);
        createWidePlatform(84f, 11.0f, 2);

        createWidePlatform(68f, -8.5f, 1);
        createWidePlatform(79f, -3.0f, 1);

        createWidePlatform(96f, 2.0f, 2);
        createWidePlatform(107f, 7.5f, 1);
        createWidePlatform(118f, 12.5f, 1);

        createWidePlatform(101f, -9.0f, 1);
        createWidePlatform(113f, -3.5f, 2);

        createWidePlatform(124f, -1.5f, 2);
        createWidePlatform(129f, -15.5f, 1);

        spawnAmmo(2f, -4.2f);
        spawnHeartPickup(15f, 0.8f);

        spawnAmmo(40f, 5.8f);
        spawnHeartPickup(50f, 11.0f);

        spawnAmmo(73f, 8.8f);
        spawnHeartPickup(84f, 13.8f);

        spawnAmmo(101f, -7.2f);
        spawnHeartPickup(124f, 1.2f);

        spawnBomb(28f, 1.5f);
        spawnBomb(68f, -6.0f);
        spawnBomb(96f, 4.5f);

        spawnWalkingSoldier(10f, -13f, 4f, 18f);
        spawnWalkingSoldier(28f, 1f, 23f, 33f);
        spawnGuardSoldier(50f, 11f);
        spawnWalkingSoldier(84f, 13.5f, 78f, 90f);
        spawnGuardSoldier(107f, 11f);
        spawnWalkingSoldier(118f, -13f, 113f, 122f);

        // flag at the end of the level
        spawnFlag(132f, -12.8f);
    }
}