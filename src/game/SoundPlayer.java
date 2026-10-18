package game;

import javax.sound.sampled.*;
import java.io.File;

public class SoundPlayer {

    // plays a sound at full volume
    public static void playSound(String fileName) {
        playSound(fileName, 1.0f);
    }

    // plays a sound with custom volume
    // 1.0f = normal volume
    // 0.5f = quieter
    // 0.0f = almost silent
    public static void playSound(String fileName, float volume) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(fileName));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

                if (volume < 0.0001f) {
                    volume = 0.0001f;
                }

                float dB = (float) (20.0 * Math.log10(volume));
                gainControl.setValue(dB);
            }

            clip.start();

        } catch (Exception e) {
            System.out.println("Error playing sound: " + fileName);
            e.printStackTrace();
        }
    }
}