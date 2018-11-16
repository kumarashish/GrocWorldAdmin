package com.ashish.grocworld_admin;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;

/**
 * Created by ashish.kumar on 05-07-2018.
 */

public class ForgetPassword extends Activity implements View.OnClickListener{
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.emailId)
    EditText emailId;
    @BindView(R.id.submit_btn)
    Button submit;
    AppController controller;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        controller=(AppController)getApplicationContext();
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
switch (view.getId())
{
    case R.id.back:
        onBackPressed();
        break;
    case R.id.submit_btn:
        if(controller.getValidation().isEmailIdValid(emailId))
        {
            if(utils.Utils.isNetworkAvailable(ForgetPassword.this))
            {
                resetPassword();
            }
        }
        break;
}
    }
    public void disableAll() {
        emailId.setEnabled(false);
        submit.setVisibility(View.GONE);
    }

    public void enableAll() {
        emailId.setEnabled(true);
        submit.setVisibility(View.VISIBLE);
    }

    public void resetPassword() {
        progressbar.setVisibility(View.VISIBLE);
        disableAll();
        Backendless.UserService.restorePassword(emailId.getText().toString(), new AsyncCallback<Void>() {
            public void handleResponse(Void response) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(ForgetPassword.this, "Your password has been reset and new password has been send to ur email id", Toast.LENGTH_SHORT).show();
                finish();
            }

            public void handleFault(BackendlessFault fault) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(ForgetPassword.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                enableAll();
            }
        });
    }
}
