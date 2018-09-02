package mugdog.com.arduinobluetoothrc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;

public class DimmerActivity extends AppCompatActivity {
    private AdView mAdView;
    private Toast  mToast;

    ConstraintLayout lyBack;
    CheckBox cbVertical, cbHexaByte;
    SeekBar sbDimmer;
//    BluetoothActivity activityBT;
    int colorBackground = Color.TRANSPARENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dimmer);

        Toolbar tbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(tbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setIcon(R.mipmap.ic_dimmer);
        setTitle("Dimmer");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sbDimmer = (SeekBar) findViewById(R.id.sbDimmer);

        cbVertical = (CheckBox) findViewById(R.id.cbVertical);
        cbVertical.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sbDimmer.setRotation(-90);
                } else {
                    sbDimmer.setRotation(0);
                }
                storeSettings();
            }
        });

        mToast = Toast.makeText(getBaseContext(), "", Toast.LENGTH_SHORT);


        cbHexaByte = (CheckBox) findViewById(R.id.cbHexaByte);
        cbHexaByte.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sbDimmer.setMax(100);
                    sbDimmer.setProgress(50);
                } else {
                    sbDimmer.setMax(9);
                    sbDimmer.setProgress(5);
                }
                int cl = ColorUtil.lighten(colorBackground, (double)sbDimmer.getProgress()/sbDimmer.getMax());
                lyBack.setBackgroundColor(cl);
                storeSettings();
            }
        });

        sbDimmer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    if (cbHexaByte.isChecked()) {
                        BluetoothActivity.sendBT((byte) progress);
                    } else {
                        BluetoothActivity.sendBT(Integer.toString(progress));
                    }
                    int cl = ColorUtil.lighten(colorBackground, (double)sbDimmer.getProgress()/sbDimmer.getMax());
                    lyBack.setBackgroundColor(cl);
                    mToast.cancel();
                    mToast = Toast.makeText(getBaseContext(), Integer.toString(progress), Toast.LENGTH_SHORT);
                    mToast.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                storeSettings();
            }
        });

        BluetoothActivity.setReadBT(null);

        lyBack = (ConstraintLayout)findViewById(R.id.lyDimmer);
        if( lyBack.getBackground() instanceof ColorDrawable)
        {
            colorBackground = ((ColorDrawable)lyBack.getBackground()).getColor();
        }
        int cl = ColorUtil.lighten(colorBackground, (double)sbDimmer.getProgress()/sbDimmer.getMax());
        lyBack.setBackgroundColor(cl);



        readSettings();

        mAdView = findViewById(R.id.adViewDimmer);
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("B4438B8CFE663D4402842E80C188748E")
                .build();
        mAdView.loadAd(adRequest);

    }

    private void readSettings() {
        Resources rc = getResources();

        SharedPreferences spf = getSharedPreferences(rc.getString(R.string.dimmer_preference_name), MODE_PRIVATE);
        cbHexaByte.setChecked(spf.getBoolean(rc.getString(R.string.hexa_byte), false));
        cbVertical.setChecked(spf.getBoolean(rc.getString(R.string.vertical), true));
        sbDimmer.setProgress(spf.getInt(rc.getString(R.string.progress), 5));

    }

    private void storeSettings() {
        Resources rc = getResources();
        SharedPreferences spf = getSharedPreferences(rc.getString(R.string.dimmer_preference_name), MODE_PRIVATE);
        spf.edit().putBoolean(rc.getString(R.string.hexa_byte), cbHexaByte.isChecked()).apply();
        spf.edit().putBoolean(rc.getString(R.string.vertical), cbVertical.isChecked()).apply();
        spf.edit().putInt(rc.getString(R.string.progress), sbDimmer.getProgress()).apply();
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
