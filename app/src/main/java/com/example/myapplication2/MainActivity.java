package com.example.myapplication2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import java.util.List;
import java.util.Optional;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText login_Text, pass_Text;
    Button btn_Save, btn_Load, btn_Delete;
    Toast toast;

    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_Text = (EditText) findViewById(R.id.emailid);
        pass_Text = (EditText) findViewById(R.id.editTextTextPassword2);

        btn_Save = (Button) findViewById(R.id.save_button);
        btn_Save.setOnClickListener(this);
        btn_Load = (Button) findViewById(R.id.load_button);
        btn_Load.setOnClickListener(this);
        btn_Delete = (Button) findViewById(R.id.delete_button);
        btn_Delete.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_button:

                if (db.addUser(new User(login_Text.getText().toString(), pass_Text.getText().toString()))){
                    toast = Toast.makeText(getApplicationContext(),
                            "Новый пользователь успешно сохранен", Toast.LENGTH_SHORT);
                } else {
                    toast = Toast.makeText(getApplicationContext(),
                            "Пользователь с данным логином уже зарегестрирован", Toast.LENGTH_SHORT);
                }
                  toast.show();
                break;
            case R.id.delete_button:
                db.deleteUser(login_Text.getText().toString());
                toast = Toast.makeText(getApplicationContext(),
                        "Пользователь удален", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case R.id.load_button:
                List<User> allUsers = db.getAllUsers();
                Optional<User> any = allUsers.stream().filter(i ->
                        i.getLogin().equals(login_Text.getText().toString()) && i.getPassword().equals(pass_Text.getText().toString())
                ).findAny();
                if (any.isPresent()) {
                    Intent i = new Intent(this, MyArrayList.class);
                    User user = any.get();
                    i.putExtra("login", user.getLogin());
                    i.putExtra("password", user.getPassword());
                    startActivity(i);
                } else {
                    toast = Toast.makeText(getApplicationContext(),
                            "Неверный логин/пароль", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            default:
                break;
        }
    }


}