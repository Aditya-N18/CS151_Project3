package utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayer {

    private static MediaPlayer currentPlayer;

    public static void play(String fileName) {
        stop();

        String path = MusicPlayer.class
                .getResource("/music/" + fileName)
                .toExternalForm();

        Media media = new Media(path);

        currentPlayer = new MediaPlayer(media);

        currentPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        currentPlayer.setVolume(0.25);

        currentPlayer.play();
    }

    public static void stop() {
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer = null;
        }
    }
}
