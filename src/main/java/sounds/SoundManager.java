package sounds;

import javax.sound.sampled.*;
import java.io.IOException;

public class SoundManager {
    private Clip chickenSound;
    private Clip backgroundMusicStart;

    public SoundManager() {
        try {
            AudioInputStream targetStream = AudioSystem.getAudioInputStream(getClass().getResource("target.wav"));
            chickenSound = AudioSystem.getClip();
            chickenSound.open(targetStream);

            AudioInputStream backgroundStartStream = AudioSystem.getAudioInputStream(getClass().getResource("backgroundMusic.wav"));
            backgroundMusicStart = AudioSystem.getClip();
            backgroundMusicStart.open(backgroundStartStream);
            backgroundMusicStart.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading sound effects");
            e.printStackTrace();
        }
    }

    public void playChickenSound() {
        if (chickenSound != null && !chickenSound.isRunning()) {
            chickenSound.setFramePosition(0);
            chickenSound.start();
        }
    }

    public void startBackgroundStartMusic() {
        backgroundMusicStart.start();
    }

    public void stopBackgroundStartMusic() {
        backgroundMusicStart.stop();
    }

}