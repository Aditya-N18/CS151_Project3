package manager.score;

import java.util.Objects;

//one row of the leaderboard

public class HighScoreEntry implements Comparable<HighScoreEntry> {

    private final String gameName;
    private final String username;
    private final int score;

    public HighScoreEntry(String gameName, String username, int score) {
        this.gameName = Objects.requireNonNull(gameName, "gameName");
        this.username = Objects.requireNonNull(username, "username");
        this.score = score;
    }

    public String getGameName() {
        return gameName;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public String serialize() {
        return gameName + "|" + username + "|" + score;
    }

    public static HighScoreEntry deserialize(String line) {
        if (line == null) {
            return null;
        }
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        String[] parts = trimmed.split("\\|", -1);
        if (parts.length != 3) {
            return null;
        }
        try {
            int score = Integer.parseInt(parts[2].trim());
            return new HighScoreEntry(parts[0], parts[1], score);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public int compareTo(HighScoreEntry other) {
        return Integer.compare(other.score, this.score);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HighScoreEntry e)) return false;
        return score == e.score && gameName.equals(e.gameName) && username.equals(e.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameName, username, score);
    }

    @Override
    public String toString() {
        return serialize();
    }
}
