package com.example.upark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity {
    // when submit button pressed:
    public void clickFunction(View view) {

        // get the values entered into the fields
        EditText fname = (EditText) findViewById(R.id.fname);
        EditText lname = (EditText) findViewById(R.id.lname);
        EditText username = (EditText) findViewById(R.id.username);
        EditText pass = (EditText) findViewById(R.id.password);
        EditText passCheck = (EditText) findViewById(R.id.confirmPass);
        EditText security = (EditText) findViewById(R.id.securityA);
        // TODO: also get selected sec question!

        // TODO: could make error look different from just a toast
        if (TextUtils.isEmpty(fname.getText().toString())) {
            makeToast("first name");
        } else if (TextUtils.isEmpty(lname.getText().toString())) {
            makeToast("last name");
        } else if (TextUtils.isEmpty(username.getText().toString())) {
            makeToast("username");
        } else if (TextUtils.isEmpty(pass.getText().toString())) {
            makeToast("password");
        } else if (TextUtils.isEmpty(passCheck.getText().toString())) {
            String toastText = "Please confirm your password.";
            Toast.makeText(CreateAccount.this, toastText, Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(security.getText().toString())) {
            String toastText = "Please answer a security question.";
            Toast.makeText(CreateAccount.this, toastText, Toast.LENGTH_LONG).show();
        } else {
            // not empty, do rest of stuff
        }

        // check if all the fields have values

        // check if username already exists

        // create new user and save data
        //      where to save security question? do first name and last name fields need to be added?

        // if everything is good to go...go to home page or profile page
    }

    public void makeToast(String s) {
        String toastText = "Please enter a " + s + ".";
        Toast.makeText(CreateAccount.this, toastText, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

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