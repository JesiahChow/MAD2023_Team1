package sg.edu.np.mad.madassignmentteam1;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.v(TAG, "Resume");
        EditText username = findViewById(R.id.editUsername);
        EditText password = findViewById(R.id.editPassword);
        Button loginButton = findViewById(R.id.button3);
        Button backButton = findViewById(R.id.button4);

        loginButton.setOnClickListener(new View.OnClickListener() {
            //if user press login leaving the fields empty
            //login validation
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(username.getText().toString())){
                        username.setError("Please enter username");
                        return;

                    }
                if(TextUtils.isEmpty(password.getText().toString())){
                        password.setError("Please enter password");
                        return;
                }
                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

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