package com.gazapay.gazapay;

import android.os.Bundle;
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

public class FragmentA extends Fragment {

    public FragmentA() {

    }

    private RecyclerView courseRV;
    private CourseRVAdapter courseRVAdapter;
    // ...
    ArrayList<ItemCard> productArrayList = new ArrayList<>();

    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container, false);
        courseRV = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progress_circular);



        //firebase
/*
        db = FirebaseFirestore.getInstance();
        addToFirestore();


        new ProductFetcher().fetchInitialProducts(10);
*/


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
                    if (itemCard.getFromCuntry().equals("northGaza"))
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

/*
        root.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                productArrayList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ItemCard university = postSnapshot.getValue(ItemCard.class);
                    productArrayList.add(university);

                    // here you can access to name property like university.name

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
        root.getReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {

                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });

        db.collection(collectionName).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // if the snapshot is not empty we are
                            // hiding our progress bar and adding
                            // our data in a list.
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                // after getting this list we are passing
                                // that list to our object class.
                                ItemCard c = d.toObject(ItemCard.class);

                                // and we will pass this object class
                                // inside our arraylist which we have
                                // created for recycler view.
                                productArrayList.add(c);
                            }
                            // after adding the data to recycler view.
                            // we are calling recycler view notifyDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            courseRVAdapter.notifyDataSetChanged();
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Log.d("TAAG", "Success");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // if we do not get any data or any error we are displaying
                        // a toast message that we do not get any data
                        Log.e("TAAG", "Failure");
                    }
                });
        return productArrayList;*/

    }

}
