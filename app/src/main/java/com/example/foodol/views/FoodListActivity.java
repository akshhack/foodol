package com.example.foodol.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.airbnb.lottie.LottieAnimationView;
import com.example.foodol.R;
import com.example.foodol.models.FoodItem;
import com.example.foodol.models.FoodItemAdapter;
import com.example.foodol.models.RemoteDatabaseManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FoodListActivity extends AppCompatActivity implements FoodItemAdapter.FoodItemsAdapterListener {

    private static final String TAG = "FLA";
    private RecyclerView recyclerView;
    private SearchView searchView;
    private FoodItemAdapter foodItemAdapter;
    private List<FoodItem> foodItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        // set up on click floating action button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addItem = new Intent(FoodListActivity.this, AddFoodItemActivity.class);
                startActivity(addItem);
            }
        });

        // setup recycler view
        this.setupRecyclerView();

        // set up real-time food items query
        this.setupQueryFoodItemsRealtime();

        // set up searchView
        this.setupSearchView();

        // cancel loading
        cancelLoading();
    }

    private void setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView: called");

        // Setup recycler view
        this.recyclerView = findViewById(R.id.recycler_view);
        this.recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FoodListActivity.this);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());

        // specify an adapter (see also next example)
        this.foodItemList = new ArrayList<>();
        this.foodItemAdapter = new FoodItemAdapter(this.foodItemList, this);

        this.recyclerView.setAdapter(this.foodItemAdapter);
    }

    private void setupSearchView() {
        this.searchView = findViewById(R.id.search_view);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            this.searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            this.searchView.setMaxWidth(Integer.MAX_VALUE);

            // listening to search query text change
            this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // filter recycler view when query submitted
                    FoodListActivity.this.foodItemAdapter.getFilter().filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    // filter recycler view when text is changed
                    FoodListActivity.this.foodItemAdapter.getFilter().filter(query);
                    return false;
                }
            });
        }

    }

    private void setupQueryFoodItemsRealtime() {
        RemoteDatabaseManager.getInstance().observeAllFoodItems(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "onEvent: error " + e.toString());
                    return;
                }

                if (queryDocumentSnapshots != null) {
                    FoodListActivity.this.foodItemList.clear();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                        FoodItem foodItem = queryDocumentSnapshot.toObject(FoodItem.class);
                        FoodListActivity.this.foodItemList.add(foodItem);
                    }

                    // notify the data set has changed.
                    FoodListActivity.this.foodItemAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onEvent: no queryDocumentSnapshots found");
                }
            }
        });
    }

    private void cancelLoading() {
        // by default views start with loading
        LottieAnimationView lottieAnimationView = findViewById(R.id.animation_view);
        LinearLayout mainViewContainer = findViewById(R.id.main_view_container);

        lottieAnimationView.setVisibility(View.GONE);
        mainViewContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFoodContactSelected(FoodItem foodItem) {
        Log.d(TAG, "onFoodContactSelected: called");

        Intent intent = new Intent(this, AddFoodItemActivity.class);
        intent.putExtra(FoodItem.class.getSimpleName(), foodItem);
        startActivity(intent);
    }
}
