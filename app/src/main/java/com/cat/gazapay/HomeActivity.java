package com.cat.gazapay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final String SAVE_LIKE = "saveLike";
    static int currunt = 0;
    SearchView search_view;
    private RecyclerView courseRV;
    private CourseRVAdapter courseRVAdapter;
    private FloatingActionButton floating_action_button;
    private FirebaseDatabase root = FirebaseDatabase.getInstance();


    ArrayList<ItemCard> productArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        courseRV = findViewById(R.id.recycle_view);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        search_view = findViewById(R.id.search_view);
        floating_action_button = findViewById(R.id.floating_action_button);
        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AddTransfer.class));
            }
        });


        readDataFromCollection(); //TODO مؤقت الى حين جلب الداتا عن طريق الوير
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                courseRV.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);

                productArrayList.clear();

                //TODO productArrayList.add(ItemCard);
                Query query1 = root.getReference().child("Collection").orderByChild("name").startAt(query).endAt(query + "\uf8ff");
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ItemCard ItemCard = ds.getValue(ItemCard.class);
                            productArrayList.add(ItemCard);
                        }
                        courseRV.setHasFixedSize(true);
                        courseRV.setLayoutManager(new LinearLayoutManager(HomeActivity.this));

                        // adding our array list to our recycler view adapter class.
                        courseRVAdapter = new CourseRVAdapter(productArrayList, HomeActivity.this);

                        // setting adapter to our recycler view.
                        courseRV.setAdapter(courseRVAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        courseRV.setVisibility(View.GONE);
                        viewPager.setVisibility(View.VISIBLE);
                        tabLayout.setVisibility(View.VISIBLE);
                    }
                });


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                courseRV.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);

                productArrayList.clear();

                //TODO productArrayList.add(ItemCard);
                Query query1 = root.getReference().child("Collection").orderByChild("name").startAt(newText).endAt(newText + "\uf8ff");
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ItemCard ItemCard = ds.getValue(ItemCard.class);
                            productArrayList.add(ItemCard);
                        }
                        courseRV.setHasFixedSize(true);
                        courseRV.setLayoutManager(new LinearLayoutManager(HomeActivity.this));

                        // adding our array list to our recycler view adapter class.
                        courseRVAdapter = new CourseRVAdapter(productArrayList, HomeActivity.this);

                        // setting adapter to our recycler view.
                        courseRV.setAdapter(courseRVAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        courseRV.setVisibility(View.GONE);
                        viewPager.setVisibility(View.VISIBLE);
                        tabLayout.setVisibility(View.VISIBLE);
                    }
                });


                return false;
            }
        });

        search_view.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                courseRV.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                return false;
            }
        });

        viewPager.setAdapter(new ViewPagerAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("شمال غزة");
                    } else if (position == 1) {
                        tab.setText("جنوب غزة");
                    } else if (position == 2) {
                        tab.setText("الخارج");
                    } else if (position == 3) {
                        tab.setText("تحويلاتي");
                    }
                }
        ).attach();
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currunt = position;
            }
        });
    }

    private void readDataFromCollection() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference chatSpaceRef = rootRef.child("Collection");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productArrayList.clear();
//                ItemCard itemCard3 = dataSnapshot.getValue(ItemCard.class);
//                Log.d("TTAAG",itemCard3.getName());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ItemCard ItemCard = ds.getValue(ItemCard.class);
                    productArrayList.add(ItemCard);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TTAAG", databaseError.getMessage());
            }
        };
        chatSpaceRef.addListenerForSingleValueEvent(eventListener);


/*        root.getReference().addValueEventListener(new ValueEventListener() {
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


    class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new FragmentA();
                case 1:
                    return new FragmentB();
                case 2:
                    return new FragmentC();
                case 3:
                    return new FragmentD();
            }
            return new FragmentA();
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }


}
