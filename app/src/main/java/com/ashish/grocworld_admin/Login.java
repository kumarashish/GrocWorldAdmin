package com.ashish.grocworld_admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;

/**
 * Created by ashish.kumar on 04-07-2018.
 */

public class Login  extends Activity implements View.OnClickListener{
    @BindView(R.id.login_btn)
    Button login;

    @BindView(R.id.forgetPassword)
    ImageView forgetPassword;
    @BindView(R.id.emailId)
    EditText emailId;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.checkbox)
    CheckBox rememberme;
    AppController controller;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        controller = (AppController) getApplicationContext();
        ButterKnife.bind(this);
        forgetPassword.setOnClickListener(this);
        login.setOnClickListener(this);
        rememberme.setChecked(true);
        if (controller.getRememberId().length() > 0) {
            emailId.setText(controller.getRememberId());
            password.setText(controller.getRememberpassword());
            emailId.setSelection(controller.getRememberId().length() );
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgetPassword:
                startActivity(new Intent(this, ForgetPassword.class));
                break;
            case R.id.login_btn:
                if (isAllFieldsValidated()) {
                    if (utils.Utils.isNetworkAvailable(Login.this)) {
                        login();
                    }
                }
                break;
        }

    }

    public boolean isAllFieldsValidated() {
        boolean status = false;
        if (controller.getValidation().isEmailIdValid(emailId)) {
            if (password.length() > 3) {
                status = true;
            }
        }
        return status;
    }

    public void disableAll() {
        emailId.setEnabled(false);
        password.setEnabled(false);
        login.setVisibility(View.GONE);
    }

    public void enableAll() {
        emailId.setEnabled(true);
        password.setEnabled(true);
        login.setVisibility(View.VISIBLE);
    }

    public void login() {
        disableAll();
        progressbar.setVisibility(View.VISIBLE);
        progressbar.bringToFront();
        Backendless.UserService.login(emailId.getText().toString(), password.getText().toString(), new AsyncCallback<BackendlessUser>() {
            public void handleResponse(BackendlessUser user) {
                // user has been logged in
                controller.setUserProfile(user);
                if (controller.getUserProfil().getIsAdmin().equalsIgnoreCase("true")) {
                    controller.setUserLoggedIn(true);
                    if (rememberme.isChecked()) {
                        controller.setRememberId(emailId.getText().toString(), password.getText().toString());
                    } else {
                        controller.setRememberId("", "");
                    }
                    Toast.makeText(Login.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent(Login.this,DashBoard.class);
                    startActivity(resultIntent);
                    finish();
                } else {
                    controller.setUserLoggedIn(false);
                    Toast.makeText(Login.this, "You dont have admin right.", Toast.LENGTH_SHORT).show();
                    enableAll();
                }
                progressbar.setVisibility(View.GONE);

            }

            public void handleFault(BackendlessFault fault) {
                // login failed, to get the error code call fault.getCode()
                Toast.makeText(Login.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
                enableAll();
            }


        });
    }
}
