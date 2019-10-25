package id.univmulia.technews.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import id.univmulia.technews.R;

public class HomeActivity extends AppCompatActivity {

    TextView userName, userEmail;
    Button btnKePost, btnKeluar;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ImageView userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        userPhoto = findViewById(R.id.homeUserPhoto);
        userName = findViewById(R.id.homeName);
        userEmail = findViewById(R.id.homeMail);
        btnKePost = findViewById(R.id.btnkePost);
        btnKeluar = findViewById(R.id.btnOut);

        userName.setText(currentUser.getDisplayName());
        userEmail.setText(currentUser.getEmail());

        //pake glide buat load photo user
        //pertama impor library
        Glide.with(this).load(currentUser.getPhotoUrl()).circleCrop().into(userPhoto);

        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                /*Intent gotoLoginActIntent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(gotoLoginActIntent);*/
                finish();
            }
        });

        btnKePost.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CreatePostActIntent = new Intent(getApplicationContext(),CreatePostActivity.class);
                startActivity(CreatePostActIntent);
            }
        }));
    }
}
