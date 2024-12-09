package com.gazapay.gazapay;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class CourseRVAdapter extends RecyclerView.Adapter<CourseRVAdapter.ViewHolder> {
    private static final String SAVE_LIKE = "save like";
    // creating variables for our ArrayList and context
    private ArrayList<ItemCard> coursesArrayList;
    private Context context;
    private FirebaseDatabase root = FirebaseDatabase.getInstance();

    private ArrayList<String> likeArray = new ArrayList<>();

    // creating constructor for our adapter class
    public CourseRVAdapter(ArrayList<ItemCard> coursesArrayList, Context context) {
        this.coursesArrayList = coursesArrayList;
        this.context = context;

        likeArray = loadData(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        ItemCard transfer = coursesArrayList.get(position);
        String date = new SimpleDateFormat("dd-MM hh:mm").format(new Date(-1*(transfer.getTime())));

        if (Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID).equals(transfer.getMyId())){
            holder.delete_icon.setVisibility(View.VISIBLE);
        }else {
            holder.delete_icon.setVisibility(View.GONE);
        }
        holder.delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("حذف")
                        .setMessage("أنت على وشك حذف التحويلة \n هل أنت متأكد من ذلك")
                        .setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                root.getReference().child("Collection").child(transfer.getTransfarKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "تم حذف التحويلة", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "فشل حذف التحويلة", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("إلغاء", null)
                        .create();
                dialog.show();
            }
        });
        holder.text_name.setText(transfer.getName());
        holder.text_cuntry.setText(transfer.getCuntryName());
        holder.text_price.setText(transfer.getPrice());
        holder.text_phone.setText(transfer.getWhatsapp());
        if (transfer.getFromCuntry().equals("northGaza"))
            holder.cuntryFrom.setText("شمال غزة");
        else if (transfer.getFromCuntry().equals("southGaza"))
            holder.cuntryFrom.setText("جنوب غزة");
        else
            holder.cuntryFrom.setText("الخارج");

        if (transfer.getToCuntry().equals("northGaza"))
            holder.cuntryTo.setText("شمال غزة");
        else if (transfer.getToCuntry().equals("southGaza"))
            holder.cuntryTo.setText("جنوب غزة");
        else
            holder.cuntryTo.setText("الخارج");

        holder.cardView.setCardBackgroundColor(getColor());
        holder.date.setText(date);
        if (transfer.isDollar()) {
            holder.dollar.setVisibility(View.VISIBLE);
            holder.shikel.setVisibility(View.GONE);
        }else {
            holder.dollar.setVisibility(View.GONE);
            holder.shikel.setVisibility(View.VISIBLE);
        }
        /*String date = new SimpleDateFormat("dd-MM hh:mm").format(new Date(-1*(product.getDate())));
        if (likeArray.contains(product.getKey())) {
            holder.like.setVisibility(View.VISIBLE);
            holder.noLike.setVisibility(View.GONE);
        } else {
            holder.like.setVisibility(View.GONE);
            holder.noLike.setVisibility(View.VISIBLE);
        }
        if (product.getCollection().equals("carton")){
            holder.linearLayout_carton.setVisibility(View.VISIBLE);
            holder.linearLayout_kilo.setVisibility(View.GONE);

            holder.name_text.setText(product.getName());
            holder.text_all.setText(product.getAllPrice());
            holder.text_one.setText(product.getOnePrice());
            holder.text_num_carton.setText(product.getNumberOfPieces());

            holder.date.setText(date);
            Glide.with(context)

                    .load(product.getImage())

                    .into(holder.circleImageView);
        }else {
            holder.linearLayout_carton.setVisibility(View.GONE);
            holder.linearLayout_kilo.setVisibility(View.VISIBLE);

            holder.name_text.setText(product.getName());
            holder.shwal_price.setText(product.getAllPrice());
            holder.text_weigth_kilo.setText(product.getNumberOfPieces());
            holder.kiloprice.setText(product.getOnePrice());
            holder.date.setText(date);
            Glide.with(context)

                    .load(product.getImage())

                    .into(holder.circleImageView);

        }



        holder.noLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.like.setVisibility(View.VISIBLE);
                holder.noLike.setVisibility(View.GONE);
                likeArray.add(product.getKey());
                saveData(context,likeArray);
               // coursesArrayList.notify();
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.like.setVisibility(View.GONE);
                holder.noLike.setVisibility(View.VISIBLE);
                likeArray = loadData(context);
                likeArray.remove(product.getKey());
                saveData(context,likeArray);
                //coursesArrayList.notify();
            }
        });*/
    }

    private int getColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return coursesArrayList.size();
    }
    public static ArrayList<String> loadData(Context context) {
        ArrayList<String> courseModalArrayList = new ArrayList<>();
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVE_LIKE, MODE_PRIVATE);

        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("courses", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<String>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        courseModalArrayList = gson.fromJson(json, type);

        // checking below if the array list is empty or not
        if (courseModalArrayList == null) {
            // if the array list is empty
            // creating a new array list.
            courseModalArrayList = new ArrayList<>();
        }
        return courseModalArrayList;
    }

    private static void saveData(Context context,ArrayList<String> courseModalArrayList) {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVE_LIKE, MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(courseModalArrayList);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("courses", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();

        // after saving data we are displaying a toast message.
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        TextView text_name,text_cuntry,text_phone,text_price,cuntryFrom,cuntryTo,date,dollar,shikel;
        CircularRevealCardView cardView;
        MaterialCardView delete_icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            text_name = itemView.findViewById(R.id.text_name);
            text_cuntry = itemView.findViewById(R.id.text_place);
            text_phone = itemView.findViewById(R.id.text_phone);
            text_price = itemView.findViewById(R.id.text_price);
            cuntryFrom = itemView.findViewById(R.id.text_from_cuntry);
            cuntryTo = itemView.findViewById(R.id.text_to_cuntry);
            date = itemView.findViewById(R.id.text_time);
            dollar = itemView.findViewById(R.id.dollar);
            shikel = itemView.findViewById(R.id.shikel);
            cardView = itemView.findViewById(R.id.circle_card_view);
            delete_icon = itemView.findViewById(R.id.delete_icon);


        }
    }
}