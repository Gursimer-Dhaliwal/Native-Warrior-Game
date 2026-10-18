package game;

import city.cs.engine.World;

// base class for all levels
public abstract class Level {

    protected final Game game;
    protected final World world;
    protected final LevelBuilder builder;

    public Level(Game game, World world, LevelBuilder builder) {
        this.game = game;
        this.world = world;
        this.builder = builder;
    }

    public abstract int getLevelNumber();

    public abstract float getWinXPosition();

    public abstract String getBackgroundImage();

    public abstract String getPlatformImage();

    public abstract void populate();

    protected void playBackgroundMusic(String filePath) {
        game.playBackgroundMusic(filePath);
    }

    protected void createPlatform(float x, float y) {
        builder.createPlatform(x, y);
    }

    protected void createPlatformRow(float startX, int tiles, float y) {
        builder.createPlatformRow(startX, tiles, y);
    }

    protected void createWidePlatform(float centreX, float y, int tiles) {
        builder.createWidePlatform(centreX, y, tiles);
    }

    protected void createTiledFloor(float startX, float endX, float y) {
        builder.createTiledFloor(startX, endX, y);
    }

    protected void spawnBomb(float x, float y) {
        builder.spawnBomb(x, y);
    }

    protected void spawnAmmo(float x, float y) {
        builder.spawnAmmo(x, y);
    }

    protected void spawnHeartPickup(float x, float y) {
        builder.spawnHeartPickup(x, y);
    }

    protected void spawnHorizontalMovingPlatform(float x, float y, float leftLimit, float rightLimit, float speed) {
        builder.spawnHorizontalMovingPlatform(x, y, leftLimit, rightLimit, speed);
    }

    protected void spawnVerticalMovingPlatform(float x, float y, float lowerLimit, float upperLimit, float speed) {
        builder.spawnVerticalMovingPlatform(x, y, lowerLimit, upperLimit, speed);
    }

    protected void spawnWalkingSoldier(float x, float y, float leftLimit, float rightLimit) {
        builder.spawnWalkingSoldier(x, y, leftLimit, rightLimit);
    }

    protected void spawnGuardSoldier(float x, float y) {
        builder.spawnGuardSoldier(x, y);
    }

    protected void spawnFlag(float x, float y) {
        builder.spawnFlag(x, y);
    }
}