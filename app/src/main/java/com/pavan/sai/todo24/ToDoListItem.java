package com.pavan.sai.todo24;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ToDoListItem {
    @SerializedName("message")
    public String todoMessage;
    @SerializedName("created")
    public Date created;
    @SerializedName("checked")
    public Boolean isChecked;

    public ToDoListItem(){}

    public ToDoListItem(String str){
        todoMessage = str;
    }
}
