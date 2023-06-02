package sg.edu.np.mad.madassignmentteam1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView link = findViewById(R.id.textView5);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        EditText email = findViewById(R.id.editTextTextEmailAddress);
        EditText password1 = findViewById(R.id.editTextTextPassword);
        Button register1 = findViewById(R.id.button5);
        Button back1 = findViewById(R.id.button6);

        register1.setOnClickListener(new View.OnClickListener() {
            //if user press login leaving the fields empty
            //login validation
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(email.getText().toString())){
                    email.setError("Please enter username");
                    return;

                }
                if(TextUtils.isEmpty(password1.getText().toString())){
                    password1.setError("Please enter password");
                    return;
                }
                //initialise firebase auth
                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password1.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success
                                    Toast.makeText(Register.this,"Account created",Toast.LENGTH_SHORT).show();


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Account already exists or password is less than 6 characters.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                /*Intent myIntent = new Intent(.this, MainActivity2.class);
                myIntent.putExtra("Username",usernameStr);
                myIntent.putExtra("UserPassword", userPasswordStr);
                startActivity(myIntent);*/
            }
        });
        //back button
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            //jesiah once user clicks back it directs user to mainActivity page
            public void onClick(View v) {
                Intent backIntent = new Intent(Register.this, MainActivity.class);
                startActivity(backIntent);
            }
        });

    }
}