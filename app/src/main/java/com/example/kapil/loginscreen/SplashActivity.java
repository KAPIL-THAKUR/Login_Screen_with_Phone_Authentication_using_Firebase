package com.example.kapil.loginscreen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {

    TextView textView2, textView4,textView5,textView6,textView7,textView,textView3;
    EditText editText,editText3;
    ImageView imageView5,imageView2,imageView4;
    Button button3;
    CheckBox checkBox;
    String phone_number;
    FirebaseAuth firebaseAuth;
    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private static final String TAG = "PhoneAuth";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView = findViewById(R.id.textView);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        textView7 = findViewById(R.id.textView7);
        editText = findViewById(R.id.editText);
        editText3 = findViewById(R.id.editText3);
        imageView5 = findViewById(R.id.imageView5);
        imageView2 = findViewById(R.id.imageView2);
        imageView4 = findViewById(R.id.imageView4);
        button3=findViewById(R.id.button3);
        checkBox = findViewById(R.id.checkBox);

        firebaseAuth = FirebaseAuth.getInstance();

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send to term & condition link
            }
        });

        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send to privacy policy link
            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    sendCode(null);

                }
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText3.getText().toString().isEmpty()){
                    Toast.makeText(SplashActivity.this,"OTP Can't be Empty",Toast.LENGTH_SHORT).show();
                }else if (editText3.getText().toString().length()!=6){
                    Toast.makeText(SplashActivity.this,"Invalid OTP",Toast.LENGTH_SHORT).show();
                }else {
                    verifyCode(null);
                }
            }
        });

    }


    public boolean validate(){
        boolean result=false;
        phone_number=editText.getText().toString();

        if(phone_number.isEmpty()){
            Toast.makeText(this,"Phone Number Can't be Empty",Toast.LENGTH_SHORT).show();
        }else if(phone_number.length()!=10){
            Toast.makeText(this,"Invalid Phone Number",Toast.LENGTH_SHORT).show();
        }
        else if(checkBox.isChecked()){
            result=true;
        }else{
            Toast.makeText(this,"Agree to Terms & Condition",Toast.LENGTH_SHORT).show();
        }
            return result;
    }

    public void sendCode(View view) {

        String phoneNumber = "+91"+editText.getText().toString();

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks);
    }

    private void setUpVerificatonCallbacks() {

        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(
                            PhoneAuthCredential credential) {


                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            Toast.makeText(SplashActivity.this,"Invalid credential",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Invalid credential: "
                                    + e.getLocalizedMessage());
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                            Toast.makeText(SplashActivity.this,"SMS Quota exceeded Try After Few Time",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "SMS Quota exceeded.");
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {

                        phoneVerificationId = verificationId;
                        resendToken = token;

                        textView6.setText(editText.getText().toString());
                         imageView4.setVisibility(View.INVISIBLE);
                        textView3.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.INVISIBLE);
                        textView2.setVisibility(View.INVISIBLE);
                        textView4.setVisibility(View.INVISIBLE);
                        checkBox.setVisibility(View.INVISIBLE);
                        editText.setVisibility(View.INVISIBLE);
                        imageView5.setVisibility(View.INVISIBLE);
                        textView5.setVisibility(View.VISIBLE);
                        textView6.setVisibility(View.VISIBLE);
                        textView7.setVisibility(View.VISIBLE);
                        button3.setVisibility(View.VISIBLE);
                        editText3.setVisibility(View.VISIBLE);
                        imageView2.setVisibility(View.VISIBLE);
                        Toast.makeText(SplashActivity.this,"OTP Sent Sucessfully",Toast.LENGTH_SHORT).show();
                    }
                };
    }

    public void verifyCode(View view) {

        String code = editText3.getText().toString();

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(phoneVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendUserData();
                            startActivity(new Intent(SplashActivity.this, DetailActivity.class));

                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(SplashActivity.this,"Invalid OTP",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void resendCode(View view) {

        String phoneNumber = "+91"+editText.getText().toString();

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendToken);
        Toast.makeText(this,"OTP Resent Sucessfully",Toast.LENGTH_SHORT).show();
    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());

        UserProfile userProfile = new UserProfile("","","","","","");
        myRef.setValue(userProfile);

    }

}
