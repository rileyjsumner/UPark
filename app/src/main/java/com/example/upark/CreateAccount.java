package com.example.upark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.upark.DAO.User;
import com.example.upark.Database.DBHelper;

public class CreateAccount extends AppCompatActivity {
    // when submit button pressed:

    Context context;
    DBHelper db;

    // TODO: Add ability to upload pfp

    /*
        Performed when submit button is pressed
     */
    public void clickFunction(View view) {

        // get the values entered into the fields
        EditText fname = (EditText) findViewById(R.id.fname);
        EditText lname = (EditText) findViewById(R.id.lname);
        EditText email = (EditText) findViewById(R.id.email);
        EditText username = (EditText) findViewById(R.id.username);
        EditText pass = (EditText) findViewById(R.id.password);
        EditText passCheck = (EditText) findViewById(R.id.confirmPass);
        EditText securityA = (EditText) findViewById(R.id.securityA);
        Spinner securityQ = (Spinner)findViewById(R.id.spinner);

        // TODO: could make error look different from just a toast
        // TODO: check that input is valid (email is in email form)
        if (TextUtils.isEmpty(fname.getText().toString())) {
            makeToast("first name");
        } else if (TextUtils.isEmpty(lname.getText().toString())) {
            makeToast("last name");
        } else if (TextUtils.isEmpty(email.getText().toString())) {
            makeToast("email");
        } else if (TextUtils.isEmpty(username.getText().toString())) {
            makeToast("username");
        } else if (TextUtils.isEmpty(pass.getText().toString())) {
            makeToast("password");
        } else if (TextUtils.isEmpty(passCheck.getText().toString())) {
            String toastText = "Please confirm your password.";
            Toast.makeText(CreateAccount.this, toastText, Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(securityA.getText().toString())) {
            String toastText = "Please answer a security question.";
            Toast.makeText(CreateAccount.this, toastText, Toast.LENGTH_LONG).show();
        } else {
            // not empty, do rest of stuff
            // check if email is in valid form

            // TODO: uncomment following code (causes program to freeze)
            // check if username is already in use
            /*
            if(db.userExists(username.getText().toString())) {
                // trigger error
                String toastText = "This username is already in use.";
                Toast.makeText(CreateAccount.this, toastText, Toast.LENGTH_LONG).show();
            }
            */

            // check that password is >= 8 characters & that the passwords match
            if (!(pass.getText().toString().length() >= 8)) {
                String toastText = "Your password should be at least 8 characters long.";
                Toast.makeText(CreateAccount.this, toastText, Toast.LENGTH_LONG).show();
            } else if (!pass.getText().toString().equals(passCheck.getText().toString())){
                String toastText = "The passwords do not match. Please try again.";
                Toast.makeText(CreateAccount.this, toastText, Toast.LENGTH_LONG).show();
            } else {
                // create new user and save data
                // TODO: put this at the top and get rid of toString() calls in other areas
                String first = fname.getText().toString();
                String last = lname.getText().toString();
                String e = email.getText().toString();
                String user = username.getText().toString();
                String pw = pass.getText().toString();
                // TODO: store the following
                String secQ = securityQ.getSelectedItem().toString();
                String secA = securityA.getText().toString();

                User newUser = new User(user, pw, e, first, last);
                // TODO: unccoment (db operations cause freezing)
                /*
                if(db.addUser(newUser)) {
                    // user added, move to main application
                    // TODO
                } else { // error in adding user
                    String toastText = "Error adding user, please try again later.";
                    Toast.makeText(CreateAccount.this, toastText, Toast.LENGTH_LONG).show();
                }
                */

                // TODO move this inside the above if-statement once db issues solved
                // go to the home_screen activity
                goToHome();

            }


        }
    }

    /*
        Used to switch activity to home_screen
     */
    public void goToHome() {
        Intent intent = new Intent(this, home_screen.class);
        startActivity(intent);
    }

    /*
        Used to create toasts indicating empty field errors
     */
    public void makeToast(String s) {
        String toastText = "Please enter a(n) " + s + ".";
        Toast.makeText(CreateAccount.this, toastText, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        context = getApplicationContext();
        db = new DBHelper(context.openOrCreateDatabase("upark", Context.MODE_PRIVATE,null));

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sec_q_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }
}