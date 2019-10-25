package id.univmulia.technews.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import id.univmulia.technews.Adapters.PostAdapter;
import id.univmulia.technews.Models.Post;
import id.univmulia.technews.R;

public class PostActivity extends AppCompatActivity  {

    private DatabaseReference databaseRef;
    private RecyclerView rvPost;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Post> listPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //inisiasi RecyclerView dan Komponen
        rvPost = findViewById(R.id.rv_postingan);
        rvPost.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvPost.setLayoutManager(layoutManager);

        //mengambil data postingan dari firebase database
        databaseRef.child("postingan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ada data baru, masukkan ke Arraylist
                listPost = new ArrayList<>();
                for(DataSnapshot notePostSnapshot : dataSnapshot.getChildren()){
                    /**
                     * Mapping data pada DataSnapshot ke dalam object Barang
                     * Dan juga menyimpan primary key pada object Barang
                     * untuk keperluan Edit dan Delete data
                     */
                    Post postingan = notePostSnapshot.getValue(Post.class);
                    postingan.setKey(notePostSnapshot.getKey());

                    listPost.add(postingan);
                }

                /**
                 * Inisialisasi adapter dan data barang dalam bentuk ArrayList
                 * dan mengeset Adapter ke dalam RecyclerView
                 */
                adapter = new PostAdapter(PostActivity.this, listPost);
                rvPost.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
