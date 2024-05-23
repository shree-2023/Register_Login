package com.example.register_login_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText email, pwd;
    Button btnLogin;
    TextView btnReg;
    String useremail, userpwd;
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private static final String LOGIN_PREFS = "loginPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.button3);
        email = findViewById(R.id.editTextText);
        pwd = findViewById(R.id.editTextText4);
        btnReg = findViewById(R.id.TextView14);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);

        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            // If logged in, go to Home Activity
            goToHomeActivity();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                useremail = email.getText().toString();
                userpwd = pwd.getText().toString();
                if (!TextUtils.isEmpty(useremail)) {
                    if (useremail.matches(emailpattern)) {
                        if (!TextUtils.isEmpty(userpwd)) {
                            LogInUser();
                        } else {
                            pwd.setError("Password is wrong");
                        }
                    } else {
                        email.setError("Enter a valid Email address");
                    }
                } else {
                    email.setError("Email field cannot be empty");
                }
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void LogInUser() {
        btnLogin.setVisibility(View.VISIBLE);
        btnReg.setVisibility(View.INVISIBLE);
        auth.signInWithEmailAndPassword(useremail, userpwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.apply();
                retrieveUserDetails();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                btnLogin.setVisibility(View.INVISIBLE);
                btnReg.setVisibility(View.VISIBLE);
            }
        });
    }

    private void retrieveUserDetails() {
        db.collection("users").document(auth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            String phone = documentSnapshot.getString("phone");

                            Intent intent = new Intent(Login.this, Home.class);
                            intent.putExtra("name", name);
                            intent.putExtra("email", email);
                            intent.putExtra("phone", phone);
                            startActivity(intent);
                            finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToHomeActivity() {
        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
        finish();
    }
}
