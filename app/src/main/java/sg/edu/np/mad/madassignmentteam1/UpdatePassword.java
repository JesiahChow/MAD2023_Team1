package sg.edu.np.mad.madassignmentteam1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class UpdatePassword extends AppCompatActivity {
private FirebaseAuth mAuth;
private FirebaseUser firebaseUser;
private EditText currentPwd,newPwd,confirmPwd;
private TextView authText;
private Button buttonAuth, buttonUpdate;
private String userPwdCurrent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Change Password");

        currentPwd = findViewById(R.id.current_password);
        newPwd = findViewById(R.id.new_password);
        confirmPwd = findViewById(R.id.confirm_new_password);
        authText = findViewById(R.id.new_password_content);
        buttonAuth = findViewById(R.id.authenticate);
        buttonUpdate = findViewById(R.id.change_password);

        //disable EditText for new password, confirm new password and make pwd button un-clickable till user is authenticated
        newPwd.setEnabled(false);
        confirmPwd.setEnabled(false);
        buttonUpdate.setEnabled(false);
        //get firebase authentication
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        //if user does not exists
        if(firebaseUser == null){
            Toast.makeText(UpdatePassword.this,"Something went wrong!User details not available",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdatePassword.this, Settings.class);
            startActivity(intent);
            finish();
        }
        else{
            //if user exists re-authenticate user
            reAuthenticate(firebaseUser);
        }
    }
    //re-authenticate before changing password
    private void reAuthenticate(FirebaseUser firebaseUser) {
        buttonAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPwdCurrent = currentPwd.getText().toString();
                if(TextUtils.isEmpty(userPwdCurrent)){
                    Toast.makeText(UpdatePassword.this,"Password is needed",Toast.LENGTH_SHORT).show();
                    currentPwd.setError("Please enter your current password");
                    currentPwd.requestFocus();
                }
                else{
                    //Re-authenticate user
                    //a credential in firebase authentication server can use to authenticate user
                    AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(firebaseUser.getEmail()),userPwdCurrent);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //disable EditText for current password and enable EditText for new password
                                currentPwd.setEnabled(false);
                                newPwd.setEnabled(true);
                                confirmPwd.setEnabled(true);
                                //enable update password button, disable authenticate button
                                buttonAuth.setEnabled(false);
                                buttonUpdate.setEnabled(true);
                                //set TextView to show user is verified
                                authText.setText("You are authenticated. You can change your password.");
                                Toast.makeText(UpdatePassword.this,"Please enter your new password",Toast.LENGTH_SHORT).show();
                                //update color of change password button
                                buttonUpdate.setBackgroundTintList(ContextCompat.getColorStateList(UpdatePassword.this,R.color.blue));
                                buttonAuth.setBackgroundTintList(ContextCompat.getColorStateList(UpdatePassword.this,R.color.grey));

                                buttonUpdate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changePassword(firebaseUser);
                                    }
                                });
                            }
                            else{
                                try{
                                    throw Objects.requireNonNull(task.getException());
                                }catch (Exception e){
                                    Toast.makeText(UpdatePassword.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void changePassword(FirebaseUser firebaseUser) {
        String userNewPwd = newPwd.getText().toString();
        String userCfmNewPwd = confirmPwd.getText().toString();
        //new password validation
        if(TextUtils.isEmpty(userNewPwd)){
            Toast.makeText(UpdatePassword.this,"New password is needed",Toast.LENGTH_SHORT).show();
            newPwd.setError("Please enter new password");
            newPwd.requestFocus();
        }
        else if(TextUtils.isEmpty(userCfmNewPwd)){
            Toast.makeText(UpdatePassword.this,"Please confirm your new password",Toast.LENGTH_SHORT).show();
            confirmPwd.setError("Please re-enter your new password");
            confirmPwd.requestFocus();
        }
        //if new password does not match confirm password
        else if(!userNewPwd.matches(userCfmNewPwd)){
            Toast.makeText(UpdatePassword.this,"Password did not match",Toast.LENGTH_SHORT).show();
            confirmPwd.setError("Please re-enter the same password");
            confirmPwd.requestFocus();
        }
        //if old password matches new password
        else if(userPwdCurrent.matches(userNewPwd)){
            Toast.makeText(UpdatePassword.this,"New password cannot be the same as old password",Toast.LENGTH_LONG).show();
            newPwd.setError("Please enter a different password");
            newPwd.requestFocus();
        }
        else{
            //firebase updates password of user
            firebaseUser.updatePassword(userNewPwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(UpdatePassword.this,"Password has been changed",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdatePassword.this,Settings.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        try{
                            throw Objects.requireNonNull(task.getException());
                        }catch (Exception e){
                            Toast.makeText(UpdatePassword.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
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
            Intent intent = new Intent(UpdatePassword.this,Settings.class);
            startActivity(intent);
            finish();
        }
        if(id == R.id.update_email){
            Intent intent = new Intent(UpdatePassword.this,UpdateEmail.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.update_password){
            Intent intent = new Intent(UpdatePassword.this,UpdatePassword.class);
            startActivity(intent);
            finish();
        }
        if(id == R.id.logout_menu){
            //sign out the firebase user
            mAuth.signOut();
            Intent intent = new Intent(UpdatePassword.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.delete_profile){
            Intent intent = new Intent(UpdatePassword.this,DeleteProfile.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}