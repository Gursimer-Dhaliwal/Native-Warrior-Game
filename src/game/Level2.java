package game;

// level 2
public class Level2 extends Level {

    public Level2(Game game, city.cs.engine.World world, LevelBuilder builder) {
        super(game, world, builder);
    }

    @Override
    public int getLevelNumber() {
        return 2;
    }

    @Override
    public float getWinXPosition() {
        return 142f;
    }

    @Override
    public String getBackgroundImage() {
        return "data/background_level2.png";
    }

    @Override
    public String getPlatformImage() {
        return "data/platform_level2.png";
    }

    @Override
    public void populate() {
        playBackgroundMusic("data/level1backgroundsound.wav");

        createTiledFloor(-20f, 18f, Game.LEVEL_FLOOR_Y);
        createTiledFloor(28f, 54f, Game.LEVEL_FLOOR_Y);
        createTiledFloor(66f, 92f, Game.LEVEL_FLOOR_Y);
        createTiledFloor(104f, 146f, Game.LEVEL_FLOOR_Y);

        createWidePlatform(-6f, -10f, 1);
        createWidePlatform(6f, -5f, 1);
        createWidePlatform(18f, 0f, 1);

        spawnHorizontalMovingPlatform(26f, -2f, 22f, 34f, 0.08f);
        createWidePlatform(38f, 3.5f, 1);
        spawnVerticalMovingPlatform(48f, -6f, -8f, 4f, 0.06f);
        createWidePlatform(58f, 7.5f, 2);

        createWidePlatform(72f, -7f, 1);
        createWidePlatform(82f, -1.5f, 1);
        spawnHorizontalMovingPlatform(92f, 4.5f, 88f, 100f, 0.09f);
        createWidePlatform(106f, 10f, 2);

        spawnVerticalMovingPlatform(116f, -3f, -6f, 8f, 0.07f);
        createWidePlatform(126f, 4.5f, 1);
        spawnHorizontalMovingPlatform(136f, 9f, 132f, 144f, 0.08f);

        createWidePlatform(144f, -1.5f, 1);
        createWidePlatform(148f, -15.5f, 1);

        spawnAmmo(6f, -3.2f);
        spawnHeartPickup(58f, 10.0f);
        spawnAmmo(82f, 1.0f);
        spawnHeartPickup(126f, 7.0f);

        spawnBomb(34f, -13.0f);
        spawnBomb(74f, -13.0f);
        spawnBomb(110f, -13.0f);
        spawnBomb(138f, -13.0f);

        spawnWalkingSoldier(8f, -13f, 2f, 16f);
        spawnGuardSoldier(38f, 6f);
        spawnWalkingSoldier(58f, 10f, 53f, 64f);
        spawnWalkingSoldier(82f, 1f, 78f, 86f);
        spawnGuardSoldier(106f, 12.5f);
        spawnWalkingSoldier(126f, 7f, 122f, 130f);
        spawnGuardSoldier(144f, 1.5f);

        // flag at the end of the level
        spawnFlag(151f, -12.8f);
    }
}