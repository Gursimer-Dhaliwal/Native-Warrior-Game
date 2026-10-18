package game;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// handles saving and loading leaderboard results
public class LeaderboardManager {

    private static final String FILE_PATH = "leaderboard.txt";

    // saves one leaderboard entry to file
    public static void saveEntry(String name, int score, float timeSeconds) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            writer.println(cleanName(name) + "|" + score + "|" + timeSeconds);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // loads all leaderboard entries from file
    public static List<LeaderboardEntry> loadEntries() {
        List<LeaderboardEntry> entries = new ArrayList<>();

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return entries;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts.length != 3) {
                    continue;
                }

                try {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    float timeSeconds = Float.parseFloat(parts[2]);

                    entries.add(new LeaderboardEntry(name, score, timeSeconds));
                } catch (NumberFormatException ignored) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        entries.sort(
                Comparator.comparingInt(LeaderboardEntry::getScore).reversed()
                        .thenComparingDouble(LeaderboardEntry::getTimeSeconds)
        );

        return entries;
    }

    // returns formatted leaderboard text
    public static String getFormattedLeaderboard() {
        List<LeaderboardEntry> entries = loadEntries();

        if (entries.isEmpty()) {
            return "No leaderboard entries saved yet.";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("RANK   NAME                     SCORE      TIME\n");
        builder.append("----------------------------------------------------\n");

        int limit = Math.min(entries.size(), 10);

        for (int i = 0; i < limit; i++) {
            LeaderboardEntry entry = entries.get(i);

            String rank = String.valueOf(i + 1);
            String name = padRight(entry.getName(), 22);
            String score = padRight(String.valueOf(entry.getScore()), 10);
            String time = formatTime(entry.getTimeSeconds());

            builder.append(rank)
                    .append("      ")
                    .append(name)
                    .append(score)
                    .append(time)
                    .append("\n");
        }

        return builder.toString();
    }

    // formats seconds as mm:ss
    public static String formatTime(float totalSeconds) {
        int seconds = Math.max(0, (int) totalSeconds);
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;

        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    // removes bad file characters and trims the name
    private static String cleanName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Player";
        }

        return name.replace("|", "").trim();
    }

    // pads text for the leaderboard display
    private static String padRight(String text, int width) {
        StringBuilder builder = new StringBuilder(text);

        while (builder.length() < width) {
            builder.append(" ");
        }

        return builder.toString();
    }
}