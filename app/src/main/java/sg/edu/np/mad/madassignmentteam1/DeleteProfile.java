package sg.edu.np.mad.madassignmentteam1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class DeleteProfile extends AppCompatActivity {
private FirebaseAuth mAuth;
private FirebaseUser firebaseUser;
private EditText editTextUserPwd;
private TextView deleteAuth;
private String userPwd;
private Button authButton, deleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_profile);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Delete Profile");
        editTextUserPwd = findViewById(R.id.password_input);
        deleteAuth = findViewById(R.id.delete_text);
        authButton = findViewById(R.id.authenticate);
        deleteButton = findViewById(R.id.delete_profile);

        //disable delete user button before authentication
        deleteButton.setEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(DeleteProfile.this, "Something went wrong!User details not available at the moment",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(DeleteProfile.this,Settings.class);
            startActivity(intent);
            finish();
        }
        else{
            reAuthenticate(firebaseUser);
        }

    }
//re-authenticate user password before continuing
    private void reAuthenticate(FirebaseUser firebaseUser) {
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPwd = editTextUserPwd.getText().toString();
                if(TextUtils.isEmpty(userPwd)){
                    Toast.makeText(DeleteProfile.this,"Password is needed",Toast.LENGTH_SHORT).show();
                    editTextUserPwd.setError("Please enter your current password");
                    editTextUserPwd.requestFocus();
                }
                else{
                    //Re-authenticate user
                    AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(firebaseUser.getEmail()),userPwd);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //disable EditText for password
                                editTextUserPwd.setEnabled(false);

                                //enable delete user button
                                authButton.setEnabled(false);
                                deleteButton.setEnabled(true);
                                //set TextView to show user is verified
                                deleteAuth.setText("You are authenticated. You can delete your profile.");
                                Toast.makeText(DeleteProfile.this,"Please proceed",Toast.LENGTH_SHORT).show();
                                //update color of change password button
                                deleteButton.setBackgroundTintList(ContextCompat.getColorStateList(DeleteProfile.this,R.color.blue));
                                authButton.setBackgroundTintList(ContextCompat.getColorStateList(DeleteProfile.this,R.color.grey));

                                deleteButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showAlertDialog();
                                    }
                                });
                            }
                            else{
                                try{
                                    throw Objects.requireNonNull(task.getException());
                                }catch (Exception e){
                                    Toast.makeText(DeleteProfile.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteProfile.this);
        builder.setTitle("Delete User Profile");
        builder.setMessage("Do you really want to delete your account? All your data will be lost forever!");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUserData();
            }
    });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(DeleteProfile.this,Settings.class);
                startActivity(intent);
                finish();
            }
        });
        builder.create().show();
    }
//delete user object
    private void deleteUser() {
        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mAuth.signOut();
                    Toast.makeText(DeleteProfile.this,"User has been deleted",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DeleteProfile.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    try{
                        throw Objects.requireNonNull(task.getException());
                    }
                    catch(Exception e){
                        Toast.makeText(DeleteProfile.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

//delete all data of user
    private void deleteUserData() {
        //delete data from realtime database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        databaseReference.child(firebaseUser.getUid()).removeValue();
        deleteUser();
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
            Intent intent = new Intent(DeleteProfile.this,Settings.class);
            startActivity(intent);
            finish();
        }
        if(id == R.id.update_email){
            Intent intent = new Intent(DeleteProfile.this,UpdateEmail.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.update_password){
            Intent intent = new Intent(DeleteProfile.this,UpdatePassword.class);
            startActivity(intent);
            finish();
        }
        if(id == R.id.logout_menu){
            //sign out the firebase user
            mAuth.signOut();
            Intent intent = new Intent(DeleteProfile.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.delete_profile){
            Intent intent = new Intent(DeleteProfile.this,DeleteProfile.class);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }
}