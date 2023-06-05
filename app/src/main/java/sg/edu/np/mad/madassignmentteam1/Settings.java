package sg.edu.np.mad.madassignmentteam1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String name,emailAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth = FirebaseAuth.getInstance();
        Button logout = findViewById(R.id.logout);
        FirebaseUser user = mAuth.getCurrentUser();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser == null){
            Toast.makeText(this, "Something went wrong! User's details unavailable", Toast.LENGTH_LONG).show();
        }
        else{
            showUserProfile(firebaseUser);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        TextView email = findViewById(R.id.email);
        TextView username = findViewById(R.id.username);
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
                    name = firebaseUser.getDisplayName();
                    email.setText(emailAddress);
                    username.setText(name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Settings.this, "Something went wrong! User's details unavailable", Toast.LENGTH_LONG).show();

            }
        });
    }
}