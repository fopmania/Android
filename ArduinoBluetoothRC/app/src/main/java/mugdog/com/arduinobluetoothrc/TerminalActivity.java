package mugdog.com.arduinobluetoothrc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;


public class TerminalActivity extends AppCompatActivity {

    EditText txtCmd, etMonitor;
    BluetoothActivity activityBT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityBT = BluetoothActivity.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);

        Toolbar tbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(tbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        txtCmd = (EditText)findViewById(R.id.txtCmd);
        etMonitor = (EditText)findViewById(R.id.etMonitor);

//        btSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(txtCmd.getText().length() > 0){
//                    String msg = txtCmd.getText().toString();
//                    etMonitor.append(msg+"\n");
//                    txtCmd.setText("");
//                    try {
//                        activityBT.sendBT(msg);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

        txtCmd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    if(txtCmd.getText().length() > 0){
                        String msg = txtCmd.getText().toString();
                        etMonitor.append(msg+"\n");
                        txtCmd.setText("");
                        try {
                            activityBT.sendBT(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                }

                return false;
            }
        });

        activityBT.setReadBT(etMonitor);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.menu, menu);
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

            case R.id.menu_settins:
//                Intent it = new Intent(TerminalActivity.this, TerminalSettingsActivity.class);
//                startActivity(it);
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}
