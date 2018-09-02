package mugdog.com.arduinobluetoothrc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;

import mugdog.com.arduinobluetoothrc.keyboard.CustomEditTextFormatter;
import mugdog.com.arduinobluetoothrc.keyboard.CustomKeyboard;
import mugdog.com.arduinobluetoothrc.keyboard.CustomKeyboard2;


public class TerminalActivity extends AppCompatActivity {


    CheckBox cbHex;
    EditText txtCmd;
    EditText etMonitor;
//    BluetoothActivity activityBT;

    CustomKeyboard mCustomKeyboard;
//    CustomKeyboard2 mCustomKeyboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);

        Toolbar tbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(tbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setIcon(R.mipmap.ic_terminal);
        setTitle("Terminal");

        txtCmd = (EditText)findViewById(R.id.txtCmd);
        etMonitor = (EditText) findViewById(R.id.etMonitor);
        etMonitor.setVerticalScrollBarEnabled(true);
        //  add scrollbar in Monitor EditText
        etMonitor.setMovementMethod(new ScrollingMovementMethod());


        txtCmd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE
                        || event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        ){
                    if(txtCmd.getText().length() > 0){
                        sendCommand(txtCmd.getText().toString(), cbHex.isChecked());
                    }
                    return true;
                }
                return false;
            }
        });


        mCustomKeyboard = new CustomKeyboard(this, R.id.keyboardview, R.xml.hex_keyboard );
//        mCustomKeyboard = new CustomKeyboard(this, R.id.flKeyboard, R.xml.hexkbd );

//        mCustomKeyboard = new CustomKeyboard2(this );

//        mCustomKeyboard.attachToEditText(txtCmd, R.xml.keyboard_hexadecimal);
//        CustomEditTextFormatter.attachToEditText(txtCmd, 4, "", 4);

        AdView adView = (AdView)findViewById(R.id.adViewTerminal);
        AdRequest adR = new AdRequest.Builder()
//                .addTestDevice("B4438B8CFE663D4402842E80C188748E")
                .build();
        adView.loadAd(adR);


//        activityBT = BluetoothActivity.getInstance();
//        if(activityBT != null)
        BluetoothActivity.setReadBT(etMonitor);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cbHex = (CheckBox)findViewById(R.id.cbHex);
        cbHex.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(txtCmd.getWindowToken(), 0);
                if(isChecked){
                    mCustomKeyboard.registerEditText(R.id.txtCmd);
//                    txtCmd.requestFocus();
                }
                else{
                    mCustomKeyboard.unregisterEditText(R.id.txtCmd);
                    mCustomKeyboard.hideCustomKeyboard();
                    txtCmd.setKeyListener(new EditText(getApplicationContext()).getKeyListener());
//                    txtCmd.requestFocus();
                }
                txtCmd.setText("");
                txtCmd.clearFocus();

                Resources rc = getResources();
                SharedPreferences spf = getSharedPreferences(rc.getString(R.string.terminal_preference_name), MODE_PRIVATE);
                spf.edit().putBoolean(rc.getString(R.string.hex_terminal), isChecked).apply();

            }
        });

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        readSetting();

    }

    private void readSetting(){
        Resources rc = getResources();
        SharedPreferences spf = getSharedPreferences(rc.getString(R.string.terminal_preference_name), MODE_PRIVATE);
        cbHex.setChecked(spf.getBoolean(rc.getString(R.string.hex_terminal), false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.menu_nosetting, menu);
        MenuItem itm = menu.findItem(R.id.menu_about);
        itm.setTitle(BluetoothActivity.version_name);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_rating:
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mCustomKeyboard == null)
            super.onBackPressed();
        else
            if( mCustomKeyboard.isCustomKeyboardVisible() ) mCustomKeyboard.hideCustomKeyboard(); else this.finish();
    }

    public void sendCommand(String msg, boolean isHex){
        if(isHex){
            etMonitor.append("\n"+"0x> "+ msg);
        }
        else{
            etMonitor.append("\n"+"> "+ msg);
        }

        txtCmd.setText("");
        try {
            BluetoothActivity.sendBT(msg, isHex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
