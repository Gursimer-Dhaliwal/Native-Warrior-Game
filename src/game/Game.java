package game;

import city.cs.engine.BodyImage;
import city.cs.engine.StaticBody;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.util.ArrayList;

// main game class
public class Game {

    private final JFrame frame;
    private final MainMenu mainMenu;
    private final AudioManager audioManager;

    private static final int VIEW_WIDTH = 1200;
    private static final int VIEW_HEIGHT = 800;
    private static final float VIEW_ZOOM = 20f;

    static final float LEVEL_LEFT = -20f;
    static final float LEVEL_RIGHT = 180f;
    static final float LEVEL_FLOOR_Y = -15.5f;
    private static final float FALL_DEATH_Y = -25f;

    private static final float AMMO_RESPAWN_TIME = 8f;

    private World world;
    private Background_View view;
    private Native Native;
    private WASDMovement movement;
    private LevelBuilder levelBuilder;
    private GameLoopController loopController;

    private int currentLevel = 1;
    private Level activeLevel;

    private boolean gameRunning = false;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private boolean gamePaused = false;
    private boolean resultSaved = false;

    // level transition screen state
    private boolean levelTransitionActive = false;
    private int nextLevelNumber = 0;

    // in-game name entry
    private boolean enteringName = false;
    private String currentInput = "";

    private int lives = 3;
    private static final int MAX_LIVES = 4;
    private final BodyImage heartImage = new BodyImage("data/heart.png", 2f);

    private final float soldierSpeed = 5f;
    private final ArrayList<Soldier> soldiers = new ArrayList<>();

    private boolean ammoRespawning = false;
    private float ammoRespawnTimer = 0f;
    private Vec2 ammoRespawnPosition = new Vec2(0f, -1.95f);

    private String statusMessage = "";
    private float messageTimer = 0f;
    private static final float MESSAGE_DURATION = 2f;

    private float elapsedTime = 0f;
    private int score = 0;

    public Game() {
        frame = new JFrame("Native Warrior");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(VIEW_WIDTH, VIEW_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        audioManager = new AudioManager();
        mainMenu = new MainMenu(this);

        mainMenu.showMainMenu();

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Game();
    }

    // shows a panel on the main game window
    public void showScreen(JPanel panel) {
        frame.getContentPane().removeAll();
        frame.setContentPane(panel);
        frame.revalidate();
        frame.repaint();
    }

    // starts a level from the menu
    public void startLevelFromMenu(int level) {
        startLevel(level);
    }

    private void startLevel(int level) {
        startLevel(level, false);
    }

    // starts a level
    private void startLevel(int level, boolean preserveProgress) {
        int savedLives = 3;
        int savedAmmoInMag = WASDMovement.DEFAULT_MAG_SIZE;
        int savedReserveAmmo = WASDMovement.DEFAULT_RESERVE_AMMO;
        float savedElapsedTime = 0f;
        int savedScore = 0;

        if (preserveProgress && movement != null) {
            savedLives = lives;
            savedAmmoInMag = movement.getAmmoInMag();
            savedReserveAmmo = movement.getReserveAmmo();
            savedElapsedTime = elapsedTime;
            savedScore = score;
        }

        stopCurrentGame();

        currentLevel = level;
        gameRunning = true;
        gameOver = false;
        gameWon = false;
        gamePaused = false;
        resultSaved = false;
        levelTransitionActive = false;
        nextLevelNumber = 0;

        enteringName = false;
        currentInput = "";

        soldiers.clear();
        statusMessage = "";
        messageTimer = 0f;
        ammoRespawning = false;
        ammoRespawnTimer = 0f;

        if (preserveProgress) {
            lives = savedLives;
            elapsedTime = savedElapsedTime;
            score = savedScore;
        } else {
            lives = 3;
            elapsedTime = 0f;
            score = 0;
        }

        world = new World();
        world.setGravity(17f);

        Native = new Native(world, this);
        movement = new WASDMovement(Native, this);
        levelBuilder = new LevelBuilder(this, world, Native, soldierSpeed);

        if (preserveProgress) {
            movement.setAmmoState(savedAmmoInMag, savedReserveAmmo);
        }

        activeLevel = createLevel(level);
        levelBuilder.setPlatformImage(activeLevel.getPlatformImage());
        activeLevel.populate();

        view = new Background_View(
                world,
                VIEW_WIDTH,
                VIEW_HEIGHT,
                this,
                activeLevel.getBackgroundImage()
        );
        view.setZoom(VIEW_ZOOM);
        view.setLayout(null);
        view.addKeyListener(movement);
        view.setFocusable(true);

        // player animation images
        BodyImage idleRight = new BodyImage("data/NATIVE1.png", 4);
        BodyImage walk1Right = new BodyImage("data/NATIVE1.png", 4);
        BodyImage walk2Right = new BodyImage("data/NATIVE2.png", 4);

        BodyImage idleLeft = new BodyImage("data/NATIVE1_left.png", 4);
        BodyImage walk1Left = new BodyImage("data/NATIVE1_left.png", 4);
        BodyImage walk2Left = new BodyImage("data/NATIVE2_left.png", 4);

        Animation animator = new Animation(
                Native,
                idleRight, walk1Right, walk2Right,
                idleLeft, walk1Left, walk2Left
        );
        world.addStepListener(animator);

        loopController = new GameLoopController(this, world, Native, movement, animator, levelBuilder);
        loopController.addCoreStepListeners();

        Native.addCollisionListener(e -> {
            if (e.getOtherBody() instanceof StaticBody) {
                movement.resetJumps();
            }
        });

        showScreen(view);

        world.start();
        view.requestFocusInWindow();
    }

    // makes the right level object
    private Level createLevel(int levelNumber) {
        if (levelNumber == 1) {
            return new Level1(this, world, levelBuilder);
        }

        if (levelNumber == 2) {
            return new Level2(this, world, levelBuilder);
        }

        return new Level3(this, world, levelBuilder);
    }

    // stops the current game
    private void stopCurrentGame() {
        audioManager.stopBackgroundMusic();

        if (world != null) {
            world.stop();
        }

        activeLevel = null;
        world = null;
        view = null;
        Native = null;
        movement = null;
        levelBuilder = null;
        loopController = null;
        gameRunning = false;
    }

    // checks if the player won or lost
    public void checkWinLoseConditions() {
        if (!gameRunning || gameOver || gameWon || levelTransitionActive || Native == null) {
            return;
        }

        if (lives <= 0) {
            triggerGameOver();
            return;
        }

        if (Native.getPosition().y < FALL_DEATH_Y) {
            triggerGameOver();
        }
    }

    // starts looping background music
    void playBackgroundMusic(String filePath) {
        audioManager.playBackgroundMusic(filePath);
    }

    // applies the universal volume to background music
    public void applyMusicVolume() {
        audioManager.applyMusicVolume();
    }

    // registers a soldier so the game can track enemies left
    public void registerSoldier(Soldier soldier) {
        soldiers.add(soldier);
    }

    // moves the camera with the player
    public void updateCamera() {
        if (gameOver || gameWon || Native == null || view == null) {
            return;
        }

        float halfViewWidth = VIEW_WIDTH / (2f * VIEW_ZOOM);
        float cameraX = Native.getPosition().x;

        if (cameraX < LEVEL_LEFT + halfViewWidth) {
            cameraX = LEVEL_LEFT + halfViewWidth;
        }

        if (cameraX > LEVEL_RIGHT - halfViewWidth) {
            cameraX = LEVEL_RIGHT - halfViewWidth;
        }

        view.setCentre(new Vec2(cameraX, 0f));
    }

    // returns how many soldiers are still alive in the level
    public int getEnemiesRemaining() {
        int enemiesRemaining = 0;

        for (Soldier soldier : soldiers) {
            if (soldier != null && soldier.getWorld() != null && soldier.isAlive()) {
                enemiesRemaining++;
            }
        }

        return enemiesRemaining;
    }

    // checks if all soldiers are gone
    public boolean allSoldiersDefeated() {
        return getEnemiesRemaining() == 0;
    }

    // tries to finish the level when the player touches the flag
    public void tryFinishLevel() {
        if (!gameRunning || gameOver || gameWon || levelTransitionActive) {
            return;
        }

        if (!allSoldiersDefeated()) {
            showStatusMessage("Defeat all enemies first");
            return;
        }

        triggerWin();
    }

    public void triggerGameOver() {
        if (!gameRunning || gameOver || gameWon) {
            return;
        }

        gameOver = true;
        gamePaused = false;
        levelTransitionActive = false;
        nextLevelNumber = 0;
        enteringName = false;
        currentInput = "";
        statusMessage = "";
        updatePauseState();

        if (movement != null) {
            movement.clearMovementInput();
        }

        if (world != null) {
            world.stop();
        }

        audioManager.stopBackgroundMusic();
        SoundPlayer.playSound(SoundEffects.GAME_LOSE);

        if (view != null) {
            view.repaint();
        }
    }

    public void triggerWin() {
        if (!gameRunning || gameOver || gameWon || levelTransitionActive) {
            return;
        }

        if (currentLevel < 3) {
            addScore(250);
            showLevelTransition(currentLevel + 1);
            return;
        }

        addScore(500 + (lives * 200));

        gameWon = true;
        gamePaused = false;
        levelTransitionActive = false;
        nextLevelNumber = 0;
        enteringName = false;
        currentInput = "";
        statusMessage = "";
        updatePauseState();

        if (movement != null) {
            movement.clearMovementInput();
        }

        if (world != null) {
            world.stop();
        }

        audioManager.stopBackgroundMusic();
        SoundPlayer.playSound(SoundEffects.GAME_WIN);

        if (view != null) {
            view.repaint();
        }
    }

    // shows the in-between-level screen and pauses gameplay
    private void showLevelTransition(int levelNumber) {
        levelTransitionActive = true;
        nextLevelNumber = levelNumber;
        gamePaused = false;
        statusMessage = "";
        messageTimer = 0f;

        if (movement != null) {
            movement.clearMovementInput();
        }

        if (world != null) {
            world.stop();
        }

        if (view != null) {
            view.repaint();
            view.requestFocusInWindow();
        }
    }

    // starts the next level from the transition screen
    public void continueToNextLevel() {
        if (!levelTransitionActive || nextLevelNumber <= 0) {
            return;
        }

        startLevel(nextLevelNumber, true);
    }

    public void restartCurrentLevel() {
        startLevel(currentLevel);
    }

    public void returnToMenu() {
        enteringName = false;
        currentInput = "";
        levelTransitionActive = false;
        nextLevelNumber = 0;
        stopCurrentGame();
        mainMenu.showMainMenu();
    }

    // keeps pause handling in one place
    private void updatePauseState() {
        if (view != null) {
            view.repaint();
            view.requestFocusInWindow();
        }
    }

    // pauses the game
    public void pauseGame() {
        if (!gameRunning || gamePaused || gameOver || gameWon || enteringName || levelTransitionActive) {
            return;
        }

        gamePaused = true;

        if (movement != null) {
            movement.clearMovementInput();
        }

        if (world != null) {
            world.stop();
        }

        updatePauseState();
    }

    // resumes the game
    public void resumeGame() {
        if (!gameRunning || !gamePaused || gameOver || gameWon || enteringName || levelTransitionActive) {
            return;
        }

        gamePaused = false;

        if (world != null) {
            world.start();
        }

        updatePauseState();
    }

    // switches between paused and unpaused
    public void togglePause() {
        if (gamePaused) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    public int getLives() {
        return lives;
    }

    public void loseLife() {
        lives--;
        if (lives < 0) {
            lives = 0;
        }
    }

    public void gainLife() {
        if (lives < MAX_LIVES) {
            lives++;
            showStatusMessage("Heart collected!");
        } else {
            showStatusMessage("Lives full");
        }
    }

    public WASDMovement getMovement() {
        return movement;
    }

    public void startAmmoRespawn() {
        ammoRespawning = true;
        ammoRespawnTimer = 0f;
    }

    public boolean isAmmoRespawning() {
        return ammoRespawning;
    }

    public void stopAmmoRespawn() {
        ammoRespawning = false;
        ammoRespawnTimer = 0f;
    }

    public void addAmmoRespawnTime(float amount) {
        ammoRespawnTimer += amount;
    }

    public float getAmmoRespawnTimer() {
        return ammoRespawnTimer;
    }

    public float getAmmoRespawnTimeLimit() {
        return AMMO_RESPAWN_TIME;
    }

    public Vec2 getAmmoRespawnPosition() {
        return ammoRespawnPosition;
    }

    public void setAmmoRespawnPosition(Vec2 ammoRespawnPosition) {
        this.ammoRespawnPosition = ammoRespawnPosition;
    }

    public void showStatusMessage(String message) {
        statusMessage = message;
        messageTimer = MESSAGE_DURATION;

        if (view != null) {
            view.repaint();
        }
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void updateStatusMessage() {
        if (messageTimer > 0) {
            messageTimer -= 1f / 60f;

            if (messageTimer <= 0) {
                statusMessage = "";
            }
        }
    }

    public int getMaxLives() {
        return MAX_LIVES;
    }

    public BodyImage getHeartImage() {
        return heartImage;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public boolean isGamePaused() {
        return gamePaused;
    }

    public boolean isLevelTransitionActive() {
        return levelTransitionActive;
    }

    public int getNextLevelNumber() {
        return nextLevelNumber;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    // in-game name entry state
    public boolean isEnteringName() {
        return enteringName;
    }

    public String getCurrentInput() {
        return currentInput;
    }

    public void addCharacter(char c) {
        if (!enteringName) {
            return;
        }

        if (currentInput.length() < 12) {
            currentInput += c;
        }

        if (view != null) {
            view.repaint();
            view.requestFocusInWindow();
        }
    }

    public void removeCharacter() {
        if (!enteringName) {
            return;
        }

        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
        }

        if (view != null) {
            view.repaint();
            view.requestFocusInWindow();
        }
    }

    public void confirmName() {
        if (!enteringName || resultSaved) {
            return;
        }

        String username = currentInput.trim();

        if (username.isEmpty()) {
            username = "Player";
        }

        LeaderboardManager.saveEntry(username, score, elapsedTime);
        resultSaved = true;
        enteringName = false;
        currentInput = "";

        returnToMenu();
    }

    // adds time to the current run
    public void addElapsedTime(float amount) {
        if (!gamePaused && !gameOver && !gameWon && !levelTransitionActive) {
            elapsedTime += amount;
        }
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public String getFormattedElapsedTime() {
        return LeaderboardManager.formatTime(elapsedTime);
    }

    // adds score to the current run
    public void addScore(int amount) {
        score += amount;
    }

    public int getScore() {
        return score;
    }

    // starts the in-game username entry
    public void saveResultToLeaderboard() {
        if (!gameWon || resultSaved) {
            return;
        }

        enteringName = true;
        currentInput = "";

        if (view != null) {
            view.repaint();
            view.requestFocusInWindow();
        }
    }
}