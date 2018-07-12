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

import java.io.IOException;

public class SwitchActivity extends AppCompatActivity {
    Switch swButton;
    TextView tvSwitchText;
    BluetoothActivity activityBT;

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
                }else{
                    tvSwitchText.setText("Off");
                    tvSwitchText.setTextColor(getResources().getColor(R.color.enable_thumb_gray));
                }
                try{
                    activityBT.sendBT(chk);
                } catch (IOException e) {
                        e.printStackTrace();
                }

                Toast.makeText(getBaseContext(), chk, Toast.LENGTH_SHORT).show();
            }
        });

        tvSwitchText = (TextView)findViewById(R.id.tvSwitchText);

        activityBT = BluetoothActivity.getInstance();
        activityBT.setReadBT(null);

    }
}
