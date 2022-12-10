package com.example.myapplication2;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MyHandler extends Handler {
    DatabaseHandler db;
    Activity activity;
    Toast toast;
    EditText login_Text;
    EditText pass_Text;
    EditText edittext1;
    ArrayList<String> myStringArray;
    ListView textList;
    ArrayAdapter<String> TextAdapter;

    public MyHandler(DatabaseHandler db, Activity activity) {
        this.db = db;
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        switch (msg.arg1) {
            case 1:
                new Thread(() -> {
                    login_Text = (EditText) activity.findViewById(R.id.emailid);
                    pass_Text = (EditText) activity.findViewById(R.id.editTextTextPassword2);
                    if (db.addUser(new User(login_Text.getText().toString(), pass_Text.getText().toString()))) {
                        activity.runOnUiThread(() -> Toast.makeText(activity,
                                "Новый пользователь успешно сохранен", Toast.LENGTH_SHORT).show());
                    } else {
                        activity.runOnUiThread(() -> Toast.makeText(activity,
                                "Пользователь с данным логином уже зарегестрирован", Toast.LENGTH_SHORT).show());

                    }
                }).start();
                break;
            case 2:
                new Thread(() -> {
                    login_Text = (EditText) activity.findViewById(R.id.emailid);
                    db.deleteUser(login_Text.getText().toString());
                }).start();
                toast = Toast.makeText(activity,
                        "Пользователь удален", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case 3:
                new Thread(() -> {
                    login_Text = (EditText) activity.findViewById(R.id.emailid);
                    pass_Text = (EditText) activity.findViewById(R.id.editTextTextPassword2);
                    List<User> allUsers = new ArrayList<>(db.getAllUsers());
                    Optional<User> any = allUsers.stream().filter(i ->
                            i.getLogin().equals(login_Text.getText().toString()) && i.getPassword().equals(pass_Text.getText().toString())
                    ).findAny();
                    if (any.isPresent()) {
                        Intent i = new Intent(activity, MyArrayList.class);
                        User user = any.get();
                        i.putExtra("login", user.getLogin());
                        i.putExtra("password", user.getPassword());
                        activity.startActivity(i);
                    } else {
                        activity.runOnUiThread((Runnable) () -> Toast.makeText(activity,
                                "Неверный логин/пароль", Toast.LENGTH_SHORT).show());
                    }
                }).start();
                break;
            case 4:
                textList = activity.findViewById(R.id.textList);
                myStringArray = new ArrayList<>();
                TextAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, myStringArray);
                edittext1 = activity.findViewById(R.id.editTextTextPersonName);
                String password = edittext1.getText().toString();
                db.updateUser(activity.getIntent().getStringExtra("login"), password);
                Toast toast = Toast.makeText(activity,
                        "Новый пароль успешно сохранен", Toast.LENGTH_SHORT);
                toast.show();
                edittext1.setText("");
                updateTextAdapter();
                break;
            case 5:
                textList = activity.findViewById(R.id.textList);
                myStringArray = new ArrayList<>();
                TextAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, myStringArray);
                new Thread(this::updateTextAdapter).start();
                break;
            default:
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void updateTextAdapter() {
        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(db.getAllUsers());

        myStringArray.removeIf(i -> true);
        for (User user :
                allUsers) {
            myStringArray.add("ID:" + user.getId() + " login: " + user.getLogin()
                    + " password: " + user.getPassword());
        }
        textList.post(() -> {
            textList.setAdapter(TextAdapter);
            TextAdapter.notifyDataSetChanged();
        });
    }
}
