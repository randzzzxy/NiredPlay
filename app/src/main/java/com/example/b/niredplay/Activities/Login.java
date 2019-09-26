package com.example.b.niredplay.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.b.niredplay.ActivityManager.BaseActivity;
import com.example.b.niredplay.R;
import com.example.b.niredplay.other.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class Login extends BaseActivity {
    private EditText number;
    private EditText password;
    private ImageButton login;
    private ImageButton signin;
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this, "e53cd95a95fc6de0fcbcff9a705ca0dc");
        number = findViewById(R.id.num);
        password = findViewById(R.id.pass);
        login = findViewById(R.id.login);
        signin = findViewById(R.id.zhuce);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               login();
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Signin.class);
                startActivity(intent);
            }
        });

    }
    private void login() {
        final User user = new User();
        //此处替换为你的用户名
        user.setUsername(number.getText().toString());
        //此处替换为你的密码
        user.setPassword(password.getText().toString());
        user.login(new SaveListener<User>() {
            @Override
            public void done(User bmobUser, BmobException e) {
                if (e == null) {
                    User user = BmobUser.getCurrentUser(User.class);
                    Toast.makeText(Login.this, "登录成功" , Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Login.this,MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user",bmobUser);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, "登录失败" , Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
