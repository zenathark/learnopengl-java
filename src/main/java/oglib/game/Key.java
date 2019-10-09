package oglib.game;

import java.util.ArrayList;

public class Key {
    private final int keyCode;
    private static ArrayList<Key> keys;

    private Key(int keyCode) {
        this.keyCode = keyCode;
    }

    @Override
    public int hashCode() {
        return keyCode;
    }

    /**
     * Factory method of each valid key.
     */
    public static ArrayList<Key> getKeys() {
        if (keys != null)
            return keys;
        keys = new ArrayList<>(349);
        for (int i = 0; i < 348; i++) {
            keys.add(new Key(i));
        }
        return keys;
    }
}