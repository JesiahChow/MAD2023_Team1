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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Button resetPassword = findViewById(R.id.resetPassword);
        EditText editEmail = findViewById(R.id.editTextTextEmailAddress);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                if(TextUtils.isEmpty(editEmail.getText().toString())){
                    editEmail.setError("Please enter email");
                    editEmail.requestFocus();

                }
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
    {
        mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Please check your inbox for password reset link", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ForgotPassword.this,"Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}