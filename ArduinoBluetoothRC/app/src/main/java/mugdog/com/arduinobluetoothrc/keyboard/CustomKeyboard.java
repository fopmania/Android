/**
 * Copyright 2013 Maarten Pennings
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * If you use this software in a product, an acknowledgment in the product
 * documentation would be appreciated but is not required.
 */

package mugdog.com.arduinobluetoothrc.keyboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import mugdog.com.arduinobluetoothrc.R;
import mugdog.com.arduinobluetoothrc.TerminalActivity;

/**
 * When an activity hosts a keyboardView, this class allows several EditText's to register for it.
 *
 * @author Maarten Pennings
 * @date   2012 December 23
 */
public class CustomKeyboard {

    /** A link to the KeyboardView that is used to render this CustomKeyboard. */
    private KeyboardView mKeyboardView;
    /** A link to the activity that hosts the {@link #mKeyboardView}. */
    private Activity     mHostActivity;

    private ViewGroup   mParentViewGroup;

    private KeyListener preKeyListener = null;
    private int preInputType;
    private OnFocusChangeListener preOnFocusChangeListener = null;
    private OnClickListener preOnClickListener = null;
    private OnTouchListener preOnTouchListener = null;
    private TextWatcher     mTW  = null;
    private EditText        mEditText;



        /** The key (code) handler. */
    private OnKeyboardActionListener mOnKeyboardActionListener;

    {
        mOnKeyboardActionListener = new OnKeyboardActionListener() {

            public final static int CodeDelete = -5; // Keyboard.KEYCODE_DELETE
            public final static int CodeCancel = -3; // Keyboard.KEYCODE_CANCEL
            public final static int CodeDone = -4; // Keyboard.KEYCODE_DONE
            public final static int CodeEnter = 42; // Keyboard.KEYCODE_ENTER
            public final static int CodePrev = 55000;
            public final static int CodeAllLeft = 55001;
            public final static int CodeLeft = 55002;
            public final static int CodeRight = 55003;
            public final static int CodeAllRight = 55004;
            public final static int CodeNext = 55005;
            public final static int CodeClear = 55006;

            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                // NOTE We can say '<Key android:codes="49,50" ... >' in the xml file; all codes come in keyCodes, the first in this list in primaryCode
                // Get the EditText and its Editable

                View focusCurrent = mHostActivity.getWindow().getCurrentFocus();
                if (focusCurrent == null)
                    return; //|| focusCurrent.getClass()!=EditText.class ) return;
                EditText edittext = (EditText) focusCurrent;
                Editable editable = edittext.getText();
                int start = edittext.getSelectionStart();
                // Apply the key to the edittext

                if(primaryCode == CodeDone){
                    if(edittext.getText().length() != 0)
                        ((TerminalActivity)mHostActivity).sendCommand(edittext.getText().toString(), true);
                }
                else if (primaryCode == CodeCancel) {
                    hideCustomKeyboard();
                } else if (primaryCode == CodeDelete) {
                    if (editable != null && start > 0) editable.delete(start - 1, start);
                } else if (primaryCode == CodeClear) {
                    if (editable != null) editable.clear();
                } else if (primaryCode == CodeLeft) {
                    if (start > 0) edittext.setSelection(start - 1);
                } else if (primaryCode == CodeRight) {
                    if (start < edittext.length()) edittext.setSelection(start + 1);
                } else if (primaryCode == CodeAllLeft) {
                    edittext.setSelection(0);
                } else if (primaryCode == CodeAllRight) {
                    edittext.setSelection(edittext.length());
                } else if (primaryCode == CodePrev) {
                    @SuppressLint("WrongConstant") View focusNew = edittext.focusSearch(View.FOCUS_BACKWARD);
                    if (focusNew != null) focusNew.requestFocus();
                } else if (primaryCode == CodeNext) {
                    @SuppressLint("WrongConstant") View focusNew = edittext.focusSearch(View.FOCUS_FORWARD);
                    if (focusNew != null) focusNew.requestFocus();
                } else { // insert character
                    editable.insert(start, Character.toString((char) primaryCode));
                }
            }

            @Override
            public void onPress(int arg0) {
            }

            @Override
            public void onRelease(int primaryCode) {
            }

            @Override
            public void onText(CharSequence text) {
            }

            @Override
            public void swipeDown() {
            }

            @Override
            public void swipeLeft() {
            }

            @Override
            public void swipeRight() {
            }

            @Override
            public void swipeUp() {
            }
        };
    }


    public CustomKeyboard(Activity host, int viewid, int layoutid) {
        mHostActivity= host;
        mKeyboardView= (KeyboardView)mHostActivity.findViewById(viewid);
        mKeyboardView.setKeyboard(new Keyboard(mHostActivity, layoutid));
        mKeyboardView.setPreviewEnabled(false); // NOTE Do not show the preview balloons
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        // Hide the standard keyboard initially
        mHostActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    /** Returns whether the CustomKeyboard is visible. */
    public boolean isCustomKeyboardVisible() {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }

    /** Make the CustomKeyboard visible, and hide the system keyboard for view v. */
    public void showCustomKeyboard( View v ) {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        if( v!=null )
            ((InputMethodManager)mHostActivity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /** Make the CustomKeyboard invisible. */
    public void hideCustomKeyboard() {
        mKeyboardView.setVisibility(View.GONE);
        mKeyboardView.setEnabled(false);
    }

    /**
     * Register <var>EditText<var> with resource id <var>resid</var> (on the hosting activity) for using this custom keyboard.
     *
     * @param resid The resource id of the EditText that registers to the custom keyboard.
     */
    public void registerEditText(int resid) {
        // Find the EditText 'resid'
        mEditText = (EditText)mHostActivity.findViewById(resid);
        // Make the custom keyboard appear
        preOnFocusChangeListener = mEditText.getOnFocusChangeListener();
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            // NOTE By setting the on focus listener, we can show the custom keyboard when the edit box gets focus, but also hide it when the edit box loses focus
            @Override public void onFocusChange(View v, boolean hasFocus) {
                if( hasFocus ) showCustomKeyboard(v); else hideCustomKeyboard();
            }
        });
        preKeyListener = mEditText.getKeyListener();
        preInputType = mEditText.getInputType();


        mTW = new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after){}

            //Right after the text is changed
            @Override
            public void afterTextChanged(Editable s)
            {
                String text = s.toString();

                int length = s.length();

                if(!text.matches("[a-fA-F0-9]+") && length > 0)
                {
                    //Delete the last character
                    s.delete(length - 1, length);
                }
                ((InputMethodManager)mHostActivity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            }
        };
        mEditText.addTextChangedListener(mTW);


        mEditText.setOnClickListener(new OnClickListener() {
            // NOTE By setting the on click listener, we can show the custom keyboard again, by tapping on an edit box that already had focus (but that had the keyboard hidden).
            @Override public void onClick(View v) {
                showCustomKeyboard(v);
            }
        });
        // Disable standard keyboard hard way
        // NOTE There is also an easy way: 'edittext.setInputType(InputType.TYPE_NULL)' (but you will not have a cursor, and no 'edittext.setCursorVisible(true)' doesn't work )
        mEditText.setOnTouchListener(new OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType();       // Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);              // Restore input type
                return true; // Consume touch event
            }
        });
        // Disable spell check (hex strings look like words to Android)
        mEditText.setInputType(mEditText.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }


    public void unregisterEditText(int resid) {
        EditText edittext= (EditText)mHostActivity.findViewById(resid);
        edittext.setKeyListener(null);
        edittext.setOnFocusChangeListener(preOnFocusChangeListener);
        edittext.setInputType(preInputType);
        edittext.setOnClickListener(preOnClickListener);
        edittext.setOnTouchListener(preOnTouchListener);
        edittext.removeTextChangedListener(mTW);

        //mParentViewGroup.removeView(mKeyboardView);

    }


}


// NOTE How can we change the background color of some keys (like the shift/ctrl/alt)?
// NOTE What does android:keyEdgeFlags do/mean
