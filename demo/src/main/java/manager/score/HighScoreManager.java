package manager.score;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utils.Constants;
import utils.FileUtil;

//show top 5 scores and record new scores

public class HighScoreManager {

    private final String filePath;
    private final List<HighScoreEntry> entries = new ArrayList<>();
    private boolean loaded = false;

    public HighScoreManager() {
        this(Constants.HIGH_SCORES_FILE);
    }

    public HighScoreManager(String filePath) {
        this.filePath = filePath;
    }

    public void loadScores() {
        entries.clear();
        for (String line : FileUtil.readLines(filePath)) {
            HighScoreEntry e = HighScoreEntry.deserialize(line);
            if (e != null) {
                entries.add(e);
            }
        }
        loaded = true;
    }

    public void saveScores() {
        List<String> lines = new ArrayList<>(entries.size());
        for (HighScoreEntry e : entries) {
            lines.add(e.serialize());
        }
        try {
            FileUtil.writeLines(filePath, lines);
        } catch (IOException ex) {
            System.err.println("HighScoreManager: failed to save: " + ex.getMessage());
        }
    }

    public void recordScore(String username, String gameName, int score) {
        ensureLoaded();
        entries.add(new HighScoreEntry(gameName, username, score));
        saveScores();
    }

    public List<HighScoreEntry> getTopScores(String gameName, int limit) {
        ensureLoaded();
        List<HighScoreEntry> filtered = new ArrayList<>();
        for (HighScoreEntry e : entries) {
            if (e.getGameName().equalsIgnoreCase(gameName)) {
                filtered.add(e);
            }
        }
        Collections.sort(filtered);
        if (limit > 0 && filtered.size() > limit) {
            return new ArrayList<>(filtered.subList(0, limit));
        }
        return filtered;
    }

    public void initializeDefaultScores() {
        ensureLoaded();
        if (!hasEntryForGame(Constants.GAME_BLACKJACK)) {
            entries.add(new HighScoreEntry(
                    Constants.GAME_BLACKJACK,
                    Constants.DEFAULT_HIGHSCORE_USER,
                    Constants.DEFAULT_STARTING_SCORE));
        }
        if (!hasEntryForGame(Constants.GAME_SNAKE)) {
            entries.add(new HighScoreEntry(
                    Constants.GAME_SNAKE,
                    Constants.DEFAULT_HIGHSCORE_USER,
                    Constants.DEFAULT_STARTING_SCORE));
        }
        saveScores();
    }

    private boolean hasEntryForGame(String gameName) {
        for (HighScoreEntry e : entries) {
            if (e.getGameName().equalsIgnoreCase(gameName)) {
                return true;
            }
        }
        return false;
    }

    private void ensureLoaded() {
        if (!loaded) {
            loadScores();
        }
    }
}
