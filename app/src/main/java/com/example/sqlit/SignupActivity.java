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

public class SignupActivity extends AppCompatActivity {

    EditText Name, Email, Password;
    Button SignupButton;
    TextView LoginRedirectText;
    String NameHolder, EmailHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    String SQLiteDataBaseQueryHolder;
    DatabaseHelper sqLiteHelper;
    Cursor cursor;
    String F_Result = "Not_Found";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initializing UI components
        Name = findViewById(R.id.signup_name);
        Email = findViewById(R.id.signup_email);
        Password = findViewById(R.id.signup_password);
        SignupButton = findViewById(R.id.signup_button);
        LoginRedirectText = findViewById(R.id.loginRedirectText);

        // Creating an instance of DatabaseHelper
        sqLiteHelper = new DatabaseHelper(this);

        // Setting click listener for the Signup button
        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if EditText fields are empty
                CheckEditTextStatus();

                // If EditText fields are not empty, proceed with registration
                if (EditTextEmptyHolder) {
                    // Check if the entered email already exists in the database
                    CheckingEmailAlreadyExistsOrNot();
                } else {
                    // Display a message if any of the registration fields are empty
                    Toast.makeText(SignupActivity.this, "Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Setting click listener for the Login redirect text
        LoginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the Login activity
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Check if EditText fields are empty
    public void CheckEditTextStatus() {
        // Get values from EditText fields
        NameHolder = Name.getText().toString();
        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();

        // Check if any of the fields are empty
        if (TextUtils.isEmpty(NameHolder) || TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)) {
            EditTextEmptyHolder = false;
        } else {
            EditTextEmptyHolder = true;
        }
    }

    // Check if the entered email already exists in the database
    public void CheckingEmailAlreadyExistsOrNot() {
        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();

        // Perform a query to check if the email already exists
        cursor = sqLiteDatabaseObj.query(DatabaseHelper.TABLE_NAME, null, " " + DatabaseHelper.Table_Column_2_Email + "=?", new String[]{EmailHolder}, null, null, null);

        while (cursor.moveToNext()) {
            if (cursor.isFirst()) {
                cursor.moveToFirst();
                F_Result = "Email Found";
                cursor.close();
            }
        }

        // Check the final result and proceed accordingly
        if (F_Result.equals("Email Found")) {
            // Display a message if the email already exists
            Toast.makeText(SignupActivity.this, "Email Already Exists", Toast.LENGTH_LONG).show();
        } else {
            // Insert data into the SQLite database
            InsertDataIntoSQLiteDatabase();

            // Display a message after successful registration
            Toast.makeText(SignupActivity.this, "User Registered Successfully", Toast.LENGTH_LONG).show();

            // Empty EditText fields after successful registration
            EmptyEditTextAfterDataInsert();

            // After successful registration, navigate to the Login activity
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        F_Result = "Not_Found";
    }

    // Insert data into SQLite database method
    public void InsertDataIntoSQLiteDatabase() {
        // If EditText fields are not empty, proceed with insertion
        if (EditTextEmptyHolder) {
            // SQLite query to insert data into the table
            SQLiteDataBaseQueryHolder = "INSERT INTO " + DatabaseHelper.TABLE_NAME + " (name,email,password) VALUES('" + NameHolder + "', '" + EmailHolder + "', '" + PasswordHolder + "');";

            // Execute the query
            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);

            // Close the SQLite database
            sqLiteDatabaseObj.close();
        }
    }

    // Empty EditText fields after data insertion
    public void EmptyEditTextAfterDataInsert() {
        Name.getText().clear();
        Email.getText().clear();
        Password.getText().clear();
    }
}
