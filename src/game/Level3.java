package game;

// final level
public class Level3 extends Level {

    public Level3(Game game, city.cs.engine.World world, LevelBuilder builder) {
        super(game, world, builder);
    }

    @Override
    public int getLevelNumber() {
        return 3;
    }

    @Override
    public float getWinXPosition() {
        return 174f;
    }

    @Override
    public String getBackgroundImage() {
        return "data/background_level3.png";
    }

    @Override
    public String getPlatformImage() {
        return "data/platform_level3.png";
    }

    @Override
    public void populate() {
        playBackgroundMusic("data/level1backgroundsound.wav");

        createTiledFloor(-20f, 180f, Game.LEVEL_FLOOR_Y);

        createWidePlatform(-8f, -11.5f, 2);
        createWidePlatform(4f, -8f, 1);
        createWidePlatform(14f, -4.5f, 1);

        spawnAmmo(4f, -5.8f);
        spawnHeartPickup(14f, -2.2f);
        spawnWalkingSoldier(10f, -13f, 4f, 18f);

        createWidePlatform(26f, -2f, 2);
        createWidePlatform(38f, 2f, 1);
        createWidePlatform(49f, 6f, 2);

        createWidePlatform(32f, -10f, 1);
        createWidePlatform(43f, -6.5f, 1);

        spawnBomb(26f, 1f);
        spawnAmmo(38f, 4.8f);
        spawnHeartPickup(49f, 8.8f);

        spawnWalkingSoldier(26f, 0f, 21f, 31f);
        spawnGuardSoldier(49f, 8f);

        createPlatformRow(58f, 3, -1f);
        createWidePlatform(76f, 3f, 1);
        createWidePlatform(87f, 7f, 2);

        createWidePlatform(66f, -9f, 1);
        createWidePlatform(78f, -5.5f, 1);

        spawnBomb(63.8f, 1.5f);
        spawnBomb(87f, 9.8f);
        spawnAmmo(76f, 5.8f);

        spawnWalkingSoldier(61f, 1.5f, 58f, 69f);
        spawnWalkingSoldier(87f, 9f, 82f, 92f);

        spawnHorizontalMovingPlatform(97f, -2f, 92f, 104f, 0.08f);
        createWidePlatform(109f, 4f, 1);
        spawnVerticalMovingPlatform(125f, -4f, -7f, 7f, 0.07f);
        createWidePlatform(132f, 9f, 2);

        createWidePlatform(104f, -8.5f, 1);
        createWidePlatform(116f, -5f, 1);

        spawnGuardSoldier(109f, 6f);
        spawnWalkingSoldier(132f, 11f, 126f, 138f);

        spawnBomb(109f, 6.5f);
        spawnAmmo(104f, -6.2f);
        spawnHeartPickup(132f, 11.8f);

        createPlatform(144f, -1.5f);
        createPlatform(151f, 2.5f);
        createPlatform(158f, 6.5f);
        createWidePlatform(166f, 10f, 2);

        createWidePlatform(148f, -9f, 1);
        createWidePlatform(160f, -5.5f, 1);

        spawnBomb(151f, 4.8f);
        spawnBomb(166f, 12.8f);

        spawnAmmo(158f, 9.2f);
        spawnHeartPickup(166f, 12.5f);

        spawnWalkingSoldier(148f, -13f, 142f, 154f);
        spawnGuardSoldier(158f, 8.5f);

        createWidePlatform(174f, 3f, 2);
        createWidePlatform(178f, -15.5f, 1);

        spawnWalkingSoldier(174f, 5f, 169f, 179f);

        spawnBomb(172f, -13f);
        spawnAmmo(174f, 5.8f);

        // flag at the end of the final level
        spawnFlag(178f, -12.8f);
    }
}