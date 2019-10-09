package oglib.game;

import java.util.HashMap;

public class GameState implements IGameState {

    private static GameState innerReference = null;
    private final HashMap<Key, KeyStates> keyboard = new HashMap<>();

    public void setKey(Key k, KeyStates state) {
        keyboard.put(k, state);
    }

    public KeyStates getKeyState(Key k) {
        return keyboard.get(k);
    }

    private GameState() {
        var keys = Key.getKeys();
        for (var k : keys)
            keyboard.put(k, KeyStates.IDLE);
    }

    public static GameState getGameState() {
        if (innerReference == null) {
            innerReference = new GameState();
        }
        return innerReference;
    }
}