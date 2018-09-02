package com.fopman.mac.hrandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;

public class CalcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);


        int earn = 0;
        int totalPR = 0;
        String result = "";

        for(Employee em : MainActivity.listEmployee){
            result += "\n------------------------------------------------------------\n";

            result += em.toString();
            earn = em.calcEarnings();
            result += "\nEarnings: " + earn;
            totalPR += earn;

            System.out.print("******************************");
            em.displayData();
            System.out.print ("******************************");
            System.out.print ("Earnings: " + earn);
        }
        result += "\n================================";
        result += "\nTotal Payroll: "+ totalPR;
        System.out.print ("Total Payroll: "+totalPR);

        ((EditText)findViewById(R.id.txtPayroll)).setText(result);

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode){
                case KeyEvent.KEYCODE_BACK: //  Back button event
                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
