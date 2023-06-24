package sg.edu.np.mad.madassignmentteam1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView link = findViewById(R.id.textView5);
        EditText password1 = findViewById(R.id.editTextTextPassword);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);

            }
        });
        //show hide password using eye icon
        ImageView passwordVisibility = findViewById(R.id.imageView4);
        passwordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sets characters within the edittext into dots
                if(password1.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //if password visible then hide it
                    password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //change icon
                    passwordVisibility.setImageResource(R.drawable.passwordhide);
                }
                else{
                    password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordVisibility.setImageResource(R.drawable.passwordshow);
                }
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        EditText email = findViewById(R.id.editTextTextEmailAddress);
        EditText password1 = findViewById(R.id.editTextTextPassword);
        EditText username = findViewById(R.id.editTextTextUsername);
        Button register1 = findViewById(R.id.button5);
        Button back1 = findViewById(R.id.button6);

        register1.setOnClickListener(new View.OnClickListener() {
            //if user press login leaving the fields empty
            //login validation
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(email.getText().toString())){
                    email.setError("Please enter email");
                    email.requestFocus();
                    return;

                }
               if(TextUtils.isEmpty(username.getText().toString())){
                    username.setError("Please enter username");
                    username.requestFocus();
                    return;
                }
               if(TextUtils.isEmpty(password1.getText().toString())){
                    password1.setError("Please enter password");
                    password1.requestFocus();
                    return;
                }
               if(password1.length() < 6){
                    password1.setError("Password must be more than 6 characters.");
                    password1.requestFocus();
                }
               if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    email.setError("Invalid email. Please enter a valid email");

                }
               else{
                   registerUser(username, email,password1);
               }

            }
        });
        //back button
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            //once user clicks back it directs user to mainActivity page
            public void onClick(View v) {
                Intent backIntent = new Intent(Register.this, MainActivity.class);
                startActivity(backIntent);
            }
        });

    }

    private void registerUser(EditText username, EditText email, EditText password1) {
        //initialise firebase authentication
        mAuth = FirebaseAuth.getInstance();
        //set email and password as registration
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password1.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Register.this,"Account created",Toast.LENGTH_SHORT).show();
                            //enter user data into firebase realtime database
                            UserDetails userDetails = new UserDetails(username.getText().toString(),email.getText().toString(),password1.getText().toString());
                            //extract user reference from database for registered users
                            DatabaseReference profile = FirebaseDatabase.getInstance().getReference("Registered Users");
                            //set the values of email, username and password into the realtime database
                            assert user != null;
                            profile.child(user.getUid()).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.sendEmailVerification();
                                        Toast.makeText(Register.this, "User registered successfully.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Register.this,HomeActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(Register.this, "User register failed, please try again.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });



                        }
                        else{
                            try{
                                throw Objects.requireNonNull(task.getException());
                            }
                            //catch error when user uses existing email to register
                            catch(FirebaseAuthUserCollisionException e){
                                password1.setError("User is already registered with this email. Use another email");
                                password1.requestFocus();
                            }
                            //catch error when password entered by user does not match email address
                            catch (FirebaseAuthInvalidCredentialsException e) {
                                email.setError("Email is invalid or already in use. Kindly re-enter.");
                                email.requestFocus();
                            }
                            catch(Exception e){
                                Toast.makeText(Register.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                });
    }
}