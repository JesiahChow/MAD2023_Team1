package sg.edu.np.mad.madassignmentteam1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class UpdateEmail extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String userOldEmail,userNewEmail,userPassword;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        Toolbar myToolbar = findViewById(R.id.email_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Update Email");
        EditText currentEmail = findViewById(R.id.current_email);
        EditText newEmail = findViewById(R.id.new_email);
        Button updateEmail = findViewById(R.id.change_email);

        //disable new email input and update button first before user authentication
        updateEmail.setEnabled(false);
        newEmail.setEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        //get current user
        firebaseUser = mAuth.getCurrentUser();
        //set old email ID on EditText
        assert firebaseUser != null;
        userOldEmail = firebaseUser.getEmail();
        currentEmail.setText(userOldEmail);
        //if user data does not exist
        if(firebaseUser == null){
            Toast.makeText(UpdateEmail.this,"Something went wrong!User details not available.",Toast.LENGTH_LONG).show();
        }
        else{
            //if user data exists re-authenticate the user
            reAuthenticate(firebaseUser);
        }



    }
//re-authenticate user before updating email
    private void reAuthenticate(FirebaseUser firebaseUser) {
        Button authenticate = findViewById(R.id.authenticate);
        EditText newEmail = findViewById(R.id.new_email);
        Button updateEmail = findViewById(R.id.change_email);
        EditText currentEmail = findViewById(R.id.current_email);
        EditText currentPassword = findViewById(R.id.current_password);
        TextView authText = findViewById(R.id.new_email_text);
        authenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtain password for authentication
                userPassword = currentPassword.getText().toString();
                if(TextUtils.isEmpty(userPassword)){
                    Toast.makeText(UpdateEmail.this,"Password is needed to continue",Toast.LENGTH_SHORT).show();
                    currentPassword.setError("Please enter your password for authentication");
                    currentPassword.requestFocus();
                }
                else{
                    //a credential in firebase authentication server can use to authenticate user
                    AuthCredential credential = EmailAuthProvider.getCredential(userOldEmail,userPassword);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(UpdateEmail.this,"Password has been verified.You can update you email now.",Toast.LENGTH_LONG).show();
                                //set textview to show that user is authenticated
                                authText.setText("User authenticated.You can update your email now.");

                                //disable EditText for password,email for verification and enable EditText for new email
                                newEmail.setEnabled(true);
                                updateEmail.setEnabled(true);
                                currentEmail.setEnabled(false);
                                currentPassword.setEnabled(false);
                                authenticate.setEnabled(false);
                                //change color of update email
                                updateEmail.setBackgroundTintList(ContextCompat.getColorStateList(UpdateEmail.this,R.color.blue));
                                authenticate.setBackgroundTintList(ContextCompat.getColorStateList(UpdateEmail.this,R.color.grey));
                                //when user clicks update
                                updateEmail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        userNewEmail = newEmail.getText().toString();
                                        if(TextUtils.isEmpty(userNewEmail)){
                                            Toast.makeText(UpdateEmail.this,"New Email is required",Toast.LENGTH_SHORT).show();
                                            newEmail.setError("Please enter new email");
                                            newEmail.requestFocus();
                                        }
                                        //to check if email is in correct expression
                                        else if(!Patterns.EMAIL_ADDRESS.matcher(userNewEmail).matches()){
                                            Toast.makeText(UpdateEmail.this,"Invalid Email",Toast.LENGTH_SHORT).show();
                                            newEmail.setError("Please enter a valid email");
                                            newEmail.requestFocus();
                                        }
                                        //if old email matches new email
                                        else if(userOldEmail.matches(userNewEmail)){
                                            Toast.makeText(UpdateEmail.this,"New email matches old email.",Toast.LENGTH_SHORT).show();
                                            newEmail.setError("Please enter a new email");
                                            newEmail.requestFocus();
                                        }
                                        else{
                                            updateNewEmail(firebaseUser);
                                        }
                                    }
                                });
                            }
                           else{
                               try{
                                   throw Objects.requireNonNull(task.getException());
                               }catch(Exception e){
                                   Toast.makeText(UpdateEmail.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateNewEmail(FirebaseUser firebaseUser) {
        //initialise updating of user email
        firebaseUser.updateEmail(userNewEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //verify Email
                    firebaseUser.sendEmailVerification();
                    Toast.makeText(UpdateEmail.this,"Email has been updated.Please verify your new email.",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UpdateEmail.this,Settings.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (Exception e) {
                        Toast.makeText(UpdateEmail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    //creating menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu items
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //when any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.profile){
            Intent intent = new Intent(UpdateEmail.this,Settings.class);
            startActivity(intent);
            finish();
        }
        if(id == R.id.update_email){
            Intent intent = new Intent(UpdateEmail.this,UpdateEmail.class);
            startActivity(intent);
            finish();
        }

        else if(id == R.id.update_password){
            Intent intent = new Intent(UpdateEmail.this,UpdatePassword.class);
            startActivity(intent);
            finish();
        }
        if(id == R.id.logout_menu){
            //sign out the firebase user
            mAuth.signOut();
            Intent intent = new Intent(UpdateEmail.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.delete_profile){
            Intent intent = new Intent(UpdateEmail.this,DeleteProfile.class);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }
}