package oglib.game;

public interface IGameState {

    enum KeyStates {
        IDLE, PRESSED, RELEASED, HOLD
    }

    public void setKey(Key k, KeyStates state);

    public KeyStates getKeyState(Key k);
}