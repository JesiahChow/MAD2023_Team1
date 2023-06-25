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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView createAccount = findViewById(R.id.createAccount);
        TextView forgotPassword = findViewById(R.id.textView6);
        EditText password = findViewById(R.id.editPassword);
        ImageView passwordVisibility = findViewById(R.id.imageView3);
        passwordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sets characters within the edittext into dots
                if(password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //if password visible then hide it
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //change icon
                    passwordVisibility.setImageResource(R.drawable.passwordhide);
                }
                else{
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordVisibility.setImageResource(R.drawable.passwordshow);
                }
            }
        });
        //if user needs to create account, redirect user to register page
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
        //if user forgot password,direct user to change password page
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent1);
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

    }


    @Override
    protected void onResume(){
        super.onResume();
        EditText email = findViewById(R.id.editTextTextEmailAddress);
        EditText password = findViewById(R.id.editPassword);
        Button loginButton = findViewById(R.id.button3);
        Button backButton = findViewById(R.id.button4);

        loginButton.setOnClickListener(new View.OnClickListener() {
            //if user press login leaving the fields empty
            //login validation
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(email.getText().toString())){
                        email.setError("Please enter email");
                        email.requestFocus();
                        return;

                    }
                if(TextUtils.isEmpty(password.getText().toString())){
                        password.setError("Please enter password");
                        password.requestFocus();
                        return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    email.setError("Invalid email");
                    email.requestFocus();
                    return;
                }
                else{
                    loginUser(email,password);
                }
                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();


            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //once user clicks back it directs user to mainActivity page
            public void onClick(View v) {
                Intent backIntent = new Intent(Login.this, MainActivity.class);
                startActivity(backIntent);
            }
        });


    }

    private void loginUser(EditText email, EditText password) {
        //initialise firebase auth
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener( Login.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this,"Login successful",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        else {
                            try
                            {
                                throw Objects.requireNonNull(task.getException());
                            }
                            //when user does not exist or no longer available
                            catch (FirebaseAuthInvalidUserException e){
                                email.setError("User does not exists or is no longer valid. Please register again.");
                                email.requestFocus();
                            }
                            //if user enters email and password and does not match
                            catch(FirebaseAuthInvalidCredentialsException e){
                                email.setError("Invalid credentials. Kindly, check and re-enter.");
                                email.requestFocus();

                            }
                            catch(Exception e){
                                Toast.makeText(Login.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();

                            }

                        }
                    }
                });
    }
    //check if user already logs in
    @Override
    protected void onStart(){
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
       FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //if user is logged in, the user will be redirected to home activity
            Toast.makeText(Login.this,"Already logged in",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(Login.this,"You can log in",Toast.LENGTH_SHORT).show();
        }
    }


}