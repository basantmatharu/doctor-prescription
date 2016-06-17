package com.scholars.doctor.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.scholars.doctor.R;
import com.scholars.doctor.model.User;
import com.scholars.doctor.model.UserManager;
import com.scholars.doctor.ui.doctor.DoctorHomeActivity;
import com.scholars.doctor.ui.patient.PatientHomeActivity;
import com.scholars.doctor.ui.pharma.PharmaHomeActivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        assert mEmailSignInButton != null;
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);

        checkLogin();
    }

    private void checkLogin() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String username = prefs.getString("username", null);
        String role = prefs.getString("role", null);
        if (username != null && role != null) {
            switch (role) {
                case "DOCTOR":
                    startDoctorActivity();
                    break;
                case "PATIENT":
                    startPatientActivity();
                    break;
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        showProgressDialog(getString(R.string.logging_in));

        UserManager.getUser(mEmailView.getText().toString(), new UserManager.UserCallBacks() {
            @Override
            public void onSuccess(User user) {
                hideProgressDialog();

                if (user != null && mPasswordView.getText().toString().equals(user.getPasswordHash())) {
                    saveUser(user);
                    String role = user.getRole();
                    if (getString(R.string.role_doctor).equals(role)) {
                        startDoctorActivity();
                    } else if (getString(R.string.role_patient).equals(role)) {
                        startPatientActivity();
                    } else if (getString(R.string.role_pharmacy).equals(role)) {
                        startPharmaActivity();
                    } else {
                        Snackbar.make(findViewById(R.id.rootView), R.string.unkown_role, Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    Snackbar.make(findViewById(R.id.rootView), getString(R.string.incorrect_password), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveUser(User user) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        UserManager.modifyUserColumn(user.getUsername(), "registrationId", prefs.getString("regId", ""));
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", user.getUsername());
        editor.putString("role", user.getRole());
        editor.apply();
    }

    private void startDoctorActivity() {
        Intent intent = new Intent(this, DoctorHomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void startPatientActivity() {
        Intent intent = new Intent(this, PatientHomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void startPharmaActivity() {
        Intent intent = new Intent(this, PharmaHomeActivity .class);
        startActivity(intent);
        finish();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return true;
//        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

}

