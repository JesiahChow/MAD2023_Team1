package sg.edu.np.mad.madassignmentteam1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mAuth = FirebaseAuth.getInstance();
        Button button = findViewById(R.id.button7);
        FirebaseUser user = mAuth.getCurrentUser();
        //if(user == null){
            //Intent intent = new Intent(HomePage.this, Login.class);
            //startActivity(intent);
        //}
        //else{
            //text.setText(user.getEmail());
        //}
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomePage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}