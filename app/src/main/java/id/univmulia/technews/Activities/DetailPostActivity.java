package id.univmulia.technews.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Locale;

import id.univmulia.technews.R;

public class DetailPostActivity extends AppCompatActivity {

    ImageView imgPost,imgUserPost;
    TextView txtPostDesc, txtPostDateName, txtPostTitle, txtPostUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        //set statusbar jadi transparan ke postingan kita
        Window w = getWindow();
        /*w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);*/


        //deklarasi
        imgPost = findViewById(R.id.post_detail_image);
        imgUserPost = findViewById(R.id.post_detail_user_img);
        txtPostUserName = findViewById(R.id.post_detail_username);
        txtPostTitle = findViewById(R.id.post_detail_title);
        txtPostDateName = findViewById(R.id.post_detail_date_name);
        txtPostDesc = findViewById(R.id.post_detail_desc);


        //pertama ambil data dari Adapter
        //kemudian kita taruh di DetailPostActivity
        String postImage = getIntent().getExtras().getString("picture");
        Glide.with(this).load(postImage).into(imgPost);

        String postTitle = getIntent().getExtras().getString("title");
        txtPostTitle.setText(postTitle);

        String userpostImage = getIntent().getExtras().getString("userPhoto");
        //cek jika user memiliki foto atau tidak !!
        if (userpostImage !=null){
            Glide.with(this).load(userpostImage).circleCrop().into(imgUserPost);
        }
        else{
            Glide.with(this).load(R.drawable.ic_kitazawa_hagumi).circleCrop().into(imgUserPost);
        }


        String postUsername = getIntent().getExtras().getString("userName");
        txtPostUserName.setText(postUsername);

        String postDescription = getIntent().getExtras().getString("description");
        txtPostDesc.setText(postDescription);

        String date = timestampToString(getIntent().getExtras().getLong("postDate"));
        txtPostDateName.setText(date);




    }
    private String timestampToString(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy HH:mm",calendar).toString();
        return date;
    }
}
