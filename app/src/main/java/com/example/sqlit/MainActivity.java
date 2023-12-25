package com.example.sqlit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    String EmailHolder;
    TextView Email;
    Button LogOUT;

    public static final String UserEmail = "UserEmail"; // Add this line

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Email = findViewById(R.id.userEmail);
        LogOUT = findViewById(R.id.logout);

        Intent intent = getIntent();

        // Receiving User Email Send By LoginActivity.
        EmailHolder = intent.getStringExtra(LoginActivity.UserEmail); // Change here

        // Setting up received email to TextView.
        Email.setText("Welcome, " + EmailHolder);

        // Adding click listener to Log Out button.
        LogOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Finishing current MainActivity activity on button click.
                finish();
                Toast.makeText(MainActivity.this, "Log Out Successful", Toast.LENGTH_LONG).show();
            }
        });
    }
}
