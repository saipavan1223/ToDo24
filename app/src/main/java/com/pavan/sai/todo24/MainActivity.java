package com.pavan.sai.todo24;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.pavan.sai.todo24.R;
import com.google.gson.Gson;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String RENEW_LIST_ACTION = "com.pavan.sai.todo24.renewList";
    EditText toDo;
    ListView listView;
    Button addButton;
    ToDoList items;
    ArrayList<String> strItems;
    BroadcastReceiver renewListReceiver;
    ToDoListAdapter toDoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null)
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        if (getActionBar() != null)
            getActionBar().setDisplayUseLogoEnabled(true);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayUseLogoEnabled(true);}

        toDo = (EditText) findViewById(R.id.enterTask);
        addButton = (Button) findViewById(R.id.addButton);
        listView = (ListView) findViewById(R.id.listView);

        items = new ToDoList();

        renewListView();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toDo.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this,"Cannot Add Empty Data",Toast.LENGTH_SHORT).show();
                    return;
                }
                String result = toDo.getText().toString();

                ToDoListItem newToDo = new ToDoListItem();
                newToDo.todoMessage = result;
                newToDo.created = new Date();
                newToDo.isChecked = false;
                items.listItems.add(newToDo);
                toDo.setText("");

                try {
                    FileUtil.writeItems(getFilesDir(), new Gson().toJson(items));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(MainActivity.this,"Task Added",Toast.LENGTH_SHORT).show();
                renewListView();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setTitle("Delete");
                alertDialogBuilder.setMessage("Are you sure you want to delete?");
                alertDialogBuilder.setNegativeButton("Cancel", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialogBuilder.setPositiveButton("Ok", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String messageToRem = items.listItems.get(position).todoMessage;
                        items.listItems.remove(position);
                        try {
                            FileUtil.writeItems(getFilesDir(), new Gson().toJson(items));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            FileUtil.writeItems(getFilesDir(), new Gson().toJson(items));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        renewListView();
                    }
                });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
                return true;
            }
        });
        renewListReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                renewListView();
            }
        };
        registerReceiver(renewListReceiver, IntentFilter.create(RENEW_LIST_ACTION, "*/*"));
        items = FileUtil.readItems(getFilesDir());
        renewListView();
        scheduler();
    }

    //@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(renewListReceiver);
    }

    private void renewListView() {
        strItems = new ArrayList<>();
        for (ToDoListItem item : items.listItems)
            strItems.add(item.todoMessage);

        toDoListAdapter = new ToDoListAdapter(this, items.listItems);
        listView.setAdapter(toDoListAdapter);
    }

    private void scheduler() {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmBcstRec = new Intent(this, CleanUpReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, alarmBcstRec, 0);
// Replace the settings with yours here...
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                CleanUpReceiver.CLEAN_UP_MILISEC,
                CleanUpReceiver.CLEAN_UP_TIME, pi);
    }


}