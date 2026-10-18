package game;

// stores one leaderboard result
public class LeaderboardEntry {

    private final String name;
    private final int score;
    private final float timeSeconds;

    public LeaderboardEntry(String name, int score, float timeSeconds) {
        this.name = name;
        this.score = score;
        this.timeSeconds = timeSeconds;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public float getTimeSeconds() {
        return timeSeconds;
    }
}