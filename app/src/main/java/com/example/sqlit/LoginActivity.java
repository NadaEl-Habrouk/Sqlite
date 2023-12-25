package com.example.sqlit;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button LogInButton;
    EditText Email, Password;
    TextView SignUpRedirectText;
    String EmailHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    DatabaseHelper databaseHelper;
    Cursor cursor;
    String TempPassword = "NOT_FOUND";
    public static final String UserEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LogInButton = findViewById(R.id.login_button);
        Email = findViewById(R.id.login_email);
        Password = findViewById(R.id.login_password);
        SignUpRedirectText = findViewById(R.id.signUpRedirectText);

        databaseHelper = new DatabaseHelper(this);

        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckEditTextStatus();
                LoginFunction();
            }
        });

        SignUpRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public void LoginFunction() {
        if (EditTextEmptyHolder) {
            sqLiteDatabaseObj = databaseHelper.getWritableDatabase();
            cursor = sqLiteDatabaseObj.query(DatabaseHelper.TABLE_NAME, null,
                    " " + DatabaseHelper.Table_Column_2_Email + "=?", new String[]{EmailHolder}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int passwordIndex = cursor.getColumnIndex(DatabaseHelper.Table_Column_3_Password);

                if (passwordIndex != -1) {
                    TempPassword = cursor.getString(passwordIndex);
                    cursor.close();
                } else {
                    // Handle the case where the password column index is -1
                    Toast.makeText(LoginActivity.this, "Invalid password column index", Toast.LENGTH_LONG).show();
                }
            }

            CheckFinalResult();
        } else {
            Toast.makeText(LoginActivity.this, "Please Enter Email or Password", Toast.LENGTH_LONG).show();
        }
    }

    public void CheckEditTextStatus() {
        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();

        if (TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)) {
            EditTextEmptyHolder = false;
        } else {
            EditTextEmptyHolder = true;
        }
    }

    public void CheckFinalResult() {
        if (TempPassword.equalsIgnoreCase(PasswordHolder)) {
            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(UserEmail, EmailHolder);
            startActivity(intent);
        } else {
            Toast.makeText(LoginActivity.this, "Email or Password is Wrong, Please Try Again", Toast.LENGTH_LONG).show();
        }
        TempPassword = "NOT_FOUND";
    }
}
