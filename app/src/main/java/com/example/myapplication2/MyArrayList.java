package com.example.myapplication2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication2.R;

import java.util.ArrayList;
import java.util.List;

public class MyArrayList extends AppCompatActivity implements View.OnClickListener {

    EditText edittext1;
    TextView textView1;
    Button Button, exite;
    ListView textList;
    ArrayList<String> myStringArray;
    ArrayAdapter<String> TextAdapter;
    DatabaseHandler db = new DatabaseHandler(this);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.array_list_activity);

        Button = findViewById(R.id.button1);
        edittext1 = findViewById(R.id.editTextTextPersonName);
        textView1 = (TextView) findViewById(R.id.login1);
        textList = findViewById(R.id.textList);
        myStringArray = new ArrayList<>();
        TextAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myStringArray);
        updateTextAdapter();
        exite = findViewById(R.id.button);
        exite.setOnClickListener(this);

        String email = getIntent().getStringExtra("login");
        textView1.setText(textView1.getText().toString() + " " + email);



        Button.post(()->{
            Button.setOnClickListener(v -> {
                String password = edittext1.getText().toString();
                db.updateUser(email, password);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Новый пароль успешно сохранен", Toast.LENGTH_SHORT);
                toast.show();
                edittext1.setText("");
                updateTextAdapter();
            });
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateTextAdapter() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<User> allUsers = new ArrayList<>();
                allUsers.addAll(db.getAllUsers());

                myStringArray.removeIf(i->true);
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


        }).start();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        updateTextAdapter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                break;
            default:
                break;
        }
    }

}