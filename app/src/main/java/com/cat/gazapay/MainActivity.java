package com.cat.gazapay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ProgressBar bar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bar = findViewById(R.id.progress_circular);
        textView = findViewById(R.id.text_message);

        bar.setVisibility(View.VISIBLE);
        readDataFromCollection();

    }

    private void readDataFromCollection() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        @SuppressLint("HardwareIds") String id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Query query1 = rootRef.child("User").orderByChild("id").equalTo(id);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bar.setVisibility(View.GONE);

                if (snapshot.hasChildren()){
                    User itemCard = snapshot.getValue(User.class);
                    if (!itemCard.isBin()) {
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();
                    }
                    else
                        textView.setText("تم إيقاف حسابك\nيرجى التواصل معنا على الواتساب على الرقم التالي +972597123297");

                }else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "فشلت عملية الاتصال", Toast.LENGTH_SHORT).show();
            }
        });

    }

}