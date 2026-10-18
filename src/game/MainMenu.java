package game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// handles all menu screens for the game
public class MainMenu {

    private final Game game;

    private final Image menuBackground;
    private final Image titleImage;

    private static final String MENU_MUSIC_PATH = "data/menu_music.wav";

    private static final Color GOLD = new Color(218, 165, 32);
    private static final Color BUTTON_GREEN = new Color(42, 78, 42);
    private static final Color BUTTON_HOVER = new Color(160, 130, 45);
    private static final Color PANEL_TINT = new Color(10, 18, 10, 165);

    public MainMenu(Game game) {
        this.game = game;

        menuBackground = new ImageIcon("data/menu_background.png").getImage();
        titleImage = new ImageIcon("data/menu_title.png").getImage();
    }

    // shows the main menu
    public void showMainMenu() {
        game.playBackgroundMusic(MENU_MUSIC_PATH);

        JPanel menuPanel = createMenuBackgroundPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(35, 0, 40, 0));

        JLabel titleLabel = createTitleImageLabel();

        JButton playButton = createMenuButton("Play");
        JButton instructionsButton = createMenuButton("Instructions");
        JButton leaderboardButton = createMenuButton("Leaderboard");
        JButton settingsButton = createMenuButton("Settings");
        JButton exitButton = createMenuButton("Exit");

        playButton.addActionListener(e -> game.startLevelFromMenu(1));
        instructionsButton.addActionListener(e -> showInstructionsMenu());
        leaderboardButton.addActionListener(e -> showLeaderboardMenu());
        settingsButton.addActionListener(e -> showSettingsMenu());
        exitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 28)));
        menuPanel.add(playButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        menuPanel.add(instructionsButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        menuPanel.add(leaderboardButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        menuPanel.add(settingsButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        menuPanel.add(exitButton);
        menuPanel.add(Box.createVerticalGlue());

        game.showScreen(menuPanel);
    }

    // shows the instructions screen
    public void showInstructionsMenu() {
        JPanel menuPanel = createMenuBackgroundPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(35, 0, 35, 0));

        JLabel title = createHeadingLabel("Instructions");

        JTextArea instructionsText = new JTextArea();
        instructionsText.setEditable(false);
        instructionsText.setFocusable(false);
        instructionsText.setLineWrap(true);
        instructionsText.setWrapStyleWord(true);
        instructionsText.setOpaque(false);
        instructionsText.setForeground(Color.WHITE);
        instructionsText.setFont(new Font("Arial", Font.PLAIN, 20));
        instructionsText.setText(
                "CONTROLS\n\n" +
                        "A - Move left\n" +
                        "D - Move right\n" +
                        "W - Jump\n" +
                        "SPACE - Shoot\n" +
                        "R - Reload\n" +
                        "P - Pause / Resume\n" +
                        "ESC - Return to menu\n\n" +
                        "HOW TO PLAY\n\n" +
                        "- Defeat all enemies in the level.\n" +
                        "- Once all enemies are defeated, reach the flag.\n" +
                        "- Pick up ammo boxes to keep shooting.\n" +
                        "- Pick up hearts to gain lives back.\n" +
                        "- Your timer and score are shown in the HUD.\n" +
                        "- Finish the campaign and press S to save your result."
        );

        JScrollPane scrollPane = createMenuScrollPane(instructionsText);

        JButton backButton = createMenuButton("Back");
        backButton.addActionListener(e -> showMainMenu());

        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(title);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        menuPanel.add(centerComponent(scrollPane));
        menuPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        menuPanel.add(backButton);
        menuPanel.add(Box.createVerticalGlue());

        game.showScreen(menuPanel);
    }

    // shows the leaderboard screen
    public void showLeaderboardMenu() {
        JPanel menuPanel = createMenuBackgroundPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(35, 0, 35, 0));

        JLabel title = createHeadingLabel("Leaderboard");

        JTextArea leaderboardText = new JTextArea();
        leaderboardText.setEditable(false);
        leaderboardText.setFocusable(false);
        leaderboardText.setOpaque(false);
        leaderboardText.setForeground(Color.WHITE);
        leaderboardText.setFont(new Font("Monospaced", Font.PLAIN, 20));
        leaderboardText.setText(LeaderboardManager.getFormattedLeaderboard());

        JScrollPane scrollPane = createMenuScrollPane(leaderboardText);

        JButton backButton = createMenuButton("Back");
        backButton.addActionListener(e -> showMainMenu());

        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(title);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        menuPanel.add(centerComponent(scrollPane));
        menuPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        menuPanel.add(backButton);
        menuPanel.add(Box.createVerticalGlue());

        game.showScreen(menuPanel);
    }

    // shows the settings screen
    public void showSettingsMenu() {
        JPanel menuPanel = createMenuBackgroundPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(35, 0, 35, 0));

        JLabel title = createHeadingLabel("Settings");

        JLabel volumeLabel = new JLabel("Universal Sound Volume");
        volumeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        volumeLabel.setForeground(Color.WHITE);
        volumeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        int currentVolume = (int) (GameSettings.getMasterVolume() * 100f);
        JSlider volumeSlider = new JSlider(0, 100, currentVolume);
        volumeSlider.setMajorTickSpacing(25);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setOpaque(false);
        volumeSlider.setForeground(Color.WHITE);
        volumeSlider.setFont(new Font("Arial", Font.BOLD, 14));
        volumeSlider.setMaximumSize(new Dimension(420, 60));
        volumeSlider.setAlignmentX(Component.CENTER_ALIGNMENT);

        volumeSlider.addChangeListener(e -> {
            GameSettings.setMasterVolume(volumeSlider.getValue() / 100f);
            game.applyMusicVolume();
        });

        JLabel skipLabel = new JLabel("Skip To Any Level");
        skipLabel.setFont(new Font("Arial", Font.BOLD, 24));
        skipLabel.setForeground(Color.WHITE);
        skipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton level1Button = createMenuButton("Skip To Level 1");
        JButton level2Button = createMenuButton("Skip To Level 2");
        JButton level3Button = createMenuButton("Skip To Level 3");
        JButton backButton = createMenuButton("Back");

        level1Button.addActionListener(e -> game.startLevelFromMenu(1));
        level2Button.addActionListener(e -> game.startLevelFromMenu(2));
        level3Button.addActionListener(e -> game.startLevelFromMenu(3));
        backButton.addActionListener(e -> showMainMenu());

        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(title);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        menuPanel.add(volumeLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        menuPanel.add(volumeSlider);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        menuPanel.add(skipLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        menuPanel.add(level1Button);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        menuPanel.add(level2Button);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        menuPanel.add(level3Button);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        menuPanel.add(backButton);
        menuPanel.add(Box.createVerticalGlue());

        game.showScreen(menuPanel);
    }

    // creates the menu background panel
    private JPanel createMenuBackgroundPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                // draw menu background image
                g2.drawImage(menuBackground, 0, 0, getWidth(), getHeight(), this);

                // dark overlay so the title and buttons stand out
                g2.setColor(new Color(0, 0, 0, 115));
                g2.fillRect(0, 0, getWidth(), getHeight());

                // soft central panel for the menu content
                int panelWidth = 520;
                int panelHeight = 650;
                int panelX = (getWidth() - panelWidth) / 2;
                int panelY = 55;

                g2.setColor(PANEL_TINT);
                g2.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 38, 38);

                g2.dispose();
            }
        };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        return panel;
    }

    // creates the image used for the main title
    private JLabel createTitleImageLabel() {
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                // draw title only
                g2.drawImage(titleImage, 0, 0, getWidth(), getHeight(), this);

                g2.dispose();
            }
        };

        label.setPreferredSize(new Dimension(650, 250));
        label.setMaximumSize(new Dimension(650, 250));
        label.setMinimumSize(new Dimension(650, 250));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        return label;
    }

    // creates a heading label for submenu pages
    private JLabel createHeadingLabel(String text) {
        JLabel title = new JLabel(text);
        title.setFont(new Font("Serif", Font.BOLD, 50));
        title.setForeground(GOLD);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        return title;
    }

    // creates a scroll pane that matches the menu style
    private JScrollPane createMenuScrollPane(JTextArea textArea) {
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(760, 360));
        scrollPane.setMaximumSize(new Dimension(760, 360));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(GOLD, 2, true),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));
        return scrollPane;
    }

    // creates a styled menu button
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 22));
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_GREEN);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorder(new LineBorder(GOLD, 2, true));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(280, 56));
        button.setPreferredSize(new Dimension(280, 56));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_GREEN);
            }
        });

        return button;
    }

    // centres wider components nicely
    private JPanel centerComponent(JComponent component) {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.add(component);
        return wrapper;
    }
}