package com.example.kapil.loginscreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {

    EditText editText2,editText4,editText5,editText6,editText7,editText8;
    ImageView imageView3;
    String name,address_line1,address_line2,city,state,pin_code,name1;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        editText2 = findViewById(R.id.editText2);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        editText6 = findViewById(R.id.editText6);
        editText7 = findViewById(R.id.editText7);
        editText8 = findViewById(R.id.editText8);
        imageView3=findViewById(R.id.imageView3);
        final ProgressDialog progressDialog=new ProgressDialog(DetailActivity.this);
        progressDialog.setMessage("Fetching Details...");
        progressDialog.show();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                name1=userProfile.getName();
                if(name1.isEmpty()){
                    progressDialog.dismiss();
                }else{
                    startActivity(new Intent(DetailActivity.this,login.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    sendUserData();
                    Toast.makeText(DetailActivity.this,"Login Sucessfull",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DetailActivity.this,login.class));
                }
            }
        });
    }

    public boolean validate(){
        boolean result=false;
        name=editText2.getText().toString();
        address_line1=editText4.getText().toString();
        address_line2=editText8.getText().toString();
        city=editText5.getText().toString();
        state=editText6.getText().toString();
        pin_code=editText7.getText().toString();

        if (name.isEmpty() || address_line1.isEmpty() || address_line2.isEmpty() || city.isEmpty() || state.isEmpty() || pin_code.isEmpty()){
            Toast.makeText(this,"Please Enter all Details First",Toast.LENGTH_SHORT).show();
        }else{
            result=true;
        }
        return result;
    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());

        UserProfile userProfile = new UserProfile(name, address_line1, address_line2, city, state, pin_code);
        myRef.setValue(userProfile);
    }

}
