package com.cat.gazapay;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentD extends Fragment {

    public FragmentD() {
    }

    private RecyclerView courseRV;
    private CourseRVAdapter courseRVAdapter;
    // ...
    ArrayList<ItemCard> productArrayList = new ArrayList<>();
    public String android_id;
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container, false);
        courseRV = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progress_circular);
        android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

//        new ProductFetcher().fetchInitialProducts(10);


        try {
            courseRV.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            readDataFromCollection();
        } catch (Exception exception) {

        }


        // adding our array list to our recycler view adapter class.

        return view;
    }

    private void readDataFromCollection() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Collection").orderByChild("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseRV.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                productArrayList.clear();
//                ItemCard itemCard3 = dataSnapshot.getValue(ItemCard.class);
//                Log.d("TTAAG",itemCard3.getName());
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ItemCard itemCard = ds.getValue(ItemCard.class);
                    if (itemCard.getMyId().equals(android_id))
                        productArrayList.add(itemCard);
                }
                courseRV.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//                linearLayoutManager.setReverseLayout(true);
//                linearLayoutManager.setStackFromEnd(true);
                courseRV.setLayoutManager(linearLayoutManager);

                // adding our array list to our recycler view adapter class.
                courseRVAdapter = new CourseRVAdapter(productArrayList, getContext());

                // setting adapter to our recycler view.
                courseRV.setAdapter(courseRVAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TTAAG", databaseError.getMessage());
                courseRV.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }
        });

    }
}
