package com.fopman.mac.hrandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static  String DataName = "HRAndroidData";
    private static  String keyCount = "EMPLOYEE_COUNT";
    private static  String keyEmployee = "EMPLOYEE";


    public static ArrayList<Employee> listEmployee  = new ArrayList<Employee>();

    ArrayAdapter<Employee>    aaEmployee;

    ListView lvEmployee;
    int selItem = -1;

    public TextView TextViewById(int id){
        return (TextView)findViewById(id);
    }
    public EditText EditTextById(int id){
        return (EditText)findViewById(id);
    }

    //  SharedPreference  데이터 저장

    void showAlert(){/*
        AlertDialog.Builder alert=new AlertDialog.Builder(ViewTask.this);
        alert.setTitle("What you want to do ?");
        alert.setIcon(R.drawable.ic_launcher);

        alert.setPositiveButton("Fix",new OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                String str=lv_view_task.getItemAtPosition(arg2).toString();
                Toast.makeText(getApplicationContext(), str,Toast.LENGTH_LONG).show();


            }
        });
        alert.setNegativeButton("Ignore",new OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        alert.setNeutralButton("cancel",new OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });
        AlertDialog ale=alert.create();
        ale.show();*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        readData();

        lvEmployee = (ListView)findViewById(R.id.lvList);

        aaEmployee = new CustomAdapter(this, R.layout.list_item, listEmployee) ;
        lvEmployee.setAdapter(aaEmployee);
        lvEmployee.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selItem = i;
                view.setSelected(true);
                setItemSelect(i);
            }
        });

        RadioGroup rb = (RadioGroup) findViewById(R.id.rgWorkType);
        rb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                TextViewById(R.id.lbl2).setVisibility(View.VISIBLE);
                EditTextById(R.id.txtVal2).setVisibility(View.VISIBLE);
                EditTextById(R.id.txtVal1).setText("");
                EditTextById(R.id.txtVal2).setText("");
                switch(checkedId){
                    case R.id.rbFullTime:
                        TextViewById(R.id.lbl1).setText("Salary :");
                        TextViewById(R.id.lbl2).setText("Bonus :");
                        break;
                    case R.id.rbPartTime:
                        TextViewById(R.id.lbl1).setText("Hours :");
                        TextViewById(R.id.lbl2).setText("Rate :");
                        break;
                    case R.id.rbIntern:
                        TextViewById(R.id.lbl1).setText("School :");
                        TextViewById(R.id.lbl2).setVisibility(View.INVISIBLE);
                        EditTextById(R.id.txtVal2).setVisibility(View.INVISIBLE);
                        break;
                }
            }

        });
    }

    void setItemSelect(int i){
        if( i < 0)  return;
        setItemSelect(listEmployee.get(i));
    }

    void setItemSelect(Employee em){
        TextViewById(R.id.txtName).setText(em.getName());
        TextViewById(R.id.txtAge).setText(Integer.toString(em.getAge()));
        if(em.getVehicle() != null){
            ((CheckBox)findViewById(R.id.cbVehicle)).setChecked(true);
            TextViewById(R.id.txtMaker).setText(em.getVehicle().getMake());
            TextViewById(R.id.txtPlate).setText(em.getVehicle().getPlate());
        }
        else{
            ((CheckBox)findViewById(R.id.cbVehicle)).setChecked(false);
        }
        onClickVehicle(findViewById(R.id.cbVehicle));

        if( em instanceof FullTime){
            ((RadioGroup)findViewById(R.id.rgWorkType)).check(R.id.rbFullTime);
            FullTime ft = (FullTime)em;
            TextViewById(R.id.txtVal1).setText(Integer.toString(ft.getSalary()));
            TextViewById(R.id.txtVal2).setText(Integer.toString(ft.getBonus()));
        }else if( em instanceof PartTime){
            ((RadioGroup)findViewById(R.id.rgWorkType)).check(R.id.rbPartTime);
            PartTime pt = (PartTime)em;
            TextViewById(R.id.txtVal1).setText(Integer.toString(pt.getHours()));
            TextViewById(R.id.txtVal2).setText(Integer.toString(pt.getRate()));
        }else if( em instanceof Intern){
            ((RadioGroup)findViewById(R.id.rgWorkType)).check(R.id.rbIntern);
            Intern it = (Intern) em;
            TextViewById(R.id.txtVal1).setText(it.getSchoolName());
        }


 //               ((RadioButton)findViewById(R.id.rbFullTime)) .setSelected(true);

    }



    private ArrayList<String> buildStringFromEmployeeList(){
        ArrayList<String> strlist = new ArrayList<>();
        for (Employee em : listEmployee){
            String str = String.format("%s %d", em.getName(), em.getAge() );
            if( em.getVehicle() != null) {
                str = str + String.format(" %s %s", em.getVehicle().getMake(), em.getVehicle().getPlate());
            }
            else{
                str = str + String.format(" %s", "No Vehicle");
            }

            if( em instanceof FullTime){
                FullTime ft = (FullTime)em;
                str = String.format("F %s %d %d", str, ft.getSalary(), ft.getBonus());
            }else if(em instanceof PartTime){
                PartTime pt = (PartTime)em;
                str = String.format("P %s %d %d", str, pt.getHours(), pt.getRate());
            }else if(em instanceof Intern){
                Intern it = (Intern)em;
                str = String.format("I %s %s", str, it.getSchoolName());
            }
            strlist.add(str);
        }
        return  strlist;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public Employee buildEmployee()
    {
        Employee em = null;
        String name = EditTextById(R.id.txtName).getText().toString();
        if( name.trim().length() == 0 ){
            EditTextById(R.id.txtName).requestFocus();
            return null;
        }

        int age;
        try {
            age = Integer.parseInt(EditTextById(R.id.txtAge).getText().toString());
        }catch(NumberFormatException e){
            EditTextById(R.id.txtAge).requestFocus();
            return null;
        }

        Vehicle v = null;
        if(((CheckBox)findViewById(R.id.cbVehicle)).isChecked()){
            String v1 = EditTextById(R.id.txtMaker).getText().toString();
            String v2 = EditTextById(R.id.txtPlate).getText().toString();
            if(v1.trim().length() == 0){
                EditTextById(R.id.txtMaker).requestFocus();
            }else if(v2.trim().length() == 0) {
                EditTextById(R.id.txtPlate).requestFocus();
            }else{
                v = new Vehicle(v1, v2);
            }
        }
        try {
            RadioGroup rg = (RadioGroup) findViewById(R.id.rgWorkType);
            switch (rg.getCheckedRadioButtonId()) {
                case R.id.rbFullTime:
                    int salary = Integer.parseInt(EditTextById(R.id.txtVal1).getText().toString());
                    int bonus = Integer.parseInt(EditTextById(R.id.txtVal2).getText().toString());
                    em = new FullTime(name, age, v, salary, bonus);
                    break;
                case R.id.rbPartTime:
                    int hours = Integer.parseInt(EditTextById(R.id.txtVal1).getText().toString());
                    int rate = Integer.parseInt(EditTextById(R.id.txtVal2).getText().toString());
                    em = new PartTime(name, age, v, hours, rate);
                    break;
                case R.id.rbIntern:
                    em = new Intern(name, age, v, EditTextById(R.id.txtVal1).getText().toString());
                    break;
            }
        }catch(NumberFormatException e){
            EditTextById(R.id.txtVal1).requestFocus();
            return null;
        }

        return em;
    }

    public void onClickAdd(View view) {
        Employee em = buildEmployee();
        if(em != null) {
            selItem = -1;
            listEmployee.add(em);
//            buildListView();
            aaEmployee.notifyDataSetChanged();
            clearEditText();
            writeData();
        }
    }

    public void onClickUpdate(View view) {

        if(selItem == -1)   { return; }
        Employee em = buildEmployee();
        if(em != null) {
            listEmployee.set(selItem, em);
//            buildListView();
//            save()
            aaEmployee.notifyDataSetChanged();
            writeData();
        }
    }

    public void onClickSearch(View view) {
        String name = TextViewById(R.id.txtName).getText().toString();
        if (name.length() == 0) {  return;  }

        for( int i = 0; i < listEmployee.size(); i++){
            if(listEmployee.get(i).getName().equals(name)){
                setItemSelect(i);
                selItem = i;
                break;
            }
        }

    }

    public void onClickCalc(View view) {
        Intent in = new Intent(MainActivity.this, CalcActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    public void onClickVehicle(View view) {
        if(((CheckBox)view).isChecked()){
            TextViewById(R.id.txtPlate).setEnabled(true);
            TextViewById(R.id.txtMaker).setEnabled(true);
        }
        else
        {
            TextViewById(R.id.txtPlate).setText("");
            TextViewById(R.id.txtMaker).setText("");
            TextViewById(R.id.txtPlate).setEnabled(false);
            TextViewById(R.id.txtMaker).setEnabled(false);
        }
    }

    public void clearEditText(){
        TextViewById(R.id.txtName).setText("");
        TextViewById(R.id.txtAge).setText("");
        TextViewById(R.id.txtMaker).setText("");
        TextViewById(R.id.txtPlate).setText("");
        TextViewById(R.id.txtVal1).setText("");
        TextViewById(R.id.txtVal2).setText("");
    }

    public void writeData(){
        SharedPreferences sharedPreference = getSharedPreferences(DataName, 0);
        SharedPreferences.Editor editor = sharedPreference.edit();


        ArrayList<String> strlist = buildStringFromEmployeeList();
        editor.putInt(keyCount, strlist.size());


        for(int i = 0; i < strlist.size(); i++){
            editor.putString(keyEmployee+i, strlist.get(i));
        }

        editor.commit();
    }


    public void readData() {
//        // get Context of other application
//        Context otherAppContext = null;
//        try{
//            otherAppContext = createPackageContext("com.example.terry.sharedpreferencewriter",0);
//        }catch(PackageManager.NameNotFoundException e){
//            // log
//            e.printStackTrace();
//            throw e;
//        }


        // getting Shared preference from other application
        SharedPreferences pref = getSharedPreferences(DataName, 0);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.clear();
//        editor.commit();

        int cnt = pref.getInt(keyCount, 0);

        listEmployee.clear();

        if(cnt == 0){
            listEmployee.add(new FullTime("Jun", 25, new Vehicle("BENZ", "2567472"), 10000, 2000));
            listEmployee.add(new PartTime("Jiwon", 32, new Vehicle("KIA", "267083"), 5, 100));
            listEmployee.add(new Intern("Ansony", 39, null, "Lamdton"));
            listEmployee.add(new PartTime("Sague", 29, new Vehicle("NISSAN", "259562"), 8, 250));
            listEmployee.add(new FullTime("Mario", 44, null, 11000, 5500));
            return;
        }

        for(int i = 0; i< cnt; i++){
            Employee em = null;
            Vehicle v = null;
            String str = pref.getString(keyEmployee+i, "");
            String[] arrStr = str.split(" ");
            if(!arrStr[3].equals("No"))  //  have Vehicle
            {
                v = new Vehicle(arrStr[3], arrStr[4]);
            }

            if(arrStr[0].equals("F")){
                em = new FullTime(arrStr[1], Integer.parseInt(arrStr[2]), v
                        , Integer.parseInt(arrStr[5]), Integer.parseInt(arrStr[6]));
            }else if(arrStr[0].equals("P")){
                em = new PartTime(arrStr[1], Integer.parseInt(arrStr[2]), v
                        , Integer.parseInt(arrStr[5]), Integer.parseInt(arrStr[6]));
            }else if(arrStr[0].equals("I")){
                em = new Intern(arrStr[1], Integer.parseInt(arrStr[2]), v, arrStr[5]);
            }
            listEmployee.add(em);
        }

    }

}
