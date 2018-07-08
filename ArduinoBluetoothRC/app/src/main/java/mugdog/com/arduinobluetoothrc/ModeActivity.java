package mugdog.com.arduinobluetoothrc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;

public class ModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        Toolbar tbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(tbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Button btTerminal = (Button)findViewById(R.id.btTerminal);
        btTerminal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ModeActivity.this, TerminalActivity.class);
                startActivity(it);
                //finish();
            }
        });

        Button btController = (Button)findViewById(R.id.btController);
        btController.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent it = new Intent(ModeActivity.this, ControllerActivity.class);
                startActivity(it);
                //finish();
            }
        });

        MobileAds.initialize(this, "ca-app-pub-2239158288105225~6675266363");

        AdView adView = (AdView)findViewById(R.id.adViewMode);
        //adView.setAdSize(AdSize.BANNER);
        //adView.setAdUnitId("ca-app-pub-2239158288105225/7625810987");
//        AdRequest adR = new AdRequest.Builder().addTestDevice("B4438B8CFE663D4402842E80C188748E").build();
        AdRequest adR = new AdRequest.Builder().build();
        adView.loadAd(adR);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


    }

    @Override
    public void onBackPressed() {
        try {
            BluetoothActivity.getInstance().destroyBTThread();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }

    public void onClickSwitch(View view) {
        Intent it = new Intent(ModeActivity.this, SwitchActivity.class);
        startActivity(it);
    }

    public void onClickDimmer(View view) {
        Intent it = new Intent(ModeActivity.this, DimmerActivity.class);
        startActivity(it);
    }
}
