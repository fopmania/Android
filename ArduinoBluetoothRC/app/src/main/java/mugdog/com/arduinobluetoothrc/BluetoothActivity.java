package mugdog.com.arduinobluetoothrc;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

    private BluetoothAdapter        BTAdapter;
    private BluetoothSocket         BTSocket = null;
    private Set<BluetoothDevice>    BTDevices;
    private ArrayAdapter<String>    BTArrayAdapter;
    private junBluetooth junBT;
    private junBluetooth.staticHandler         sHandle;
    private junBluetooth.ConnectThread         BTThread = null;
    private ProgressBar pBar = null;


    private final static UUID       BTUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private ListView lvBT;

    private final static int REQUEST_ENABLE_BT  = 104;

    private static BluetoothActivity _instance;


    public static BluetoothActivity getInstance()   {   return _instance;    }

    public void sendBT(String msg) throws IOException {
        BTThread.write(msg);
    }

    public void setReadBT(EditText et){
        sHandle.setEditText(et);
    }
    public void destroyBTThread() throws IOException {   BTThread.closeSocket(); }


    @SuppressLint("HandlerLeak")
    Handler pBarHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            pBar.setVisibility(View.GONE);
        }
    };

    private final BroadcastReceiver brReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BTArrayAdapter.add(dev.getName() + "\n" + dev.getAddress());
                BTArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    private AdapterView.OnItemClickListener onClickListenerBT = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            if(BTThread != null){
//                try {
//                    destroyBTThread();
//                    Thread.sleep(200);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

            pBar.setVisibility(View.VISIBLE);
            if(!BTAdapter.isEnabled()){
                Toast.makeText(getBaseContext(), "Bluetooth is not on.", Toast.LENGTH_SHORT).show();
                return;
            }

            String info = ((TextView) view).getText().toString();

            final String addr = info.substring(info.length() - 17);
            final String name = info.substring(0, info.length() - 17);

            Thread th = new Thread(){
                @Override
                public void run() {
                    boolean fail = false;
                    BluetoothDevice dev = BTAdapter.getRemoteDevice(addr);
                    try {
                        BTSocket = dev.createInsecureRfcommSocketToServiceRecord(BTUUID);
                    } catch (IOException e) {
                        fail = true;
                    }

                    try{
                        BTSocket.connect();
                    } catch (IOException e) {
                        fail = true;
                        try {
                            BTSocket.close();
                            sHandle.obtainMessage(junBluetooth.CONNECTING_STATUS, -1, -1 ).sendToTarget();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                         //   Toast.makeText(getBaseContext(), "Socket create failed", Toast.LENGTH_SHORT).show();
                        }
                        e.printStackTrace();
                        //Toast.makeText(getBaseContext(), "Socket create failed", Toast.LENGTH_SHORT).show();
                    }
                    if(fail == false){
                        BTThread = junBT.new ConnectThread(BTSocket, sHandle);
                        BTThread.start();
                        Intent it = new Intent(BluetoothActivity.this, ModeActivity.class);
                        startActivity(it);
                        //finish();
                        pBarHandler.sendMessage(pBarHandler.obtainMessage());
                    }
                }
            };
            th.start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        Toolbar tbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(tbar);

        //ca-app-pub-2239158288105225/7625810987
        MobileAds.initialize(this, "ca-app-pub-2239158288105225~6675266363");
//        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");

        AdView adView = (AdView)findViewById(R.id.adViewBluetooth);
        //adView.setAdSize(AdSize.BANNER);
        //adView.setAdUnitId("ca-app-pub-2239158288105225/7625810987");
        AdRequest adR = new AdRequest.Builder().build();
//        AdRequest adR = new AdRequest.Builder().addTestDevice("B4438B8CFE663D4402842E80C188748E").build();
        adView.loadAd(adR);

        pBar = (ProgressBar)findViewById(R.id.progBar);
        pBar.setVisibility(View.GONE);

        BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        BTAdapter = BluetoothAdapter.getDefaultAdapter();

        lvBT = (ListView)findViewById(R.id.lvBT);
        lvBT.setAdapter(BTArrayAdapter);
        lvBT.setOnItemClickListener(onClickListenerBT);

        junBT = new junBluetooth();

        sHandle = junBT.new staticHandler();

        if(BTAdapter == null){
            //Toast.makeText(getBaseContext(), "Can not use Bluetooth.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if(!BTAdapter.isEnabled()){
            Intent it = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(it, REQUEST_ENABLE_BT);
        }else{
            findBluetoothDevices();
            updateBTDevices();
        }

        //  portrait screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //  keep the screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        BluetoothActivity._instance = this;
    }

    public void setBTAdapter(){
        if(!BTAdapter.isEnabled()){
            Intent it = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(it, REQUEST_ENABLE_BT);
        }else{
            findBluetoothDevices();
            updateBTDevices();
        }
//        if(BTThread != null){
//            try {
//                BTThread.closeSocket();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }



    //  store information of the activity
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.print(requestCode);
        System.out.print(resultCode);
        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode == RESULT_OK){
                updateBTDevices();
            }
        }
    }

    void findBluetoothDevices(){
        if(!BTAdapter.isEnabled())      return;

        BTAdapter.startDiscovery();
        registerReceiver(brReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    void updateBTDevices(){
        if(!BTAdapter.isEnabled())      return;
        BTArrayAdapter.clear();

        BTDevices = BTAdapter.getBondedDevices();
        for(BluetoothDevice dev : BTDevices) {
            BTArrayAdapter.add(dev.getName() + "\n" + dev.getAddress());
        }
        BTArrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(brReceiver);
        try {
            if(BTThread != null)
                BTThread.closeSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_bluetooth, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuBTScan:
                findBluetoothDevices();
                updateBTDevices();
                break;
            case R.id.menuBTRating:
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
