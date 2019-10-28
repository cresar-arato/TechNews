package id.univmulia.technews.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.univmulia.technews.Adapters.PostAdapter;
import id.univmulia.technews.Models.Post;
import id.univmulia.technews.R;

public class PostActivity extends AppCompatActivity  {

    private RecyclerView mRecyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<Post> mPost;
    private PostAdapter mAdapter;

    //buat klik kembali
    private static final int TIME_INTERVAL = 1000;
    private long mBackPressed;

    //keluat aplikasi
    @Override
    public void onBackPressed() {
        finish();
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //set statusbar jadi transparan ke postingan kita
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        mRecyclerView = findViewById(R.id.rv_postingan);
        mRecyclerView.setHasFixedSize(true);

        //set layout ke linear layout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPost=new ArrayList<>();

        //kirim kueri ke firebasedatabase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Postingan");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    Post post = postSnapshot.getValue(Post.class);
                    mPost.add(post);
                }
                mAdapter = new PostAdapter(PostActivity.this,mPost);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PostActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
