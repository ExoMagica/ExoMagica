package exomagica.api.spells;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class SpellKey {

    public static final SpellKey MOUSE_1 = new SpellKey(true, 0);
    public static final SpellKey MOUSE_2 = new SpellKey(true, 1);
    public static final SpellKey KEY_0 = new SpellKey(false, Keyboard.KEY_0);
    public static final SpellKey KEY_1 = new SpellKey(false, Keyboard.KEY_1);
    public static final SpellKey KEY_2 = new SpellKey(false, Keyboard.KEY_2);
    public static final SpellKey KEY_3 = new SpellKey(false, Keyboard.KEY_3);
    public static final SpellKey KEY_4 = new SpellKey(false, Keyboard.KEY_4);
    public static final SpellKey KEY_5 = new SpellKey(false, Keyboard.KEY_5);
    public static final SpellKey KEY_6 = new SpellKey(false, Keyboard.KEY_6);
    public static final SpellKey KEY_7 = new SpellKey(false, Keyboard.KEY_7);
    public static final SpellKey KEY_8 = new SpellKey(false, Keyboard.KEY_8);
    public static final SpellKey KEY_9 = new SpellKey(false, Keyboard.KEY_9);

    private final boolean mouse;
    private final int key;
    public SpellKey(boolean mouse, int key) {
        this.mouse = mouse;
        this.key = key;
    }

    public boolean isDown() {
        return mouse ? Mouse.isButtonDown(key) : Keyboard.isKeyDown(key);
    }

}
