package game;

// stores game settings chosen from the menu
public class GameSettings {

    private static float masterVolume = 0.7f;   // 0.0 to 1.0

    public static float getMasterVolume() {
        return masterVolume;
    }

    public static void setMasterVolume(float masterVolume) {
        if (masterVolume < 0f) {
            GameSettings.masterVolume = 0f;
        } else if (masterVolume > 1f) {
            GameSettings.masterVolume = 1f;
        } else {
            GameSettings.masterVolume = masterVolume;
        }
    }
}