package sg.edu.np.mad.madassignmentteam1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Objects;

public class ForgotPassword extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Button resetPassword = findViewById(R.id.resetPassword);
        EditText editEmail = findViewById(R.id.editTextTextEmailAddress);
        ImageView backButton = findViewById(R.id.back_button);

        //when user clicks back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
        //password validation
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                if(TextUtils.isEmpty(editEmail.getText().toString())){
                    editEmail.setError("Please enter email");
                    editEmail.requestFocus();

                }
                //to check if email is in correct expression
                else if(!Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString()).matches()){
                    editEmail.setError("Please enter valid email");
                    editEmail.requestFocus();
                }
                else{
                    newPassword(email);
                }
            }
        });
    }

    private void newPassword(String email)
    { EditText editEmail = findViewById(R.id.editTextTextEmailAddress);
        //initialise firebase authentication
        mAuth = FirebaseAuth.getInstance();
        //send reset password email to user email
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Please check your inbox for password reset link", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    try {
                    throw Objects.requireNonNull(task.getException());
                }
                    //if user does not exists within firebase
                    catch(FirebaseAuthInvalidUserException e){
                        editEmail.setError("User does not exist or is no longer valid. Please register again. ");
                    }
                    catch(Exception e){
                        Toast.makeText(ForgotPassword.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                }
            }
        });
    }
}