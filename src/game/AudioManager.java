package game;

import javax.sound.sampled.*;
import java.io.File;

// handles looping background music for the game
public class AudioManager {

    private Clip backgroundClip;
    private String currentMusicPath;

    // plays looping background music, but only restarts if it is a different track
    public void playBackgroundMusic(String filePath) {
        try {
            // if this exact track is already playing, do nothing
            if (backgroundClip != null
                    && backgroundClip.isRunning()
                    && filePath.equals(currentMusicPath)) {
                return;
            }

            // stop old track if a different one is being requested
            stopBackgroundMusic();

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);

            currentMusicPath = filePath;
            applyMusicVolume();

            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundClip.start();

        } catch (Exception e) {
            System.out.println("Error playing background music: " + filePath);
            e.printStackTrace();
        }
    }

    // stops the current background music
    public void stopBackgroundMusic() {
        if (backgroundClip != null) {
            backgroundClip.stop();
            backgroundClip.close();
            backgroundClip = null;
        }

        currentMusicPath = null;
    }

    // applies the current master volume to the music clip
    public void applyMusicVolume() {
        if (backgroundClip == null) {
            return;
        }

        try {
            if (backgroundClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl =
                        (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);

                float volume = GameSettings.getMasterVolume();

                if (volume < 0.0001f) {
                    volume = 0.0001f;
                }
                if (volume > 1.0f) {
                    volume = 1.0f;
                }

                float dB = (float) (20.0 * Math.log10(volume));

                if (dB < gainControl.getMinimum()) {
                    dB = gainControl.getMinimum();
                }
                if (dB > gainControl.getMaximum()) {
                    dB = gainControl.getMaximum();
                }

                gainControl.setValue(dB);
            }
        } catch (Exception e) {
            System.out.println("Error applying music volume");
            e.printStackTrace();
        }
    }
}