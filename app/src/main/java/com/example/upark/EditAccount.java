package com.example.upark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.upark.DAO.User;
import com.example.upark.Database.DBHelper;

import java.util.ArrayList;

public class EditAccount extends AppCompatActivity {

    String current_user;
    Context context;
    DBHelper db;

    public void email_change(View v) {
        TextView first_email = (TextView) findViewById(R.id.first_email_area);
        TextView second_email = (TextView) findViewById(R.id.confirm_email_area);
        String email_one = first_email.getText().toString();
        String email_two = second_email.getText().toString();
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email_one).matches() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email_two).matches()) {
            Toast toast = Toast.makeText(context, "Enter a valid email!", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        if(!email_one.equals(email_two)) {
            Toast toast = Toast.makeText(context, "Emails must match!", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        User user = db.getUserByUsername(current_user);
        ArrayList<User> user_list = db.readAllUsers();
        long user_id = user.getUserID();
        boolean found = false;
        for(User u: user_list) {
            long temp_id = u.getUserID();
            if(temp_id == user_id) {
                found = db.updateEmail(u, email_one);
            }
        }

        if(!found) {
            Toast toast = Toast.makeText(context, "Error Updating email!", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        Intent intent = new Intent(EditAccount.this, Account.class);
        intent.putExtra("current_user", current_user);
        startActivity(intent);
    }

    public void password_change(View v) {
        Intent intent = new Intent(EditAccount.this, ForgotPassword.class);
        intent.putExtra("current_user", current_user);
        startActivity(intent);
    }

    public void go_back_account(View v) {
        Intent intent = new Intent(EditAccount.this, Account.class);
        intent.putExtra("current_user", current_user);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        Intent intent = getIntent();
        current_user = intent.getStringExtra("current_user");
        context = getApplicationContext();
        db = new DBHelper(context.openOrCreateDatabase("upark", Context.MODE_PRIVATE,null));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.home_screen) {
            Intent intent = new Intent(EditAccount.this, HomeScreen.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.my_account) {
            Intent intent = new Intent(EditAccount.this, Account.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.find_parks) {
            Intent intent = new Intent(EditAccount.this, FindPark.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.favorites) {
            Intent intent = new Intent(EditAccount.this, Favorites.class);
            intent.putExtra("current_user", current_user);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.log_out) {
            Intent intent = new Intent(EditAccount.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }
}