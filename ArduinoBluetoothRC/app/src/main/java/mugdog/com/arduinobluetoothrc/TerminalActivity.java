package mugdog.com.arduinobluetoothrc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;


public class TerminalActivity extends AppCompatActivity {

    EditText txtCmd, etMonitor;
    BluetoothActivity activityBT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);

        Toolbar tbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(tbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setIcon(R.mipmap.ic_terminal);
        setTitle("Terminal");

        txtCmd = (EditText)findViewById(R.id.txtCmd);
        etMonitor = (EditText)findViewById(R.id.etMonitor);
        etMonitor.setVerticalScrollBarEnabled(true);
        //  add scrollbar in Monitor EditText
        etMonitor.setMovementMethod(new ScrollingMovementMethod());


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
                        etMonitor.append("\n"+"> "+ msg);
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

//        ShapeDrawable shape = new ShapeDrawable(new RectShape());
//        shape.getPaint().setColor(Color.DKGRAY);
//        shape.getPaint().setStyle(Paint.Style.STROKE);
//        shape.getPaint().setStrokeWidth(3);
//        etMonitor.setBackground(shape);
//
//        ShapeDrawable shape2 = new ShapeDrawable(new RectShape());
//        shape2.getPaint().setColor(Color.LTGRAY);
//        shape2.getPaint().setStyle(Paint.Style.STROKE);
//        shape2.getPaint().setStrokeWidth(3);
//        txtCmd.setBackground(shape2);


        AdView adView = (AdView)findViewById(R.id.adViewTerminal);
        AdRequest adR = new AdRequest.Builder()
//                .addTestDevice("B4438B8CFE663D4402842E80C188748E")
                .build();
        adView.loadAd(adR);


        activityBT = BluetoothActivity.getInstance();
        activityBT.setReadBT(etMonitor);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.menu_nosetting, menu);
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
