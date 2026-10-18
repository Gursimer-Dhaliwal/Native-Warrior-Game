package game;

import org.jbox2d.common.Vec2;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// handles keyboard movement, shooting and reload controls
public class WASDMovement extends KeyAdapter {

    private Native player;
    private Game game;

    private boolean leftHeld = false;
    private boolean rightHeld = false;

    private final float Speed = 8f;
    private final float Jump_Speed = 10.5f;

    private int jumpsUsed = 0;
    private final int Max_Jumps = 2;

    public static final int DEFAULT_MAG_SIZE = 10;
    public static final int DEFAULT_RESERVE_AMMO = 20;

    private final int MAG_SIZE = DEFAULT_MAG_SIZE;
    private int ammoInMag = DEFAULT_MAG_SIZE;
    private int reserveAmmo = DEFAULT_RESERVE_AMMO;

    private boolean reloading = false;
    private long reloadFinishTime = 0;
    private static final int RELOAD_TIME_MS = 1500;

    public WASDMovement(Native player, Game game) {
        this.player = player;
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        // username input mode (disables gameplay controls)
        if (game.isEnteringName()) {

            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                game.removeCharacter();
                return;
            }

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                game.confirmName();
                return;
            }

            char c = e.getKeyChar();

            if (Character.isLetterOrDigit(c) || c == ' ') {
                game.addCharacter(c);
            }

            return;
        }

        // level transition screen controls
        if (game.isLevelTransitionActive()) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                game.continueToNextLevel();
            }

            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                game.returnToMenu();
            }

            return;
        }

        Vec2 v = player.getLinearVelocity();

        switch (e.getKeyCode()) {

            case KeyEvent.VK_A:
                if (!game.isGamePaused() && !game.isGameOver() && !game.isGameWon()) {
                    leftHeld = true;
                    player.setDirection(-1);
                }
                break;

            case KeyEvent.VK_D:
                if (!game.isGamePaused() && !game.isGameOver() && !game.isGameWon()) {
                    rightHeld = true;
                    player.setDirection(1);
                }
                break;

            case KeyEvent.VK_W:
                if (!game.isGamePaused() && !game.isGameOver() && !game.isGameWon()) {
                    if (jumpsUsed < Max_Jumps) {
                        player.setLinearVelocity(new Vec2(v.x, Jump_Speed));
                        jumpsUsed++;
                        SoundPlayer.playSound(SoundEffects.PLAYER_JUMP, 0.15f);
                    }
                }
                break;

            case KeyEvent.VK_SPACE:
                if (!game.isGamePaused() && !game.isGameOver() && !game.isGameWon()) {
                    shoot();
                }
                break;

            case KeyEvent.VK_R:
                // restart if game ended
                if (game.isGameOver() || game.isGameWon()) {
                    game.restartCurrentLevel();
                }
                // otherwise reload
                else if (!game.isGamePaused()) {
                    startReload();
                }
                break;

            case KeyEvent.VK_S:
                if (game.isGameWon()) {
                    game.saveResultToLeaderboard();
                }
                break;

            case KeyEvent.VK_P:
                if (!game.isGameOver() && !game.isGameWon()) {
                    game.togglePause();
                }
                break;

            case KeyEvent.VK_ESCAPE:
                game.returnToMenu();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {

            case KeyEvent.VK_A:
                leftHeld = false;
                break;

            case KeyEvent.VK_D:
                rightHeld = false;
                break;
        }
    }

    public void updateMovement() {

        if (game.isGamePaused() || game.isGameOver() || game.isGameWon() || game.isLevelTransitionActive()) {
            player.setLinearVelocity(new Vec2(0, player.getLinearVelocity().y));
            return;
        }

        Vec2 v = player.getLinearVelocity();
        float x = 0;

        if (leftHeld && !rightHeld) {
            x = -Speed;
        }

        if (rightHeld && !leftHeld) {
            x = Speed;
        }

        player.setLinearVelocity(new Vec2(x, v.y));

        if (reloading && System.currentTimeMillis() >= reloadFinishTime) {
            finishReload();
        }
    }

    public boolean isMoving() {
        return leftHeld || rightHeld;
    }

    public void resetJumps() {
        jumpsUsed = 0;
    }

    public void clearMovementInput() {
        leftHeld = false;
        rightHeld = false;
    }

    private void shoot() {

        if (reloading) {
            game.showStatusMessage("RELOADING...");
            return;
        }

        if (ammoInMag <= 0) {
            SoundPlayer.playSound(SoundEffects.EMPTY_MAG);
            game.showStatusMessage("MAG EMPTY");
            return;
        }

        Vec2 playerPos = player.getPosition();
        Vec2 bulletStart = new Vec2(playerPos.x + player.getDirection() * 1.5f, playerPos.y);

        new Bullet(player.getWorld(), bulletStart, player.getDirection());

        ammoInMag--;

        SoundPlayer.playSound(SoundEffects.PLAYER_SHOOT, 0.8f);
    }

    private void startReload() {

        if (reloading) {
            return;
        }

        if (ammoInMag == MAG_SIZE) {
            game.showStatusMessage("MAG FULL");
            return;
        }

        if (reserveAmmo <= 0) {
            game.showStatusMessage("NO AMMO");
            return;
        }

        reloading = true;
        reloadFinishTime = System.currentTimeMillis() + RELOAD_TIME_MS;

        SoundPlayer.playSound(SoundEffects.RELOAD, 0.25f);
        game.showStatusMessage("RELOADING");
    }

    private void finishReload() {

        int bulletsNeeded = MAG_SIZE - ammoInMag;
        int bulletsToLoad = Math.min(bulletsNeeded, reserveAmmo);

        ammoInMag += bulletsToLoad;
        reserveAmmo -= bulletsToLoad;
        reloading = false;

        game.showStatusMessage("");
    }

    public int getAmmoInMag() {
        return ammoInMag;
    }

    public int getReserveAmmo() {
        return reserveAmmo;
    }

    public boolean isReloading() {
        return reloading;
    }

    public void addReserveAmmo(int amount) {
        reserveAmmo += amount;
        game.showStatusMessage("Ammo collected!");
    }

    public void setAmmoState(int ammoInMag, int reserveAmmo) {
        this.ammoInMag = Math.max(0, Math.min(MAG_SIZE, ammoInMag));
        this.reserveAmmo = Math.max(0, reserveAmmo);
        this.reloading = false;
        this.reloadFinishTime = 0;
    }
}