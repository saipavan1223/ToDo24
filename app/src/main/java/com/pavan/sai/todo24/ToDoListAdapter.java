package com.pavan.sai.todo24;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.pavan.sai.todo24.R;
import com.google.gson.Gson;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class ToDoListAdapter extends ArrayAdapter<ToDoListItem> {

    ArrayList<ToDoListItem> list;

    public ToDoListAdapter(Context context, ArrayList<ToDoListItem> items) {
        super(context, R.layout.single_row, items);
        list = new ArrayList<>(items);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_row, parent, false);

            ViewHolder viewHolder = new ViewHolder();

            viewHolder.listText = (TextView) row.findViewById(R.id.listText);
            viewHolder.dateText = (TextView) row.findViewById(R.id.dateText);
            viewHolder.checkBox = (CheckBox) row.findViewById(R.id.checkBox);
            viewHolder.linearLayout = (LinearLayout) row.findViewById(R.id.rowLayout);

            row.setTag(viewHolder);
        }
        ToDoListItem item = getItem(position);

        final ViewHolder viewHolder = (ViewHolder) row.getTag();

        viewHolder.listText.setText(item.todoMessage);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm MM-dd-yyyy", Locale.US);
        String str = sdf.format(item.created);
        viewHolder.dateText.setText(str);
        viewHolder.checkBox.setChecked(item.isChecked);
        viewHolder.checkBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChange(position, viewHolder);
            }
        });
        return row;
    }

    private void checkChange(int position, ViewHolder viewHolder) {
        try {
            boolean bool = viewHolder.checkBox.isChecked();
            if (list != null && list.size() > 0) {
                list.get(position).isChecked = bool;
                //viewHolder.checkBox.setChecked(bool);
                ToDoList toDoList = new ToDoList();
                toDoList.listItems = list;
                FileUtil.writeItems(getContext().getFilesDir(), new Gson().toJson(toDoList));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class ViewHolder {
        public LinearLayout linearLayout;
        public TextView listText;
        public TextView dateText;
        public CheckBox checkBox;
    }
}