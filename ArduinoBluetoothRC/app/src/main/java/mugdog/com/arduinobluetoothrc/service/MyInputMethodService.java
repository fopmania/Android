package mugdog.com.arduinobluetoothrc.service;

import android.annotation.SuppressLint;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.View;

import mugdog.com.arduinobluetoothrc.R;
import mugdog.com.arduinobluetoothrc.TerminalActivity;

public class MyInputMethodService extends InputMethodService implements KeyboardView.OnKeyListener{
    public final static int CodeDelete = -5; // Keyboard.KEYCODE_DELETE
    public final static int CodeCancel = -3; // Keyboard.KEYCODE_CANCEL
    public final static int CodeDone = -4; // Keyboard.KEYCODE_DONE
    public final static int CodePrev = 55000;
    public final static int CodeAllLeft = 55001;
    public final static int CodeLeft = 55002;
    public final static int CodeRight = 55003;
    public final static int CodeAllRight = 55004;
    public final static int CodeNext = 55005;
    public final static int CodeClear = 55006;

    @Override
    public View onCreateInputView() {
        KeyboardView keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.hexkeyboardview, null);
        Keyboard keyboard = new Keyboard(this, R.xml.hex_keyboard);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener((KeyboardView.OnKeyboardActionListener) this);
        return keyboardView;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
/*
        if(keyCode == CodeDone){
            if(edittext.getText().length() != 0)
                ((TerminalActivity)mHostActivity).sendCommand(edittext.getText().toString(), true);
        }
        else if (keyCode == CodeCancel) {
            hideCustomKeyboard();
        } else if (keyCode == CodeDelete) {
            if (editable != null && start > 0) editable.delete(start - 1, start);
        } else if (keyCode == CodeClear) {
            if (editable != null) editable.clear();
        } else if (keyCode == CodeLeft) {
            if (start > 0) edittext.setSelection(start - 1);
        } else if (keyCode == CodeRight) {
            if (start < edittext.length()) edittext.setSelection(start + 1);
        } else if (keyCode == CodeAllLeft) {
            edittext.setSelection(0);
        } else if (keyCode == CodeAllRight) {
            edittext.setSelection(edittext.length());
        } else if (keyCode == CodePrev) {
            @SuppressLint("WrongConstant") View focusNew = edittext.focusSearch(View.FOCUS_BACKWARD);
            if (focusNew != null) focusNew.requestFocus();
        } else if (keyCode == CodeNext) {
            @SuppressLint("WrongConstant") View focusNew = edittext.focusSearch(View.FOCUS_FORWARD);
            if (focusNew != null) focusNew.requestFocus();
        } else { // insert character
            editable.insert(start, Character.toString((char) keyCode));
        }
        */
        return false;
    }
}
