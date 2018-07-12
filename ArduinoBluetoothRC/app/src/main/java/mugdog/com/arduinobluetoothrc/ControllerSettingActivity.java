package mugdog.com.arduinobluetoothrc;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ControllerSettingActivity extends AppCompatActivity {

    EditText etUp, etDown, etLeft, etRight, etX, etO, etT, etQ, etStart, etSelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller_setting);
        Toolbar tbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(tbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setIcon(R.mipmap.ic_setting);
        setTitle("Setting");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        etUp = (EditText) findViewById(R.id.etUp);
        setEditorActionListener(etUp);
        etDown = (EditText) findViewById(R.id.etDown);
        setEditorActionListener(etDown);
        etLeft = (EditText) findViewById(R.id.etLeft);
        setEditorActionListener(etLeft);
        etRight = (EditText) findViewById(R.id.etRight);
        setEditorActionListener(etRight);
        etO = (EditText) findViewById(R.id.etO);
        setEditorActionListener(etO);
        etX = (EditText) findViewById(R.id.etX);
        setEditorActionListener(etX);
        etQ = (EditText) findViewById(R.id.etQ);
        setEditorActionListener(etQ);
        etT = (EditText) findViewById(R.id.etT);
        setEditorActionListener(etT);
        etSelect = (EditText) findViewById(R.id.etSelect);
        setEditorActionListener(etSelect);
        etStart = (EditText) findViewById(R.id.etStart);
        setEditorActionListener(etStart);

        readKeySettings();
    }

    private void setEditorActionListener(EditText et){
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(v.getText().toString().trim().length() == 0){
                        return true;
                    }
                    String val = v.getText().toString();
                    switch(v.getId()){
                        case R.id.etDown:
                            ControllerActivity.getInstance().keyDown = val;
                            break;
                        case R.id.etLeft:
                            ControllerActivity.getInstance().keyLeft = val;
                            break;
                        case R.id.etUp:
                            ControllerActivity.getInstance().keyUp = val;
                            break;
                        case R.id.etRight:
                            ControllerActivity.getInstance().keyRight = val;
                            break;
                        case R.id.etO:
                            ControllerActivity.getInstance().keyO = val;
                            break;
                        case R.id.etX:
                            ControllerActivity.getInstance().keyX = val;
                            break;
                        case R.id.etQ:
                            ControllerActivity.getInstance().keyQ = val;
                            break;
                        case R.id.etT:
                            ControllerActivity.getInstance().keyT = val;
                            break;
                        case R.id.etSelect:
                            ControllerActivity.getInstance().keySelect = val;
                            break;
                        case R.id.etStart:
                            ControllerActivity.getInstance().keyStart = val;
                            break;
                    }
                    ControllerActivity.getInstance().storeKeySettings();
                }
                return false;
            }
        });
    }

    private void readKeySettings(){

        ControllerActivity ca = ControllerActivity.getInstance();
        etUp.setText(ca.keyUp);
        etDown.setText(ca.keyDown);
        etLeft.setText(ca.keyLeft);
        etRight.setText(ca.keyRight);
        etX.setText(ca.keyX);
        etO.setText(ca.keyQ);
        etT.setText(ca.keyT);
        etQ.setText(ca.keyQ);
        etSelect.setText(ca.keySelect);
        etStart.setText(ca.keyStart);
    }

    @Override
    public void onBackPressed() {
//        ControllerActivity.getInstance().storeKeySettings();
        super.onBackPressed();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickEditText(View view) {
        EditText et = (EditText)view;
        et.setText("");
    }
}

