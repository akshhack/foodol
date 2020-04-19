package com.example.foodol.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodol.R;

import java.util.ArrayList;
import java.util.List;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodContactViewHolder>
    implements Filterable {

    private static final float FUZZY_THRESHOLD = 0.9F;

    private List<FoodItem> foodItemList;
    private List<FoodItem> foodItemListFiltered;
    private FoodItemsAdapterListener listener;

    // TODO: needs review because should show FoodItem
    class FoodContactViewHolder extends RecyclerView.ViewHolder {
        TextView type, qty;

        FoodContactViewHolder(View view) {
            super(view);
            FoodContactViewHolder.this.type = view.findViewById(R.id.food_item_type);
            FoodContactViewHolder.this.qty = view.findViewById(R.id.food_item_qty);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  selected contact in callback
                    FoodItemAdapter.this.listener
                            .onFoodContactSelected(foodItemListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public FoodItemAdapter(List<FoodItem> foodItemList, FoodItemsAdapterListener listener) {
        this.listener = listener;
        this.foodItemList = foodItemList;
        this.foodItemListFiltered = foodItemList;
    }

    @Override
    public FoodContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_item_row, parent, false);

        return new FoodContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FoodContactViewHolder holder, final int position) {
        final FoodItem foodItem = foodItemListFiltered.get(position);
        holder.type.setText(String.valueOf(foodItem.getFoodItem()));
        holder.qty.setText(String.valueOf(foodItem.getQuantity()));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    foodItemListFiltered = foodItemList;
                } else {
                    List<FoodItem> filteredList = new ArrayList<>();
                    for (FoodItem row : foodItemList) {
                        // search fuzzy matches item closely
                        if (FuzzySearch.ratio(row.getFoodItem().toLowerCase(),
                                charSequence.toString().toLowerCase()) > FUZZY_THRESHOLD) {
                            filteredList.add(row);
                        }
                    }

                    foodItemListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = foodItemListFiltered;
                return filterResults;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                foodItemListFiltered = (ArrayList<FoodItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return this.foodItemListFiltered.size();
    }

    public interface FoodItemsAdapterListener {
        void onFoodContactSelected(FoodItem foodItem);
    }
}
