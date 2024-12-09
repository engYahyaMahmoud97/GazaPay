package com.cat.gazapay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class AddTransfer extends AppCompatActivity {

    TextInputEditText name,cuntry,price,phone;
    RadioButton dollar,shikel,n_gaza_from,s_gaza_from,out_gaza_from,n_gaza_to,s_gaza_to,out_gaza_to;
    MaterialButton addTransfar;
    RadioGroup radioGroupDSH,radioGroupFrom,radioGroupTo;
    ProgressBar progressBar;
    private boolean isDollar = false;
    private String cuntryFrom = "southGaza";
    private String cuntryTo = "northGaza";



    private FirebaseDatabase root = FirebaseDatabase.getInstance();
    private String android_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transfer);
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        addTransfar = findViewById(R.id.button_add);
        name = findViewById(R.id.edit_text_name);
        cuntry = findViewById(R.id.edit_text_cuntry);
        price = findViewById(R.id.edit_text_price);
        phone = findViewById(R.id.edit_text_phone);
        dollar = findViewById(R.id.dollar);
        shikel = findViewById(R.id.shikel);
        n_gaza_from = findViewById(R.id.n_gaza_from);
        s_gaza_from = findViewById(R.id.s_gaza_from);
        out_gaza_from = findViewById(R.id.out_gaza_from);
        n_gaza_to = findViewById(R.id.n_gaza_to);
        s_gaza_to = findViewById(R.id.s_gaza_to);
        out_gaza_to = findViewById(R.id.out_gaza_to);
        radioGroupDSH = findViewById(R.id.radio_group_d_sh);
        radioGroupFrom = findViewById(R.id.radio_group_from);
        radioGroupTo = findViewById(R.id.radio_group_to);
        progressBar = findViewById(R.id.progress_circular);


        radioGroupDSH.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // on below line we are getting radio button from our group.
                RadioButton radioButton = findViewById(checkedId);

                isDollar = radioButton.getTag().toString().equals("dollar");

                // on below line we are displaying a toast message.
            }
        });
        radioGroupFrom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // on below line we are getting radio button from our group.
                RadioButton radioButton = findViewById(checkedId);

                cuntryFrom = radioButton.getTag().toString();

            }
        });
        radioGroupTo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // on below line we are getting radio button from our group.
                RadioButton radioButton = findViewById(checkedId);

                cuntryTo = radioButton.getTag().toString();

                // on below line we are displaying a toast message.
            }
        });


        addTransfar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().equals(""))
                    name.setError("قم بإدخال الاسم");
                else if (cuntry.getText().toString().equals(""))
                    cuntry.setError("قم بإدخال مكان الاقامة");
                else if (price.getText().toString().equals(""))
                    price.setError("قم بإدخال المبلغ المراد تحويله");
                else if (phone.getText().toString().equals(""))
                    phone.setError("قم بإدخال رقم التواصل");
                else{
                    String name1 = name.getText().toString();
                    String cuntry1 = cuntry.getText().toString();
                    String price1 = price.getText().toString();
                    String phone1 = phone.getText().toString();


                    String key = root.getReference().push().getKey();

                    Long date2 = -1 * (new Date().getTime());

                    ItemCard itemCard = new ItemCard(name1,cuntryFrom,cuntryTo,cuntry1,isDollar,price1,date2,phone1,key,android_id);
                    progressBar.setVisibility(View.VISIBLE);
                    root.getReference().child("Collection").child(key).setValue(itemCard).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("TTAAG", "success upload");
                            Toast.makeText(AddTransfer.this, "تم إضافة التحويلة بنجاح", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TTAAG", "failure upload");
                            Toast.makeText(AddTransfer.this, "فشلت العملية", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    });

                }
            }
        });

    }
}