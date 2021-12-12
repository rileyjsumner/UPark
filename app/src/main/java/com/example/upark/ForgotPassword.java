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


public class ForgotPassword extends AppCompatActivity {
    Context context;
    DBHelper db;

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
            ArrayList<SecurityQuestion> securityQA = user.getSecurityQuestions();
            if (!(securityQA == null)){
                String question = securityQA.get(0).getQuestionText();
                Log.i("msg", question); // TODO remove
            }

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
        //TODO check and makeToast for security question answer.



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

        secQ.setVisibility(View.INVISIBLE);
        ans.setVisibility(View.INVISIBLE);
        newPass.setVisibility(View.INVISIBLE);
        confirmPass.setVisibility(View.INVISIBLE);
        submitPass.setVisibility(View.INVISIBLE);

        // TODO: initially hide forgot password fields only show username, show fields when
        // valid username entered



    }
}