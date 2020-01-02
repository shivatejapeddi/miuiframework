package android.view;

import android.view.WindowInsets.Type;

public interface WindowInsetsController {
    void controlWindowInsetsAnimation(int i, WindowInsetsAnimationControlListener windowInsetsAnimationControlListener);

    void hide(int i);

    void show(int i);

    void controlInputMethodAnimation(WindowInsetsAnimationControlListener listener) {
        controlWindowInsetsAnimation(Type.ime(), listener);
    }

    void showInputMethod() {
        show(Type.ime());
    }

    void hideInputMethod() {
        hide(Type.ime());
    }
}
