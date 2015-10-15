package com.pavan.sai.todo24;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class ToDoList {
    @SerializedName("list")
    public ArrayList<ToDoListItem> listItems;

    public ToDoList() {
        listItems = new ArrayList<>();
    }

    public void CleanUpList() {
        Date currentDateTime = new Date();
        ToDoList cleanToDo = new ToDoList();
        for (ToDoListItem item : listItems) {
            if (!checkDate(currentDateTime, item.created)) {
                cleanToDo.listItems.add(item);
            }
        }
        listItems = new ArrayList<>(cleanToDo.listItems);
    }

    private boolean checkDate(Date dt2, Date dt1) {
        long diff = dt2.getTime() - dt1.getTime();
        long diffMin = diff / (60 * 1000);

        if (diffMin >= 24 * 60) {
            return true;
        }

        return false;
    }
}
