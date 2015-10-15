package com.pavan.sai.todo24;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

import java.io.FileNotFoundException;

public class CleanUpReceiver extends BroadcastReceiver {
    public static int CLEAN_UP_MILISEC = 5000;
    public static int CLEAN_UP_TIME = 1000 * 60 * 60 * 24;

    public CleanUpReceiver() {
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        ToDoList items = FileUtil.readItems(context.getFilesDir());
        items.CleanUpList();
        try {
            FileUtil.writeItems(context.getFilesDir(), new Gson().toJson(items));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        context.sendBroadcast(new Intent(MainActivity.RENEW_LIST_ACTION));
    }
}
