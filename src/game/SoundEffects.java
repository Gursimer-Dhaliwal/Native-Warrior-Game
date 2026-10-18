package game;

// stores all sound effect file paths in one place
public class SoundEffects {

    public static final String PLAYER_SHOOT = "data/gunshot.wav";
    public static final String PLAYER_HIT = "data/hit.wav";
    public static final String RELOAD = "data/reload.wav";
    public static final String EMPTY_MAG = "data/empty.wav";

    public static final String GAME_WIN = "data/game_win.wav";
    public static final String GAME_LOSE = "data/game_lose.wav";
    public static final String BULLET_HIT_ENEMY = "data/bullet_hit_enemy.wav";
    public static final String BOMB_EXPLOSION = "data/bomb_explosion.wav";
    public static final String PLAYER_JUMP = "data/jump.wav";
    public static final String HEART_PICKUP = "data/heart_pickup.wav";
    public static final String AMMO_PICKUP = "data/ammo_pickup.wav";
    public static final String CLING = "data/cling.wav";

    private SoundEffects() {
        // stops this class being made into an object
    }
}