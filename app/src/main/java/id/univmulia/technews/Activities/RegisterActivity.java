package id.univmulia.technews.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import id.univmulia.technews.R;

public class RegisterActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 1;
    ImageView ImgUserPhoto;
    Uri pickedImgUri;

    private EditText userName, userEmail, userPassword, userPassword2;
    private ProgressBar loadingProgress;
    private Button regBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //set statusbar jadi transparan ke postingan kita
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //deklarasi
        ImgUserPhoto = findViewById(R.id.regUserPhoto);
        userName = findViewById(R.id.regName);
        userEmail = findViewById(R.id.regEmail);
        userPassword = findViewById(R.id.regPassword);
        userPassword2 = findViewById(R.id.regPassword2);
        regBtn = findViewById(R.id.btnReg);
        loadingProgress = findViewById(R.id.progressBar);
        loadingProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility((View.VISIBLE));
                final String email = userEmail.getText().toString();
                final String name = userName.getText().toString();
                final String password = userPassword.getText().toString();
                final String password2 = userPassword2.getText().toString();

                if(email.isEmpty() || name.isEmpty() || password.isEmpty() || !password.equals(password2)){
                    //sesuatu engga beres : harap di isi semua
                    //tampilkan error
                    showMessage("Semua Field Wajib Terisi");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }
                else {
                    //semua terisi dan siap proses
                    //mulai buat akun baru
                    CreateUserAcc(name,email,password);
                }
            }
        });

        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    private void CreateUserAcc(final String name, String email, final String password) {
        //method ini membuat akun user dengan spesifik email dan password
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // akun berhasil dibuat
                            showMessage("Akun Berhasil dibuat");
                            //setelah buat akun, kita perlu update foto dan namanya
                            //cek dulu apakah user mengupload fotonya  atau tidak
                            if(pickedImgUri !=null){
                                UpdateUserInfo(name,pickedImgUri,mAuth.getCurrentUser());

                            }else
                                UpdateUserInfoNoPhoto(name,mAuth.getCurrentUser());


                        }
                        else{
                            // akun gagal dibuat
                            showMessage("Akun Gagal dibuat" +task.getException().getMessage());
                            regBtn.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    //update photo dan nama
    private void UpdateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        //pertama kita perlu update foto ke firebase storage dan dapatkan url-nya
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //photo berhasil terunggah
                //sekarang kita dapat url photo-nya
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                    //uri terdapat image url-mya
                        UserProfileChangeRequest profileupdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileupdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        //info user berhasil diperbaharui
                                        showMessage("Register Complete");
                                        updateUI();
                                    }
                                    }
                                });
                    }
                });
            }
        });
    }

    //update nama saja (jika user tidak memasukkan foto)
    private void UpdateUserInfoNoPhoto(final String name, final FirebaseUser currentUser) {
        //kita panggil ini jika user tidak mengupload fotonya kefirebase
                        UserProfileChangeRequest profileupdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        currentUser.updateProfile(profileupdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            //info user berhasil diperbaharui
                                            showMessage("Register Complete");
                                            updateUI();
                                        }
                                    }
                                });
    }

    private void updateUI() {
    Intent homeactivity = new Intent(getApplicationContext(),HomeActivity.class);
    startActivity(homeactivity);
    finish();
    }

    //cara mudah menampilkan pesan toast
    private void showMessage(String pesan_error) {
        Toast.makeText(getApplicationContext(),pesan_error,Toast.LENGTH_LONG).show();
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
            ImgUserPhoto.setImageURI(pickedImgUri);
        }
    }
}
