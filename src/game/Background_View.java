package game;

import city.cs.engine.UserView;
import city.cs.engine.World;

import java.awt.*;

// draws the game view and hud
public class Background_View extends UserView {

    private final Game game;
    private final Image backgroundImage;
    private final Image heartHudImage;

    public Background_View(World world, int width, int height, Game game, String backgroundPath) {
        super(world, width, height);

        this.game = game;
        this.backgroundImage = new javax.swing.ImageIcon(backgroundPath).getImage();
        this.heartHudImage = new javax.swing.ImageIcon("data/heart.png").getImage();
    }

    @Override
    protected void paintBackground(Graphics2D g) {
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    protected void paintForeground(Graphics2D g) {
        drawHud(g);

        if (game.isLevelTransitionActive()) {
            drawLevelTransitionOverlay(g);
        }

        if (game.isGamePaused()) {
            drawPauseOverlay(g);
        }

        if (game.isGameOver()) {
            drawGameOverOverlay(g);
        }

        if (game.isGameWon()) {
            drawGameWonOverlay(g);
        }

        if (game.isEnteringName()) {
            drawNameEntryOverlay(g);
        }
    }

    // draws the main hud
    private void drawHud(Graphics2D g) {

        // top bar background
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), 60);

        g.setColor(new Color(218, 165, 32));
        g.setStroke(new BasicStroke(2));
        g.drawLine(0, 60, getWidth(), 60);

        // LIVES (left side)
        int heartX = 20;
        int heartY = 15;

        for (int i = 0; i < game.getLives(); i++) {
            g.drawImage(heartHudImage, heartX + (i * 40), heartY, 30, 30, this);
        }

        // AMMO (left-middle)
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.WHITE);
        g.drawString(
                "AMMO: " + game.getMovement().getAmmoInMag() + " / " + game.getMovement().getReserveAmmo(),
                200,
                35
        );

        // TIME (center)
        String timeText = "TIME: " + game.getFormattedElapsedTime();
        FontMetrics metrics = g.getFontMetrics();
        int timeX = (getWidth() - metrics.stringWidth(timeText)) / 2;
        g.drawString(timeText, timeX, 35);

        // SCORE (right-middle)
        g.drawString(
                "SCORE: " + game.getScore(),
                getWidth() - 300,
                35
        );

        // ENEMIES (far right)
        g.drawString(
                "ENEMIES: " + game.getEnemiesRemaining(),
                getWidth() - 150,
                35
        );

        // status message (center top, slightly below bar)
        if (!game.getStatusMessage().isEmpty()) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.YELLOW);

            FontMetrics msgMetrics = g.getFontMetrics();
            int x = (getWidth() - msgMetrics.stringWidth(game.getStatusMessage())) / 2;

            g.drawString(game.getStatusMessage(), x, 85);
        }
    }

    // draws the pause overlay
    private void drawPauseOverlay(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.setColor(Color.WHITE);
        drawCenteredString(g, "PAUSED", getWidth(), getHeight() / 2 - 30);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        drawCenteredString(g, "Press P to continue", getWidth(), getHeight() / 2 + 20);
    }

    // draws the between-level overlay
    private void drawLevelTransitionOverlay(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 190));
        g.fillRect(0, 0, getWidth(), getHeight());

        int boxWidth = 520;
        int boxHeight = 320;
        int x = (getWidth() - boxWidth) / 2;
        int y = (getHeight() - boxHeight) / 2;

        g.setColor(new Color(18, 18, 18, 225));
        g.fillRoundRect(x, y, boxWidth, boxHeight, 30, 30);

        g.setColor(new Color(218, 165, 32));
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(x, y, boxWidth, boxHeight, 30, 30);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 42));
        drawCenteredString(g, "LEVEL " + game.getNextLevelNumber(), getWidth(), y + 65);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        drawCenteredString(g, "Get ready for the next stage", getWidth(), y + 105);

        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString("Score:", x + 65, y + 165);
        g.drawString(String.valueOf(game.getScore()), x + 210, y + 165);

        g.drawString("Lives:", x + 65, y + 210);
        g.drawString(String.valueOf(game.getLives()), x + 210, y + 210);

        g.drawString("Ammo:", x + 65, y + 255);
        g.drawString(
                game.getMovement().getAmmoInMag() + " / " + game.getMovement().getReserveAmmo(),
                x + 210,
                y + 255
        );

        g.setFont(new Font("Arial", Font.PLAIN, 22));
        g.setColor(new Color(235, 235, 235));
        drawCenteredString(g, "Press SPACE to start", getWidth(), y + 300);
    }

    // draws the game over overlay
    private void drawGameOverOverlay(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setFont(new Font("Arial", Font.BOLD, 52));
        g.setColor(Color.RED);
        drawCenteredString(g, "GAME OVER", getWidth(), getHeight() / 2 - 50);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.setColor(Color.WHITE);
        drawCenteredString(g, "Press R to restart", getWidth(), getHeight() / 2 + 10);
        drawCenteredString(g, "Press ESC to return to menu", getWidth(), getHeight() / 2 + 45);
    }

    // draws the win overlay
    private void drawGameWonOverlay(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setFont(new Font("Arial", Font.BOLD, 52));
        g.setColor(new Color(218, 165, 32));
        drawCenteredString(g, "YOU WIN!", getWidth(), getHeight() / 2 - 70);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.setColor(Color.WHITE);
        drawCenteredString(g, "Press S to save your score", getWidth(), getHeight() / 2);
        drawCenteredString(g, "Press R to restart", getWidth(), getHeight() / 2 + 35);
        drawCenteredString(g, "Press ESC to return to menu", getWidth(), getHeight() / 2 + 70);
    }

    // draws the name entry overlay
    private void drawNameEntryOverlay(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 190));
        g.fillRect(0, 0, getWidth(), getHeight());

        int boxWidth = 460;
        int boxHeight = 160;
        int x = (getWidth() - boxWidth) / 2;
        int y = (getHeight() - boxHeight) / 2;

        g.setColor(new Color(20, 45, 20));
        g.fillRoundRect(x, y, boxWidth, boxHeight, 25, 25);

        g.setColor(Color.YELLOW);
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(x, y, boxWidth, boxHeight, 25, 25);

        g.setFont(new Font("Arial", Font.BOLD, 26));
        g.setColor(Color.WHITE);
        g.drawString("Enter Username", x + 25, y + 40);

        g.setColor(new Color(0, 0, 0, 120));
        g.fillRoundRect(x + 20, y + 55, boxWidth - 40, 45, 15, 15);

        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.WHITE);
        g.drawString(game.getCurrentInput() + "_", x + 30, y + 85);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Press ENTER to save", x + 25, y + 130);
        g.drawString("BACKSPACE to delete", x + 240, y + 130);
    }

    // draws centred text
    private void drawCenteredString(Graphics2D g, String text, int width, int y) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x = (width - metrics.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }
}