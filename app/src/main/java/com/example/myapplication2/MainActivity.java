package com.example.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication2.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText email;
    EditText password;
    Button loginbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=findViewById(R.id.emailid);
        password=findViewById(R.id.editTextTextPassword2);
        loginbutton = findViewById(R.id.button2);
        loginbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button2:
                String e = email.getText().toString();
                String p = password.getText().toString();
                if ((e.equals("email.com"))&(p.equals("123"))){
                    Intent i = new Intent(this, MyArrayList.class);
                    i.putExtra("e", e);
                    startActivity(i);
                }else  {
                    Toast.makeText(MainActivity.this, "Пароль и почта введены с ошибкой!", Toast.LENGTH_LONG).show();
                    email.setText("");
                    password.setText("");
                }

                break;
            default:
                break;
        }

    }
}