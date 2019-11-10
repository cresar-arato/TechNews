package id.univmulia.technews.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    //mematikan tombol kembali
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Silahkan Tekan Tombol Keluar", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //set statusbar jadi transparan ke postingan kita
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

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
        if (currentUser.getPhotoUrl() !=null){
            Glide.with(this).load(currentUser.getPhotoUrl()).circleCrop().into(userPhoto);
        }else
        Glide.with(this).load(R.drawable.ic_kitazawa_hagumi).circleCrop().into(userPhoto);

        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent gotoLoginActIntent = new Intent(getApplicationContext(),LoginActivity.class);
                gotoLoginActIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                gotoLoginActIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gotoLoginActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                gotoLoginActIntent.putExtra("KELUAR",true);
                startActivity(gotoLoginActIntent);
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
