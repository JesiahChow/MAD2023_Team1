package sg.edu.np.mad.madassignmentteam1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Settings extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String name,emailAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth = FirebaseAuth.getInstance();
        AppCompatButton backButton = findViewById(R.id.back_button);
        TextView name1 = findViewById(R.id.titleName);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Profile");
        //when user logs in to display user details
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser == null){
            Toast.makeText(this, "Something went wrong! User's details unavailable", Toast.LENGTH_LONG).show();
        }
        else{
            showUserProfile(firebaseUser);
        }
        

        //when user goes back to dashboard
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        //user clicks on name
        name1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                        builder.setTitle("Change Username");
                        final EditText input = new EditText(Settings.this);
                        input.setInputType(InputType.TYPE_CLASS_TEXT);

                        builder.setView(input);
                        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String username = input.getText().toString();
                                if(TextUtils.isEmpty(username)){
                                    input.setError("Please enter a new username");
                                    input.requestFocus();
                                }
                                else{
                                mAuth = FirebaseAuth.getInstance();
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                //get unique id
                                    assert firebaseUser != null;
                                    String userID = firebaseUser.getUid();
                                //extract user reference from database for "registered users"
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(userID);
                                //initiate hashmap class to store data in key-value pairs
                                Map<String,Object>username1 = new HashMap<>();
                                //Enter user data into database
                                username1.put("name",username);
                                //update user data in database
                                databaseReference.updateChildren(username1);
                                Toast.makeText(Settings.this,"Username changed",Toast.LENGTH_SHORT).show();
                                finish();}

                            }
                        });
                        builder .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.create().show();
            }
        });
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        TextView email = findViewById(R.id.email);
        TextView username = findViewById(R.id.username);
        TextView titleName = findViewById(R.id.titleName);
        String userID = firebaseUser.getUid();
        //extracting user reference from database for "Registered Users"
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        //get user info from unique id
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails userDetails = snapshot.getValue(UserDetails.class);
                if(userDetails != null){
                    emailAddress = firebaseUser.getEmail();
                    name = userDetails.name;
                    email.setText(emailAddress);
                    username.setText(name);
                    titleName.setText(name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Settings.this, "Something went wrong! User's details unavailable", Toast.LENGTH_LONG).show();

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
            Intent intent = new Intent(Settings.this,Settings.class);
            startActivity(intent);

        }
       if(id == R.id.update_email){
            Intent intent = new Intent(Settings.this,UpdateEmail.class);
            startActivity(intent);
        }
        else if(id == R.id.update_password){
            Intent intent = new Intent(Settings.this,UpdatePassword.class);
            startActivity(intent);
        }
        if(id == R.id.logout_menu){
            //sign out the firebase user
            mAuth.signOut();
            Intent intent = new Intent(Settings.this,MainActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.delete_profile){
            Intent intent = new Intent(Settings.this,DeleteProfile.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}