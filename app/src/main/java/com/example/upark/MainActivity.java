package com.example.upark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.upark.Database.DBHelper;

public class MainActivity extends AppCompatActivity {

    EditText usernameText;
    EditText passwordText;
    DBHelper db;
    Context context;

    public void login(View view) {
        String user;
        String pword;

        usernameText = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);

        user = usernameText.getText().toString();
        pword = passwordText.getText().toString();

        boolean login_success = false;

        login_success = db.login(user,pword);

        if(login_success) {
            Intent intent = new Intent(MainActivity.this, home_screen.class);
            startActivity(intent);
        }
        else {
            String toastText = "Incorrect Username or Password. Please try again.";
            Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_LONG).show();
        }
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
        context = getApplicationContext();
        db = new DBHelper(context.openOrCreateDatabase("upark", Context.MODE_PRIVATE,null));
    }
}