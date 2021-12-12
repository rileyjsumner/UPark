package com.example.upark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.upark.DAO.SecurityQuestion;
import com.example.upark.DAO.User;
import com.example.upark.Database.DBHelper;

import java.util.ArrayList;
import java.util.Arrays;


public class ForgotPassword extends AppCompatActivity {
    Context context;
    DBHelper db;

    public void submitPass (View view) {
        EditText ans = (EditText) findViewById(R.id.answerField);
        EditText newPass = (EditText) findViewById(R.id.newPassField);
        EditText confirmPass = (EditText) findViewById(R.id.confirmNewPassField);
        Button submitPass = (Button) findViewById(R.id.submitPassButton);

        // check if fields are empty
        // array of size 7, stores EditText fields
        ArrayList<EditText> fields = new ArrayList<EditText>(Arrays.asList(
                ans,
                newPass,
                confirmPass));
        // stores empty fields
        ArrayList<EditText> errorArray = new ArrayList<EditText>();


        for (int i = 0; i < fields.size(); i++){
            if(TextUtils.isEmpty(fields.get(i).getText().toString())){
                errorArray.add(fields.get(i));
            }
        }

        if (errorArray.size() != 0){
            for (int i = 0; i < errorArray.size(); i++){
                errorArray.get(i).setError("This field cannot be empty.");
            }
        } else {
            String answer = ans.getText().toString();
            String pass = newPass.getText().toString();
            String confirm = confirmPass.getText().toString();

            // TODO: get and store stored answer

            if (false) { // TODO: check if answer matches stored answer

            } else if (!(pass.length() >= 8)) { // check that password is >= 8 characters
                newPass.setError("Your password should be at least 8 characters long.");
            } else if (!hasNum(pass)) { // check that password has at least 1 num
                newPass.setError("Your password should have at least one number.");
            } else if (!pass.equals(confirm)) { // check if pass and confirmation match
                newPass.setError("The passwords do not match. Please try again.");
                confirmPass.setError("The passwords do not match. Please try again.");
            } else { // set user pass
                // update password in user obj
                // update password in db
            }
        }

    }

    public void submitUser(View view) {
        EditText userField = (EditText) findViewById(R.id.userField);
        String username = userField.getText().toString();
        TextView secQ = (TextView) findViewById(R.id.secQ);
        EditText ans = (EditText) findViewById(R.id.answerField);
        EditText newPass = (EditText) findViewById(R.id.newPassField);
        EditText confirmPass = (EditText) findViewById(R.id.confirmNewPassField);
        Button submitPass = (Button) findViewById(R.id.submitPassButton);

        if (TextUtils.isEmpty(username)) { // empty
            userField.setError("Please enter a username.");
        } else if (db.userExists(username)){ // valid user
            // retrieve security question

            User user = db.getUserByUsername(username);

            SecurityQuestion security = db.getUsersSecurityQuestion(user.getUserID());
            String question = security.getQuestionText();
            Log.i("questiontxt", question);

            // set secQ to user's security question
            secQ.setText("Security Question Would Go Here");

            // show additional fields
            secQ.setVisibility(View.VISIBLE);
            ans.setVisibility(View.VISIBLE);
            newPass.setVisibility(View.VISIBLE);
            confirmPass.setVisibility(View.VISIBLE);
            submitPass.setVisibility(View.VISIBLE);

        } else { // user dne
            String toastText = "User not found. Please try again.";
            Toast.makeText(ForgotPassword.this, toastText, Toast.LENGTH_LONG).show();

            // hides additional fields again
            secQ.setVisibility(View.INVISIBLE);
            ans.setVisibility(View.INVISIBLE);
            newPass.setVisibility(View.INVISIBLE);
            confirmPass.setVisibility(View.INVISIBLE);
            submitPass.setVisibility(View.INVISIBLE);
        }
    }


    public void clickFunction(View view) {
        /*
        EditText newPassword = (EditText) findViewById(R.id.newPassword);
        String updatedPassword = newPassword.getText().toString();
        EditText confirmPassword = (EditText) findViewById(R.id.confirmNewPassword);
        EditText securityQuestionAnswer = (EditText) findViewById(R.id.securityQuestionAnswer);

        if (TextUtils.isEmpty(securityQuestionAnswer.getText().toString())) {
            makeToast("a security answer");
        } if (TextUtils.isEmpty(newPassword.getText().toString())) {
            makeToast("a new password");
        } else if (TextUtils.isEmpty(confirmPassword.getText().toString())) {
            makeToast("a confirmation password");
        } else if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
            makeToast("matching passwords. " + newPassword.getText().toString() + " " + confirmPassword.getText().toString());
        } else {
            submitResetPassword(updatedPassword);
        }


         */



    }

    boolean hasNum(String pass) {
        char[] passChars = pass.toCharArray();
        boolean hasNum = false;
        for (int i = 0; i < passChars.length; i++) {
            if (Character.isDigit(passChars[i])){
                hasNum = true;
            }
        }

        return hasNum;

    }

    public void submitResetPassword(String s) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void makeToast(String s) {
        String toastText = "Please enter " + s + ".";
        Toast.makeText(ForgotPassword.this, toastText, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        context = getApplicationContext();
        db = new DBHelper(context.openOrCreateDatabase("upark", Context.MODE_PRIVATE,null));

        TextView secQ = (TextView) findViewById(R.id.secQ);
        EditText ans = (EditText) findViewById(R.id.answerField);
        EditText newPass = (EditText) findViewById(R.id.newPassField);
        EditText confirmPass = (EditText) findViewById(R.id.confirmNewPassField);
        Button submitPass = (Button) findViewById(R.id.submitPassButton);

        // initially set additional fields to not visible
        secQ.setVisibility(View.INVISIBLE);
        ans.setVisibility(View.INVISIBLE);
        newPass.setVisibility(View.INVISIBLE);
        confirmPass.setVisibility(View.INVISIBLE);
        submitPass.setVisibility(View.INVISIBLE);



    }
}