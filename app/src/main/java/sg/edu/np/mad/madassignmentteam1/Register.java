package sg.edu.np.mad.madassignmentteam1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
    @Override
    protected void onResume(){
        super.onResume();
        EditText username1 = findViewById(R.id.editTextText);
        EditText password1 = findViewById(R.id.editTextTextPassword);
        Button register1 = findViewById(R.id.button5);
        Button back1 = findViewById(R.id.button6);

        register1.setOnClickListener(new View.OnClickListener() {
            //if user press login leaving the fields empty
            //login validation
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(username1.getText().toString())){
                    username1.setError("Please enter username");
                    return;

                }
                if(TextUtils.isEmpty(password1.getText().toString())){
                    password1.setError("Please enter password");
                    return;
                }
                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

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