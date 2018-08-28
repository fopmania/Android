package mugdog.com.arduinobluetoothrc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;

public class ControllerActivity extends AppCompatActivity {

    BluetoothActivity activityBT;

    private Toast mToast = null;

    static ControllerActivity _instance = null;

    Vibrator    vib;
    private final int vib_time = 50;

    public boolean isVibration = true;
    public String keyUp = "u", keyDown = "d", keyLeft = "l", keyRight = "r",
            keyX = "x", keyO = "o", keyT = "t", keyQ = "q", keyStart = "s", keySelect = "e";

    static public ControllerActivity getInstance()  {   return  _instance;  }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        ControllerActivity._instance = this;

        Toolbar tbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(tbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setIcon(R.mipmap.ic_controller);
        setTitle("Controller");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        mToast = Toast.makeText(getBaseContext(), "", Toast.LENGTH_SHORT);

        vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        makeTouchEvent(R.id.btDown);
        makeTouchEvent(R.id.btUp);
        makeTouchEvent(R.id.btLeft);
        makeTouchEvent(R.id.btRight);
        makeTouchEvent(R.id.btT);
        makeTouchEvent(R.id.btO);
        makeTouchEvent(R.id.btQ);
        makeTouchEvent(R.id.btX);
        makeTouchEvent(R.id.btSelect);
        makeTouchEvent(R.id.btStart);

        activityBT = BluetoothActivity.getInstance();
        if(activityBT != null)
            activityBT.setReadBT(null);

        readKeySettings();

        AdView adView = (AdView)findViewById(R.id.adViewController);
        //adView.setAdSize(AdSize.BANNER);
        //adView.setAdUnitId("ca-app-pub-2239158288105225/7625810987");
//        AdRequest adR = new AdRequest.Builder().addTestDevice("B4438B8CFE663D4402842E80C188748E").build();
        AdRequest adR = new AdRequest.Builder()
//                .addTestDevice("B4438B8CFE663D4402842E80C188748E")
                .build();
        adView.loadAd(adR);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void makeTouchEvent(int id) {
        ImageButton bt = (ImageButton)findViewById(id);
        bt.setOnTouchListener(new View.OnTouchListener() {
              @Override
              public boolean onTouch(View v, MotionEvent event) {
                  int id = v.getId();
                  boolean r = false;
                  String pKey = "";
                  try{
                      switch(event.getAction()){
                          case MotionEvent.ACTION_DOWN:
                              if(isVibration)
                                  vib.vibrate(vib_time);
                              if(R.id.btDown == id){
                                  pKey = keyDown;
                              }
                              else if(R.id.btUp == id){
                                  pKey = keyUp;
                              }
                              else if(R.id.btLeft == id){
                                  pKey = keyLeft;
                              }
                              else if(R.id.btRight == id){
                                  pKey = keyRight;
                              }
                              else if(R.id.btT == id){
                                  pKey = keyT;
                              }
                              else if(R.id.btO == id){
                                  pKey = keyO;
                              }
                              else if(R.id.btQ == id){
                                  pKey = keyQ;
                              }
                              else if(R.id.btX == id){
                                  pKey = keyX;
                              }
                              else if(R.id.btSelect == id){
                                  pKey = keySelect;
                              }
                              else if(R.id.btStart == id){
                                  pKey = keyStart;
                              }
                              r = true;
                              break;

                          case MotionEvent.ACTION_UP:
                              pKey = "0";
                              r = false;
                              break;

                      }

                      if(!pKey.isEmpty()){
                          activityBT.sendBT(pKey);
                          mToast.cancel();
                          mToast = Toast.makeText(getBaseContext(), pKey, Toast.LENGTH_SHORT);
                          mToast.show();
                      }

                  }catch (IOException e) {
                      e.printStackTrace();
                  }
                  return r;
              }
          }
        );
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
            case R.id.menu_settins:
                Intent it = new Intent(this, ControllerSettingActivity.class);
                startActivity(it);

                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void readKeySettings(){
        Resources rc = getResources();

        SharedPreferences spf = getSharedPreferences( rc.getString(R.string.controller_preference_name), MODE_PRIVATE);

        keyDown = spf.getString(rc.getString(R.string.key_down), "d");
        keyUp = spf.getString(rc.getString(R.string.key_up), "u");
        keyLeft = spf.getString(rc.getString(R.string.key_Left), "l");
        keyRight = spf.getString(rc.getString(R.string.key_Right), "r");
        keyX = spf.getString(rc.getString(R.string.key_X), "x");
        keyO = spf.getString(rc.getString(R.string.key_O), "o");
        keyT = spf.getString(rc.getString(R.string.key_T), "t");
        keyQ = spf.getString(rc.getString(R.string.key_Q), "q");
        keySelect = spf.getString(rc.getString(R.string.key_Select), "e");
        keyStart = spf.getString(rc.getString(R.string.key_Start), "s");
        isVibration = spf.getBoolean(rc.getString(R.string.key_Vibration), true);
    }
    public void storeKeySettings(){
        Resources rc = getResources();

        SharedPreferences spf = getSharedPreferences( rc.getString(R.string.controller_preference_name), MODE_PRIVATE);

        SharedPreferences.Editor ed = spf.edit();
        ed.putString(rc.getString(R.string.key_down), keyDown);
        ed.putString(rc.getString(R.string.key_up), keyUp);
        ed.putString(rc.getString(R.string.key_Left), keyLeft);
        ed.putString(rc.getString(R.string.key_Right), keyRight);
        ed.putString(rc.getString(R.string.key_X), keyX);
        ed.putString(rc.getString(R.string.key_O), keyO);
        ed.putString(rc.getString(R.string.key_T), keyT);
        ed.putString(rc.getString(R.string.key_Q), keyQ);
        ed.putString(rc.getString(R.string.key_Select), keySelect);
        ed.putString(rc.getString(R.string.key_Start), keyStart);
        ed.putBoolean(rc.getString(R.string.key_Vibration), isVibration);

        ed.apply();
    }
}
