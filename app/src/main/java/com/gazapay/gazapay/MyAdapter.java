package com.gazapay.gazapay;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.circularreveal.cardview.CircularRevealCardView;

import java.util.ArrayList;
import java.util.Random;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static final String SAVE_LIKE = "save like";
    // creating variables for our ArrayList and context
    private ArrayList<ItemCard> coursesArrayList;
    private Context context;


    // creating constructor for our adapter class
    public MyAdapter(ArrayList<ItemCard> coursesArrayList, Context context) {
        this.coursesArrayList = coursesArrayList;
        this.context = context;

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
        ItemCard product = coursesArrayList.get(position);

        holder.name.setText(product.getName());

        holder.cardView2.setCardBackgroundColor(getColor());


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


    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.

        private final TextView name, phone,price,fromCuntry,toCuntry,place,time; //₪
        private final CircularRevealCardView cardView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            name = itemView.findViewById(R.id.text_name);
            price = itemView.findViewById(R.id.text_place);
            phone = itemView.findViewById(R.id.text_phone);
            fromCuntry = itemView.findViewById(R.id.text_from_cuntry);
            toCuntry = itemView.findViewById(R.id.text_to_cuntry);
            place = itemView.findViewById(R.id.text_place);
            time = itemView.findViewById(R.id.text_time);

            cardView2 = itemView.findViewById(R.id.circle_card_view);
        }
    }
}