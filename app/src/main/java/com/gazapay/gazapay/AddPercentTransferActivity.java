package com.gazapay.gazapay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Date;

public class AddPercentTransferActivity extends AppCompatActivity {

    TextInputEditText editName,editCuntry,editPercent,editDesc;
    MaterialButton addButton;
    ProgressBar bar;
    private String android_id;
    private FirebaseDatabase root = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_percent_transfer);
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        editName = findViewById(R.id.edit_text_name);
        editCuntry = findViewById(R.id.edit_text_cuntry);
        editPercent = findViewById(R.id.edit_text_percent);
        editDesc = findViewById(R.id.edit_text_desc);
        addButton = findViewById(R.id.button_add);
        bar = findViewById(R.id.progress_circular);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editName.getText().toString().equals(""))
                    editName.setError("قم بإدخال الاسم");
                else if (editCuntry.getText().toString().equals(""))
                    editCuntry.setError("قم بإدخال مكان الاقامة");
                else if (editPercent.getText().toString().equals(""))
                    editPercent.setError("قم بإدخال المبلغ المراد تحويله");
                else if (editDesc.getText().toString().equals(""))
                    editDesc.setError("قم بإدخال رقم التواصل");
                else {
                    String name = editName.getText().toString();
                    String cuntry = editCuntry.getText().toString();
                    String percent = editPercent.getText().toString();
                    String desc = editDesc.getText().toString();


                    String key = root.getReference().push().getKey();

                    Long date2 = -1 * (new Date().getTime());

                    ItemCardPercent itemCard = new ItemCardPercent(name,cuntry,percent,desc,date2,key,android_id);
                    bar.setVisibility(View.VISIBLE);
                    root.getReference().child("CollectionPercent").child(key).setValue(itemCard).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("TTAAG", "success upload");
                            Toast.makeText(AddPercentTransferActivity.this, "تم إضافة التحويلة بنجاح", Toast.LENGTH_SHORT).show();
                            bar.setVisibility(View.GONE);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TTAAG", "failure upload");
                            Toast.makeText(AddPercentTransferActivity.this, "فشلت العملية", Toast.LENGTH_SHORT).show();
                            bar.setVisibility(View.GONE);

                        }
                    });

                }
            }
        });

    }



}