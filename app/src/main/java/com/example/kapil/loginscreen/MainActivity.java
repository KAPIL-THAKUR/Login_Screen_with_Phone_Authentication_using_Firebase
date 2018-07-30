package com.example.kapil.loginscreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT=2000;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String name1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressBar=findViewById(R.id.progressBar);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(2000);
                firebaseAuth=FirebaseAuth.getInstance();

                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                         startActivity(new Intent(MainActivity.this,DetailActivity.class));
                }else{
                    Intent Splash=new Intent(MainActivity.this,SplashActivity.class);
                    startActivity(Splash);
                }
                finish();
            }
        },SPLASH_TIME_OUT);

    }
}
