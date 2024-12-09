package com.gazapay.gazapay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText name;
    ProgressBar bar;
    MaterialButton buttonReg;
    private FirebaseDatabase root = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.edit_text_name);
        bar = findViewById(R.id.progress_circular_login);
        buttonReg = findViewById(R.id.button_regis);

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().equals(""))
                    name.setError("قم بادخال اسمك");
                else
                    addUser(name.getText().toString());
            }
        });
    }

    private void addUser(String toString) {
        String key = root.getReference().push().getKey();

        @SuppressLint("HardwareIds") String id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        User user = new User(false, toString, id, key,false);
        bar.setVisibility(View.VISIBLE);
        assert key != null;
        root.getReference().child("User").child(key).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("TTAAG", "success upload");
                bar.setVisibility(View.GONE);
                putSheardPref(user);
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TTAAG", "failure upload");
                Toast.makeText(LoginActivity.this, "فشلت العملية حاول مرة أخرى", Toast.LENGTH_SHORT).show();
                bar.setVisibility(View.GONE);

            }
        });
    }

    private void putSheardPref(User user) {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

// Storing the key and its value as the data fetched from edittext
        myEdit.putString("name", user.getName());
        myEdit.putString("id", user.getId());
        myEdit.putString("key", user.getKey());
        myEdit.putBoolean("bin", user.isBin());
        myEdit.putBoolean("isBay", user.isBay());

// Once the changes have been made, we need to commit to apply those changes made,
// otherwise, it will throw an error
        myEdit.commit();
    }


}