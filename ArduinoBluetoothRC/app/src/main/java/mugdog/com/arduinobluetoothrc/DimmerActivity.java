package mugdog.com.arduinobluetoothrc;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;

public class DimmerActivity extends AppCompatActivity {
    CheckBox cbVertical, cbHexaByte;
    SeekBar sbDimmer;
    BluetoothActivity activityBT;

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

                Resources rc = getResources();
                SharedPreferences spf = getSharedPreferences(rc.getString(R.string.dimmer_preference_name), MODE_PRIVATE);
                spf.edit().putBoolean(rc.getString(R.string.vertical), isChecked).commit();
            }
        });

        cbHexaByte = (CheckBox) findViewById(R.id.cbHexaByte);
        cbHexaByte.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sbDimmer.setMax(100);
                } else {
                    sbDimmer.setMax(9);
                }
                Resources rc = getResources();
                SharedPreferences spf = getSharedPreferences(rc.getString(R.string.dimmer_preference_name), MODE_PRIVATE);
                spf.edit().putBoolean(rc.getString(R.string.hexa_byte), isChecked).commit();
            }
        });

        sbDimmer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    if (cbHexaByte.isChecked()) {
                        activityBT.sendBT((byte) progress);
                    } else {
                        activityBT.sendBT(Integer.toString(progress));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int v = sbDimmer.getProgress();
                if(cbHexaByte.isChecked()){
                    Toast.makeText(getBaseContext(), "0x" + Integer.toString(v), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getBaseContext(), Integer.toString(v), Toast.LENGTH_SHORT).show();
                }
            }
        });

        activityBT = BluetoothActivity.getInstance();
        activityBT.setReadBT(null);

        readSettings();

    }

    private void readSettings() {
        Resources rc = getResources();

        SharedPreferences spf = getSharedPreferences(rc.getString(R.string.dimmer_preference_name), MODE_PRIVATE);
        cbHexaByte.setChecked(spf.getBoolean(rc.getString(R.string.hexa_byte), false));
        cbVertical.setChecked(spf.getBoolean(rc.getString(R.string.vertical), true));

    }

    public void storeSettings() {
        Resources rc = getResources();
        SharedPreferences spf = getSharedPreferences(rc.getString(R.string.dimmer_preference_name), MODE_PRIVATE);
        spf.edit().putBoolean(rc.getString(R.string.hexa_byte), cbHexaByte.isChecked());
        spf.edit().putBoolean(rc.getString(R.string.vertical), cbVertical.isChecked());
        spf.edit().commit();
    }
}
