package com.scholars.doctor.ui.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import com.scholars.doctor.ui.BaseActivity;
import com.scholars.doctor.R;
import com.scholars.doctor.model.User;
import com.scholars.doctor.model.managers.UserManager;


public class SelectPatientActivity extends BaseActivity implements View.OnClickListener {

    public static final String RESULT_SELECTED_USER = "selected_user";

    FloatingActionButton selectButton;
    TextInputEditText usernameText, nameText, addresstext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_patient);

        selectButton = (FloatingActionButton) findViewById(R.id.selectButton);
        usernameText = (TextInputEditText) findViewById(R.id.usernameEdt);
        nameText = (TextInputEditText) findViewById(R.id.fullNameEdt);
        addresstext = (TextInputEditText) findViewById(R.id.addressEdt);

        selectButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectButton:
                fetchOrCreateUser();
                break;
        }
    }

    private void fetchOrCreateUser() {
        showProgressDialog(getString(R.string.fetching_user));
        UserManager userManager = new UserManager();
        userManager.createUser(
                usernameText.getText().toString(),
                nameText.getText().toString(),
                addresstext.getText().toString(),
                "Initial1",
                getString(R.string.role_patient),
                new UserManager.UserCallBacks() {
                    @Override
                    public void onSuccess(User user) {
                        Intent intent = new Intent();
                        intent.putExtra(RESULT_SELECTED_USER, user);
                        setResult(RESULT_OK, intent);
                        hideProgressDialog();
                        finishAfterTransition();
                    }
                });
    }
}
