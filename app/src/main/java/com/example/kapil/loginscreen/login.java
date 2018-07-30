package com.example.kapil.loginscreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    Button button;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();

        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                Toast.makeText(login.this,"Logout Sucessfull",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(login.this,SplashActivity.class));
            }
        });
    }
}
