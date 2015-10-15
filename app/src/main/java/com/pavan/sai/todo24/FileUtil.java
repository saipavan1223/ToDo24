package com.pavan.sai.todo24;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class FileUtil {
    private static void writeHelper(String data, OutputStream stream) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(stream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String readHelper(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static ToDoList readItems(File filesDir) {
        File todoFile = new File(filesDir, "todo.td2");
        ToDoList items = new ToDoList();
        FileInputStream aa = null;

        try {
            aa = new FileInputStream(todoFile);
            String str = readHelper(aa);

            ToDoList readed = new Gson().fromJson(str, ToDoList.class);
            items.listItems.addAll(readed.listItems);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (aa != null) aa.close();
            } catch (IOException e) {

            }
        }

        return items;
    }

    public static void writeItems(File filesDir, String json) throws FileNotFoundException {

        File todoFile = new File(filesDir, "todo.td2");

        FileOutputStream aa = null;

        try {
            aa = new FileOutputStream(todoFile);
            writeHelper(json, aa);
        } catch (Exception e) {

        } finally {
            try {
                if (aa != null) aa.close();
            } catch (IOException e) {/*should be like this.. stupid java))*/}
        }
    }
}
