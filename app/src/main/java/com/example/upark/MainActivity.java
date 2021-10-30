package com.example.upark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public void login(View view) {

        //TODO: This is temp until we get an actual login database working. Currently there seems
        //      to be errors when creating an account when I look in CreateAccount.java -Garrett
        Intent intent = new Intent(MainActivity.this, home_screen.class);
        startActivity(intent);
    }

    public void create_account(View view) {
        TextView createAccount = (TextView) findViewById(R.id.createAccount);

        createAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateAccount.class);
                startActivity(intent);
            }
        });
    }

    public void forgot_password(View view) {
        TextView forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}