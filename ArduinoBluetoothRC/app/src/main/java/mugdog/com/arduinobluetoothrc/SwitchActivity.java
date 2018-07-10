package mugdog.com.arduinobluetoothrc;

import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SwitchActivity extends AppCompatActivity {
    Switch swButton;
    TextView tvSwitchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
        Toolbar tbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(tbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setIcon(R.drawable.bt_left);
        setTitle("SWITCH");



        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        swButton = (Switch)findViewById(R.id.swButton);
        swButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String chk = "0";
                if(isChecked){
                    chk = "1";
                    tvSwitchText.setText("On");
                }else{
                    tvSwitchText.setText("Off");
                }

                Toast.makeText(getBaseContext(), chk, Toast.LENGTH_SHORT).show();
            }
        });

        tvSwitchText = (TextView)findViewById(R.id.tvSwitchText);


    }
}
