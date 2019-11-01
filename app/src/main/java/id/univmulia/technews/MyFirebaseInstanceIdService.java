package id.univmulia.technews;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService{

    final String TAG = MyFirebaseInstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        //Mendapatkan Instance dan Memperoleh Token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Menampilkan Token pada Log
        Log.d(TAG, "Token Saya : "+ refreshedToken);

        saveToken(refreshedToken);
    }

    //Method berikut ini digunakan untuk memperoleh token dan mennyimpannya ke server atau sistem lainnya
    private void saveToken(String refreshedToken) {
        SharedPrefManager.getInstance(getApplicationContext()).setToken(refreshedToken);
    }
}
