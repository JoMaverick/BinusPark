package com.example.binuspark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText emailET, passET;
    Button loginButton;
    TextView registerRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailET = findViewById(R.id.emailLoginET);
        passET = findViewById(R.id.passwordLoginET);
        registerRedirectText = findViewById(R.id.RegisterRedirect);
        loginButton = findViewById(R.id.loginBTN);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emailValidation() | !passwordValidation()) {

                } else {
                    userLoginValidation();
                }
            }
        });

        registerRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    public Boolean emailValidation() {
        String str = emailET.getText().toString();
        if (str.isEmpty()) {
            emailET.setError("Username must be filled!");
            return false;
        } else {
            emailET.setError(null);
            return true;
        }
    }


    public Boolean passwordValidation() {
        String str = passET.getText().toString();
        if (str.isEmpty()) {
            passET.setError("Password must be filled!");
            return false;
        } else {
            passET.setError(null);
            return true;
        }
    }

    public void userLoginValidation() {
        String userUsername = "Jomav";
        String userEmail = emailET.getText().toString().trim();
        String userPassword = passET.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(userEmail);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    emailET.setError(null);
                    String passwordFromDB = snapshot.child(userEmail).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userPassword)) {
                        emailET.setError(null);

                        //Pass the data using intent

                        String nameFromDB = snapshot.child(userEmail).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userEmail).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(userEmail).child("username").getValue(String.class);

                        Intent intent = new Intent(Login.this, MainActivity2.class);

                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);

                        startActivity(intent);
                    } else {
                        passET.setError("Password Incorrect, Please try again !!!");
                        passET.requestFocus();
                    }
                } else {
                    emailET.setError("User is not registered !!!");
                    emailET.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }


}