package mugdog.com.arduinobluetoothrc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;

public class SwitchActivity extends AppCompatActivity {
    ConstraintLayout lyBack;
    private AdView mAdView;
    private Toast  mToast = null;

    Switch swButton;
    TextView tvSwitchText;
    BluetoothActivity activityBT;
    int colorBackground = Color.TRANSPARENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
        Toolbar tbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(tbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setIcon(R.mipmap.ic_switch);
        setTitle("Switch");


        mToast = Toast.makeText(getBaseContext(), "", Toast.LENGTH_SHORT);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        swButton = (Switch)findViewById(R.id.swButton);

        swButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String chk = "0";
                if(isChecked){
                    chk = "1";
                    tvSwitchText.setText("On");
                    tvSwitchText.setTextColor(getResources().getColor(R.color.enable_thumb_green));
                    lyBack.setBackgroundColor(ColorUtil.lighten(colorBackground, 1));
                }else{
                    tvSwitchText.setText("Off");
                    tvSwitchText.setTextColor(getResources().getColor(R.color.enable_thumb_gray));
                    lyBack.setBackgroundColor(ColorUtil.lighten(colorBackground, 0));
                }
                try{
                    activityBT.sendBT(chk);
                } catch (IOException e) {
                        e.printStackTrace();
                }
                mToast.cancel();
                mToast = Toast.makeText(getBaseContext(), chk, Toast.LENGTH_SHORT);
                mToast.show();
//                Toast.makeText(getBaseContext(), chk, Toast.LENGTH_SHORT).show();

                storeSettings();
            }
        });

        tvSwitchText = (TextView)findViewById(R.id.tvSwitchText);

        activityBT = BluetoothActivity.getInstance();
        if(activityBT != null)
            activityBT.setReadBT(null);

        lyBack = ((ConstraintLayout)findViewById(R.id.lySwitch));
        if(lyBack.getBackground() instanceof ColorDrawable){
            colorBackground = ((ColorDrawable)lyBack.getBackground()).getColor();
        }

        mAdView = findViewById(R.id.adViewSwitch);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        readSettings();

    }

    private void readSettings() {
        Resources rc = getResources();

        SharedPreferences spf = getSharedPreferences(rc.getString(R.string.switch_preference_name), MODE_PRIVATE);
        swButton.setChecked(spf.getBoolean(rc.getString(R.string.switch_on), false));

    }

    public void storeSettings() {
        Resources rc = getResources();
        SharedPreferences spf = getSharedPreferences(rc.getString(R.string.switch_preference_name), MODE_PRIVATE);
        spf.edit().putBoolean(rc.getString(R.string.switch_on), swButton.isChecked()).commit();
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

}
