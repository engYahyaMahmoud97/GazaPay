package com.gazapay.gazapay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private static final String SAVE_LIKE = "saveLike";
    static int currunt = 0;
    SearchView search_view;
    private RecyclerView courseRV;
    private CourseRVAdapter courseRVAdapter;
    private FloatingActionButton floating_action_button;
    private FirebaseDatabase root = FirebaseDatabase.getInstance();


    ArrayList<ItemCard> productArrayList = new ArrayList<>();


    FloatingActionButton addTransferFloat, addPercentTransferFloat, addPercentSyolaFloat;

    // Use the ExtendedFloatingActionButton to handle the
    // parent FAB
    ExtendedFloatingActionButton mAddFab;

    // These TextViews are taken to make visible and
    // invisible along with FABs except parent FAB's action
    // name
    TextView addTransfer, addPercentTransfer, addPercentSyola;
    User itemCard;
    // to check whether sub FABs are visible or not
    Boolean isAllFabsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        courseRV = findViewById(R.id.recycle_view);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        search_view = findViewById(R.id.search_view);
        addTransferFloat = findViewById(R.id.add_transfer_fab);
        addPercentTransferFloat = findViewById(R.id.add_transfer_percent_fab);
        addPercentSyolaFloat = findViewById(R.id.add_syola_fab);
        addTransfer = findViewById(R.id.transfer_text);
        addPercentTransfer = findViewById(R.id.add_person_action_text);
        addPercentSyola = findViewById(R.id.add_syola_text);
        mAddFab = findViewById(R.id.add_fab);
        startFab();
        mAddFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {

                            // when isAllFabsVisible becomes
                            // true make all the action name
                            // texts and FABs VISIBLE.
                            addTransferFloat.show();
                            addPercentTransferFloat.show();
                            addPercentSyolaFloat.show();
                            addTransfer.setVisibility(View.VISIBLE);
                            addPercentTransfer.setVisibility(View.VISIBLE);
                            addPercentSyola.setVisibility(View.VISIBLE);

                            // Now extend the parent FAB, as
                            // user clicks on the shrinked
                            // parent FAB
                            mAddFab.extend();

                            // make the boolean variable true as
                            // we have set the sub FABs
                            // visibility to GONE
                            isAllFabsVisible = true;
                        } else {

                            // when isAllFabsVisible becomes
                            // true make all the action name
                            // texts and FABs GONE.
                            addTransferFloat.hide();
                            addPercentTransferFloat.hide();
                            addPercentSyolaFloat.hide();
                            addTransfer.setVisibility(View.GONE);
                            addPercentTransfer.setVisibility(View.GONE);
                            addPercentSyola.setVisibility(View.GONE);

                            // Set the FAB to shrink after user
                            // closes all the sub FABs
                            mAddFab.shrink();

                            // make the boolean variable false
                            // as we have set the sub FABs
                            // visibility to GONE
                            isAllFabsVisible = false;
                        }
                    }
                });
        addTransferFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddTransfer.class));
            }
        });
        addPercentTransferFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!itemCard.isBay())
                    popDialog();
            }
        });
        addPercentSyolaFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!itemCard.isBay())
                    popDialog();
            }
        });
/*
        floating_action_button = findViewById(R.id.floating_action_button);
        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AddTransfer.class));
            }
        });
*/


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
        @SuppressLint("HardwareIds") String id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Query query1 = rootRef.child("User").orderByChild("id").equalTo(id);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChildren()) {
                    itemCard = snapshot.getValue(User.class);


                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void popDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("عذرا")
                .setMessage("سيتم تفعيل التحويل بالعمولة والسيولة قريبا")
                .setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create();
        dialog.show();
    }

    private void startFab() {
//        addPercentSyolaFloat.setVisibility(View.GONE);
//        addTransferFloat.setVisibility(View.GONE);
//        addPercentTransferFloat.setVisibility(View.GONE);
        addTransfer.setVisibility(View.GONE);
        addPercentTransfer.setVisibility(View.GONE);
        addPercentSyola.setVisibility(View.GONE);
        addTransferFloat.hide();
        addPercentTransferFloat.hide();
        addPercentSyolaFloat.hide();
        // make the boolean variable as false, as all the
        // action name texts and all the sub FABs are
        // invisible
        isAllFabsVisible = false;

        // Set the Extended floating action button to
        // shrinked state initially
        mAddFab.shrink();
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
