package mugdog.com.arduinobluetoothrc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class ControllerActivity extends AppCompatActivity {

    Vibrator    vib;
    private final int vib_time = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        Toolbar tbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(tbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public void onBackPressed() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu, menu);

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
                Intent it = new Intent(this, ControllerSettingActivity.class);
                startActivity(it);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickUp(View view) {
        vib.vibrate(vib_time);
    }

    public void onClickLeft(View view) {
        vib.vibrate(vib_time);
    }

    public void onClickDown(View view) {
        vib.vibrate(vib_time);
    }

    public void onClickRight(View view) {
        vib.vibrate(vib_time);
    }

    public void onClickT(View view) {
        vib.vibrate(vib_time);
    }

    public void onClickS(View view) {
        vib.vibrate(vib_time);
    }

    public void onClickO(View view) {
        vib.vibrate(vib_time);
    }

    public void onClickX(View view) {
        vib.vibrate(vib_time);
    }

    public void onClickSelect(View view) {
        vib.vibrate(vib_time);
    }

    public void onClickStart(View view) {
        vib.vibrate(vib_time);
    }
}
