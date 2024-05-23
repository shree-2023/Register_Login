package com.example.register_login_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText name, email, phnum, pwd;
    Button btnRegister, hom;
    TextView btnLog;
    String username, useremail, userphnum, userpwd;
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnRegister = findViewById(R.id.button);
        name = findViewById(R.id.editTextText);
        email = findViewById(R.id.editTextText2);
        phnum = findViewById(R.id.editTextText3);
        pwd = findViewById(R.id.editTextText4);
        btnLog = findViewById(R.id.textView2);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = name.getText().toString();
                useremail = email.getText().toString();
                userphnum = phnum.getText().toString();
                userpwd = pwd.getText().toString();

                if (!TextUtils.isEmpty(username)) {
                    if (!TextUtils.isEmpty(useremail)) {
                        if (!TextUtils.isEmpty(userphnum)) {
                            if (userphnum.length() == 10) {
                                if (!TextUtils.isEmpty(userpwd)) {
                                    RegisterUser();
                                } else {
                                    pwd.setError("Password cannot be empty");
                                }
                            } else {
                                phnum.setError("Enter a valid phone number");
                            }
                        }
                    } else {
                        email.setError("Email field cannot be empty");
                    }
                } else {
                    name.setError("Name field cannot be empty");
                }
            }
        });
    }

    private void RegisterUser() {
        btnLog.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.INVISIBLE);
        auth.createUserWithEmailAndPassword(useremail, userpwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                saveUserDetails();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                btnRegister.setVisibility(View.VISIBLE);
                btnLog.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void saveUserDetails() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", username);
        user.put("email", useremail);
        user.put("phone", userphnum);

        db.collection("users").document(auth.getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this, Home.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        btnRegister.setVisibility(View.VISIBLE);
                        btnLog.setVisibility(View.INVISIBLE);
                    }
                });
    }
}
