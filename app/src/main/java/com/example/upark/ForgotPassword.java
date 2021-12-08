package com.example.upark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ForgotPassword extends AppCompatActivity {

    public void submitUser(View view) {
        // todo: find user in db and find their security q and answer
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

        // TODO: initially hide forgot password fields only show username, show fields when
        // valid username entered



    }
}