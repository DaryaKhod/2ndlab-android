package com.example.myapplication2;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText login_Text, pass_Text;
    Button btn_Save, btn_Load, btn_Delete;

    MyHandler handler;

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

        handler = new MyHandler(db, this);
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
        Message msg = new Message();
        switch (v.getId()) {
            case R.id.save_button:
                msg.arg1 = 1;
                handler.handleMessage(msg);
                break;
            case R.id.delete_button:
                msg.arg1 = 2;
                handler.handleMessage(msg);
                break;
            case R.id.load_button:
                msg.arg1 = 3;
                handler.handleMessage(msg);
                break;
            default:
                break;
        }
    }


}