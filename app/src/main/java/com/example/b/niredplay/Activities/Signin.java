package com.example.b.niredplay.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.b.niredplay.ActivityManager.BaseActivity;
import com.example.b.niredplay.R;
import com.example.b.niredplay.other.User;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class Signin extends BaseActivity {
    EditText account;
    EditText password;
    EditText email;
    ImageButton signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        account = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        signin = findViewById(R.id.singnin);
        email = findViewById(R.id.email);
        Bmob.initialize(this, "e53cd95a95fc6de0fcbcff9a705ca0dc");
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        final User user = new User();
        user.setUsername(account.getText().toString());
        user.setPassword(password.getText().toString());
        user.setEmail(email.getText().toString());
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Toast.makeText(Signin.this, "注册成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Signin.this,Login.class);
                } else {
                    Toast.makeText(Signin.this, "注册失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
