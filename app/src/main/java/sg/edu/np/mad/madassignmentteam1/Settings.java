package sg.edu.np.mad.madassignmentteam1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import org.w3c.dom.Text;

public class Settings extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String name,emailAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth = FirebaseAuth.getInstance();
        ImageView profilePic = findViewById(R.id.profile_pic);
        ImageView backButton = findViewById(R.id.back_button);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Profile");
        //when user logs in to display user details
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser == null){
            Toast.makeText(this, "Something went wrong! User's details unavailable", Toast.LENGTH_LONG).show();
        }
        else{
            showUserProfile(firebaseUser);
        }
        //when user clicks on profile image to upload a pic
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this,UploadProfile.class);
                startActivity(intent);
            }
        });

        //when user goes back to dashboard
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
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
       if(id == R.id.update_email){
            Intent intent = new Intent(Settings.this,UpdateEmail.class);
            startActivity(intent);
        }
        /*else if(id == R.id.change_name){
            Intent intent = new Intent(Settings.this,UpdateName.class);
            startActivity(intent);
        }*/
        else if(id == R.id.update_password){
            Intent intent = new Intent(Settings.this,UpdatePassword.class);
            startActivity(intent);
        }
        if(id == R.id.logout_menu){
            //sign out the firebase user
            mAuth.getInstance().signOut();
            Intent intent = new Intent(Settings.this,MainActivity.class);
            startActivity(intent);
        }
        /*else if(id == R.id.delete_profile){
            Intent intent = new Intent(Settings.this,DeleteProfile.class);
            startActivity(intent);
        }*/


        return super.onOptionsItemSelected(item);
    }
}