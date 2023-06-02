package sg.edu.np.mad.madassignmentteam1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView createAccount = findViewById(R.id.createAccount);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onResume(){
        super.onResume();
        Log.v(TAG, "Resume");
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
                        return;

                    }
                if(TextUtils.isEmpty(password.getText().toString())){
                        password.setError("Please enter password");
                        return;
                }
                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
               //initialise firebase auth
                mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                   Toast.makeText(Login.this,"Login successful",Toast.LENGTH_SHORT).show();
                                   Intent intent = new Intent(Login.this, HomePage.class);
                                   startActivity(intent);
                                   finish();


                                }
                                else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Login.this, "Invalid email or password.",
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
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //jesiah once user clicks back it directs user to mainActivity page
            public void onClick(View v) {
                Intent backIntent = new Intent(Login.this, MainActivity.class);
                startActivity(backIntent);
            }
        });


    };
}