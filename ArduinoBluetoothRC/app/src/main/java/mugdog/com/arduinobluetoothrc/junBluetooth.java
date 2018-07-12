package mugdog.com.arduinobluetoothrc;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

public class junBluetooth {
    public final static int MESSAGE_READ       = 2;
    public final static int CONNECTING_STATUS  = 3;

    public class staticHandler extends Handler {
        private EditText etMsg = null;

//        private final WeakReference<BluetoothActivity> btActivity;
//
//        staticHandler(BluetoothActivity btActivity) {
//            this.btActivity = new WeakReference<BluetoothActivity>(btActivity);
//        }

        public void setEditText(EditText et){
            etMsg = et;
        }

        @Override
        public void handleMessage(Message msg) {
//            BluetoothActivity act = btActivity.get();
//            if(act == null) return;

            switch(msg.what){
                case MESSAGE_READ:
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[])msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if(etMsg != null && readMessage != null){
                        etMsg.append("< "+readMessage + "\n");
                    }
                    break;
                case CONNECTING_STATUS:
                    if(msg.arg1 == 1){
                        //  connect
                    }else{
                        //  disconnect
                    }
                    break;
            }
        }
    };

    public class ConnectThread extends Thread{
        private final BluetoothSocket   btSocket;
        private final InputStream       inStream;
        private final OutputStream      outStream;
        private final staticHandler     sHandler;
        private boolean loop = true;


        public ConnectThread(BluetoothSocket btS, staticHandler sH) {
            this.btSocket = btS;
            this.sHandler = sH;
            InputStream tIn = null;
            OutputStream tOut = null;
            try {
                tIn = btS.getInputStream();
                tOut = btS.getOutputStream();
            } catch (IOException e) { }

            inStream = tIn;
            outStream = tOut;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes = 0;
            while(loop){
                try {
                    bytes = this.inStream.available();
                    if(bytes != 0){
                        SystemClock.sleep(100);
                        bytes = inStream.read(buffer, 0, bytes);
                        sHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(String Msg) throws IOException {
            outStream.write(Msg.getBytes());
        }
        public void write(byte Msg) throws IOException {
            outStream.write(Msg);
        }

        public void closeSocket() throws IOException {
            loop = false;
            btSocket.close();
        }
    }
}
