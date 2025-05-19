package pt.isec.pa.chess.ui.res;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class SoundManager {
    private SoundManager() {
    }

    private static MediaPlayer mp;

    public static boolean play(String filename) {
        try {
            var url = SoundManager.class.getResource("sounds/en/" + filename);
            if (url == null) return false;
            String path = url.toExternalForm();
            Media music = new Media(path);
            stop();
            mp = new MediaPlayer(music);
            mp.setStartTime(Duration.ZERO);
            mp.setStopTime(music.getDuration());
            mp.setAutoPlay(true);

            // mp.setOnPlaying(()-> System.out.println("onPlaying"));
            // mp.setOnReady(()-> System.out.println("onReady"));
            // mp.setOnEndOfMedia(()-> System.out.println("onEndMedia"));
            // mp.setOnStopped(()-> System.out.println("onStopped"));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isPlaying() {
        return mp != null && mp.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public static void stop() {
        if (mp != null && mp.getStatus() == MediaPlayer.Status.PLAYING) {
            mp.stop();
        }
    }

    public static List<String> getSoundList() {
        File soundsDir = new File(SoundManager.class.getResource("sounds/en/").getFile());
        return Arrays.stream(soundsDir.listFiles()).map(x -> x.getName()).toList();
    }
}
