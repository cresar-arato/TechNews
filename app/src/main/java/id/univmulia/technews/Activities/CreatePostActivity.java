package id.univmulia.technews.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import id.univmulia.technews.Models.Post;
import id.univmulia.technews.R;

public class CreatePostActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 1;
    ImageView ImgPost;
    EditText PostTitle,PostDescription;
    private Uri pickedImgUri = null;
    Button BtnPost;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ProgressBar PostProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //deklarasi
        ImgPost = findViewById(R.id.imgPost);
        PostTitle = findViewById(R.id.postTitle);
        PostDescription = findViewById(R.id.postDescription);
        BtnPost = findViewById(R.id.btnUploadPost);
        PostProgress = findViewById(R.id.postProgress);

        //klik untuk buka gallery
        ImgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        //klik untuk Post Berita
        BtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BtnPost.setVisibility(View.INVISIBLE);
                PostProgress.setVisibility(View.VISIBLE);

                //cek semua field telah terisi
               if (!PostTitle.getText().toString().isEmpty()
                   && !PostDescription.getText().toString().isEmpty()
                   && pickedImgUri != null){

                   //semua telah terisi
                   //TODO lakukan posting dan tambahkan ke firebase database dan storage

                   //pertama upload gambar post
                   //akses firebase storage
                   StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("post_images");
                   final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                   imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   String imageDownLink = uri.toString();

                                   //buat objek postingan
                                   Post post = new Post(PostTitle.getText().toString(),
                                                        PostDescription.getText().toString(),
                                                        imageDownLink,
                                                        currentUser.getUid(),
                                                        currentUser.getPhotoUrl().toString());

                                   //tambahkan postingan
                                   addPost(post);
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   //postingan gagal diproses
                                   showMessage(e.getMessage());
                                   BtnPost.setVisibility(View.VISIBLE);
                                   PostProgress.setVisibility(View.INVISIBLE);
                               }
                           });
                       }
                   });

               }
               else
               {
                   showMessage("Cek Semua Field Harus Terisi dan Gambar telah terpilih");

               }
            }
        });
    }

    private void addPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataref = database.getReference("Postingan").push();

        //mendapat id unik dan post key
        String key = dataref.getKey();
        post.setKey(key);

        //tambahkan data postingan ke firebase database
        dataref.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Postingan Berhasil di Tambahkan");
                BtnPost.setVisibility(View.VISIBLE);
                PostProgress.setVisibility(View.INVISIBLE);
                Intent HomePostActIntent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(HomePostActIntent);
            }
        });
    }

    private void showMessage(String pesan) {
        Toast.makeText(CreatePostActivity.this,pesan,Toast.LENGTH_LONG).show();
    }

    private void openGallery() {
        //TODO : buka gallery dan tunggu user memilih gambar
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null){
            //user berhasil mengambil gambar dari storage
            //simpan referensi ke uri variable
            pickedImgUri = data.getData();
            ImgPost.setImageURI(pickedImgUri);
        }
    }
}
